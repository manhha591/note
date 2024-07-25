package com.example.note;

import android.content.Intent;
import android.os.Bundle;

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

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteViewModel = new ViewModelProvider(this,
                new NoteViewModel.NoteViewModelFactory(getApplication())).get(NoteViewModel.class);

        initControls();
        initEvents();
    }

    private void initEvents() {
        findViewById(R.id.btn_open_add_activity).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });
    }

    private void initControls() {
        adapter = new NoteAdapter(this, onItemClick, onItemDelete);

        binding.rvNote.setHasFixedSize(true);
        binding.rvNote.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNote.setAdapter(adapter);

        LiveData<List<Note>> notesLiveData = noteViewModel.getAllNote();
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