package com.vkbao.notebook.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.vkbao.notebook.models.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(Note...notes);

    @Update
    void update(Note...notes);

    @Delete
    void delete(Note...notes);

    @Query("DELETE FROM Note")
    void deleteAllNotes();

    @Query("DELETE FROM Note WHERE note_id = :note_id")
    void deleteNoteByID(long note_id);

    @Query("SELECT * FROM Note ORDER BY modified_at DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM Note WHERE note_id=:note_id")
    Note getNoteByID(final long note_id);
}
