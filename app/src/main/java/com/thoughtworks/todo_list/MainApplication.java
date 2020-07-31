package com.thoughtworks.todo_list;

import android.app.Application;

import androidx.room.Room;

import com.thoughtworks.todo_list.repository.AppDatabase;
import com.thoughtworks.todo_list.repository.task.TaskRepository;
import com.thoughtworks.todo_list.repository.task.TaskRepositoryImpl;
import com.thoughtworks.todo_list.repository.user.UserRepository;
import com.thoughtworks.todo_list.repository.user.UserRepositoryImpl;

public class MainApplication extends Application {
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, this.getClass().getSimpleName())
                .fallbackToDestructiveMigration().build();
        userRepository = new UserRepositoryImpl(appDatabase.userDBDataSource());
        taskRepository = new TaskRepositoryImpl(appDatabase.taskDataSource());
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public UserRepository userRepository() {
        return userRepository;
    }
}
