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

    public void insert(Note note) {
        new InsertThread(noteDao, note).start();
    }

    public void update(Note note) {
        new UpdateThread(noteDao, note).start();
    }

    public void delete(Note note) {
        new DeleteThread(noteDao, note).start();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    //Runnable for database CRUD
    private static class InsertThread extends Thread {
        private NoteDao noteDao;
        private Note note;

        private InsertThread(NoteDao noteDao, Note note) {
            this.noteDao = noteDao;
            this.note = note;
        }

        @Override
        public void run() {
            noteDao.insert(note);
        }
    }

    private static class DeleteThread extends Thread {
        private NoteDao noteDao;
        private Note note;

        private DeleteThread(NoteDao noteDao, Note notes) {
            this.noteDao = noteDao;
            this.note = note;
        }

        @Override
        public void run() {
            noteDao.delete(note);
        }
    }

    private static class UpdateThread extends Thread {
        private NoteDao noteDao;
        private Note note;

        private UpdateThread(NoteDao noteDao, Note note) {
            this.noteDao = noteDao;
            this.note = note;
        }

        @Override
        public void run() {
            noteDao.update(note);
        }
    }
}
