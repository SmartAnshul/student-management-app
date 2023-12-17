package com.techmnc.studentmanagementapp.activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techmnc.studentmanagementapp.MainActivity;
import com.techmnc.studentmanagementapp.databinding.ActivityStudentSignupBinding;

import java.util.HashMap;
import java.util.Map;

public class studentSignup extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    // Firebase
    private FirebaseAuth mAuth;

    // View Binding
    private ActivityStudentSignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentSignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize Firebase
        FirebaseApp.initializeApp(this); // Add this line to initialize Firebase
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();



        // Signup Button Click
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        // Retrieve user input
        String fullName = binding.fullNameEditText.getText().toString().trim();
        String email = binding.emailEditText.getText().toString().trim();
        String phone = binding.phoneEditText.getText().toString().trim();
        String studentId = binding.studentIdEditText.getText().toString().trim();
        String grade = binding.gradeEditText.getText().toString().trim();
        String subject = binding.subjectEditText.getText().toString().trim();
        String homeAddress = binding.homeAddressEditText.getText().toString().trim();
        String guardianNames = binding.guardianNamesEditText.getText().toString().trim();
        String guardianContact = binding.guardianContactEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(fullName)) {
            binding.fullNameLayout.setError("Enter your full name");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            binding.emailLayout.setError("Enter your email address");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            binding.phoneLayout.setError("Enter your phone number");
            return;
        }

        if (TextUtils.isEmpty(studentId)) {
            binding.studentIdLayout.setError("Enter your student ID");
            return;
        }

        if (TextUtils.isEmpty(grade)) {
            binding.gradeLayout.setError("Enter your grade/class");
            return;
        }

        if (TextUtils.isEmpty(subject)) {
            binding.subjectLayout.setError("Enter your courses/subjects");
            return;
        }

        if (TextUtils.isEmpty(homeAddress)) {
            binding.homeAddressLayout.setError("Enter your home address");
            return;
        }

        if (TextUtils.isEmpty(guardianNames)) {
            binding.guardianNamesLayout.setError("Enter your parent/guardian names");
            return;
        }

        if (TextUtils.isEmpty(guardianContact)) {
            binding.guardianContactLayout.setError("Enter your parent/guardian contact information");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.passwordLayout.setError("Enter your password");
            return;
        }

        // Create user account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Store additional user information in Firestore (you may customize this part)
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("fullName", fullName);
                            userData.put("email", email);
                            userData.put("phone", phone);
                            userData.put("studentId", studentId);
                            userData.put("grade", grade);
                            userData.put("subject", subject);
                            userData.put("homeAddress", homeAddress);
                            userData.put("guardianNames", guardianNames);
                            userData.put("guardianContact", guardianContact);

                            // Add user data to Firestore
                            db.collection("users").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "User data added to Firestore successfully");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Error adding user data to Firestore", e);
                                        }
                                    });

                            // Update UI or navigate to the next activity
                            Intent intent = new Intent(studentSignup.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Close the current activity
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(studentSignup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}