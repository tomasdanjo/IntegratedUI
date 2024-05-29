package com.example.firebaseconnection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class CatShopActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    private List<Map<String, Object>> catShopList, userCatsList;
    private final String documentId = "xv6JGkDrmpJMylgUIeEz";
    private String UID;
    private TextView tvCatName;
    private Button btnPurchaseCat, btnRedirectToProfile;
    private ImageView ivCatImage;
    private FirebaseAuth mAuth;
    private Long userCoins, newUserCoins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cat_shop);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getUid();
        catShopList  = new ArrayList<>();
//        ivCatImage = findViewById(R.id.ivCatImage);
//
//        tvCatName = findViewById(R.id.tvCatName);
//        btnPurchaseCat = findViewById(R.id.btnPurchaseCat);
//        btnRedirectToProfile = findViewById(R.id.btnRedirectToProfile);
//
//        btnPurchaseCat.setOnClickListener(v->{
//            getUserCoins();
//        });
//
//        btnRedirectToProfile.setOnClickListener(v->{
//            Intent intent = new Intent(CatShopActivity.this, ProfileActivity.class);
//            startActivity(intent);
//        });

        fetchCats(documentId);
        fetchUserCats();
    }

    private void fetchCats(String documentId) {
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
                                catShopList.add(catMap);

                                Log.d("TAG", "Cat Name: " + catName);
                                Log.d("TAG", "Cat Image URL: " + catImageUrl);
                                Log.d("TAG", "Cat Price: " + catPrice);
                                Log.d("TAG", "Cat Rarity: " + catRarity);
                            }
                            updateUIWithCats();
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

    private void fetchUserCats(){
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
//                            updateUIDisableButton2();
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

            String catName = (String) catShopList.get(9).get("catName");
            String catImageUrl = (String) catShopList.get(9).get("catImageURL");

            tvCatName.setText(catName);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child(catImageUrl);

            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                //if fails, check gradle / rebuild proj
                Glide.with(this)
                        .load(uri)
                        .into(ivCatImage);
            }).addOnFailureListener(exception -> {
                Log.e("TAG", "Error fetching image URL", exception);
                //ivCatImage.setImageResource(R.drawable.placeholder_image); //set a placeholder image
            });

        } else {
            Log.i("TAG", "EMPTY LIST");
            tvCatName.setText("No cats available");
        }
        Log.i("TAG", "Total cats: " + catShopList.size());
    }

    private void getUserCoins() {
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
                        checkCoinBalance(userCoins, (Long) catShopList.get(0).get("catPrice"));
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error fetching user document", e));
    }

    private void checkCoinBalance(Long userCoins, Long catFee){
        assert userCoins >= catFee;
        newUserCoins = userCoins - catFee;
        updateUserCoins(newUserCoins);
    }

    private void updateUserCoins(Long newUserCoins) {
        DocumentReference userRef = firebaseFirestore.collection("users").document(UID);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userRef.update("coins", newUserCoins)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("TAG", "Username updated successfully");

                                    addCatToUser(catShopList.get(0).get("catName").toString(), catShopList.get(0).get("catImageURL").toString());
                                })
                                .addOnFailureListener(e -> Log.e("TAG", "Error updating username", e));
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error fetching user document", e));
    }

    private void addCatToUser(String catName, String catImage) {
        DocumentReference userRef = firebaseFirestore.collection("users").document(UID);

        Map<String, String> newCat = new HashMap<>();
        newCat.put("catName", catName);
        newCat.put("catImage", catImage);

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

    private void fetchUserInfo() {
        DocumentReference userRef = firebaseFirestore.collection("users").document(UID);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Log.d("TAG", "User document fetched successfully");
//                        updateUIDisableButton();
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error fetching user document", e);
                });
    }



//    private void updateUIDisableButton() {
//        //disable button for users' purchased cats
//
//        //an array of buttons guro nya each index sa button kay same ug
//        //index sa catShopList. if naa ang usersCatsList sacatShopList
//        //then idisable ang button
//
//        Button btnPurchaseCat = findViewById(R.id.btnPurchaseCat);
//        btnPurchaseCat.setText("purchased");
//        btnPurchaseCat.setEnabled(false);
//    }
//
//    private void updateUIDisableButton2() {
//        Button btnPurchaseCat = findViewById(R.id.btnPurchaseCat);
//        btnPurchaseCat.setText("purchased");
//        btnPurchaseCat.setEnabled(false);
//    }




}