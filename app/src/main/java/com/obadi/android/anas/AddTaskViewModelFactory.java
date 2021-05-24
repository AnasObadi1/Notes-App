package com.obadi.android.anas;

import com.obadi.android.anas.database.AppDatabase;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AddTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mTaskId;

    public AddTaskViewModelFactory(AppDatabase database, int taskId) {
        mDb = database;
        mTaskId = taskId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AddTaskViewModel(mDb, mTaskId);
    }
}
