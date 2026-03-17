package com.example.betweenus.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betweenus.R;
import com.example.betweenus.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private OnTaskDeleteListener deleteListener;

    public interface OnTaskDeleteListener {
        void onTaskDelete(Task task);
    }

    public TaskAdapter(List<Task> taskList, OnTaskDeleteListener deleteListener) {
        this.taskList = taskList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        
        String displayedTitle = "Task #" + task.getTaskId() + ": " + task.getTaskGoal();
        holder.tvTitle.setText(displayedTitle);
        holder.tvTime.setText("Due: " + task.getDueDate());

        int[] colors = {
                Color.parseColor("#F6A6A6"), // pink
                Color.parseColor("#FFD166"), // yellow
                Color.parseColor("#A8E6CF"), // green
                Color.parseColor("#F4A261")  // orange
        };

        holder.taskCard.setCardBackgroundColor(colors[position % colors.length]);

        // Handle initial state and strikethrough
        holder.cbComplete.setOnCheckedChangeListener(null); // Clear listener before setting checked state
        holder.cbComplete.setChecked(task.isComplete());
        updateStrikethrough(holder.tvTitle, task.isComplete());

        holder.cbComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setComplete(isChecked); // Update in-memory model
            updateStrikethrough(holder.tvTitle, isChecked);
            // Database is NOT updated as per user request
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onTaskDelete(task);
            }
        });
    }

    private void updateStrikethrough(TextView textView, boolean isComplete) {
        if (isComplete) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateTasks(List<Task> newTasks) {
        this.taskList = newTasks;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTime;
        ImageButton btnDelete;
        CheckBox cbComplete;
        CardView taskCard;


        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.taskTitle);
            tvTime = itemView.findViewById(R.id.taskTime);
            btnDelete = itemView.findViewById(R.id.deleteTaskButton);
            taskCard = itemView.findViewById(R.id.taskCard);
            cbComplete = itemView.findViewById(R.id.task_complete_checkbox);
        }
    }
}
