package com.vkbao.notebook.viewmodels;

import android.app.Application;
import android.telecom.Call;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vkbao.notebook.helper.CallBack;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.models.NoteLabel;
import com.vkbao.notebook.respository.NoteLabelRepository;

import java.util.List;
import java.util.concurrent.Future;

public class NoteLabelViewModel extends AndroidViewModel {
    private NoteLabelRepository noteLabelRepository;
    private LiveData<List<NoteLabel>> allNoteLabel;

    public NoteLabelViewModel(@NonNull Application application) {
        super(application);
        noteLabelRepository = new NoteLabelRepository(application);
        allNoteLabel = noteLabelRepository.getAllNoteLabel();
    }

    public void insert(NoteLabel...noteLabel) {
        noteLabelRepository.insert(noteLabel);
    }

    public void update(NoteLabel...noteLabel) {
        noteLabelRepository.update(noteLabel);
    }

    public void delete(NoteLabel...noteLabel) {
        noteLabelRepository.delete(noteLabel);
    }

    public void deleteAll() {
        noteLabelRepository.deleteAll();
    }

    public LiveData<List<NoteLabel>> getAllNoteLabel() {
        return allNoteLabel;
    }

    public void getLabelsByNoteID(long note_id, CallBack<List<Label>> callBack) {
        noteLabelRepository.getLabelsByNoteID(note_id, callBack);
    }

    public Future<List<Label>> getLabelsByNoteID(long note_id) {
        return noteLabelRepository.getLabelsByNoteID(note_id);
    }

    public void getNotesByLabelID(long label_id, CallBack<List<Note>> callBack) {
        noteLabelRepository.getNotesByLabelID(label_id, callBack);
    }

    public void getNoteLabelByNoteID(long note_id, CallBack<List<NoteLabel>> callBack) {
        noteLabelRepository.getNoteLabelByNoteID(note_id, callBack);
    }

    public void getNoteLabelByLabelID(long label_id, CallBack<List<NoteLabel>> callBack) {
        noteLabelRepository.getNoteLabelByLabelID(label_id, callBack);
    }

    public void getNotesByLabelName(String label_name, CallBack<List<Note>> callBack) {
        noteLabelRepository.getNotesByLabelName(label_name, callBack);
    }

    public void getNotesLiveDataByLabelName(String label_name, CallBack<LiveData<List<Note>>> callBack) {
        noteLabelRepository.getNotesLiveDataByLabelName(label_name, callBack);
    }

    public void deleteNoteLabelByNoteID(long note_id) {
        noteLabelRepository.deleteNoteLabelByNoteID(note_id);
    }
}
