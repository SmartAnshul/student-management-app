package com.techmnc.studentmanagementapp.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techmnc.studentmanagementapp.R;
import com.techmnc.studentmanagementapp.StudentAdapter;

import java.util.ArrayList;
import java.util.List;

// RemoveStudentActivity.java
public class removeStudent extends AppCompatActivity {

    private EditText searchStudentEditText;
    private RecyclerView studentRecyclerView;
    private StudentAdapter studentAdapter;

    // Initialize Firebase components
    private FirebaseFirestore firestore;

    private List<removeStudent.Student> studentList;
    private List<removeStudent.Student> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_student);

        // Initialize Firebase components
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI components
        searchStudentEditText = findViewById(R.id.searchStudentEditText);
        studentRecyclerView = findViewById(R.id.studentRecyclerView);

        // Set up RecyclerView with an empty list (you'll fetch data from Firebase)
        studentList = new ArrayList<>();
        filteredList = new ArrayList<>();
        studentAdapter = new StudentAdapter(filteredList, firestore); // Pass the FirebaseFirestore instance
        studentRecyclerView.setAdapter(studentAdapter);

        // Set a layout manager for RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        studentRecyclerView.setLayoutManager(layoutManager);

        // You need to fetch data from Firebase and update the studentList
        // For simplicity, I'll assume you have a method `fetchStudents` to get student data
        fetchStudents();

        // Add text change listener for search functionality
        searchStudentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterStudents(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void filterStudents(String query) {
        filteredList.clear();
        for (removeStudent.Student student : studentList) {
            String name = student.getName();
            if (name != null && name.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(student);
            }
        }
        studentAdapter.notifyDataSetChanged();
    }


    private void fetchStudents() {
        firestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        studentList.clear(); // Clear the existing list
                        for (DocumentSnapshot document : task.getResult()) {
                            // Assuming your document has fields "fullName" and "studentId"
                            String fullName = document.getString("fullName");
                            String studentId = document.getString("studentId");

                            removeStudent.Student student = new removeStudent.Student(fullName, studentId);
                            studentList.add(student);
                        }

                        // Initially, display the filtered list with all students
                        filteredList.addAll(studentList);
                        studentAdapter.notifyDataSetChanged();
                    } else {
                        // Handle the error
                        Toast.makeText(removeStudent.this, "Failed to fetch students", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Create a simple data class for holding student details
    public static class Student {
        private String fullName;
        private String studentId;

        public Student(String fullName, String studentId) {
            this.fullName = fullName;
            this.studentId = studentId;
        }

        public String getName() {
            return fullName;
        }

        public String getId() {
            return studentId;
        }
    }
}