package com.thoughtworks.todo_list.ui.login;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.thoughtworks.todo_list.repository.utils.DateUtil;

import java.util.Objects;

public class AddTaskViewModel extends ViewModel {
    private UserRepository userRepository;
    private MutableLiveData<Boolean> isTaskValid = new MutableLiveData<>();

    void initAddTaskViewModel(UserRepository userRepository){
        this.userRepository = userRepository;
        isTaskValid.postValue(false);
    }

    public void observeIsTaskValid(LifecycleOwner owner, Observer<Boolean> observer){
        isTaskValid.observe(owner,observer);
    }

    public void createTask(boolean checked, String date, boolean mIsRemindChecked, String header, String description){
        Log.d("AddTaskViewModel", "isfinish: "+checked+"\n"+"date: "+ DateUtil.stringToDate(date).toString() +"\n"+
                "isRemind: "+mIsRemindChecked+"\n"+"header: "+header+"\n"+"des: "+description);
    }

    public void checkTask(String date, String header) {
        if(Objects.isNull(DateUtil.stringToDate(date)) || TextUtils.isEmpty(header)){
            isTaskValid.postValue(false);
        }else {
            isTaskValid.postValue(true);
        }
    }
}
