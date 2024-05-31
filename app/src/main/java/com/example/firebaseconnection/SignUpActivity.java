package com.example.firebaseconnection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {
    EditText signUpName, signUpEmail, signUpUsername, signUpPassword;
    LinearLayout btnSignUp;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    ImageView checkbox;
    static String UID = null;
    static boolean isRegistered = false;
    boolean agreedToPrivacyPolicy = false;
    TextView txtPrivacyPolicy;
    TextView txtSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_2_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setContentView(R.layout.activity_2_sign_up);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        txtPrivacyPolicy = findViewById(R.id.txtPrivacyPolicy);

        btnSignUp = findViewById(R.id.btnSignUp);
        checkbox = findViewById(R.id.checkbox);
        signUpName = findViewById(R.id.fieldInputName);
        signUpUsername = findViewById(R.id.fieldInputUsername);
        signUpEmail = findViewById(R.id.fieldInputEmail);
        signUpPassword = findViewById(R.id.fieldInputPassword);
        txtSignIn = findViewById(R.id.txtSignIn);


        btnSignUp.setOnClickListener(v -> {
            if (!agreedToPrivacyPolicy) {
                Toast.makeText(this, "Please agree to the privacy policy.", Toast.LENGTH_SHORT).show();
                return;
            }
            String name,username,email,password;
            name = signUpName.getText().toString();
            username = signUpUsername.getText().toString();
            email = signUpEmail.getText().toString();
            password = signUpPassword.getText().toString();
            registerAccount(name, username, email, password);
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agreedToPrivacyPolicy = !agreedToPrivacyPolicy;
                if (!agreedToPrivacyPolicy) {
                    checkbox.setImageResource(R.drawable.button_white);
                } else {
                    checkbox.setImageResource(R.drawable.button_white_filled);
                }
            }
        });

        txtPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_privacy_policy, null);

                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                PopupWindow privacyPolicyPopup = new PopupWindow(popupView, width, height, true);

                privacyPolicyPopup.showAtLocation(findViewById(R.id.main), Gravity.CENTER_VERTICAL, 0, 0);

                LinearLayout btnDone = popupView.findViewById(R.id.btnDone);
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        privacyPolicyPopup.dismiss();
                    }
                });
            }
        });
    }

    private void onCompleteRegistration(boolean isSuccessful, FirebaseUser user, String username, String email, String name) {
        if (isSuccessful && user != null) {
            addUserToFirestore(user.getUid(), email, username, name);
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(SignUpActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void registerAccount(String name, String username, String email, String password) {
        Log.d("TAG", "Starting registration");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "createUserWithEmail:success");
                        Toast.makeText(SignUpActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        onCompleteRegistration(true, user, email, username, name);
                    } else {
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        onCompleteRegistration(false, null, null, null, null);
                    }
                });
    }


    private void addUserToFirestore(String userId, String email, String username, String name) {
        Log.i("TAG", "Adding user to Firestore with ID: " + userId);
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("username", username);
        userData.put("name", name);

        List<HashMap<String, String>> catsList = new ArrayList<>();
        userData.put("cats", catsList);
        List<HashMap<String, Object>> taskList = new ArrayList<>();
        userData.put("tasks", taskList);

        ArrayList badgeList = new ArrayList();
        userData.put("badge", badgeList);
        userData.put("coins", 14);


        if (firebaseFirestore == null) {
            Log.e("TAG", "Firestore is not initialized");
            return;
        }

        firebaseFirestore.collection("users")
                .document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> Log.d("TAG", "User added to Firestore"))
                .addOnFailureListener(e -> Log.e("TAG", "Error adding user to Firestore", e));
    }
}