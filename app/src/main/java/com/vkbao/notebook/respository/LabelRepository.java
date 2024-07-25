package com.vkbao.notebook.respository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vkbao.notebook.daos.LabelDao;
import com.vkbao.notebook.databases.AppDatabase;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;

import java.util.List;

public class LabelRepository {
    private LabelDao labelDao;
    private LiveData<List<Label>> allLabels;

    public LabelRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        labelDao = appDatabase.getLabelDao();
        allLabels = labelDao.getAllLabels();
    }

    LiveData<List<Label>> getAllLabels() {
        return allLabels;
    }

    public void insert(Label label) {
        new InsertThread(labelDao, label).start();
    }

    public void update(Label label) {
        new UpdateThread(labelDao, label).start();
    }

    public void delete(Label label) {
        new DeleteThread(labelDao, label).start();
    }

    private static class InsertThread extends Thread {
        private LabelDao labelDao;
        Label label;

        private InsertThread(LabelDao labelDao, Label label) {
            this.labelDao = labelDao;
            this.label = label;
        }

        @Override
        public void run() {
            labelDao.insert(label);
        }
    }

    private static class UpdateThread extends Thread {
        private LabelDao labelDao;
        Label label;

        private UpdateThread(LabelDao labelDao, Label label) {
            this.labelDao = labelDao;
            this.label = label;
        }

        @Override
        public void run() {
            labelDao.update(label);
        }
    }

    private static class DeleteThread extends Thread {
        private LabelDao labelDao;
        Label label;

        private DeleteThread(LabelDao labelDao, Label label) {
            this.labelDao = labelDao;
            this.label = label;
        }

        @Override
        public void run() {
            labelDao.delete(label);
        }
    }
}
