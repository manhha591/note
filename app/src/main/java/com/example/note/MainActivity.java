package com.example.note;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.note.Activity.AddNoteActivity;
import com.example.note.Activity.UpdateNoteActivity;
import com.example.note.adapter.NoteAdapter;
import com.example.note.databinding.ActivityMainBinding;
import com.example.note.model.Note;
import com.example.note.viewmodel.NoteViewModel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding  = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteViewModel = new ViewModelProvider(this,
                new NoteViewModel.NoteViewModelFactory(getApplicationContext())).get(NoteViewModel.class);

        initControls();
        initEvents();

        binding.datePicker.setOnSelectionChanged(date -> {
            Toast.makeText(this, date.toString(), Toast.LENGTH_SHORT).show();
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(date);
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(date);
            noteViewModel.loadNotesByState(DateUtils.getStartOfDayTimestamp(startDate), DateUtils.getEndOfDayTimestamp(endDate));
            return null;
        });
    }

    private void initEvents() {
        findViewById(R.id.btn_open_add_activity).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });
    }

    private void initControls() {
        adapter = new NoteAdapter(this, onItemClick, onItemDelete);

       // binding.rvNote.setHasFixedSize(true);
        binding.rvNote.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNote.setAdapter(adapter);

        LiveData<List<Note>> notesLiveData = noteViewModel.getAllNotes();
        notesLiveData.observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });
    }

    private final NoteAdapter.OnItemClickListener onItemClick = note -> {
        Intent intent = new Intent(MainActivity.this, UpdateNoteActivity.class);
        intent.putExtra("UPDATE_NOTE", note);
        startActivity(intent);
    };

    private final NoteAdapter.OnItemDeleteListener onItemDelete = note -> noteViewModel.deleteNote(note);


}