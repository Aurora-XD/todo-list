package com.thoughtworks.todo_list.repository.user.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private boolean isFinish;
    private Date deadline;
    private boolean isRemind;
    private String header;
    private String description;

    public Task() {
    }

    public Task(String username, boolean isFinish, Date deadline, boolean isRemind, String header, String description) {
        this.username = username;
        this.isFinish = isFinish;
        this.deadline = deadline;
        this.isRemind = isRemind;
        this.header = header;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public Date getDeadline() {
        return deadline;
    }

    public boolean isRemind() {
        return isRemind;
    }

    public String getHeader() {
        return header;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setRemind(boolean remind) {
        isRemind = remind;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
