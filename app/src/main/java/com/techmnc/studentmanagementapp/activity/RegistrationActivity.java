package com.techmnc.studentmanagementapp.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techmnc.studentmanagementapp.MainActivity;
import com.techmnc.studentmanagementapp.databinding.ActivityRegistrationBinding;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        binding.registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle registration activity navigation
                startActivity(new Intent(RegistrationActivity.this, RegistrationActivity.class));
            }
        });
    }

    private void loginUser() {
        String email = binding.emailEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login successful, handle accordingly
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Login failed. Check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void registerUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful, store user type in Firestore
                            storeUserType(email);
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void storeUserType(String email) {
        String userType = binding.studentRadioButton.isChecked() ? "student" : "teacher";

        Map<String, Object> user = new HashMap<>();
        user.put("userType", userType);

        firestore.collection("users").document(email).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // User type stored successfully, navigate to home activity
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Error storing user type.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
