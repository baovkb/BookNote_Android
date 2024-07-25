package com.vkbao.notebook.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Image",
        foreignKeys = @ForeignKey(
                entity = Note.class,
                parentColumns = "note_id",
                childColumns = "note_id",
                onDelete = ForeignKey.CASCADE))
public class Image {
    @PrimaryKey(autoGenerate = true)
    private int image_id;
    private int note_id;
    private String url;
    private String description;

    public Image(int note_id, String url, String description) {
        this.note_id = note_id;
        this.url = url;
        this.description = description;
    }

    public int getImage_id() {
        return image_id;
    }

    public String getUrl() {
        return url;
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

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }
}
