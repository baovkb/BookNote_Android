package com.vkbao.notebook.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.vkbao.notebook.models.Image;

import java.util.List;

@Dao
public interface ImageDao {
    @Insert
    void insert(Image...images);

    @Update
    void update(Image...images);

    @Delete
    void delete(Image...images);

    @Query("DELETE FROM Image")
    void deleteAllImages();

    @Query("DELETE FROM Image WHERE image_id = :image_id")
    void deleteImageByID(long image_id);

    @Query("DELETE FROM Image WHERE note_id = :note_id")
    void deleteImageByNoteID(long note_id);

    @Query("SELECT * FROM Image WHERE note_id=:note_id")
    List<Image> getImagesByNoteID(final long note_id);

    @Query("SELECT * FROM Image")
    LiveData<List<Image>> getAllImages();
}
