package com.vkbao.notebook.respository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vkbao.notebook.daos.ImageDao;
import com.vkbao.notebook.daos.NoteDao;
import com.vkbao.notebook.databases.AppDatabase;
import com.vkbao.notebook.models.Image;
import com.vkbao.notebook.models.Note;

import java.util.List;

public class ImageRepository {
    private ImageDao imageDao;
    private LiveData<List<Image>> allImages;

    public ImageRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        imageDao = database.getImageDao();
        allImages = imageDao.getAllImages();
    }

    public void insert(Image image) {
        new InsertThread(imageDao, image).start();
    }

    public void update(Image image) {
        new UpdateThread(imageDao, image).start();
    }

    public void delete(Image image) {
        new DeleteThread(imageDao, image).start();
    }

    public LiveData<List<Image>> getAllImages() {
        return allImages;
    }

    //Runnable for database CRUD
    private static class InsertThread extends Thread {
        private ImageDao imageDao;
        private Image image;

        private InsertThread(ImageDao imageDao, Image image) {
            this.imageDao = imageDao;
            this.image = image;
        }

        @Override
        public void run() {
            imageDao.insert(image);
        }
    }

    private static class DeleteThread extends Thread {
        private ImageDao imageDao;
        private Image image;

        private DeleteThread(ImageDao imageDao, Image image) {
            this.imageDao = imageDao;
            this.image = image;
        }

        @Override
        public void run() {
            imageDao.delete(image);
        }
    }

    private static class UpdateThread extends Thread {
        private ImageDao imageDao;
        private Image image;

        private UpdateThread(ImageDao imageDao, Image image) {
            this.imageDao = imageDao;
            this.image = image;
        }

        @Override
        public void run() {
            imageDao.update(image);
        }
    }
}
