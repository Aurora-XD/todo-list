package com.thoughtworks.todo_list.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thoughtworks.todo_list.MainApplication;
import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.repository.task.TaskRepository;
import com.thoughtworks.todo_list.repository.task.entity.Task;
import com.thoughtworks.todo_list.repository.utils.TaskAdapter;
import com.thoughtworks.todo_list.ui.login.LoginActivity;
import com.thoughtworks.todo_list.ui.task.AddTaskActivity;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    public static final String TASK_ID = "taskId";

    @BindView(R.id.home_menu_bar)
    Toolbar mToolbar;

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

        setSupportActionBar(mToolbar);

        homeViewModel = obtainViewModel();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this);
        mRecyclerView.setAdapter(taskAdapter);

        homeViewModel.getCurrentDate();

        homeViewModel.observeToday(this, new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                mDateDay.setText(homeViewModel.getDayOfDate());
                mDateMonth.setText(homeViewModel.getMonthOfDate());
            }
        });

        homeViewModel.observeUpdateTask(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                refreshTaskList();
            }
        });

        homeViewModel.observeAllTask(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                Log.d("taskId", "taskId:" + tasks.toString());
                mTextCount.setText(tasks.size()+"个任务");
                taskAdapter.setAllTask(tasks);
                taskAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mToolbar.inflateMenu(R.menu.home_menu);//设置右上角的填充菜单
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(R.id.home_menu == item.getItemId()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshTaskList(){
        homeViewModel.getAllTask(((MainApplication) getApplicationContext()).getCurrentUser().getName());
    }

    public void updateTaskIsFinish(Task task,Boolean isFinish){
        homeViewModel.updateTaskIsFinish(task,isFinish);
    }

    @OnClick(R.id.home_button_add_task)
    void startAddTask(){
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    private HomeViewModel obtainViewModel() {
        TaskRepository taskRepository = (((MainApplication) getApplicationContext())).getTaskRepository();
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.initHomeViewModel(taskRepository);
        return homeViewModel;
    }

    public void goTaskDetail(int id) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(TASK_ID,id);
        startActivity(intent);
    }
}