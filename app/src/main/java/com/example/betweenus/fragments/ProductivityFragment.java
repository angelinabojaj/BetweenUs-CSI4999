package com.example.betweenus.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betweenus.DatabaseHelper;
import com.example.betweenus.R;
import com.example.betweenus.adapter.TaskAdapter;
import com.example.betweenus.model.Task;

import java.util.ArrayList;
import java.util.List;

public class ProductivityFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private DatabaseHelper dbHelper;
    private EditText etTaskInput;
    private Button btnAddTask;

    // Hardcoded for now, should ideally come from user session
    private int currentUserId = 1;

    public ProductivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productivity, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        taskList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.taskRecyclerView);
        etTaskInput = view.findViewById(R.id.et_task_input);
        btnAddTask = view.findViewById(R.id.btn_add_task);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(taskList, task -> {
            dbHelper.markTaskComplete(task.getTaskId()); // Using existing markTaskComplete as delete/complete for now
            loadTasks();
        });
        recyclerView.setAdapter(taskAdapter);

        loadTasks();

        btnAddTask.setOnClickListener(v -> {
            String goal = etTaskInput.getText().toString().trim();
            if (!goal.isEmpty()) {
                long result = dbHelper.createTask(currentUserId, goal, 0, "Today");
                if (result != -1) {
                    etTaskInput.setText("");
                    loadTasks();
                    Toast.makeText(getContext(), "Task Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to add task", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please enter a task", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loadTasks() {
        taskList.clear();
        Cursor cursor = dbHelper.getUserTasks(String.valueOf(currentUserId));
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("TaskID"));
                int makerId = cursor.getInt(cursor.getColumnIndexOrThrow("TaskCreator"));
                String goal = cursor.getString(cursor.getColumnIndexOrThrow("TaskGoal"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("DueDate"));
                int complete = cursor.getInt(cursor.getColumnIndexOrThrow("IsComplete"));

                taskList.add(new Task(id, makerId, goal, date, complete == 1));
            } while (cursor.moveToNext());
            cursor.close();
        }
        taskAdapter.updateTasks(taskList);
    }
}
