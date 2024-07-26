package com.vkbao.notebook.respository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.vkbao.notebook.daos.NoteDao;
import com.vkbao.notebook.databases.AppDatabase;
import com.vkbao.notebook.models.Note;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        noteDao = database.getNoteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note...notes) {
        new InsertThread(noteDao, notes).start();
    }

    public void update(Note...notes) {
        new UpdateThread(noteDao, notes).start();
    }

    public void delete(Note...notes) {
        new DeleteThread(noteDao, notes).start();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    //Runnable for database CRUD
    private static class InsertThread extends Thread {
        private NoteDao noteDao;
        private Note[] notes;

        private InsertThread(NoteDao noteDao, Note[] notes) {
            this.noteDao = noteDao;
            this.notes = notes;
        }

        @Override
        public void run() {
            noteDao.insert(notes);
        }
    }

    private static class DeleteThread extends Thread {
        private NoteDao noteDao;
        private Note[] notes;

        private DeleteThread(NoteDao noteDao, Note[] notes) {
            this.noteDao = noteDao;
            this.notes = notes;
        }

        @Override
        public void run() {
            noteDao.delete(notes);
        }
    }

    private static class UpdateThread extends Thread {
        private NoteDao noteDao;
        private Note[] notes;

        private UpdateThread(NoteDao noteDao, Note[] notes) {
            this.noteDao = noteDao;
            this.notes = notes;
        }

        @Override
        public void run() {
            noteDao.update(notes);
        }
    }
}
