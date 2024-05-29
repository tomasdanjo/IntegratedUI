package com.example.firebaseconnection;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
    private static List<Map<String, Object>> catShopList, userCatsList;
    private final String documentId = "xv6JGkDrmpJMylgUIeEz";
    private String UID;
    private TextView tvCatName;
    private Button btnPurchaseCat, btnRedirectToProfile;
    private ImageView ivCatImage;
    private FirebaseAuth mAuth;
    private Long userCoins, newUserCoins;

    public static GridLayout catsGrid;
    public static ConstraintLayout catShop;

    public ArrayList<Cat> cats;
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

        cats = new ArrayList<>();
        catShop = findViewById(R.id.catShop);
        catsGrid = findViewById(R.id.catsGrid);
        firebaseFirestore = FirebaseFirestore.getInstance();
        catShopList  = new ArrayList<>();

        fetchCats(documentId);

//        generateCats();

        mAuth = FirebaseAuth.getInstance();
//        UID = mAuth.getCurrentUser().getUid();
        UID = "YkbW5nnkv1aLDXUvEYxZDMB1oj03";
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
                            generateCats();
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

    private void updateUIWithCats() {
        if (!catShopList.isEmpty()) {

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


    public void getCats() {
        cats.clear();
        for (int i = 0; i < catShopList.size(); i++) {
            Log.d("SIMON", "Sasdd");
            String catImageURL = (String) catShopList.get(i).get("catImageURL");
            String catName = (String) catShopList.get(i).get("catImageURL");
            Long catPrice = (Long) catShopList.get(i).get("catPrice");
            Long catRarity = (Long) catShopList.get(i).get("catRarity");
            cats.add(new Cat(catImageURL, catName, catPrice, catRarity));
        }
    }

    public void generateCats() {
        getCats();
        catsGrid.removeAllViews();
        for (Cat cat : cats) {
            catsGrid.addView(cat.generate(catsGrid.getContext(), catShop));
        }
    }
}