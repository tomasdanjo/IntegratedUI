package com.example.firebaseconnection;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PawShopActivity extends AppCompatActivity {
    private static PawShopActivity instance;

    static FirebaseFirestore firebaseFirestore;
    private static List<Map<String, Object>> catShopList;
    static List<Map<String, Object>> userCatsList;
    private final String documentId = "xv6JGkDrmpJMylgUIeEz";
    private static String UID;
    private FirebaseAuth mAuth;
    private static Long userCoins;
    private static Long newUserCoins;
    public static ArrayList<Paw> paws;
    static RecyclerView pawsRecyclerView;
    LinearLayout btnMyCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_7_paw_shop);
        instance = this;
        LinearLayout btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PawShopActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        btnMyCollection = findViewById(R.id.btnMyCollection);

        btnMyCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PawShopActivity.this, MyCollection.class);
                startActivity(i);
            }
        });

        paws = new ArrayList<>();
        pawsRecyclerView = findViewById(R.id.pawsRecyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        catShopList  = new ArrayList<>();
        userCatsList = new ArrayList<>();


        fetchCats(documentId);

        mAuth = FirebaseAuth.getInstance();
//        UID = mAuth.getCurrentUser().getUid();
        UID = "YkbW5nnkv1aLDXUvEYxZDMB1oj03";

        fetchUserCats();
        getUserBalance(UID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    public static PawShopActivity getInstance() {
        return instance;
    }

    private static void fetchCats(String documentId) {
        //reference to the cat document
        DocumentReference catRef = firebaseFirestore.collection("cats").document(documentId);

        catRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> cats = (Map<String, Object>) documentSnapshot.getData();
                        if (cats != null) {
                            catShopList.clear();
                            for (Map.Entry<String, Object> entry : cats.entrySet()) {
                                String catName = entry.getKey();
                                Map<String, Object> catDetails = (Map<String, Object>) entry.getValue();

                                // Extract the cat details
                                String catImageUrl = (String) catDetails.get("catImageURL");
                                Long catPrice = (Long) catDetails.get("catPrice");
                                Long catRarity = (Long) catDetails.get("catRarity");

                                // Create a new map to hold the cat details
                                Map<String, Object> catMap = new HashMap<>();
                                catMap.put("catName", catName);
                                catMap.put("catImageURL", catImageUrl);
                                catMap.put("catPrice", catPrice);
                                catMap.put("catRarity", catRarity);

                                // Add the cat map to the catShopList
                                paws.add(new Paw(catName, catImageUrl, catPrice, catRarity));
                                Log.d("TAG", "Cat Name: " + catName);
                                Log.d("TAG", "Cat Image URL: " + catImageUrl);
                                Log.d("TAG", "Cat Price: " + catPrice);
                                Log.d("TAG", "Cat Rarity: " + catRarity);
                            }
                            generatePaws();
                        } else {
                            Log.d("TAG", "No cats found");
                        }
                    } else {
                        Log.d("TAG", "Cat document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error fetching cats", e);
                    // Handle the error here
                });
    }

    public static void fetchUserCats(){
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

                                Log.i("TAG", "Size " + userCatsList.size());
                                Log.d("TAG", "catImageURL: " + catImageURL);
                                Log.d("TAG", "catName: " + catName);
                            }
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

    private void updateUIWithCats() {
        if (!catShopList.isEmpty()) {
            generatePaws();
        } else {
            Log.i("TAG", "EMPTY LIST");
        }
        Log.i("TAG", "Total cats: " + catShopList.size());
    }

    public static void getUserCoins(String catName, Long price) {
        DocumentReference userRef = firebaseFirestore.collection("users").document(UID);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        //get the "coins" field from the document
                        userCoins = documentSnapshot.getLong("coins");
                        if (userCoins != null) {
                            Log.d("TAG", "User has " + userCoins + " coins.");
                        } else {
                            Log.d("TAG", "Coins field is not found in the document.");
                        }
                        checkCoinBalance(catName, userCoins, price);
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error fetching user document", e));
    }

    private static void checkCoinBalance(String catName, Long userCoins, Long catFee){
        assert userCoins >= catFee;
        newUserCoins = userCoins - catFee;
        updateUserCoins(catName, newUserCoins);
    }

    private static void updateUserCoins(String catName, Long newUserCoins) {
        DocumentReference userRef = firebaseFirestore.collection("users").document(UID);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userRef.update("coins", newUserCoins)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("TAG", "Username updated successfully");

                                    addCatToUser(catName, catName + ".svg");
                                })
                                .addOnFailureListener(e -> Log.e("TAG", "Error updating username", e));
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error fetching user document", e));
    }

    private static void addCatToUser(String catName, String catImage) {
        DocumentReference userRef = firebaseFirestore.collection("users").document(UID);

        Map<String, String> newCat = new HashMap<>();
        newCat.put("catName", catName);
        newCat.put("catImageURL", catImage);

        //update cats field
        userRef.update("cats", FieldValue.arrayUnion(newCat))
            .addOnSuccessListener(aVoid -> {
                Log.d("TAG", "Cat added successfully");
                fetchUserInfo();
            })
            .addOnFailureListener(e -> {
                Log.e("TAG", "Error adding cat", e);
            });
    }

    private static void fetchUserInfo() {
        DocumentReference userRef = firebaseFirestore.collection("users").document(UID);

        userRef.get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Log.d("TAG", "User document fetched successfully");
                } else {
                    Log.d("TAG", "User document does not exist");
                }
            })
            .addOnFailureListener(e -> {
                Log.e("TAG", "Error fetching user document", e);
            });
    }

    public static void generatePaws() {
        RecyclerViewAdapterPaw adapterPaw = new RecyclerViewAdapterPaw(getInstance(), paws);
        pawsRecyclerView.setAdapter(adapterPaw);
        pawsRecyclerView.setLayoutManager(new GridLayoutManager(getInstance(), 2));
    }

    public void getUserBalance(String userID) {
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
        TextView txtCoin =  findViewById(R.id.txtCoinBalance);
        txtCoin.setText(String.valueOf(userCoins));
    }
}