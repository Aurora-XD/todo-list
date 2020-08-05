package com.thoughtworks.todo_list.repository.task;

import com.thoughtworks.todo_list.repository.task.entity.Task;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface TaskDateSource {
    Completable saveTask(Task task);
    Maybe<List<Task>> getAllTask(String name);
    Completable updateTask(Task task);
    Maybe<Task> findTaskById(int taskId);
    Completable deleteTask(Task task);
}
