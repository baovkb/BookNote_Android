package com.vkbao.notebook.models;

import java.util.ArrayList;
import java.util.List;

public class Note{
    private String title;
    private String content;
    private List<String> urlImage;

    public Note() {
        title = "";
        content = "";
        urlImage = new ArrayList<>();
    }

    public Note(String title, String content, List<String> urlImage) {
        this.title = title;
        this.content = content;
        this.urlImage = urlImage;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) { this.title = title;}
    public String getContent() { return content;}
    public void setContent(String content) { this.content = content; }
    public List<String> getUrlImage() { return urlImage;}
    public void setUrlImage(List<String> urlImage) { this.urlImage = urlImage; }
}
