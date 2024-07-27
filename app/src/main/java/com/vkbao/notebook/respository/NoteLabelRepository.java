package com.vkbao.notebook.respository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vkbao.notebook.daos.NoteLabelDao;
import com.vkbao.notebook.databases.AppDatabase;
import com.vkbao.notebook.models.NoteLabel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteLabelRepository {
    private NoteLabelDao noteLabelDao;
    private LiveData<List<NoteLabel>> allNoteLabel;
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public NoteLabelRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        noteLabelDao = database.getNoteLabelDao();
        allNoteLabel = noteLabelDao.getAllNoteLabel();
    }

    public void insert(NoteLabel...noteLabels) {
        executorService.execute(() -> noteLabelDao.insert(noteLabels));
    }

    public void update(NoteLabel...noteLabels) {
        executorService.execute(() -> noteLabelDao.update(noteLabels));
    }

    public void delete(NoteLabel...noteLabels) {
        executorService.execute(() -> noteLabelDao.delete(noteLabels));
    }

    public void deleteAll() {
        executorService.execute(() -> noteLabelDao.deleteAllNoteLabel());
    }

    public LiveData<List<NoteLabel>> getAllNoteLabel() {
        return allNoteLabel;
    }
}
