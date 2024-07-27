package com.example.note.database.respository;

import android.app.Application;
import android.content.Context;


import com.example.note.database.NoteDatabase;
import com.example.note.database.dao.NoteDao;
import com.example.note.model.Note;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class NoteRepository {

    private final NoteDao noteDao;
    private final CompositeDisposable disposables = new CompositeDisposable();
    public NoteRepository(Context app) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(app);
        noteDao = noteDatabase.getNoteDao();
    }

    public Completable insertNote(Note note) {
        return Completable.fromAction(() -> noteDao.insertNote(note));
    }
    public Completable updateNote(Note note) {
        return Completable.fromRunnable(() -> noteDao.updateNote(note))
                .subscribeOn(Schedulers.io());
    }

    public Completable deleteNote(Note note) {
        return Completable.fromRunnable(() -> noteDao.deleteNote(note))
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Note>> getAllNotes() {
        return noteDao.getAllNote()
                .toObservable()
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Note>> getByDate(Long stratDate, Long endDate) {
        return noteDao.getByDate(stratDate,endDate)
                .toObservable()
                .subscribeOn(Schedulers.io());
    }
}
