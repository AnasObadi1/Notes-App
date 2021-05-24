
package com.obadi.android.anas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.obadi.android.anas.R;
import com.obadi.android.anas.database.AppDatabase;
import com.obadi.android.anas.database.AppExecutors;
import com.obadi.android.anas.database.TaskEntry;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class AddTaskActivity extends AppCompatActivity {


    public static final String EXTRA_TASK_ID = "extraTaskId";

    public static final String INSTANCE_TASK_ID = "instanceTaskId";

    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    private static final int DEFAULT_TASK_ID = -1;

    private static final String TAG = AddTaskActivity.class.getSimpleName();

    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mButton;

    private int mTaskId = DEFAULT_TASK_ID;


    private AppDatabase mDb;
    AddTaskViewModel viewModel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText(R.string.update_button);
            if (mTaskId == DEFAULT_TASK_ID) {
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);
                AddTaskViewModelFactory factory = new AddTaskViewModelFactory(mDb, mTaskId);
                viewModel = ViewModelProviders.of(this, factory).get(AddTaskViewModel.class);
                viewModel.getTask().observe(this, new Observer<TaskEntry>() {
                    @Override
                    public void onChanged(TaskEntry taskEntry) {
                        viewModel.getTask().removeObserver(this);
                        populateUI(taskEntry);
                    }
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
    }


    private void initViews() {
        mEditText = findViewById(R.id.editTextTaskDescription);
        mRadioGroup = findViewById(R.id.radioGroup);

        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }


    private void populateUI(TaskEntry task) {

        if (task == null) {
            return;
        }


        mEditText.setText(task.getDescription());
        setPriorityInViews(task.getPriority());
    }


    public void onSaveButtonClicked() {
        String description = mEditText.getText().toString();
        int priority = getPriorityFromViews();
        Date date = new Date();

        final TaskEntry task = new TaskEntry(description, priority, date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                if (mTaskId == DEFAULT_TASK_ID) {

                    mDb.taskDao().insertTask(task);
                } else {

                    task.setId(mTaskId);
                    viewModel.updateTask(task);

                }
                finish();
            }
        });
    }


    public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }


    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }
}