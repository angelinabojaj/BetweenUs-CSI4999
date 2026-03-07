package com.example.betweenus;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MemoriesDetailActivity extends AppCompatActivity {
    ImageView memoryImage;
    TextView title, body, likes;
    RecyclerView commentsRecycler;
    ImageButton settingsButton;

    DatabaseHelper dbHelper;
    int memoryID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memories_detail);

        memoryImage = findViewById(R.id.memory_image);
        title = findViewById(R.id.memory_title);
        body = findViewById(R.id.memory_body);
        likes = findViewById(R.id.memory_likes);
        settingsButton = findViewById(R.id.btn_settings);
        commentsRecycler = findViewById(R.id.recycler_comments);

        dbHelper = new DatabaseHelper(this);

        memoryID = getIntent().getIntExtra("MEMORY_ID", -1);

        dbHelper.getMemoryByID(memoryID);
        dbHelper.getPostComments(memoryID);


        settingsButton.setOnClickListener(v1 -> showSettingsMenu());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
    }

    private void showSettingsMenu() {

        PopupMenu popupMenu = new PopupMenu(this, settingsButton);

        popupMenu.getMenuInflater().inflate(R.menu.memory_settings_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.edit_post) {

                showEditDialog();
                return true;

            } else if (item.getItemId() == R.id.delete_post) {

                dbHelper.deleteMemory(memoryID);
                finish(); // closes detail page
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

    private void showEditDialog() {

        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_edit_memory, null);

        EditText editTitle = dialogView.findViewById(R.id.edit_title);
        EditText editBody = dialogView.findViewById(R.id.edit_body);

        // Pre-fill existing values
        editTitle.setText(title.getText().toString());
        editBody.setText(body.getText().toString());

        new AlertDialog.Builder(this)
                .setTitle("Edit Memory")
                .setView(dialogView)

                .setPositiveButton("Save", (dialog, which) -> {

                    String newTitle = editTitle.getText().toString();
                    String newBody = editBody.getText().toString();

                    dbHelper.updateMemory(memoryID, newTitle, newBody);

                    title.setText(newTitle);
                    body.setText(newBody);

                })

                .setNegativeButton("Cancel", null)
                .show();
    }
}