package com.example.note.database.respository;


import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.note.database.NoteDatabase;
import com.example.note.database.dao.NoteDao;
import com.example.note.model.Note;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteRepository {

    private final NoteDao noteDao;

    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.getNoteDao();
    }

    public Completable insertNote(Note note) {
        return Completable.fromAction(() -> noteDao.insertNote(note))
                .subscribeOn(Schedulers.io());
    }

    public Completable updateNote(Note note) {
        return Completable.fromAction(() -> noteDao.updateNote(note))
                .subscribeOn(Schedulers.io());
    }

    public Completable deleteNote(Note note) {
        return Completable.fromAction(() -> noteDao.deleteNote(note))
                .subscribeOn(Schedulers.io());
    }

    public Flowable<List<Note>> getAllNote() {
        return noteDao.getAllNote()
                .subscribeOn(Schedulers.io());
    }
}
