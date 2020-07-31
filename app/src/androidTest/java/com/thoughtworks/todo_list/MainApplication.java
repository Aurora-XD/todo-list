package com.thoughtworks.todo_list;

import android.app.Application;

import com.thoughtworks.todo_list.repository.task.TaskRepository;
import com.thoughtworks.todo_list.repository.task.TaskRepositoryImpl;
import com.thoughtworks.todo_list.repository.user.UserRepository;
import com.thoughtworks.todo_list.repository.user.UserRepositoryImpl;
import com.thoughtworks.todo_list.repository.user.entity.User;

import static org.mockito.Mockito.mock;

public class MainApplication extends Application {
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public UserRepository userRepository() {
        if (userRepository == null) {
            userRepository = mock(UserRepositoryImpl.class);
        }
        return userRepository;
    }

    public TaskRepository getTaskRepository() {
        if(taskRepository == null){
            taskRepository = mock(TaskRepositoryImpl.class);
        }
        return taskRepository;
    }

    public User getCurrentUser() {
        if(currentUser == null){
            currentUser = mock(User.class);
        }
        return currentUser;
    }
}
