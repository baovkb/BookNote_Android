package com.vkbao.notebook.respository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vkbao.notebook.daos.LabelDao;
import com.vkbao.notebook.daos.NoteDao;
import com.vkbao.notebook.daos.NoteLabelDao;
import com.vkbao.notebook.databases.AppDatabase;
import com.vkbao.notebook.helper.CallBack;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.models.NoteLabel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NoteLabelRepository {
    private NoteLabelDao noteLabelDao;
    private NoteDao noteDao;
    private LabelDao labelDao;
    private LiveData<List<NoteLabel>> allNoteLabel;
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public NoteLabelRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        noteLabelDao = database.getNoteLabelDao();
        noteDao = database.getNoteDao();
        labelDao = database.getLabelDao();
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

    public void getLabelsByNoteID(long note_id, CallBack<List<Label>> callBack) {
        executorService.execute(() -> {
            List<Label> labelList = noteLabelDao.getLabelsByNoteID(note_id);
            if (labelList != null) {
                new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(labelList));
            } else
                new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(new ArrayList<>()));
        });
    }

    public Future<List<Label>> getLabelsByNoteID(long note_id) {
        return executorService.submit(new Callable<List<Label>>() {
            @Override
            public List<Label> call() throws Exception {
                return noteLabelDao.getLabelsByNoteID(note_id);
            }
        });
    }

    public void getNotesByLabelID(long label_id, CallBack<List<Note>> callBack) {
        executorService.execute(() -> {
            List<Note> noteList = noteLabelDao.getNotesByLabelID(label_id);
            if (noteList != null) {
                new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(noteList));
            } else {
                new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(new ArrayList<>()));
            }
        });
    }

    public void getNoteLabelByNoteID(long note_id, CallBack<List<NoteLabel>> callBack) {
        executorService.execute(() -> {
            List<NoteLabel> res = noteLabelDao.getNoteLabelByNoteID(note_id);
            if (res != null) {
                new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(res));
            } else {
                new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(new ArrayList<>()));
            }

        });
    }

    public void getNoteLabelByLabelID(long label_id, CallBack<List<NoteLabel>> callBack) {
        executorService.execute(() -> {
            List<NoteLabel> res = noteLabelDao.getNoteLabelByLabelID(label_id);
            if (res != null) {
                new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(res));
            } else {
                new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(new ArrayList<>()));
            }
        });
    }

    public void getNotesByLabelName(String label_name, CallBack<List<Note>> callBack) {
        executorService.execute(() -> {
            List<Note> noteList = noteLabelDao.getNotesByLabelName(label_name);
            if (noteList != null) {
                new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(noteList));
            } else {
                new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(new ArrayList<>()));
            }
        });
    }

    public void getNotesLiveDataByLabelName(String label_name, CallBack<LiveData<List<Note>>> callBack) {
        executorService.execute(() -> {
            LiveData<List<Note>> noteListLiveData = noteLabelDao.getNotesLiveDataByLabelName(label_name);

            new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(noteListLiveData));
        });
    }

    public void deleteNoteLabelByNoteID(long note_id) {
        executorService.execute(() -> {
            noteLabelDao.deleteNoteLabelByNoteID(note_id);
        });
    }
}
