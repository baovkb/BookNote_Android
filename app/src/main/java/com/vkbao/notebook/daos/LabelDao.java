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
    void insert(Label...label);

    @Update
    void update(Label...labels);

    @Delete
    void delete(Label...labels);

    @Query("SELECT * FROM Label WHERE name = :name")
    Label getLabelByName(String name);

    @Query("SELECT * FROM Label WHERE label_id = :label_id")
    Label getLabelByID(long label_id);

    @Query("DELETE FROM Label")
    void deleteAllLabels();

    @Query("SELECT * FROM Label")
    LiveData<List<Label>> getAllLabels();
}
