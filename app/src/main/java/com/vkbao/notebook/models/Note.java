package com.vkbao.notebook.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Note")
public class Note implements Parcelable {
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

    @Ignore
    public Note(long note_id, String title, String description, long create_at, long modified_at) {
        this(title, description, create_at, modified_at);
        this.note_id = note_id;
    }

    protected Note(Parcel in) {
        note_id = in.readLong();
        title = in.readString();
        description = in.readString();
        create_at = in.readLong();
        modified_at = in.readLong();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(note_id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeLong(create_at);
        parcel.writeLong(modified_at);
    }
}
