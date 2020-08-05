package com.thoughtworks.todo_list.repository.task;

import com.thoughtworks.todo_list.repository.task.entity.Task;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class TaskRepositoryImpl implements TaskRepository {

    private TaskDateSource taskDateSource;

    public TaskRepositoryImpl(TaskDateSource taskDateSource) {
        this.taskDateSource = taskDateSource;
    }

    @Override
    public Completable saveTask(Task task) {
        return taskDateSource.saveTask(task);
    }

    @Override
    public Maybe<List<Task>> getAllTask(String name) {
        return taskDateSource.getAllTask(name);
    }

    @Override
    public Completable updateTask(Task task) {
        return taskDateSource.updateTask(task);
    }

}
