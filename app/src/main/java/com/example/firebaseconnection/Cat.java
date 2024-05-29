package com.example.firebaseconnection;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

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
        LinearLayout mainLayout = new LinearLayout(context);
        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mainLayoutParams.weight = 1;
        mainLayout.setLayoutParams(mainLayoutParams);
        mainLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.button_white));
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(
                context.getResources().getDimensionPixelSize(R.dimen.spacing_small),
                context.getResources().getDimensionPixelSize(R.dimen.spacing_small),
                context.getResources().getDimensionPixelSize(R.dimen.spacing_small),
                context.getResources().getDimensionPixelSize(R.dimen.spacing_small));

        // Create and add the TextView for cat name
        TextView pawName = new TextView(context);
        pawName.setId(View.generateViewId());
        pawName.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        pawName.setText(catName);
        pawName.setTextSize(pxToSp(context, context.getResources().getDimension(R.dimen.font_size_medium)));
        pawName.setTypeface(context.getResources().getFont(R.font.nunito_black));
        mainLayout.addView(pawName);

        // Create and add the ImageView for cat image
        ImageView pawImage = new ImageView(context);
        pawImage.setId(View.generateViewId());
        pawImage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        pawImage.setBackground(ContextCompat.getDrawable(context, R.drawable.button_white));
        String catImageResource = catImageURL.replace(".svg", "");
        catImageResource = catImageResource.toLowerCase();
        int resId = context.getResources().getIdentifier(catImageResource, "drawable", context.getPackageName());
        if (resId != 0) {
            pawImage.setImageResource(resId);
        }
        mainLayout.addView(pawImage);

        // Create the LinearLayout for price and rarity
        LinearLayout priceRarityLayout = new LinearLayout(context);
        priceRarityLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        priceRarityLayout.setOrientation(LinearLayout.HORIZONTAL);
        priceRarityLayout.setGravity(Gravity.CENTER);
        priceRarityLayout.setPadding(0, context.getResources().getDimensionPixelSize(R.dimen.spacing_very_small), 0, 0);

        // Create and add the price layout
        LinearLayout priceLayout = new LinearLayout(context);
        priceLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        priceLayout.setOrientation(LinearLayout.HORIZONTAL);
        priceLayout.setGravity(Gravity.CENTER);

        ImageView priceIcon = new ImageView(context);
        priceIcon.setId(View.generateViewId());
        priceIcon.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        priceIcon.setImageResource(R.drawable.icon_coin);
        priceLayout.addView(priceIcon);

        TextView pawPrice = new TextView(context);
        pawPrice.setId(View.generateViewId());
        pawPrice.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        pawPrice.setText(String.valueOf(catPrice));
        pawPrice.setTypeface(context.getResources().getFont(R.font.nunito_black));
        pawPrice.setTextSize(pxToSp(context, context.getResources().getDimension(R.dimen.font_size_regular)));
        pawPrice.setPadding(
                context.getResources().getDimensionPixelSize(R.dimen.spacing_smallest),
                0, 0, 0);
        priceLayout.addView(pawPrice);

        priceRarityLayout.addView(priceLayout);

        // Add a spacer view
        View spacer = new View(context);
        spacer.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                0, 1));
        priceRarityLayout.addView(spacer);

        // Create and add the rarity layout
        LinearLayout rarityLayout = new LinearLayout(context);
        rarityLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rarityLayout.setOrientation(LinearLayout.VERTICAL);
        rarityLayout.setGravity(Gravity.CENTER);

        TextView rarityLabel = new TextView(context);
        rarityLabel.setId(View.generateViewId());
        rarityLabel.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rarityLabel.setText("RARITY");
        rarityLabel.setTypeface(context.getResources().getFont(R.font.nunito_black));
        rarityLabel.setTextSize(10);
        rarityLabel.setTextColor(ContextCompat.getColor(context, R.color.gray));
        rarityLayout.addView(rarityLabel);

        TextView pawRarity = new TextView(context);
        pawRarity.setId(View.generateViewId());
        pawRarity.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        pawRarity.setText(String.valueOf(catRarity));
        pawRarity.setTypeface(context.getResources().getFont(R.font.nunito_black));
        pawRarity.setTextColor(ContextCompat.getColor(context, R.color.orange_dark));
        rarityLayout.addView(pawRarity);

        priceRarityLayout.addView(rarityLayout);
        mainLayout.addView(priceRarityLayout);

        // Create and add the purchase button
        LinearLayout purchaseButtonLayout = new LinearLayout(context);
        purchaseButtonLayout.setId(View.generateViewId());
        purchaseButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        purchaseButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
        purchaseButtonLayout.setGravity(Gravity.CENTER);
        purchaseButtonLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.button_green));
        purchaseButtonLayout.setPadding(
                context.getResources().getDimensionPixelSize(R.dimen.spacing_small),
                context.getResources().getDimensionPixelSize(R.dimen.spacing_small),
                context.getResources().getDimensionPixelSize(R.dimen.spacing_small),
                context.getResources().getDimensionPixelSize(R.dimen.spacing_small));
        purchaseButtonLayout.setClickable(true);
        purchaseButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View popupView = inflater.inflate(R.layout.popup_purchase_paw, null);
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                PopupWindow purchasePawPopUp = new PopupWindow(popupView, width, height, true);
                purchasePawPopUp.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);

                TextView pawName = popupView.findViewById(R.id.pawName);
                ImageView pawImage = popupView.findViewById(R.id.pawImage);
                TextView pawPrice = popupView.findViewById(R.id.pawPrice);
                TextView pawRarity = popupView.findViewById(R.id.pawRarity);

                pawName.setText(catName);
                pawImage.setImageResource(resId);
                pawPrice.setText(String.valueOf(catPrice));
                pawRarity.setText(String.valueOf(catRarity));

                LinearLayout purchasePawButton = popupView.findViewById(R.id.purchasePawButton);
                purchasePawButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        purchasePawPopUp.dismiss();
                    }
                });
            }
        });

        TextView purchaseText = new TextView(context);
        purchaseText.setId(View.generateViewId());
        purchaseText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        purchaseText.setText("Purchase");
        purchaseText.setTypeface(context.getResources().getFont(R.font.nunito_extrabold));
        purchaseText.setTextColor(ContextCompat.getColor(context, R.color.white));
        purchaseText.setPadding(0, 0, context.getResources().getDimensionPixelSize(R.dimen.border_width), context.getResources().getDimensionPixelSize(R.dimen.border_width));
        purchaseText.setPadding(0, 0, context.getResources().getDimensionPixelSize(R.dimen.border_width), context.getResources().getDimensionPixelSize(R.dimen.border_width));
        purchaseButtonLayout.addView(purchaseText);

        mainLayout.addView(purchaseButtonLayout);

        return mainLayout;
    }

    private float pxToSp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }
}
