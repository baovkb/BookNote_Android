package com.vkbao.notebook.databases;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.vkbao.notebook.daos.ImageDao;
import com.vkbao.notebook.daos.LabelDao;
import com.vkbao.notebook.daos.NoteDao;
import com.vkbao.notebook.daos.NoteLabelDao;
import com.vkbao.notebook.models.Image;
import com.vkbao.notebook.models.Label;
import com.vkbao.notebook.models.Note;
import com.vkbao.notebook.models.NoteLabel;

@Database(entities = {
        Image.class,
        Label.class,
        Note.class,
        NoteLabel.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract NoteDao getNoteDao();
    public abstract LabelDao getLabelDao();
    public abstract ImageDao getImageDao();
    public abstract NoteLabelDao getNoteLabelDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Note_App_Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

}
