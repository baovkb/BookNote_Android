package com.vkbao.notebook.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.respository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.getAllNotes();
    }

    public void insert(Note...notes) {
        noteRepository.insert(notes);
    }
    public void update(Note...notes) {
        noteRepository.update(notes);
    }
    public void delete(Note...notes) {
        noteRepository.delete(notes);
    }
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

}
