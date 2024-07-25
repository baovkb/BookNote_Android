package com.vkbao.notebook.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.models.NoteLabel;

import java.util.List;

@Dao
public interface NoteLabelDao {
    @Insert
    void insert(NoteLabel...noteLabels);

    @Update
    void update(NoteLabel...noteLabels);

    @Delete
    void delete(NoteLabel...noteLabels);

    @Query("SELECT * FROM NoteLabel")
    LiveData<List<NoteLabel>> getAllNoteLabel();

    @Query("SELECT * FROM Note, Label, NoteLabel " +
            "WHERE Note.note_id = NoteLabel.note_id " +
            "AND Label.label_id = NoteLabel.label_id " +
            "AND Note.note_id = :note_id")
    List<Label> getLabelsByNoteID(final int note_id);

    @Query("SELECT * FROM Note, Label, NoteLabel " +
            "WHERE Note.note_id = NoteLabel.note_id " +
            "AND Label.label_id = NoteLabel.label_id " +
            "AND Label.label_id = :label_id")
    List<Note> getNotesByLabelID(final int label_id);
}