package com.vkbao.notebook.respository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vkbao.notebook.daos.ImageDao;
import com.vkbao.notebook.databases.AppDatabase;
import com.vkbao.notebook.models.Image;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageRepository {
    private ImageDao imageDao;
    private LiveData<List<Image>> allImages;
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ImageRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        imageDao = database.getImageDao();
        allImages = imageDao.getAllImages();
    }

    public void insert(Image...images) {
        executorService.execute(() -> imageDao.insert(images));
    }

    public void update(Image...images) {
        executorService.execute(() -> imageDao.update(images));
    }

    public void delete(Image...images) {
        executorService.execute(() -> imageDao.delete(images));
    }

    public void deleteAll() {
        executorService.execute(() -> imageDao.deleteAllImages());
    }

    public void deleteByID(long image_id) {
        executorService.execute(() -> imageDao.deleteImageByID(image_id));
    }

    public LiveData<List<Image>> getAllImages() {
        return allImages;
    }

}
