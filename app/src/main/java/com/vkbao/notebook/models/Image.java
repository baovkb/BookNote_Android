package com.vkbao.notebook.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Image",
        foreignKeys = @ForeignKey(
                entity = Note.class,
                parentColumns = "note_id",
                childColumns = "note_id",
                onDelete = ForeignKey.CASCADE))
public class Image implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long image_id;
    private long note_id;
    private String url;
    private String description;

    public Image(long note_id, String url, String description) {
        this.note_id = note_id;
        this.url = url;
        this.description = description;
    }

    protected Image(Parcel in) {
        image_id = in.readLong();
        note_id = in.readLong();
        url = in.readString();
        description = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public long getImage_id() {
        return image_id;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public long getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(image_id);
        parcel.writeLong(note_id);
        parcel.writeString(url);
        parcel.writeString(description);
    }
}
