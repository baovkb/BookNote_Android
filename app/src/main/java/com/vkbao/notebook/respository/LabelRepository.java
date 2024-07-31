package com.vkbao.notebook.respository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vkbao.notebook.daos.LabelDao;
import com.vkbao.notebook.databases.AppDatabase;
import com.vkbao.notebook.helper.CallBack;
import com.vkbao.notebook.models.Label;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LabelRepository {
    private LabelDao labelDao;
    private LiveData<List<Label>> allLabels;
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public LabelRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        labelDao = appDatabase.getLabelDao();
        allLabels = labelDao.getAllLabels();
    }

    public LiveData<List<Label>> getAllLabels() {
        return allLabels;
    }

    public void insert(Label...labels) {
        executorService.execute(() -> labelDao.insert(labels));
    }

    public void update(Label...labels) {
        executorService.execute(() -> labelDao.update(labels));
    }

    public void delete(Label...labels) {
        executorService.execute(() -> labelDao.delete(labels));
    }

    public void deleteAll() {
        executorService.execute(() -> labelDao.deleteAllLabels());
    }

    public void getLabelByName(String name, CallBack<Label> callBack) {
        executorService.execute(() -> {
            Label label = labelDao.getLabelByName(name);
            callBack.onResult(label);
        });
    }

    public void getLabelByID(long label_id, CallBack<Label> callBack) {
        executorService.execute(() -> {
            Label label = labelDao.getLabelByID(label_id);
            callBack.onResult(label);
        });
    }
}
