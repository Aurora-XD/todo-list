package com.thoughtworks.todo_list.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.thoughtworks.todo_list.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    private String currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiey_home);

        ButterKnife.bind(this);
        currentUser = getIntent().getExtras().getString(getString(R.string.prompt_username));
    }

    @OnClick(R.id.home_button_add_task)
    void startAddTask(){
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(getString(R.string.prompt_username),currentUser);
        startActivity(intent);
    }
}