package com.example.firebaseconnection;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.ImageViewCompat;

import java.util.Date;

public class Task extends AppCompatActivity {
    private String taskName;

    private boolean taskMode;
    private int taskDuration;
    private int taskCoins;
    private Date taskDate;

    public LinearLayout generateLayout(Context context) {
        // Define dimensions and other resources
        int spacingSmall = context.getResources().getDimensionPixelSize(R.dimen.spacing_small);
        int spacingMedium = context.getResources().getDimensionPixelSize(R.dimen.spacing_medium);
        int spacingVerySmall = context.getResources().getDimensionPixelSize(R.dimen.spacing_very_small);
        int fontSizeLarge = context.getResources().getDimensionPixelSize(R.dimen.font_size_large);
        int fontSizeRegular = context.getResources().getDimensionPixelSize(R.dimen.font_size_regular);
        int borderWidth = context.getResources().getDimensionPixelSize(R.dimen.border_width);

        // Create the main LinearLayout
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(spacingMedium, spacingMedium, spacingMedium, spacingMedium);
        mainLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.task_bg));

        // First inner LinearLayout
        LinearLayout topInnerLayout = new LinearLayout(context);
        topInnerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        topInnerLayout.setOrientation(LinearLayout.HORIZONTAL);
        topInnerLayout.setGravity(Gravity.CENTER);

        TextView titleTextView = new TextView(context);
        titleTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        titleTextView.setText(taskName);
        titleTextView.setTextColor(Color.WHITE);
        titleTextView.setTextSize(fontSizeLarge);
        titleTextView.setTypeface(ResourcesCompat.getFont(context, R.font.nunito_extrabold));

        ImageView arrowImageView = new ImageView(context);
        arrowImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ImageViewCompat.setImageTintList(arrowImageView, ContextCompat.getColorStateList(context, R.drawable.icon_arrow_down_big));

        topInnerLayout.addView(titleTextView);
        topInnerLayout.addView(arrowImageView);

        mainLayout.addView(topInnerLayout);

        // Second TextView
        TextView dateTextView = new TextView(context);
        dateTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        dateTextView.setText((CharSequence) taskDate);
        dateTextView.setTextColor(Color.WHITE);
        dateTextView.setTextSize(fontSizeRegular);

        mainLayout.addView(dateTextView);

        // Middle LinearLayout
        LinearLayout middleLayout = new LinearLayout(context);
        middleLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        middleLayout.setOrientation(LinearLayout.HORIZONTAL);
        middleLayout.setPadding(spacingSmall, spacingVerySmall, spacingSmall, spacingVerySmall);
        middleLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.button_white));

        // First inner middle LinearLayout
        LinearLayout durationLayout = new LinearLayout(context);
        durationLayout.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        durationLayout.setOrientation(LinearLayout.HORIZONTAL);
        durationLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        durationLayout.setPadding(0, 0, 0, borderWidth);

        ImageView durationImageView = new ImageView(context);
        durationImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        durationImageView.setPadding(0, 0, spacingVerySmall, 0);
        ImageViewCompat.setImageTintList(durationImageView, ContextCompat.getColorStateList(context, R.drawable.icon_duration));

        TextView durationTextView = new TextView(context);
        durationTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        durationTextView.setText(taskDuration);
        durationTextView.setTypeface(ResourcesCompat.getFont(context, R.font.nunito_bold));

        durationLayout.addView(durationImageView);
        durationLayout.addView(durationTextView);

        // Second inner middle LinearLayout
        LinearLayout modeLayout = new LinearLayout(context);
        modeLayout.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        modeLayout.setOrientation(LinearLayout.HORIZONTAL);
        modeLayout.setGravity(Gravity.CENTER);
        modeLayout.setPadding(0, 0, 0, borderWidth);

        ImageView modeImageView = new ImageView(context);
        modeImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        modeImageView.setPadding(0, 0, spacingVerySmall, 0);
        ImageViewCompat.setImageTintList(modeImageView, ContextCompat.getColorStateList(context, R.drawable.icon_mode));

        TextView modeTextView = new TextView(context);
        modeTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        modeTextView.setText((taskMode) ? "Focus" : "Chill");
        modeTextView.setTextColor(Color.RED);
        modeTextView.setTypeface(ResourcesCompat.getFont(context, R.font.nunito_bold));

        modeLayout.addView(modeImageView);
        modeLayout.addView(modeTextView);

        // Third inner middle LinearLayout
        LinearLayout coinLayout = new LinearLayout(context);
        coinLayout.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        coinLayout.setOrientation(LinearLayout.HORIZONTAL);
        coinLayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        coinLayout.setPadding(0, 0, 0, borderWidth);

        ImageView coinImageView = new ImageView(context);
        coinImageView.setLayoutParams(new LinearLayout.LayoutParams(
                20,
                20
        ));
        coinImageView.setPadding(0, 0, spacingVerySmall, 0);
        ImageViewCompat.setImageTintList(coinImageView, ContextCompat.getColorStateList(context, R.drawable.icon_coin));

        TextView coinTextView = new TextView(context);
        coinTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        coinTextView.setText(taskCoins);
        coinTextView.setTypeface(ResourcesCompat.getFont(context, R.font.nunito_bold));

        coinLayout.addView(coinImageView);
        coinLayout.addView(coinTextView);

        middleLayout.addView(durationLayout);
        middleLayout.addView(modeLayout);
        middleLayout.addView(coinLayout);

        mainLayout.addView(middleLayout);

        // Bottom LinearLayout
        LinearLayout bottomLayout = new LinearLayout(context);
        bottomLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        bottomLayout.setOrientation(LinearLayout.HORIZONTAL);

        // First button
        LinearLayout deleteButtonLayout = new LinearLayout(context);
        deleteButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        deleteButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
        deleteButtonLayout.setPadding(spacingSmall, spacingVerySmall, spacingSmall, spacingVerySmall);
        deleteButtonLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.button_red));

        ImageView deleteButtonImageView = new ImageView(context);
        deleteButtonImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        deleteButtonImageView.setPadding(0, 0, borderWidth, borderWidth);
        ImageViewCompat.setImageTintList(deleteButtonImageView, ContextCompat.getColorStateList(context, R.drawable.icon_delete));

        deleteButtonLayout.addView(deleteButtonImageView);

        // Second button
        LinearLayout editButtonLayout = new LinearLayout(context);
        editButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        editButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
        editButtonLayout.setPadding(spacingSmall, spacingVerySmall, spacingSmall, spacingVerySmall);
        editButtonLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.button_yellow));

        ImageView editButtonImageView = new ImageView(context);
        editButtonImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        editButtonImageView.setPadding(0, 0, borderWidth, borderWidth);
        ImageViewCompat.setImageTintList(editButtonImageView, ContextCompat.getColorStateList(context, R.drawable.icon_edit));

        editButtonLayout.addView(editButtonImageView);

        // Third button
        LinearLayout startButtonLayout = new LinearLayout(context);
        startButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        ));
        startButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
        startButtonLayout.setGravity(Gravity.CENTER);
        startButtonLayout.setPadding(spacingSmall, spacingVerySmall, spacingSmall, spacingVerySmall);
        startButtonLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.button_green));

        TextView startButtonTextView = new TextView(context);
        startButtonTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        startButtonTextView.setText("START TASK");
        startButtonTextView.setTextColor(Color.WHITE);
        startButtonTextView.setTypeface(ResourcesCompat.getFont(context, R.font.nunito_black));

        ImageView startButtonImageView = new ImageView(context);
        startButtonImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ImageViewCompat.setImageTintList(startButtonImageView, ContextCompat.getColorStateList(context, R.drawable.icon_arrow_right_thick));

        startButtonLayout.addView(startButtonTextView);
        startButtonLayout.addView(startButtonImageView);

        bottomLayout.addView(deleteButtonLayout);
        bottomLayout.addView(editButtonLayout);
        bottomLayout.addView(startButtonLayout);

        mainLayout.addView(bottomLayout);

        return mainLayout;
    }

}
