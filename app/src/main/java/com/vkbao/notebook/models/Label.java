package com.vkbao.notebook.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Label")
public class Label implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long label_id;
    
    private String name;

    public Label(String name) {
        this.name = name;
    }

    @Ignore
    public Label(long label_id, String name) {
        this.label_id = label_id;
        this.name = name;
    }

    protected Label(Parcel in) {
        label_id = in.readLong();
        name = in.readString();
    }

    public static final Creator<Label> CREATOR = new Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel in) {
            return new Label(in);
        }

        @Override
        public Label[] newArray(int size) {
            return new Label[size];
        }
    };

    public long getLabel_id() {
        return label_id;
    }

    public void setLabel_id(long label_id) {
        this.label_id = label_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Label)) {
            return false;
        } else {
            return ((Label) o).getLabel_id() == this.getLabel_id()
                    && ((Label) o).getName().equals(this.getName());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(label_id);
        parcel.writeString(name);
    }
}
