package com.example.firebaseconnection;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    String UID;
    TextView tvUserUsername, tvUserEmail;
    Button btnEditUserInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard_totals);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
//        UID = mAuth.getCurrentUser().getUid();
        UID ="YkbW5nnkv1aLDXUvEYxZDMB1oj03";

//        tvUserEmail = findViewById(R.id.username);
//        tvUserUsername = findViewById(R.id.username);


//        btnEditUserInformation = findViewById(R.id.btnEditUserInformation);

//        btnEditUserInformation.setOnClickListener(v->{
//            updateUsername(UID,"newUsername");
//        });

        fetchUserInfo(UID);
    }

    private void updateUsername(String userId, String newUsername) {
        DocumentReference userRef = firebaseFirestore.collection("users").document(userId);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userRef.update("username", newUsername)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("TAG", "Username updated successfully");
                                    // Update local tasksList and UI
                                    fetchUserInfo(userId); // Re-fetch user info to update local list and UI
                                })
                                .addOnFailureListener(e -> Log.e("TAG", "Error updating username", e));
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error fetching user document", e));
    }


    private void fetchUserInfo(String userId) {
        DocumentReference userRef = firebaseFirestore.collection("users").document(userId);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Log.d("TAG", "User document fetched successfully");
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");
                        updateUIWithProfile(username, email);
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error fetching user document", e);
                });
    }

    private void updateUIWithProfile(String username, String email) {
        TextView tvUserUsername = findViewById(R.id.username);
//        TextView tvUserEmail = findViewById(R.id.tvUserEmail);

        tvUserUsername.setText(username);
//        tvUserEmail.setText(email);
    }




}