package com.vkbao.notebook.respository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vkbao.notebook.daos.NoteLabelDao;
import com.vkbao.notebook.databases.AppDatabase;
import com.vkbao.notebook.models.NoteLabel;

import java.util.List;

public class NoteLabelRepository {
    private NoteLabelDao noteLabelDao;
    private LiveData<List<NoteLabel>> allNoteLabel;

    public NoteLabelRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        noteLabelDao = database.getNoteLabelDao();
        allNoteLabel = noteLabelDao.getAllNoteLabel();
    }

    public void insert(NoteLabel...noteLabels) {
        new InsertThread(noteLabelDao, noteLabels).start();
    }

    public void update(NoteLabel...noteLabels) {
        new UpdateThread(noteLabelDao, noteLabels).start();
    }

    public void delete(NoteLabel...noteLabels) {
        new DeleteThread(noteLabelDao, noteLabels).start();
    }

    public LiveData<List<NoteLabel>> getAllNoteLabel() {
        return allNoteLabel;
    }

    private static class InsertThread extends Thread {
        private NoteLabelDao noteLabelDao;
        private NoteLabel[] noteLabels;

        private InsertThread(NoteLabelDao noteLabelDao, NoteLabel[] noteLabels) {
            this.noteLabelDao = noteLabelDao;
            this.noteLabels = noteLabels;
        }

        @Override
        public void run() {
            noteLabelDao.insert(noteLabels);
        }
    }

    private static class UpdateThread extends Thread {
        private NoteLabelDao noteLabelDao;
        private NoteLabel[] noteLabels;

        private UpdateThread(NoteLabelDao noteLabelDao, NoteLabel[] noteLabels) {
            this.noteLabelDao = noteLabelDao;
            this.noteLabels = noteLabels;
        }

        @Override
        public void run() {
            noteLabelDao.update(noteLabels);
        }
    }

    private static class DeleteThread extends Thread {
        private NoteLabelDao noteLabelDao;
        private NoteLabel[] noteLabels;

        private DeleteThread(NoteLabelDao noteLabelDao, NoteLabel[] noteLabels) {
            this.noteLabelDao = noteLabelDao;
            this.noteLabels = noteLabels;
        }

        @Override
        public void run() {
            noteLabelDao.delete(noteLabels);
        }
    }
}
