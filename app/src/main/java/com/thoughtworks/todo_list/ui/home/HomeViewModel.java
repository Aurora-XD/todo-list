package com.thoughtworks.todo_list.ui.home;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.thoughtworks.todo_list.repository.task.TaskRepository;
import com.thoughtworks.todo_list.repository.task.entity.Task;
import com.thoughtworks.todo_list.repository.utils.DateTrans;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    private TaskRepository taskRepository;
    private MutableLiveData<List<Task>> allTask = new MutableLiveData<>();
    private MutableLiveData<Date> today = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static final String TAG = "HomeViewModel";
    private MutableLiveData<Boolean> update = new MutableLiveData<>();

    public void initHomeViewModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        update.postValue(false);
    }

    public void observeAllTask(LifecycleOwner owner, Observer<List<Task>> observer) {
        allTask.observe(owner, observer);
    }

    public void observeToday(LifecycleOwner owner, Observer<Date> observer) {
        today.observe(owner, observer);
    }

    public void observeUpdateTask(LifecycleOwner owner, Observer<Boolean> observer) {
        update.observe(owner, observer);
    }

    public void getAllTask(String name) {
        Disposable disposable = taskRepository.getAllTask(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Task>>() {
                    @Override
                    public void accept(List<Task> tasks) throws Exception {
                        allTask.setValue(tasks);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("getAllTask", "accept: Failed");
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void updateTask(Task task) {
        Disposable updateTaskDisposable = taskRepository.updateTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "Update task successfully!");
                        Boolean value = !update.getValue();
                        update.setValue(value);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "Update task failed!");
                    }
                });
        compositeDisposable.add(updateTaskDisposable);
    }

    public void getCurrentDate() {
        today.postValue(Calendar.getInstance().getTime());
    }

    public String getDayOfDate() {
        return DateTrans.getWeekOfMonth(today.getValue()) + ", " + DateTrans.getDayOfMonth(today.getValue());
    }

    public String getMonthOfDate() {
        return DateTrans.getMonth(today.getValue());
    }

    @Override
    protected void onCleared() {
        taskRepository = null;
        compositeDisposable.clear();
        super.onCleared();
    }
}
