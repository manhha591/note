package com.example.note.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.note.R;
import com.example.note.model.Note;
import com.example.note.viewmodel.NoteViewModel;

public class AddNoteActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;
    private EditText edtNoteTitle;
    private EditText edtNoteDes;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

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
                noteViewModel.insertNote(note);
                finish();
            }
        });
    }
}
