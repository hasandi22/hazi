package com.example.nutrimind;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewPastDiaries extends AppCompatActivity implements DiaryAdapter.DiaryActionListener {

    private ListView listView;
    private DiaryAdapter diaryAdapter;
    private List<DiaryModel> diaryList = new ArrayList<>();
    private FirebaseFirestore db;
    private Button btnBackToFeelingsDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_diaries);

        listView = findViewById(R.id.listView);
        btnBackToFeelingsDiary = findViewById(R.id.btnBackToView);
        db = FirebaseFirestore.getInstance();

        // Fetch and display diary entries
        fetchPastDiaries();

        // Initialize adapter and set it to the ListView
        diaryAdapter = new DiaryAdapter(this, diaryList);
        listView.setAdapter(diaryAdapter);

        // Back to FeelingDiary
        btnBackToFeelingsDiary.setOnClickListener(v -> {
            Intent intent = new Intent(ViewPastDiaries.this, FeelingDiary.class);
            startActivity(intent);
            finish();
        });
    }

    // Method to fetch past diaries from Firestore
    private void fetchPastDiaries() {
        db.collection("diaryEntries")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)  // Order by timestamp, descending (most recent first)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        diaryList.clear();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot document : querySnapshot) {
                                DiaryModel diary = document.toObject(DiaryModel.class);
                                if (diary != null) {
                                    diary.setDocumentId(document.getId());
                                    diaryList.add(diary);
                                }
                            }
                            diaryAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ViewPastDiaries.this, "No past diary entries found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ViewPastDiaries.this, "Failed to fetch entries.", Toast.LENGTH_SHORT).show();
                    }
                });


}

    // Override onResume to refresh the diary list when the activity is brought back
    @Override
    protected void onResume() {
        super.onResume();
        fetchPastDiaries();  // Reload the diary entries
    }

    // Handle delete action
    @Override
    public void onDelete(DiaryModel diary) {
        db.collection("diaryEntries")
                .document(diary.getDocumentId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Diary Deleted", Toast.LENGTH_SHORT).show();
                    diaryList.remove(diary);
                    diaryAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to Delete", Toast.LENGTH_SHORT).show();
                });
    }

    // Handle edit action
    @Override
    public void onEdit(DiaryModel diary) {
        // Pass the existing feeling and documentId to FeelingDiary for editing
        Intent intent = new Intent(ViewPastDiaries.this, FeelingDiary.class);
        intent.putExtra("diaryId", diary.getDocumentId());  // Pass the diary ID
        intent.putExtra("feeling", diary.getFeeling());      // Pass the existing feeling
        startActivity(intent); // Start the FeelingDiary activity
    }
}

