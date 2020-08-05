package com.thoughtworks.todo_list.ui.task;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.thoughtworks.todo_list.MainApplication;
import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.repository.task.TaskRepository;
import com.thoughtworks.todo_list.repository.task.entity.Task;
import com.thoughtworks.todo_list.repository.utils.DateTrans;
import com.thoughtworks.todo_list.ui.home.HomeActivity;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.thoughtworks.todo_list.ui.home.HomeActivity.TASK_ID;

public class AddTaskActivity extends AppCompatActivity {
    private AddTaskViewModel addTaskViewModel;

    @BindView(R.id.add_task_is_finish)
    CheckBox mIsFinish;

    @BindView(R.id.add_task_date)
    Button mBtnDate;

    @BindView(R.id.add_task_remind)
    Switch mIsRemind;

    @BindView(R.id.add_task_header)
    EditText mEditHeader;

    @BindView(R.id.add_task_description)
    EditText mEditDescription;

    @BindView(R.id.add_task_confirm)
    Button mBtnConfirm;

    @BindView(R.id.task_detail_delete)
    Button mBtnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

        addTaskViewModel = obtainViewModel();
        getTaskDetail(this.getIntent());

        addTaskViewModel.observeIsTaskValid(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d("TAG", "mBtnConfirm: " + aBoolean);
                mBtnConfirm.setEnabled(aBoolean);
            }
        });

        addTaskViewModel.observeTaskDetail(this, new Observer<Task>() {
            @Override
            public void onChanged(Task task) {
                fillTaskDetail(task);
            }
        });

        addTaskViewModel.observeUpdateTaskResult(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        addTaskViewModel.observeDeleteUpdateTaskResult(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addTaskViewModel.observeCreateTaskResult(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                addTaskViewModel.checkTask(mBtnDate.getText().toString(), mEditHeader.getText().toString());
            }
        };
        mEditHeader.addTextChangedListener(textWatcher);
        mBtnDate.addTextChangedListener(textWatcher);
    }

    private void getTaskDetail(Intent intent){
        if(Objects.nonNull(intent.getExtras())){
            int taskId = intent.getExtras().getInt(TASK_ID);
            addTaskViewModel.getTaskDetail(taskId);
        }
    }

    private void fillTaskDetail(Task task){
        mBtnDelete.setVisibility(View.VISIBLE);
        mIsFinish.setChecked(task.isFinish());
        mBtnDate.setText(DateTrans.dateToString(task.getDeadline()));
        mIsRemind.setChecked(task.isRemind());
        mEditHeader.setText(task.getHeader());
        mEditDescription.setText(task.getDescription());
    }

    @OnClick(R.id.add_task_date)
    void setTaskDeadline() {
        Calendar now = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mBtnDate.setText(DateTrans.dateStringFormat(year,month,day));
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.add_task_confirm)
    void createTask() {
        if(Objects.nonNull(this.getIntent().getExtras())){
            addTaskViewModel.updateTask(mIsFinish.isChecked(), mBtnDate.getText().toString(), mIsRemind.isChecked(),
                    mEditHeader.getText().toString(), mEditDescription.getText().toString());
        }else {
            addTaskViewModel.createTask(((MainApplication) getApplicationContext()).getCurrentUser().getName(),mIsFinish.isChecked(), mBtnDate.getText().toString(), mIsRemind.isChecked(),
                    mEditHeader.getText().toString(), mEditDescription.getText().toString());
        }
    }

    @OnClick(R.id.task_detail_delete)
    void deleteTask(){
        addTaskViewModel.deleteTask();
    }

    private AddTaskViewModel obtainViewModel() {
        TaskRepository taskRepository = (((MainApplication) getApplicationContext())).getTaskRepository();
        AddTaskViewModel addTaskViewModel = new ViewModelProvider(this).get(AddTaskViewModel.class);
        addTaskViewModel.initAddTaskViewModel(taskRepository);
        return addTaskViewModel;
    }
}
