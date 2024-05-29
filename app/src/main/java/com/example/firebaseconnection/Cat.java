package com.example.firebaseconnection;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    public LinearLayout generate(Context context, View parent) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(
                (int) context.getResources().getDimension(R.dimen.spacing_small),
                (int) context.getResources().getDimension(R.dimen.spacing_small),
                (int) context.getResources().getDimension(R.dimen.spacing_small),
                (int) context.getResources().getDimension(R.dimen.spacing_small)
        );

        // Cat Name TextView
        TextView catNameTextView = new TextView(context);
        catNameTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        catNameTextView.setText(catName);
        catNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                context.getResources().getDimension(R.dimen.font_size_medium));
        linearLayout.addView(catNameTextView);

        // Cat Image ImageView
        ImageView catImageView = new ImageView(context);
        catImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Glide.with(parent).load(catImageURL).into(catImageView);
        linearLayout.addView(catImageView);

        // Price and Rarity LinearLayout
        LinearLayout priceAndRarityLayout = new LinearLayout(context);
        priceAndRarityLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        priceAndRarityLayout.setOrientation(LinearLayout.HORIZONTAL);
        priceAndRarityLayout.setGravity(Gravity.CENTER_VERTICAL);

        // Price TextView
        TextView priceTextView = new TextView(context);
        priceTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        priceTextView.setText(String.valueOf(catPrice));
        priceAndRarityLayout.addView(priceTextView);

        // Rarity TextView
        TextView rarityTextView = new TextView(context);
        rarityTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rarityTextView.setText(String.valueOf(catRarity));
        priceAndRarityLayout.addView(rarityTextView);

        linearLayout.addView(priceAndRarityLayout);

        // Purchase Button LinearLayout
        LinearLayout purchaseButtonLayout = new LinearLayout(context);
        purchaseButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        purchaseButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
        purchaseButtonLayout.setGravity(Gravity.CENTER);

        // Purchase Button TextView
        TextView purchaseButtonTextView = new TextView(context);
        purchaseButtonTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        purchaseButtonTextView.setText("Purchase");
        purchaseButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle purchase button click
            }
        });
        purchaseButtonLayout.addView(purchaseButtonTextView);
        linearLayout.addView(purchaseButtonLayout);
        return linearLayout;
    }
}
