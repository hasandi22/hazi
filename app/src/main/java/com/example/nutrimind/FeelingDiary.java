package com.example.nutrimind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FeelingDiary extends AppCompatActivity {

    private EditText editFeeling;
    private Button btnSave, btnViewPastDiary, btnBackToHome;
    private String diaryId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling_diary);

        // Initialize views
        editFeeling = findViewById(R.id.etFeelingEntry);
        btnSave = findViewById(R.id.btnSaveEntry);
        btnViewPastDiary = findViewById(R.id.btnViewPastDiary);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        db = FirebaseFirestore.getInstance();

        // Get data passed from ViewPastDiaries (for editing mode)
        Intent intent = getIntent();
        diaryId = intent.getStringExtra("diaryId");
        String currentFeeling = intent.getStringExtra("feeling");

        if (currentFeeling != null && !currentFeeling.isEmpty()) {
            editFeeling.setText(currentFeeling);
        }

        // Save or update diary entry
        btnSave.setOnClickListener(v -> {
            String newFeeling = editFeeling.getText().toString().trim();

            if (newFeeling.isEmpty()) {
                Toast.makeText(FeelingDiary.this, "Feeling cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                if (diaryId != null) {
                    updateDiary(diaryId, newFeeling);
                } else {
                    saveNewDiary(newFeeling);
                }
            }
        });

        // Navigate to view past diaries
        btnViewPastDiary.setOnClickListener(v -> {
            Intent viewIntent = new Intent(FeelingDiary.this, ViewPastDiaries.class);
            startActivity(viewIntent);
        });

        // Navigate back to home page
        btnBackToHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(FeelingDiary.this, HomeActivity.class);
            startActivity(homeIntent);
            finish();
        });
    }

    private void updateDiary(String diaryId, String updatedFeeling) {
        db.collection("diaryEntries")
                .document(diaryId)
                .update("feeling", updatedFeeling)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(FeelingDiary.this, "Diary updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(FeelingDiary.this, "Failed to update diary", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveNewDiary(String newFeeling) {
        DiaryModel newDiary = new DiaryModel();
        newDiary.setFeeling(newFeeling);
        newDiary.setTimestamp(System.currentTimeMillis());

        db.collection("diaryEntries")
                .add(newDiary)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(FeelingDiary.this, "Diary saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(FeelingDiary.this, "Failed to save diary", Toast.LENGTH_SHORT).show();
                });
    }
}
