package com.techmnc.studentmanagementapp.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.techmnc.studentmanagementapp.fragmentss.AttendenceFragment;

public class studentManagementsActivity extends AppCompatActivity {

    private LinearLayout addStudentLayout;
    private EditText emailEditText, passwordEditText, studentIdEditText;
    private Button addStudentButton;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_managements);

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        findViewById(R.id.addStudentButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(studentManagementsActivity.this,studentAddAccount.class));
                finish();
            }
        });
        findViewById(R.id.removeStudentButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(studentManagementsActivity.this,removeStudent.class));
                finish();
            }
        });
        findViewById(R.id.viewAttendanceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(studentManagementsActivity.this,  AttendenceFragment.class));
//                finish();
            }
        });





    }


}
