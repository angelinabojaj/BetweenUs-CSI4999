package com.example.betweenus.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.betweenus.DatabaseHelper;
import com.example.betweenus.R;

public class CreateMemoryActivity extends AppCompatActivity {
    EditText etTitle, etBody;
    Button btnPost;
    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_memory);

        etTitle = findViewById(R.id.et_title);
        etBody = findViewById(R.id.et_body);
        btnPost = findViewById(R.id.btn_post);

        dbHelper = new DatabaseHelper(this);

        btnPost.setOnClickListener(v1 -> {

            String title = etTitle.getText().toString();
            String body = etBody.getText().toString();

            dbHelper.createMemory(
                    1, // PosterID (current user)
                    title,
                    body,
                    "image",
                    "default_memory.jpg",
                    System.currentTimeMillis() + ""
            );

            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
    }
}