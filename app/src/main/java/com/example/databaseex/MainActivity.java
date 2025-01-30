package com.example.databaseex;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.databaseex.CommentDatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private CommentDatabaseHelper dbHelper;
    private EditText nameEditText, commentEditText;
    private TextView commentTextView;
    private Spinner commentSpinner;
    private ArrayList<String> commentList;
    private ArrayList<Integer> commentIds;
    private ArrayAdapter<String> spinnerAdapter;
    private int selectedCommentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new CommentDatabaseHelper(this);
        nameEditText = findViewById(R.id.nameEditText);
        commentEditText = findViewById(R.id.commentEditText);
        commentTextView = findViewById(R.id.commentTextView);
        commentSpinner = findViewById(R.id.commentSpinner);
        Button createButton = findViewById(R.id.createButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button viewButton = findViewById(R.id.viewButton);

        commentList = new ArrayList<>();
        commentIds = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, commentList);
        commentSpinner.setAdapter(spinnerAdapter);
        checkComments();

        commentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCommentId = commentIds.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCommentId = -1;
                commentTextView.setText("");
                checkComments();
            }
        });
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSelectedComment();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String comment = commentEditText.getText().toString();
                dbHelper.addComment(name, comment);
                loadComments();
                nameEditText.setText("");
                commentEditText.setText("");

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCommentId != -1) {
                    dbHelper.deleteComment(selectedCommentId);
                    loadComments();
                    commentTextView.setText("");
                    selectedCommentId = -1;
                    checkComments();
                }
            }
        });

        loadComments();
    }
    public void checkComments() {
        if (commentList.isEmpty()) {
            commentList.add("No hay comentarios disponibles");
            spinnerAdapter.notifyDataSetChanged();
        } else {
            loadSelectedComment();
        }
    }

    private void loadComments() {
        commentList.clear();
        commentIds.clear();
        Cursor cursor = dbHelper.getComments();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            commentList.add(name);
            commentIds.add(id);
        }
        cursor.close();
        spinnerAdapter.notifyDataSetChanged();
    }

    private void loadSelectedComment() {
        if (selectedCommentId != -1) {
            Cursor cursor = dbHelper.getCommentById(selectedCommentId);
            if (cursor.moveToFirst()) {
                String name = cursor.getString(1);
                String text = cursor.getString(2);
                commentTextView.setText(name + ": " + text);
            }
            cursor.close();
        }
    }
}
