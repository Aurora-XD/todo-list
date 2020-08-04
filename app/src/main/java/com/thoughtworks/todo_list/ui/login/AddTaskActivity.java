package com.thoughtworks.todo_list.ui.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.thoughtworks.todo_list.repository.utils.DateTrans;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

        addTaskViewModel = obtainViewModel();

        addTaskViewModel.observeIsTaskValid(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d("TAG", "mBtnConfirm: " + aBoolean);
                mBtnConfirm.setEnabled(aBoolean);
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
        addTaskViewModel.createTask(((MainApplication) getApplicationContext()).getCurrentUser().getName(),mIsFinish.isChecked(), mBtnDate.getText().toString(), mIsRemind.isChecked(),
                mEditHeader.getText().toString(), mEditDescription.getText().toString());
    }

    private AddTaskViewModel obtainViewModel() {
        TaskRepository taskRepository = (((MainApplication) getApplicationContext())).getTaskRepository();
        AddTaskViewModel addTaskViewModel = new ViewModelProvider(this).get(AddTaskViewModel.class);
        addTaskViewModel.initAddTaskViewModel(taskRepository);
        return addTaskViewModel;
    }
}
