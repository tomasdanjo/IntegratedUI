package com.example.firebaseconnection;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

public class Task {
    private String taskName, taskMode, taskCoins;
    private Date taskDate;

    public LinearLayout generateLayout(Context context){
        LinearLayout parentLayout = new LinearLayout(context);
        parentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        parentLayout.setPadding(
                (int) context.getResources().getDimension(R.dimen.spacing_medium),
                0,
                (int) context.getResources().getDimension(R.dimen.spacing_medium),
                0
        );

        // Background
        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setShape(GradientDrawable.RECTANGLE);
        backgroundDrawable.setCornerRadius(16);
        backgroundDrawable.setColor(Color.parseColor("#FF5722")); // Change color as needed
        parentLayout.setBackground(backgroundDrawable);

        // First LinearLayout inside parentLayout
        LinearLayout firstLinearLayout = new LinearLayout(context);
        firstLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        firstLinearLayout.setGravity(Gravity.CENTER);
        firstLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        parentLayout.addView(firstLinearLayout);

        // TextView inside firstLinearLayout
        TextView tvTaskName = new TextView(context);
        tvTaskName.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1
        ));
        tvTaskName.setText("Morning Jog");
        tvTaskName.setTextColor(Color.WHITE);
        tvTaskName.setTextSize(context.getResources().getDimension(R.dimen.font_size_large));
        firstLinearLayout.addView(tvTaskName);

        // ImageView inside firstLinearLayout
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        imageView.setImageResource(R.drawable.icon_arrow_down_big);
        firstLinearLayout.addView(imageView);

        // Second TextView
        TextView textView8 = new TextView(context);
        textView8.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        textView8.setText("May 13 at 6 AM");
        textView8.setTextColor(Color.WHITE);
        textView8.setTextSize(context.getResources().getDimension(R.dimen.font_size_regular));
        parentLayout.addView(textView8);

        // Remaining LinearLayouts and views
        // Add your code here for the remaining parts of the layout...

        return parentLayout;
    }

}
