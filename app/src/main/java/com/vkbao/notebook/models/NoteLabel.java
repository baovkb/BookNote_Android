package com.vkbao.notebook.models;

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
public class NoteLabel {
    private int note_id;
    private int label_id;

    public NoteLabel(int note_id, int label_id) {
        this.note_id = note_id;
        this.label_id = label_id;
    }

    public int getNote_id() {
        return note_id;
    }

    public int getLabel_id() {
        return label_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public void setLabel_id(int label_id) {
        this.label_id = label_id;
    }
}
