package com.thoughtworks.todo_list.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thoughtworks.todo_list.MainApplication;
import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.repository.task.TaskRepository;
import com.thoughtworks.todo_list.repository.task.entity.Task;
import com.thoughtworks.todo_list.repository.utils.TaskAdapter;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.home_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.home_task_count)
    TextView mTextCount;

    @BindView(R.id.home_date_day)
    TextView mDateDay;

    @BindView(R.id.home_date_month)
    TextView mDateMonth;

    private TaskAdapter taskAdapter;

    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiey_home);

        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter();
        mRecyclerView.setAdapter(taskAdapter);

        homeViewModel = obtainViewModel();

        homeViewModel.getCurrentDate();

        homeViewModel.observeToday(this, new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                mDateDay.setText(homeViewModel.getDayOfDate());
                mDateMonth.setText(homeViewModel.getMonthOfDate());
            }
        });

        homeViewModel.observeAllTask(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                mTextCount.setText(tasks.size()+"个任务");
                taskAdapter.setAllTask(tasks);
                taskAdapter.notifyDataSetChanged();
            }
        });

        refreshTaskList();
    }

    private void refreshTaskList(){
        homeViewModel.getAllTask(((MainApplication) getApplicationContext()).getCurrentUser().getName());
    }

    @OnClick(R.id.home_button_add_task)
    void startAddTask(){
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    private HomeViewModel obtainViewModel() {
        TaskRepository taskRepository = (((MainApplication) getApplicationContext())).getTaskRepository();
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.setTaskRepository(taskRepository);
        return homeViewModel;
    }
}