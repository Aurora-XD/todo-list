package com.thoughtworks.todo_list.ui.login;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.thoughtworks.todo_list.repository.utils.DateUtil;

public class AddTaskViewModel extends ViewModel {
    private UserRepository userRepository;

    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createTask(boolean checked, String date, boolean mIsRemindChecked, String header, String description){
        Log.d("AddTaskViewModel", "isfinish: "+checked+"\n"+"date: "+ DateUtil.stringToDate(date).toString() +"\n"+
                "isRemind: "+mIsRemindChecked+"\n"+"header: "+header+"\n"+"des: "+description);
    }
}
