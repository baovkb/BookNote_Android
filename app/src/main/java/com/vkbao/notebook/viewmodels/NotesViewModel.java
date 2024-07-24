package com.vkbao.notebook.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vkbao.notebook.models.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesViewModel extends ViewModel {
    private MutableLiveData<List<Note>> notes;

    NotesViewModel() {
        notes = new MutableLiveData<>(new ArrayList<>());
    }

    public boolean setNote(Note note) {
        List<Note> listNotes = notes.getValue();
        if (listNotes != null) {
            listNotes.add(note);
            //notify that notes is changed to observers
            notes.setValue(listNotes);
            return true;
        }
        return false;
    }


    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public boolean removeNote(int noteID) {
        List<Note> listNotes = notes.getValue();
        if (listNotes != null) {
            listNotes.remove(noteID);
            //notify that notes is changed to observers
            notes.setValue(listNotes);
            return true;
        }
        return false;
    }

}
