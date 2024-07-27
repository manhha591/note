package com.example.note.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.note.model.Note;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


@Dao
public interface NoteDao {

    @Insert
    void insertNote(Note note);

    @Update
    Completable updateNote(Note note);

    @Delete
    Completable deleteNote(Note note);

    @Query("SELECT * FROM note_table")
    Flowable<List<Note>> getAllNote();


    @Query("SELECT * FROM note_table WHERE date_col BETWEEN :startDate AND :endDate")
    Flowable<List<Note>> getByDate(Long startDate, Long endDate);

    /* @Query("SELECT * FROM note_table WHERE title_col = :title")
    Flowable<List<Note>> getNoteByTitle(String title); */
}
