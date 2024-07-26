package com.vkbao.notebook.respository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vkbao.notebook.daos.ImageDao;
import com.vkbao.notebook.databases.AppDatabase;
import com.vkbao.notebook.models.Image;

import java.util.List;

public class ImageRepository {
    private ImageDao imageDao;
    private LiveData<List<Image>> allImages;

    public ImageRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        imageDao = database.getImageDao();
        allImages = imageDao.getAllImages();
    }

    public void insert(Image...images) {
        new InsertThread(imageDao, images).start();
    }

    public void update(Image...images) {
        new UpdateThread(imageDao, images).start();
    }

    public void delete(Image...images) {
        new DeleteThread(imageDao, images).start();
    }

    public LiveData<List<Image>> getAllImages() {
        return allImages;
    }

    //Runnable for database CRUD
    private static class InsertThread extends Thread {
        private ImageDao imageDao;
        private Image[] images;

        private InsertThread(ImageDao imageDao, Image[] images) {
            this.imageDao = imageDao;
            this.images = images;
        }

        @Override
        public void run() {
            imageDao.insert(images);
        }
    }

    private static class DeleteThread extends Thread {
        private ImageDao imageDao;
        private Image[] images;

        private DeleteThread(ImageDao imageDao, Image[] images) {
            this.imageDao = imageDao;
            this.images = images;
        }

        @Override
        public void run() {
            imageDao.delete(images);
        }
    }

    private static class UpdateThread extends Thread {
        private ImageDao imageDao;
        private Image[] images;

        private UpdateThread(ImageDao imageDao, Image[] images) {
            this.imageDao = imageDao;
            this.images = images;
        }

        @Override
        public void run() {
            imageDao.update(images);
        }
    }
}
