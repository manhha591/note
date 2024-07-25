package com.example.note.Activity;



import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.note.R;
import com.example.note.model.Note;
import com.example.note.viewmodel.NoteViewModel;


public class UpdateNoteActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private EditText edtNoteTitle;
    private EditText edtNoteDes;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_node);

        noteViewModel = new ViewModelProvider(this,
                new NoteViewModel.NoteViewModelFactory(getApplication())).get(NoteViewModel.class);

        edtNoteTitle = findViewById(R.id.edt_note_title);
        edtNoteDes = findViewById(R.id.edt_note_des);
        btnUpdate = findViewById(R.id.btn_update);

        Note note = (Note) getIntent().getSerializableExtra("UPDATE_NOTE");
        if (note != null) {
            edtNoteTitle.setText(note.getTitle());
            edtNoteDes.setText(note.getDescription());
        }

        btnUpdate.setOnClickListener(v -> {
            if (note != null) {
                note.setTitle(edtNoteTitle.getText().toString());
                note.setDescription(edtNoteDes.getText().toString());
                noteViewModel.updateNote(note);
                finish();
            }
        });
    }
}
