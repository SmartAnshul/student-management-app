package com.techmnc.studentmanagementapp.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.techmnc.studentmanagementapp.R;

public class LoginActivity extends AppCompatActivity {

    private RadioGroup userTypeRadioGroup;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = userTypeRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);

                if (selectedRadioButton != null) {
                    String userType = selectedRadioButton.getText().toString();

                    // Open the appropriate signup activity based on the selected user type
                    if ("Student".equals(userType)) {
                        startActivity(new Intent(LoginActivity.this, studentSignup.class));
                    } else if ("Teacher".equals(userType)) {
                        startActivity(new Intent(LoginActivity.this, teacherSignup.class));
                    }
                }
            }
        });
    }

    // Function to handle login option click
    public void onLoginClicked(View view) {
        // Open the registration activity for existing users
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }
}
