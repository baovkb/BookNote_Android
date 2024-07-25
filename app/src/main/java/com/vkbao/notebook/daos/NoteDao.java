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

    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM Note WHERE note_id=:note_id")
    Note findNoteByID(final int note_id);
}
