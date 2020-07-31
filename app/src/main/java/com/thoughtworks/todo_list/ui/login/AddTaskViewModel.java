package com.thoughtworks.todo_list.ui.login;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.thoughtworks.todo_list.repository.task.TaskRepository;
import com.thoughtworks.todo_list.repository.utils.DateTrans;

import java.util.Objects;

public class AddTaskViewModel extends ViewModel {
    private TaskRepository taskRepository;
    private MutableLiveData<Boolean> isTaskValid = new MutableLiveData<>();

    void initAddTaskViewModel(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
        isTaskValid.postValue(false);
    }

    public void observeIsTaskValid(LifecycleOwner owner, Observer<Boolean> observer){
        isTaskValid.observe(owner,observer);
    }

    public void createTask(String currentUser, boolean checked, String date, boolean mIsRemindChecked, String header, String description){
        Log.d("AddTaskViewModel", "username: "+currentUser+"\n"+"isfinish: "+checked+"\n"+"date: "+ DateTrans.stringToDate(date).toString() +"\n"+
                "isRemind: "+mIsRemindChecked+"\n"+"header: "+header+"\n"+"des: "+description);
    }

    public void checkTask(String date, String header) {
        if(Objects.isNull(DateTrans.stringToDate(date)) || TextUtils.isEmpty(header)){
            isTaskValid.postValue(false);
        }else {
            isTaskValid.postValue(true);
        }
    }
}
