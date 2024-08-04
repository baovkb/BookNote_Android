package com.vkbao.notebook.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vkbao.notebook.models.Image;
import com.vkbao.notebook.respository.ImageRepository;

import java.util.List;
import java.util.concurrent.Future;

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

    public void deleteAll() {
        imageRepository.deleteAll();
    }

    public void deleteByID(long image_id) {
        imageRepository.deleteByID(image_id);
    }

    public void deleteByNoteID(long note_id) {
        imageRepository.deleteByNoteID(note_id);
    }

    public void deleteByName(String name) {
        imageRepository.deleteByName(name);
    }

    public Future<List<Image>> getImagesByNoteID(long note_id) {
        return imageRepository.getImagesByNoteID(note_id);
    }

    public LiveData<List<Image>> getAll() {
        return allImages;
    }
}
