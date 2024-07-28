package com.example.note.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.note.R;
import com.example.note.databinding.ActivityAddNoteBinding;
import com.example.note.model.Note;
import com.example.note.viewmodel.NoteViewModel;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;
    private EditText edtNoteTitle;
    private EditText edtNoteDes;
    private Button btnAdd;
    private ActivityAddNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Khởi tạo ViewModel
        noteViewModel = new ViewModelProvider(this,
                new NoteViewModel.NoteViewModelFactory(getApplication())).get(NoteViewModel.class);

        // Khởi tạo các thành phần giao diện người dùng
        edtNoteTitle = findViewById(R.id.edt_note_title);
        edtNoteDes = findViewById(R.id.edt_note_des);
        btnAdd = findViewById(R.id.btn_add);

        // Thiết lập sự kiện nhấp nút
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtNoteTitle.getText().toString();
                String description = edtNoteDes.getText().toString();
                Note note = new Note(title, description);
                note.setStartDate(selectedDateTime);
                note.setState(0);
               // note.setStartDate(System.currentTimeMillis()+6000);
                noteViewModel.insertNote(note);
                finish();
            }
        });
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(new IchangeTime() {
                    @Override
                    public void onChangeTime(Calendar calendar) {
                        selectedDateTime = calendar;
                    }
                });

            }
        });
    }

    private Calendar selectedDateTime;
    private String formattedDateTime;

    private void showDatePickerDialog(IchangeTime ichangeTime) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.SpinnerDatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Lưu ngày đã chọn
                        selectedDateTime = Calendar.getInstance();
                        selectedDateTime.set(year, monthOfYear, dayOfMonth);

                        // Hiển thị TimePickerDialog để chọn thời gian
                        showTimePickerDialog(ichangeTime);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void showTimePickerDialog(IchangeTime ichangeTime) {
        int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDateTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.SpinnerTimePickerDialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Lưu thời gian đã chọn
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);

                        // Định dạng dữ liệu thành chuỗi


                        // In ra chuỗi ngày giờ đã chọn
                        System.out.println("Selected date time: " + formattedDateTime);
                        try {
                            ichangeTime.onChangeTime(selectedDateTime);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        ;
                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private interface IchangeTime {
        void onChangeTime(Calendar calendar) throws ParseException;
    }

}
