package com.example.nutrimind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    // Declare UI elements: input fields, button, and text view
    EditText edUsername, edPassword;
    Button btnLogin;
    TextView tViewSignUp;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Set the layout for this activity

        // Initialize UI elements by linking them with their respective views in the layout
        //links java variables with UI components
        edUsername = findViewById(R.id.editTextLoginUserName);
        edPassword = findViewById(R.id.editTextLoginPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        tViewSignUp = findViewById(R.id.textViewSignUp);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Set up the login button click listener
        btnLogin.setOnClickListener(v -> {
            String email = edUsername.getText().toString().trim();  // Get inputs from the user
            String password = edPassword.getText().toString().trim();

            // Check if both username and password are entered
            if (email.isEmpty() || password.isEmpty()) {
                // Show a message if either field is empty
                Toast.makeText(getApplicationContext(), "Please enter both email and password", Toast.LENGTH_SHORT).show();
            } else {
                // Firebase Authentication Login
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, HomeActivity.class));
                                    finish();
                                }
                            } else {
                                // Display specific error message
                                Toast.makeText(Login.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Set up the sign-up link to navigate to the Signup activity
        tViewSignUp.setOnClickListener(v -> startActivity(new Intent(Login.this, Signup.class)));
    }
}
