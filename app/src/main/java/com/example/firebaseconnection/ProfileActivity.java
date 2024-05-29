package com.example.firebaseconnection;

import static com.example.firebaseconnection.CatShopActivity.firebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    static FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    private List<Map<String, Object>> userCatsList;
    static String UID;
    TextView tvUserUsername, tvUserEmail;
    Button btnEditUserInformation;

    static String username;
    private static int totalCats, totalFinishedTasks;
    static TextView txtCoin;

    static Long userCoins;


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

        LinearLayout btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, Menu.class);
                startActivity(i);
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
//        UID = mAuth.getCurrentUser().getUid();
        UID ="YkbW5nnkv1aLDXUvEYxZDMB1oj03";




        getUserCoins(UID);
        fetchUserInfo(UID);
        fetchUserCats(UID);
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
                        username = documentSnapshot.getString("username");
                        fetchUserCats(userId);
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

                        List<Map<String, Object>> cats = (List<Map<String, Object>>) documentSnapshot.get("cats");
                        if (cats != null) {
                            totalCats = cats.size();
                            Log.d("SIMON!!!!!", String.valueOf(totalCats));
                            fetchUserTasks(userId);
                        }else{
                            totalCats = 0;
                        }
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

                        List<Map<String, String>> tasks = (List<Map<String, String>>) documentSnapshot.get("tasks");
                        if (tasks != null) {
                            totalFinishedTasks = tasks.size();
                            updateUIWithProfile(userId);
                        }else{
                            totalFinishedTasks = 0;
                        }
                    }

                })
                .addOnFailureListener(e -> {
                    totalFinishedTasks = 0;
                });
    }


    private void updateUIWithProfile(String userId) {


        TextView tvUserUsername = findViewById(R.id.username);
        TextView tvTotalPaws = findViewById(R.id.tvTotalCats);
        TextView tvTotalTasks = findViewById(R.id.tvTotalTasks);

        tvUserUsername.setText(username);
        tvTotalPaws.setText(String.valueOf(totalCats));
        tvTotalTasks.setText(String.valueOf(totalFinishedTasks));
//        tvUserEmail.setText(email);
    }

    public void getUserCoins(String userID) {
        DocumentReference userRef = firebaseFirestore.collection("users").document(userID);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        //get the "coins" field from the document

                        userCoins = documentSnapshot.getLong("coins");
                        if (userCoins != null) {
                            updateCoinText();
                            Log.d("TAG", "User has " + userCoins + " coins.");
                        } else {
                            Log.d("TAG", "Coins field is not found in the document.");
                        }
                        //update



                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error fetching user document", e));
    }

    private void updateCoinText(){
        txtCoin =  findViewById(R.id.txtCoinBalance);
        txtCoin.setText(String.valueOf(userCoins));
    }




}