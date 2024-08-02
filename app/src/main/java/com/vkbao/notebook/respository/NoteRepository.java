package com.vkbao.notebook.respository;

import android.app.Application;
import android.os.Looper;
import android.util.Log;
import android.os.Handler;

import androidx.lifecycle.LiveData;

import com.vkbao.notebook.daos.NoteDao;
import com.vkbao.notebook.databases.AppDatabase;
import com.vkbao.notebook.helper.CallBack;
import com.vkbao.notebook.models.Note;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public NoteRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        noteDao = database.getNoteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(CallBack<long[]> callBack, Note...notes) {
        executorService.execute(() -> {
            long[] note_ids = noteDao.insert(notes);
            if (callBack != null) {
                new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(note_ids));
            }
        });
    }

    public void update(Note...notes) {
        executorService.execute(() -> noteDao.update(notes));
    }

    public void delete(Note...notes) {
        executorService.execute(() -> noteDao.delete(notes));
    }

    public void deleteByID(long note_id) {
        executorService.execute(() -> noteDao.deleteNoteByID(note_id));
    }

    public void deleteAll() {
        executorService.execute(() -> noteDao.deleteAllNotes());
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public void getNoteByID(long note_id, CallBack<Note> callBack) {
        executorService.execute(() -> {
            Note note = noteDao.getNoteByID(note_id);
            new Handler(Looper.getMainLooper()).post(() -> callBack.onResult(note));
        });
    }

    public void searchNote(String keyword, CallBack<List<Note>> callBack) {
        executorService.execute(() -> {
            List<Note> noteListLiveData = noteDao.searchNote(keyword);
            new Handler(Looper.getMainLooper()).post(() -> {
                callBack.onResult(noteListLiveData);
            });
        });
    }
}
