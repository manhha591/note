package com.example.note.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "note_table")
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id_col")
    private int id;

    @ColumnInfo(name = "title_col")
    private String title;

    @ColumnInfo(name = "description_col")
    private String description;

    // Constructor mặc định (không có tham số)
    public Note() {
        this.title = "";
        this.description = "";
    }

    // Constructor với tham số
    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getter và Setter cho id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter và Setter cho title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter và Setter cho description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
