package com.techmnc.studentmanagementapp.activity;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techmnc.studentmanagementapp.R;

import java.util.HashMap;
import java.util.Map;

public class studentAddAccount extends AppCompatActivity {

    private LinearLayout addStudentLayout;
    private EditText emailEditText, passwordEditText, studentIdEditText,nameeditext;
    private Button addStudentButton;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add_account);  // Make sure to replace with your XML file name

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI components
        addStudentLayout = findViewById(R.id.addStudentLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        studentIdEditText = findViewById(R.id.studentIdEditText);
        addStudentButton = findViewById(R.id.addstubutton);
        nameeditext = findViewById(R.id.fullNameEditTexts);

        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle visibility of email, password, and student ID fields
                toggleAddStudentFieldsVisibility();

                // Call the method to add a student
                addStudent();
            }
        });
    }

    private void toggleAddStudentFieldsVisibility() {
        // Toggle visibility of email, password, and student ID fields
        if (addStudentLayout.getVisibility() == View.VISIBLE) {
            addStudentLayout.setVisibility(View.GONE);
        } else {
            addStudentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void addStudent() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String studentId = studentIdEditText.getText().toString().trim();
        String fullname = nameeditext.getText().toString().trim();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty() || studentId.isEmpty()|| fullname.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase authentication to create a new student user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Student user created successfully
                            String userId = auth.getCurrentUser().getUid();

                            // Save additional student details to Firestore
                            saveStudentDetails(userId, email, studentId,fullname);

                            Toast.makeText(studentAddAccount.this, "Student added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(studentAddAccount.this, "Failed to add student. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveStudentDetails(String userId, String email, String studentId, String fullname) {
        // Assuming you have a "users" collection in Firestore
        Map<String, Object> studentData = new HashMap<>();
        studentData.put("email", email);
        studentData.put("studentId", studentId);
        studentData.put("fullName", fullname);
        // Add user data to Firestore
        firestore.collection("users").document(userId)
                .set(studentData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Student details saved successfully
                            Log.d(TAG, "Student details added to Firestore successfully");
                        } else {
                            // Failed to save student details
                            Log.e(TAG, "Error adding student details to Firestore", task.getException());
                        }
                    }
                });
    }
//    private static class Student {
//        private String email;
//        private String studentId;
//
//        public Student(String email, String studentId) {
//            this.email = email;
//            this.studentId = studentId;
//        }
//    }
}
