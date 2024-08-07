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

    @Query("DELETE FROM NoteLabel")
    void deleteAllNoteLabel();

    @Query("SELECT * FROM NoteLabel")
    LiveData<List<NoteLabel>> getAllNoteLabel();

    @Query("SELECT * FROM NoteLabel " +
            "WHERE note_id = :note_id ")
    List<NoteLabel> getNoteLabelByNoteID(final long note_id);

    @Query("SELECT * FROM NoteLabel " +
            "WHERE label_id = :label_id")
    List<NoteLabel> getNoteLabelByLabelID(final long label_id);

    @Query("SELECT Label.* FROM Note, NoteLabel, Label " +
            "WHERE Note.note_id = NoteLabel.note_id " +
            "AND Label.label_id = NoteLabel.label_id " +
            "AND Note.note_id = :note_id")
    List<Label> getLabelsByNoteID(long note_id);

    @Query("SELECT Note.* FROM Note, NoteLabel, Label " +
            "WHERE Note.note_id = NoteLabel.note_id " +
            "AND Label.label_id = NoteLabel.label_id " +
            "AND Label.label_id = :label_id")
    List<Note> getNotesByLabelID(long label_id);

    @Query("SELECT Note.* FROM Note, NoteLabel, Label " +
            "WHERE Note.note_id = NoteLabel.note_id " +
            "AND Label.label_id = NoteLabel.label_id " +
            "AND Label.name = :label_name")
    List<Note> getNotesByLabelName(String label_name);

    @Query("SELECT Note.* FROM Note, NoteLabel, Label " +
            "WHERE Note.note_id = NoteLabel.note_id " +
            "AND Label.label_id = NoteLabel.label_id " +
            "AND Label.name = :label_name")
    LiveData<List<Note>> getNotesLiveDataByLabelName(String label_name);

    @Query("DELETE FROM NoteLabel WHERE note_id = :note_id")
    void deleteNoteLabelByNoteID(long note_id);
}
