package com.example.firebaseconnection;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Cat {

    public String catImageURL;
    public String catName;
    public Long catPrice;
    public Long catRarity;

    public Cat(String catImageURL, String catName, Long catPrice, Long catRarity) {
        this.catImageURL = catImageURL;
        this.catName = catName;
        this.catPrice = catPrice;
        this.catRarity = catRarity;
    }

    public LinearLayout generate() {
        LinearLayout linearLayout;

//        TODO GENERATE CAT XML
//        FOR CAT IMAGE VIEW
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference().child(catImageURL);
//
//        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//            Glide.with(this)
//                    .load(uri)
//                    .into(image);

        return new LinearLayout();
    }
}
