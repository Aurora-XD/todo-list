package com.thoughtworks.todo_list.ui.task;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.thoughtworks.todo_list.repository.task.TaskRepository;
import com.thoughtworks.todo_list.repository.task.entity.Task;
import com.thoughtworks.todo_list.repository.utils.DateTrans;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddTaskViewModel extends ViewModel {
    private TaskRepository taskRepository;
    private MutableLiveData<Boolean> isTaskValid = new MutableLiveData<>();
    private MutableLiveData<Boolean> createTaskResult = new MutableLiveData<>();
    private MutableLiveData<Task> taskDetail = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static final String TAG = "AddTaskViewModel";

    void initAddTaskViewModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        isTaskValid.postValue(false);
    }

    public void observeIsTaskValid(LifecycleOwner owner, Observer<Boolean> observer) {
        isTaskValid.observe(owner, observer);
    }

    public void observeCreateTaskResult(LifecycleOwner owner, Observer<Boolean> observer) {
        createTaskResult.observe(owner, observer);
    }

    public void observeTaskDetail(LifecycleOwner owner, Observer<Task> observer) {
        taskDetail.observe(owner, observer);
    }

    public void createTask(String currentUser, boolean isFinish, String date, boolean isRemind, String header, String description) {

        Task task = new Task(currentUser, isFinish, DateTrans.stringToDate(date), isRemind, header, description);
        Disposable createTaskDisposable = taskRepository.saveTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "Create task successfully!");
                        createTaskResult.setValue(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "Create task failed!");
                        createTaskResult.setValue(false);
                    }
                });
        compositeDisposable.add(createTaskDisposable);
    }

    public void getTaskDetail(int taskId) {
        Disposable detailDisposable = taskRepository.findTaskById(taskId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Log.d(TAG, "Task not found, task id is " + taskId))
                .subscribe(new Consumer<Task>() {
                    @Override
                    public void accept(Task task) throws Exception {
                        taskDetail.setValue(task);
                    }
                });

        compositeDisposable.add(detailDisposable);
    }

    public void checkTask(String date, String header) {
        if (Objects.isNull(DateTrans.stringToDate(date)) || TextUtils.isEmpty(header)) {
            isTaskValid.postValue(false);
        } else {
            isTaskValid.postValue(true);
        }
    }

    @Override
    protected void onCleared() {
        taskRepository = null;
        compositeDisposable.clear();
        super.onCleared();
    }
}
