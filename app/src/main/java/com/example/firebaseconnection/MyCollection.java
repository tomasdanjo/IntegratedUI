package com.example.firebaseconnection;


import static com.example.firebaseconnection.ProfileActivity.UID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCollection extends AppCompatActivity {
    private static MyCollection instance;
    TextView txtCoin;
    Long userCoins;
    static FirebaseFirestore firebaseFirestore;
    static ArrayList<Paw> paws;
    static RecyclerView pawsRecyclerView;
    FirebaseAuth mAuth;
    static List<Map<String, Object>> userCatsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_8_my_collection);
        instance = this;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyCollection.this, MenuActivity.class);
                startActivity(i);
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
//        UID = mAuth.getCurrentUser().getUid();
        UID ="YkbW5nnkv1aLDXUvEYxZDMB1oj03";

        userCatsList = new ArrayList<>();
        paws = new ArrayList<>();
        getUserBalance(UID);
        fetchUserCats();
        pawsRecyclerView = findViewById(R.id.pawsRecyclerView);
        generatePaws();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    public static MyCollection getInstance() {
        return instance;
    }

    public static void fetchUserCats(){
//        UID = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = firebaseFirestore.collection("users").document(UID);
        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> cats = (List<Map<String, Object>>) documentSnapshot.get("cats");
                        if (cats != null) {
                            userCatsList.clear();
                            for (Map<String, Object> cat : cats) {
                                String catImageURL = (String) cat.get("catImageURL");
                                String catName = (String) cat.get("catName");

                                Map<String, Object> taskMap = new HashMap<>();
                                taskMap.put("catImageURL", catImageURL);
                                taskMap.put("catName", catName);

                                userCatsList.add(taskMap);
                                paws.add(new Paw(catName, catImageURL, null, null));

                                Log.i("TAG", "Size " + userCatsList.size());
                                Log.d("TAG", "catImageURL: " + catImageURL);
                                Log.d("TAG", "catName: " + catName);
                            }
                            generatePaws();
                        } else {
                            Log.d("TAG", "No tasks found");
                        }
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error fetching tasks", e);
                });
    }

    public void getUserBalance(String userID) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
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

    public static void getPaws() {
        paws.clear();
        for (Map<String, Object> map : userCatsList) {
            String catImageURL = (String) map.get("catImageURL");
            String catName = (String) map.get("catName");
            paws.add(new Paw(catName, catImageURL, null, null));
        }
    }

    public static void generatePaws() {
        Log.d("Lookie here Lookie here", String.valueOf(paws.size()));
        RecyclerViewAdapterPaw adapterTask = new RecyclerViewAdapterPaw(getInstance(), paws);
        pawsRecyclerView.setAdapter(adapterTask);
        pawsRecyclerView.setLayoutManager(new LinearLayoutManager(getInstance()));
    }

    private void updateCoinText(){
        txtCoin =  findViewById(R.id.txtCoinBalance);
        txtCoin.setText(String.valueOf(userCoins));
    }
}