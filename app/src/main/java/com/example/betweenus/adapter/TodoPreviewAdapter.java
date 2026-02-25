package com.example.betweenus.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.betweenus.R;
import com.example.betweenus.model.Task;

import java.util.List;

public class TodoPreviewAdapter extends RecyclerView.Adapter<TodoPreviewAdapter.ViewHolder> {

    private List<Task> tasks;

    public TodoPreviewAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void updateTasks(List<Task> newTasks) {
        this.tasks = newTasks;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Task task = tasks.get(position);

        holder.txtGoal.setText(task.getTaskGoal());

        if (task.isComplete()) {
            holder.txtGoal.setPaintFlags(
                    holder.txtGoal.getPaintFlags() |
                            Paint.STRIKE_THRU_TEXT_FLAG
            );
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(tasks.size(), 3); // show only 3 max in preview
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtGoal;

        ViewHolder(View itemView) {
            super(itemView);
            txtGoal = itemView.findViewById(R.id.txtTaskGoal);
        }
    }
}
