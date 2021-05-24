package com.obadi.android.anas;

import com.obadi.android.anas.database.AppDatabase;
import com.obadi.android.anas.database.TaskEntry;
import com.obadi.android.anas.database.TasksRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AddTaskViewModel extends ViewModel {

    private LiveData<TaskEntry> task;
    private final TasksRepository tasksRepository;


    public AddTaskViewModel(AppDatabase database, int taskId) {

        tasksRepository = new TasksRepository(database);
        task = tasksRepository.getloadTaskById(taskId);
    }


    public LiveData<TaskEntry> getTask() {
        return task;
    }

    public void updateTask(TaskEntry task) {
        tasksRepository.updateTaskById(task);
    }
}
