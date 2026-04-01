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
    private OnTaskActionListener listener;

    public interface OnTaskActionListener {
        void onTaskDelete(Task task);
        void onTaskComplete(Task task);
        void onTaskEdit(Task task);
    }

    public TaskAdapter(List<Task> taskList, OnTaskActionListener listener) {
        this.taskList = taskList;
        this.listener = listener;
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
        
        holder.tvTitle.setText(task.getTaskGoal());
        holder.tvTime.setText("Due: " + task.getDueDate());

        int[] colors = {
                Color.parseColor("#F6A6A6"), // pink
                Color.parseColor("#FFD166"), // yellow
                Color.parseColor("#A8E6CF"), // green
                Color.parseColor("#F4A261")  // orange
        };

        holder.taskCard.setCardBackgroundColor(colors[position % colors.length]);

        holder.cbComplete.setOnCheckedChangeListener(null);
        holder.cbComplete.setChecked(task.isComplete());
        updateStrikethrough(holder.tvTitle, task.isComplete());

        holder.cbComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && listener != null) {
                listener.onTaskComplete(task);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskDelete(task);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskEdit(task);
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
