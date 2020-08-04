package com.thoughtworks.todo_list.repository.task;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.thoughtworks.todo_list.repository.task.entity.Task;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Dao
public interface DBTaskDataSource extends TaskDateSource {
    @Insert
    Completable saveTask(Task task);

    @Query("select * from task where username = :name order by deadline desc")
    Maybe<List<Task>> getAllTask(String name);
}
