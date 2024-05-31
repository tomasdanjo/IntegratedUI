package com.example.firebaseconnection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    EditText loginUsername, loginPassword;
    LinearLayout btnLogin;
    TextView signUpRedirection;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_3_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLogin = findViewById(R.id.btnSignUp);
        loginUsername = findViewById(R.id.fieldInputUsername);
        loginPassword = findViewById(R.id.fieldInputPassword);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(v -> {

            String email = loginUsername.getText().toString();
            String password = loginPassword.getText().toString();
            if(password.isEmpty()){
                loginPassword.setError("Please enter your password.");

            }
            if(email.isEmpty()){
                loginUsername.setError("Please enter your email.");
            }
            if(!password.isEmpty() && !email.isEmpty())loginAccount(email, password);
        });

    }

    protected void loginAccount(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        System.out.println("Log-in Successful!");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(SignInActivity.this, "Log-in successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, MenuActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignInActivity.this, "Log-in failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}