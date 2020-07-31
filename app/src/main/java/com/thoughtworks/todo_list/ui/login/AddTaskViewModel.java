package com.thoughtworks.todo_list.ui.login;

import android.service.autofill.DateTransformation;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.thoughtworks.todo_list.repository.utils.DataUtil;

import java.text.ParseException;

public class AddTaskViewModel extends ViewModel {
    private UserRepository userRepository;

    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createTask(boolean checked, String date, boolean mIsRemindChecked, String header, String description){
        Log.d("AddTaskViewModel", "isfinish: "+checked+"\n"+"date: "+ DataUtil.stringToDate(date).toString() +"\n"+
                "isRemind: "+mIsRemindChecked+"\n"+"header: "+header+"\n"+"des: "+description);
    }
}
