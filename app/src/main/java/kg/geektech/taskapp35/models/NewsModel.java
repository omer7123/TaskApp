package kg.geektech.taskapp35.models;

import java.io.Serializable;

public class NewsModel implements Serializable {
    private String title;
    private long createdAt;
    private String email;

    public NewsModel() {

    }

    public NewsModel(String title, long createdAt, String email) {
        this.title = title;
        this.createdAt = createdAt;
        this.email = email;
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
