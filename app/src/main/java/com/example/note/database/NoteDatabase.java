package com.example.note.database;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.note.database.dao.NoteDao;
import com.example.note.model.Note;


@Database(entities = {Note.class}, version = 2)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao getNoteDao();

    private static volatile NoteDatabase instance;

    public static NoteDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (NoteDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    NoteDatabase.class, "NoteDatabase")
                            .build();
                }
            }
        }
        return instance;
    }
}
