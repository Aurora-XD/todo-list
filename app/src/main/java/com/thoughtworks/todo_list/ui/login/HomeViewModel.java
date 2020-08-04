package com.thoughtworks.todo_list.ui.login;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.thoughtworks.todo_list.repository.task.TaskRepository;
import com.thoughtworks.todo_list.repository.task.entity.Task;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    private TaskRepository taskRepository;
    private MutableLiveData<List<Task>> allTask = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void observeAllTask(LifecycleOwner owner, Observer<List<Task>> observer){
        allTask.observe(owner,observer);
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

    @Override
    protected void onCleared() {
        taskRepository = null;
        compositeDisposable.clear();
        super.onCleared();
    }
}
