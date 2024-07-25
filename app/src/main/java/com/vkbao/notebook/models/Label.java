package com.vkbao.notebook.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Label")
public class Label {
    @PrimaryKey(autoGenerate = true)
    private int label_id;

    private String name;

    public Label(String name) {
        this.name = name;
    }

    public int getLabel_id() {
        return label_id;
    }

    public void setLabel_id(int label_id) {
        this.label_id = label_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
