package com.vkbao.notebook.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Note")
public class Note{
    @PrimaryKey(autoGenerate = true)
    private int note_id;
    private String title;
    private String description;

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Note() {
        this("", "");
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
