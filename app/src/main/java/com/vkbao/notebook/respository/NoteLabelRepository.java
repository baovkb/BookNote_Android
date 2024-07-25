package com.vkbao.notebook.respository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vkbao.notebook.daos.ImageDao;
import com.vkbao.notebook.daos.NoteLabelDao;
import com.vkbao.notebook.databases.AppDatabase;
import com.vkbao.notebook.models.Image;
import com.vkbao.notebook.models.Note;
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

    public void insert(NoteLabel noteLabel) {
        new InsertThread(noteLabelDao, noteLabel).start();
    }

    public void update(NoteLabel noteLabel) {
        new UpdateThread(noteLabelDao, noteLabel).start();
    }

    public void delete(NoteLabel noteLabel) {
        new DeleteThread(noteLabelDao, noteLabel).start();
    }

    private static class InsertThread extends Thread {
        private NoteLabelDao noteLabelDao;
        private NoteLabel noteLabel;

        private InsertThread(NoteLabelDao noteLabelDao, NoteLabel noteLabel) {
            this.noteLabelDao = noteLabelDao;
            this.noteLabel = noteLabel;
        }

        @Override
        public void run() {
            noteLabelDao.insert(noteLabel);
        }
    }

    private static class UpdateThread extends Thread {
        private NoteLabelDao noteLabelDao;
        private NoteLabel noteLabel;

        private UpdateThread(NoteLabelDao noteLabelDao, NoteLabel noteLabel) {
            this.noteLabelDao = noteLabelDao;
            this.noteLabel = noteLabel;
        }

        @Override
        public void run() {
            noteLabelDao.update(noteLabel);
        }
    }

    private static class DeleteThread extends Thread {
        private NoteLabelDao noteLabelDao;
        private NoteLabel noteLabel;

        private DeleteThread(NoteLabelDao noteLabelDao, NoteLabel noteLabel) {
            this.noteLabelDao = noteLabelDao;
            this.noteLabel = noteLabel;
        }

        @Override
        public void run() {
            noteLabelDao.delete(noteLabel);
        }
    }
}
