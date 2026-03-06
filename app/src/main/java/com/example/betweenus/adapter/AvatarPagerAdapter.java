package com.example.betweenus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.betweenus.R;
import com.example.betweenus.model.AvatarGroup;

import java.util.List;

public class AvatarPagerAdapter extends RecyclerView.Adapter<AvatarPagerAdapter.ViewHolder> {

    private List<AvatarGroup> avatarGroups;

    public AvatarPagerAdapter(List<AvatarGroup> avatarGroups) {
        this.avatarGroups = avatarGroups;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView left, center, right;

        public ViewHolder(View view) {
            super(view);
            left = view.findViewById(R.id.avatarLeft);
            center = view.findViewById(R.id.avatarCenter);
            right = view.findViewById(R.id.avatarRight);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_avatar_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AvatarGroup group = avatarGroups.get(position);

        holder.left.setImageResource(group.leftAvatar);
        holder.center.setImageResource(group.centerAvatar);
        holder.right.setImageResource(group.rightAvatar);
    }

    @Override
    public int getItemCount() {
        return avatarGroups.size();
    }
}


