package com.vkbao.notebook.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vkbao.notebook.helper.CallBack;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.respository.LabelRepository;

import java.util.List;

public class LabelViewModel extends AndroidViewModel {
    private LabelRepository labelRepository;
    private LiveData<List<Label>> allLabel;

    public LabelViewModel(@NonNull Application application) {
        super(application);
        labelRepository = new LabelRepository(application);
        allLabel = labelRepository.getAllLabels();
    }

    public void insert(Label...labels) {
        labelRepository.insert(labels);
    }
    public void update(Label...labels) {
        labelRepository.update(labels);
    }
    public void delete(Label...labels) {
        labelRepository.delete(labels);
    }
    public void deleteAll() {
        labelRepository.deleteAll();
    }
    public LiveData<List<Label>> getAllLabel() {
        return allLabel;
    }
    public void getLabelByName(String name, CallBack<Label> callBack) {
        labelRepository.getLabelByName(name, callBack);
    }
    public void getLabelByID(long label_id, CallBack<Label> callBack) {
        labelRepository.getLabelByID(label_id, callBack);
    }
}
