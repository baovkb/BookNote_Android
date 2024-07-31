package com.vkbao.notebook.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.vkbao.notebook.models.Label;

import java.util.ArrayList;
import java.util.List;

public class AddNoteLabelViewModel extends AndroidViewModel {
    private MutableLiveData<List<Label>> labelMutableLiveData;

    public AddNoteLabelViewModel(@NonNull Application application) {
        super(application);
        this.labelMutableLiveData.setValue(new ArrayList<Label>());
    }

    public AddNoteLabelViewModel(@NonNull Application application, List<Label> labelList) {
        super(application);
        this.labelMutableLiveData.setValue(labelList);
    }

    public MutableLiveData<List<Label>> getLabelMutableLiveData() {
        return labelMutableLiveData;
    }

    public void addLabel(Label label) {
        this.labelMutableLiveData.getValue().add(label);
    }

    public void removeLabel(Label label) {
        this.labelMutableLiveData.getValue().remove(label);
    }

    public void removeLabel(int index) {
        this.labelMutableLiveData.getValue().remove(index);
    }
}
