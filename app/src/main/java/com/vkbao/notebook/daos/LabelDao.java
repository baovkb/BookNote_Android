package com.vkbao.notebook.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.vkbao.notebook.models.Label;

import java.util.List;

@Dao
public interface LabelDao {
    @Insert
    public void insert(Label...label);

    @Update
    public void update(Label...labels);

    @Delete
    public void delete(Label...labels);

    @Query("SELECT * FROM Label")
    public LiveData<List<Label>> getAllLabels();
}
