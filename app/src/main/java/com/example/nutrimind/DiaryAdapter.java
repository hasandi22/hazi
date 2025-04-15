package com.example.nutrimind;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DiaryAdapter extends BaseAdapter {

    private List<DiaryModel> diaryList;
    private LayoutInflater inflater;
    private Context context;

    public interface DiaryActionListener {
        void onDelete(DiaryModel diary);
        void onEdit(DiaryModel diary);
    }

    private DiaryActionListener actionListener;

    public DiaryAdapter(Context context, List<DiaryModel> diaryList) {
        this.context = context;
        this.diaryList = diaryList;
        this.inflater = LayoutInflater.from(context);

        if (context instanceof DiaryActionListener) {
            this.actionListener = (DiaryActionListener) context;
        } else {
            throw new ClassCastException("Activity must implement DiaryActionListener");
        }
    }

    @Override
    public int getCount() {
        return diaryList.size();
    }

    @Override
    public Object getItem(int position) {
        return diaryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_diary_item, parent, false);
        }

        DiaryModel entry = diaryList.get(position);

        TextView feelingText = convertView.findViewById(R.id.textFeeling);
        TextView dateText = convertView.findViewById(R.id.textDate);
        ImageView iconEdit = convertView.findViewById(R.id.btnEdit);
        ImageView iconDelete = convertView.findViewById(R.id.btnDelete);

        feelingText.setText(entry.getFeeling());

        if (entry.getTimestamp() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            dateText.setText(dateFormat.format(entry.getTimestamp()));
        } else {
            dateText.setText("Unknown Date");
        }

        iconDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Entry")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        if (actionListener != null) {
                            actionListener.onDelete(entry);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        iconEdit.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEdit(entry);
            }
        });

        return convertView;
    }
}
