package com.example.betweenus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.betweenus.R;
import com.example.betweenus.model.User;

import java.util.ArrayList;
import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {

    private List<User> users;
    private int currentUserId;

    public AvatarAdapter(List<User> users, int currentUserId) {
        this.currentUserId = currentUserId;
        this.users = sortUsers(users);
    }

    private List<User> sortUsers(List<User> list) {
        List<User> sorted = new ArrayList<>();

        User current = null;

        for (User u : list) {
            if (u.getUserId() == currentUserId)
                current = u;
        }

        if (current != null) {
            sorted.add(current);
            list.remove(current);
        }

        sorted.addAll(list);
        return sorted;
    }

    public void updateUsers(List<User> newUsers) {
        users = sortUsers(newUsers);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_avatar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        User user = users.get(position);
//
//        int avatarResId = getAvatarDrawable(user);
//        holder.avatar.setImageResource(avatarResId);

        User user = users.get(position);
        int avatarResId;

        switch (user.getAvatar()) {
            case 1:
                avatarResId = R.drawable.bunny_happy;
                break;
            case 2:
                avatarResId = R.drawable.giraffe_happy;
                break;
            case 3:
                avatarResId = R.drawable.kitty_happy;
                break;
            case 4:
                avatarResId = R.drawable.puppy_happy;
                break;
            default:
                avatarResId = R.drawable.bunny_happy;
        }
        holder.avatar.setImageResource(avatarResId);
    }

//    private int getAvatarDrawable(User user) {
//
//        String mood = user.getMood();
//        int avatarType = user.getAvatar();

//        switch (avatarType) {
//
//            case 1: // Kitty
//                switch (mood) {
//                    case "Happy": return R.drawable.kitty_happy;
//                    case "Sad": return R.drawable.kitty_sad;
//                    case "Neutral": return R.drawable.kitty_neutral;
//                    case "Angry": return R.drawable.kitty_angry;
//                    case "Excited": return R.drawable.kitty_excited;
//                    default: return R.drawable.kitty_neutral;
//                }
//
//            case 2: // Giraffe
//                switch (mood) {
//                    case "Happy": return R.drawable.giraffe_happy;
//                    case "Sad": return R.drawable.giraffe_sad;
//                    case "Neutral": return R.drawable.giraffe_neutral;
//                    case "Angry": return R.drawable.giraffe_angry;
//                    case "Excited": return R.drawable.giraffe_excited;
//                    default: return R.drawable.giraffe_neutral;
//                }
//
//            case 3: // Bunny
//                switch (mood) {
//                    case "Happy": return R.drawable.bunny_happy;
//                    case "Sad": return R.drawable.bunny_sad;
//                    case "Neutral": return R.drawable.bunny_neutral;
//                    case "Angry": return R.drawable.bunny_angry;
//                    case "Excited": return R.drawable.bunny_excited;
//                    default: return R.drawable.bunny_neutral;
//                }
//
//            case 4: // Puppy
//                switch (mood) {
//                    case "Happy": return R.drawable.puppy_happy;
//                    case "Sad": return R.drawable.puppy_sad;
//                    case "Neutral": return R.drawable.puppy_neutral;
//                    case "Angry": return R.drawable.puppy_angry;
//                    case "Excited": return R.drawable.puppy_excited;
//                    default: return R.drawable.puppy_neutral;
//                }
//
//            default:
//                return R.drawable.kitty_neutral;
//        }
//    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
}
