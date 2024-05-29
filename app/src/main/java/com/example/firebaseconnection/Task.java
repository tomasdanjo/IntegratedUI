package com.example.firebaseconnection;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.Date;


public class Task extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private String UID;

    private String taskName;

    private boolean taskMode;

    private String taskDuration;
    private int taskCoins;
    private Timestamp taskDate;


    public Task(String taskName, boolean taskMode, String taskDuration, int taskCoins, Timestamp taskDate) {
        this.taskName = taskName;
        this.taskMode = taskMode;
        this.taskDuration = taskDuration;
        this.taskCoins = taskCoins;
        this.taskDate = taskDate;
    }

    @SuppressLint("ResourceType")
    public LinearLayout generate(Context context) {
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
        topInnerLayout.setClickable(true); // Make clickable
        topInnerLayout.setOnClickListener(v -> {
            // Handle click event

        });

        TextView titleTextView = new TextView(context);
        titleTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        titleTextView.setText(taskName);
        titleTextView.setTextColor(Color.WHITE);
        titleTextView.setTextSize(34);
        titleTextView.setTypeface(ResourcesCompat.getFont(context, R.font.nunito_extrabold));

        ImageView arrowImageView = new ImageView(context);
        arrowImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        arrowImageView.setImageResource(R.drawable.icon_arrow_down_big);

        topInnerLayout.addView(titleTextView);
        topInnerLayout.addView(arrowImageView);

        mainLayout.addView(topInnerLayout);

        // Second TextView
        TextView dateTextView = new TextView(context);
        dateTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        dateTextView.setText((taskDate.toString()));
        dateTextView.setTextColor(Color.WHITE);
        dateTextView.setTextSize(12);

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
        middleLayout.setClickable(true); // Make clickable
        middleLayout.setOnClickListener(v -> {
            // Handle click event
        });

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
        durationLayout.setClickable(true); // Make clickable
        durationLayout.setOnClickListener(v -> {
            // Handle click event
        });

        ImageView durationImageView = new ImageView(context);
        durationImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        durationImageView.setPadding(0, 0, spacingVerySmall, 0);
        durationImageView.setImageResource(R.drawable.icon_duration);

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
        modeLayout.setClickable(true); // Make clickable
        modeLayout.setOnClickListener(v -> {
            // Handle click event
        });

        ImageView modeImageView = new ImageView(context);
        modeImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        modeImageView.setPadding(0, 0, spacingVerySmall, 0);
        modeImageView.setImageResource(R.drawable.icon_mode);

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
        coinLayout.setClickable(true); // Make clickable
        coinLayout.setOnClickListener(v -> {
            // Handle click event
        });

        ImageView coinImageView = new ImageView(context);
        coinImageView.setLayoutParams(new LinearLayout.LayoutParams(
                20,
                20
        ));
        coinImageView.setPadding(0, 0, spacingVerySmall, 0);
        coinImageView.setImageResource(R.drawable.icon_coin);

        TextView coinTextView = new TextView(context);
        coinTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        coinTextView.setText(String.valueOf(taskCoins));
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
        bottomLayout.setClickable(true); // Make clickable
        bottomLayout.setOnClickListener(v -> {
            // Handle click event
        });

        // First button
        LinearLayout deleteButtonLayout = new LinearLayout(context);
        deleteButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        deleteButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
        deleteButtonLayout.setPadding(spacingSmall, spacingVerySmall, spacingSmall, spacingVerySmall);
        deleteButtonLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.button_red));
        deleteButtonLayout.setClickable(true); // Make clickable
        deleteButtonLayout.setOnClickListener(v -> {
            // TODO delete button
            firebaseFirestore = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            UID = "YkbW5nnkv1aLDXUvEYxZDMB1oj03";


            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_delete_task, null);

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            PopupWindow deleteTaskPopUp = new PopupWindow(popupView, width, height, true);

            deleteTaskPopUp.showAtLocation(findViewById(R.id.tasks), Gravity.CENTER_VERTICAL, 0, 0);

            LinearLayout btnYes = findViewById(R.id.btnDeleteYes);
            LinearLayout btnNo = findViewById(R.id.btnDeleteNo);

            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTaskPopUp.dismiss();
                }
            });
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String taskNameToDelete = "Updated Task Name";
                    TaskListActivity.deleteTaskFromUser(UID, taskNameToDelete);
                }



        });


        });

        ImageView deleteButtonImageView = new ImageView(context);
        deleteButtonImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        deleteButtonImageView.setPadding(0, 0, borderWidth, borderWidth);
        deleteButtonImageView.setImageResource(R.drawable.icon_delete);

        deleteButtonLayout.addView(deleteButtonImageView);

        // Second button
        LinearLayout editButtonLayout = new LinearLayout(context);
        editButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        editButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
        editButtonLayout.setPadding(spacingSmall, spacingVerySmall, spacingSmall, spacingVerySmall);
        editButtonLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.button_blue));
        editButtonLayout.setClickable(true); // Make clickable
        editButtonLayout.setOnClickListener(v -> {
            // TODO edit button
        });

        ImageView editButtonImageView = new ImageView(context);
        editButtonImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        editButtonImageView.setPadding(0, 0, borderWidth, borderWidth);
        editButtonImageView.setImageResource(R.drawable.icon_edit);

        editButtonLayout.addView(editButtonImageView);

        // Third button
        LinearLayout startButtonLayout = new LinearLayout(context);
        startButtonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        startButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
        startButtonLayout.setPadding(spacingSmall, spacingVerySmall, spacingSmall, spacingVerySmall);
        startButtonLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.button_green));
        startButtonLayout.setClickable(true); // Make clickable
        TextView startTextView = new TextView(startButtonLayout.getContext());
        startTextView.setText(("START TASK"));
        startButtonLayout.addView(startTextView);
        startButtonLayout.setOnClickListener(v -> {
            // TODO start button
        });

        bottomLayout.addView(deleteButtonLayout);
        bottomLayout.addView(editButtonLayout);
        bottomLayout.addView(startButtonLayout);

        mainLayout.addView(bottomLayout);

        return mainLayout;
    }
}
