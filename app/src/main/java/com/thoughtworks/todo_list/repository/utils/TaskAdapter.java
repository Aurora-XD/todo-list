package com.thoughtworks.todo_list.repository.utils;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.repository.task.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    List<Task> allTask = new ArrayList<>();

    public void setAllTask(List<Task> allTask) {
        this.allTask = allTask;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder{
        private CheckBox mIsFinish;
        private TextView mTaskHeader,mDeadline;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mIsFinish = itemView.findViewById(R.id.home_task_is_finish);
            mDeadline = itemView.findViewById(R.id.home_task_deadline);
            mTaskHeader = itemView.findViewById(R.id.home_task_header);
        }
    }

    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(taskView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task task = allTask.get(position);
        holder.mIsFinish.setChecked(task.isFinish());
        holder.mTaskHeader.setText(task.getHeader());
        if(task.isFinish()){
            holder.mTaskHeader.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.mDeadline.setText(DateTrans.dateToString(task.getDeadline()).split("年")[1]);
    }

    @Override
    public int getItemCount() {
        return allTask.size();
    }
}
