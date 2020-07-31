package com.thoughtworks.todo_list.repository.task;

import androidx.room.Dao;
import androidx.room.Insert;

import com.thoughtworks.todo_list.repository.task.entity.Task;

import io.reactivex.Completable;

@Dao
public interface DBTaskDataSource extends TaskDateSource {
    @Insert
    Completable saveTask(Task task);
}
