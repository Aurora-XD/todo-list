package com.thoughtworks.todo_list.repository.task;

import com.thoughtworks.todo_list.repository.task.entity.Task;

import io.reactivex.Completable;

public class TaskRepositoryImpl implements TaskRepository {

    private TaskDateSource taskDateSource;

    public TaskRepositoryImpl(TaskDateSource taskDateSource) {
        this.taskDateSource = taskDateSource;
    }

    @Override
    public Completable saveTask(Task task) {
        return null;
    }
}
