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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    private List<Map<String, Object>> userCatsList;
    String UID;
    TextView tvUserUsername, tvUserEmail;
    Button btnEditUserInformation;
    private static int totalCats, totalFinishedTasks;

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
                        updateUIWithProfile(username,userId);
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error fetching user document", e);
                });
    }

    private void fetchUserCats(String userId){
        DocumentReference userRef = firebaseFirestore.collection("users").document(userId);
        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {

                        List<Object> cats = (List<Object>) documentSnapshot.get("cats");
                        if (cats != null) {
                            totalCats = cats.size();
                        }
                    }else{
                        totalCats = 0;
                    }
                })
                .addOnFailureListener(e -> {
                    totalCats = 0;
                });
    }

    private void fetchUserTasks(String userId){
        DocumentReference userRef = firebaseFirestore.collection("users").document(userId);
        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {

                        List<Object> tasks = (List<Object>) documentSnapshot.get("tasks");
                        if (tasks != null) {
                            totalFinishedTasks = tasks.size();
                        }else{
                            totalFinishedTasks=0;
                        }
                    }

                })
                .addOnFailureListener(e -> {
                    totalFinishedTasks = 0;
                });
    }


    private void updateUIWithProfile(String username, String userId) {
        fetchUserCats(userId);
        fetchUserTasks(userId);


        TextView tvUserUsername = findViewById(R.id.username);
        TextView tvTotalPaws = findViewById(R.id.tvTotalTasksFinished);
        TextView tvTotalTasks = findViewById(R.id.tvTotalTasksFinished);

        tvUserUsername.setText(username);
        tvTotalPaws.setText(String.valueOf(totalCats));
        tvTotalTasks.setText(String.valueOf(totalFinishedTasks));
//        tvUserEmail.setText(email);
    }




}