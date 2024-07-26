package com.vkbao.notebook.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vkbao.notebook.models.Image;
import com.vkbao.notebook.respository.ImageRepository;

import java.util.List;

public class ImageViewModel extends AndroidViewModel {
    private ImageRepository imageRepository;
    private LiveData<List<Image>> allImages;

    public ImageViewModel(@NonNull Application application) {
        super(application);
        imageRepository = new ImageRepository(application);
        allImages = imageRepository.getAllImages();
    }

    public void insert(Image...images) {
        imageRepository.insert(images);
    }
    public void update(Image...images) {
        imageRepository.update(images);
    }
    public void delete(Image...images) {
        imageRepository.delete(images);
    }
    public LiveData<List<Image>> getAllNotes() {
        return allImages;
    }
}
