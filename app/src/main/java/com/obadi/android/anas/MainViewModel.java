package com.obadi.android.anas;

import android.app.Application;
import android.util.Log;
import com.obadi.android.anas.database.AppDatabase;
import com.obadi.android.anas.database.TaskEntry;
import com.obadi.android.anas.database.TasksRepository;

import java.util.List;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<TaskEntry>> tasks;
    private final TasksRepository tasksRepository;
    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        tasksRepository = new TasksRepository(database);
        tasks = tasksRepository.getloadAllTasks();
    }

    public LiveData<List<TaskEntry>> getTasks() {
        return tasks;
    }

    public void deleteTask(TaskEntry taskEntry) {
        tasksRepository.deleteTasks(taskEntry);
    }
}