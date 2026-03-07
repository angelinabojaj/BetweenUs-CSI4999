package com.example.betweenus.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betweenus.CreateMemoryActivity;
import com.example.betweenus.DatabaseHelper;
import com.example.betweenus.R;
import com.example.betweenus.adapter.MemoriesAdapter;
import com.example.betweenus.model.MemoriesItem;

import java.util.ArrayList;
import java.util.List;

public class MemoriesFragment extends Fragment {
    private RecyclerView recyclerView;
    private MemoriesAdapter memoriesAdapter;
    private List<MemoriesItem> memoriesItems;
    private DatabaseHelper dbHelper;

    private ImageButton btnAddMemory;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memories, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_posts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        btnAddMemory = view.findViewById(R.id.btn_add_memory);

        btnAddMemory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateMemoryActivity.class);
            startActivity(intent);
        });

        dbHelper = new DatabaseHelper(requireContext());
        memoriesItems = new ArrayList<>();
        loadMemories();
        memoriesAdapter = new MemoriesAdapter(requireContext(), memoriesItems);
        recyclerView.setAdapter(memoriesAdapter);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        memoriesItems.clear();
        loadMemories();
        memoriesAdapter.notifyDataSetChanged();
    }

    private void loadMemories() {
        Cursor cursor = dbHelper.getMemories();
        if (cursor.moveToFirst()) {
            do {
                int memoryID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MEMORY_ID));
                int posterID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_POSTER_ID));
                String memoryTitle = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MEMORY_TITLE));
                String memoryBody = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MEMORY_BODY));
                String mediaURL = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MEDIA_URL));
                String timeStamp = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TIME_STAMP));
                int likes = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LIKES));
                memoriesItems.add(new MemoriesItem(memoryID, posterID, memoryTitle, memoryBody, mediaURL, timeStamp, likes));

            } while (cursor.moveToNext());
        }
        cursor.close();

    }
}





