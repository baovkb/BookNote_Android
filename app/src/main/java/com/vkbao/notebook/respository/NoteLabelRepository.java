package com.vkbao.notebook.respository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vkbao.notebook.daos.LabelDao;
import com.vkbao.notebook.daos.NoteDao;
import com.vkbao.notebook.daos.NoteLabelDao;
import com.vkbao.notebook.databases.AppDatabase;
import com.vkbao.notebook.helper.CallBack;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.models.NoteLabel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
            List<NoteLabel> noteLabelList = noteLabelDao.getNoteLabelByNoteID(note_id);
            List<Label> labelList = new ArrayList<>();
            for (NoteLabel fNoteLabel: noteLabelList) {
                Label findedLabel = labelDao.getLabelByID(fNoteLabel.getLabel_id());
                if (findedLabel != null) {
                    labelList.add(findedLabel);
                }
            }
            callBack.onResult(labelList);
        });
    }

    public void getNoteLabelByNoteID(long note_id, CallBack<List<NoteLabel>> callBack) {
        executorService.execute(() -> {
            List<NoteLabel> res = noteLabelDao.getNoteLabelByNoteID(note_id);
            if (res != null) {
                callBack.onResult(res);
            } else {
                callBack.onResult(new ArrayList<>());
            }

        });
    }
}
