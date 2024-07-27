package com.vkbao.notebook.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Note")
public class Note{
    @PrimaryKey(autoGenerate = true)
    private long note_id;
    private String title;
    private String description;
    private long create_at;
    private long modified_at;

    public Note(String title, String description, long create_at, long modified_at) {
        this.title = title;
        this.description = description;
        this.create_at = create_at;
        this.modified_at = modified_at;
    }

    public long getModified_at() {
        return modified_at;
    }

    public void setModified_at(long modified_at) {
        this.modified_at = modified_at;
    }

    public long getCreate_at() {
        return create_at;
    }

    public void setCreate_at(long create_at) {
        this.create_at = create_at;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getNote_id() {
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
