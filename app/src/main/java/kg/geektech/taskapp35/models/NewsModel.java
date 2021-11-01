package kg.geektech.taskapp35.models;

import android.net.Uri;

import java.io.Serializable;
import java.net.URL;

public class NewsModel implements Serializable {
    private String title;
    private long createdAt;
    private String email;
    private String id;
    private String describe;
    private String imageUrl;
    private long view_count;

    public NewsModel() {

    }

    public NewsModel(String title, long createdAt, String email) {
        this.title = title;
        this.createdAt = createdAt;
        this.email = email;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getView_count() {
        return view_count;
    }

    public void setView_count(long view_count) {
        this.view_count = view_count;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
