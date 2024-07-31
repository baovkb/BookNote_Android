package com.vkbao.notebook.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "NoteLabel",
        primaryKeys = {"note_id", "label_id"},
        foreignKeys = {
            @ForeignKey(entity = Note.class,
                    parentColumns = "note_id",
                    childColumns = "note_id",
                    onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Label.class,
                    parentColumns = "label_id",
                    childColumns = "label_id",
                    onDelete = ForeignKey.CASCADE)
        })
public class NoteLabel implements Parcelable {
    private long note_id;
    private long label_id;

    public NoteLabel(long note_id, long label_id) {
        this.note_id = note_id;
        this.label_id = label_id;
    }

    protected NoteLabel(Parcel in) {
        note_id = in.readLong();
        label_id = in.readLong();
    }

    public static final Creator<NoteLabel> CREATOR = new Creator<NoteLabel>() {
        @Override
        public NoteLabel createFromParcel(Parcel in) {
            return new NoteLabel(in);
        }

        @Override
        public NoteLabel[] newArray(int size) {
            return new NoteLabel[size];
        }
    };

    public long getNote_id() {
        return note_id;
    }

    public long getLabel_id() {
        return label_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public void setLabel_id(int label_id) {
        this.label_id = label_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(note_id);
        parcel.writeLong(label_id);
    }
}
