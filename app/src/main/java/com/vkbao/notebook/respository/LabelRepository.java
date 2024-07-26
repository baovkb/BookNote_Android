package com.vkbao.notebook.respository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vkbao.notebook.daos.LabelDao;
import com.vkbao.notebook.databases.AppDatabase;
import com.vkbao.notebook.models.Label;

import java.util.List;

public class LabelRepository {
    private LabelDao labelDao;
    private LiveData<List<Label>> allLabels;

    public LabelRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        labelDao = appDatabase.getLabelDao();
        allLabels = labelDao.getAllLabels();
    }

    public LiveData<List<Label>> getAllLabels() {
        return allLabels;
    }

    public void insert(Label...labels) {
        new InsertThread(labelDao, labels).start();
    }

    public void update(Label...labels) {
        new UpdateThread(labelDao, labels).start();
    }

    public void delete(Label...labels) {
        new DeleteThread(labelDao, labels).start();
    }

    private static class InsertThread extends Thread {
        private LabelDao labelDao;
        Label[] labels;

        private InsertThread(LabelDao labelDao, Label...labesl) {
            this.labelDao = labelDao;
            this.labels = labels;
        }

        @Override
        public void run() {
            labelDao.insert(labels);
        }
    }

    private static class UpdateThread extends Thread {
        private LabelDao labelDao;
        Label[] labels;

        private UpdateThread(LabelDao labelDao, Label...labels) {
            this.labelDao = labelDao;
            this.labels = labels;
        }

        @Override
        public void run() {
            labelDao.update(labels);
        }
    }

    private static class DeleteThread extends Thread {
        private LabelDao labelDao;
        Label[] labels;

        private DeleteThread(LabelDao labelDao, Label...labels) {
            this.labelDao = labelDao;
            this.labels = labels;
        }

        @Override
        public void run() {
            labelDao.delete(labels);
        }
    }
}
