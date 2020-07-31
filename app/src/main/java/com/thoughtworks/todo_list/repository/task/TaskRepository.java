package com.thoughtworks.todo_list.repository.task;

import com.thoughtworks.todo_list.repository.task.entity.Task;

import io.reactivex.Completable;

public interface TaskRepository {
    Completable saveTask(Task task);
}
