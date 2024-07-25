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

    @Query("SELECT * FROM Image WHERE note_id=:note_id")
    List<Image> getImagesByNoteID(final int note_id);

    @Query("SELECT * FROM IMAGE")
    LiveData<List<Image>> getAllImages();
}
