package com.example.note.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Calendar;

@Entity(tableName = "note_table")
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id_col")
    private int id;

    @ColumnInfo(name = "title_col")
    private String title;

    @ColumnInfo(name = "description_col")
    private String description;
    @ColumnInfo(name = "date_col")
    private Long startDate;
    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }
    public Long getStartDate() {
        return startDate;
    }
    public Calendar getDate() {
        if (startDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startDate); // Thiết lập thời gian cho Calendar
            return calendar; // Trả về Calendar đã được thiết lập
        } else {
            return null; // Nếu startDate là null, trả về null
        }
    }


    public void setStartDate(Calendar startDate) {
        this.startDate = startDate != null ? startDate.getTimeInMillis() : null;
    }



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
