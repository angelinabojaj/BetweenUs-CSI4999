package com.example.betweenus.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betweenus.DatabaseHelper;
import com.example.betweenus.R;
import com.example.betweenus.adapter.AvatarAdapter;
import com.example.betweenus.adapter.DashboardAdapter;
import com.example.betweenus.adapter.TodoPreviewAdapter;
import com.example.betweenus.databinding.FragmentHomeBinding;
import com.example.betweenus.model.DashboardMessage;
import com.example.betweenus.model.User;
import com.example.betweenus.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Spinner spinnerStatus;
    private RecyclerView recyclerDashboard, recyclerAvatars, recyclerTodoPreview;
    private ImageButton moodHappy, moodNeutral, moodSad, moodAngry, moodExcited;

    private DashboardAdapter dashboardAdapter;
    private AvatarAdapter avatarAdapter;
    private TodoPreviewAdapter todoPreviewAdapter;

    private SharedTodoViewModel todoViewModel;
    private DatabaseHelper dbHelper;

    private int currentUserId = 1;

    private final long EXPIRATION_HOURS = 4; // dashboard expiration

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dbHelper = new DatabaseHelper(getContext());

        Bundle args = getArguments();
        if (args != null) {
            currentUserId = args.getInt("USER_ID", -1);
        }

        initializeViews(view);
        setupSpinner();
        setupDashboard();
        setupAvatars();
        setupTodoPreview();
        setupMoodButtons();
        setupNavigation(view);

        return view;
    }

    private void initializeViews(View view) {
        spinnerStatus = view.findViewById(R.id.spinnerStatus);
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard);
        recyclerAvatars = view.findViewById(R.id.recyclerAvatars);
        recyclerTodoPreview = view.findViewById(R.id.recyclerTodoPreview);

        moodHappy = view.findViewById(R.id.moodHappy);
        moodNeutral = view.findViewById(R.id.moodNeutral);
        moodSad = view.findViewById(R.id.moodSad);
        moodAngry = view.findViewById(R.id.moodAngry);
        moodExcited = view.findViewById(R.id.moodExcited);
    }

    // -----------------------------
    // SPINNER SETUP
    // -----------------------------
    private void setupSpinner() {
        String[] statuses = {
                "Working",
                "Unplugged",
                "Exercising",
                "Studying",
                "Relaxing"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                statuses
        );

        spinnerStatus.setAdapter(adapter);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                dbHelper.updateUserStatus(currentUserId, statuses[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // -----------------------------
    // DASHBOARD
    // -----------------------------
    private void setupDashboard() {

        recyclerDashboard.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        List<DashboardMessage> messages =
                dbHelper.getValidDashboardMessages(EXPIRATION_HOURS);

        dashboardAdapter = new DashboardAdapter(messages);
        recyclerDashboard.setAdapter(dashboardAdapter);

        recyclerDashboard.setOnClickListener(v -> {
            showDashboardDialog();
        });
    }

    private void showDashboardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText input = new EditText(getContext());

        builder.setTitle("Post to Dashboard");
        builder.setView(input);

        builder.setPositiveButton("Post", (dialog, which) -> {
            String text = input.getText().toString();
            dbHelper.insertDashboardMessage(currentUserId, text);
            refreshDashboard();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void refreshDashboard() {
        List<DashboardMessage> messages =
                dbHelper.getValidDashboardMessages(EXPIRATION_HOURS);
        dashboardAdapter.updateList(messages);
    }

    // -----------------------------
    // TODO PREVIEW
    // -----------------------------
    private void setupTodoPreview() {

        recyclerTodoPreview.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        todoViewModel = new ViewModelProvider(requireActivity())
                .get(SharedTodoViewModel.class);

        todoPreviewAdapter = new TodoPreviewAdapter(new ArrayList<>());
        recyclerTodoPreview.setAdapter(todoPreviewAdapter);

        todoViewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            todoPreviewAdapter.updateTasks(tasks);
        });

        todoViewModel.loadTasks(dbHelper, currentUserId);
    }

    // -----------------------------
    // AVATARS
    // -----------------------------
    private void setupAvatars() {

        recyclerAvatars.setLayoutManager(
                new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL, false)
        );

        List<User> users = dbHelper.getUserAndFriends(currentUserId);

        avatarAdapter = new AvatarAdapter(users, currentUserId);
        recyclerAvatars.setAdapter(avatarAdapter);
    }

    // -----------------------------
    // MOOD BUTTONS
    // -----------------------------
    private void setupMoodButtons() {

        moodHappy.setOnClickListener(v -> updateMood("Happy"));
        moodNeutral.setOnClickListener(v -> updateMood("Neutral"));
        moodSad.setOnClickListener(v -> updateMood("Sad"));
        moodAngry.setOnClickListener(v -> updateMood("Angry"));
        moodExcited.setOnClickListener(v -> updateMood("Excited"));
    }

    private void updateMood(String mood) {
        dbHelper.updateUserMood(currentUserId, mood);

        // refresh avatars
        List<User> users = dbHelper.getUserAndFriends(currentUserId);
        avatarAdapter.updateUsers(users);
    }

    // -----------------------------
    // NAVIGATION
    // -----------------------------
    private void setupNavigation(View view) {

        view.findViewById(R.id.cardTodo).setOnClickListener(v -> {
            Fragment productivityFragment = new ProductivityFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", currentUserId);
            productivityFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, productivityFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}