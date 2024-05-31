package com.example.firebaseconnection;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TimerActivity extends AppCompatActivity {

    private TextView timerTextView, tvModeDisplay;
    LinearLayout btnPurrsueLater;
    private CountDownTimer countDownTimer;
    private Long timeLeftInMillis, userCoins, newCoins;
    private boolean isTimerRunning;
    FirebaseFirestore firebaseFirestore;
    String UID;

    private List<Map<String, Object>> userCatsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_6_timer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.timer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        timerTextView = findViewById(R.id.txtTimer);
        btnPurrsueLater = findViewById(R.id.btnPurrsueLater);
        tvModeDisplay = findViewById(R.id.tvModeDisplay);
        firebaseFirestore = FirebaseFirestore.getInstance();
        UID = "YkbW5nnkv1aLDXUvEYxZDMB1oj03";

        userCatsList = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            Log.d("Simon", extras.getString("taskModeIntent"));
            tvModeDisplay.setText("You are in " + extras.getString("taskModeIntent") + " mode.");
            timerTextView.setText(extras.getString("taskDurationIntent"));
        }

        btnPurrsueLater.setOnClickListener(v ->{
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_purrsue_later, null);

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            PopupWindow timerPopup = new PopupWindow(popupView, width, height, true);

            timerPopup.showAtLocation(findViewById(R.id.timer), Gravity.CENTER_VERTICAL, 0, 0);
            pauseTimer();

            LinearLayout btnPurrsueYes = popupView.findViewById(R.id.btnPurrsueYes);
            LinearLayout btnPurrsueNo = popupView.findViewById(R.id.btnPurrsueNo);

            btnPurrsueYes.setOnClickListener(x ->{
                timerPopup.dismiss();
                isTimerRunning = false;
                timerTextView.setText("00:00");
                onTimerFinish();
                btnPurrsueLater.setEnabled(false);
                fetchUserCats();
                //cat punishment
                //intent
            });

            btnPurrsueNo.setOnClickListener(x ->{
                timerPopup.dismiss();
                resumeTimer();
            });
        });



        int time = Integer.parseInt(timerTextView.getText().toString());

        newCoins = (long) ((time == 1) ? 1: time/2);


        Long milliseconds = timeLeftInMillis = time * 60 * 1000L;
        startTimer();
        startLockTaskMode();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                timerTextView.setText("00:00");
                getUserCoins();
                onTimerFinish();

            }
        }.start();
        isTimerRunning = true;
    }

    private void startLockTaskMode() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (am.getLockTaskModeState() == ActivityManager.LOCK_TASK_MODE_NONE) {
            startLockTask();

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_task_finished, null);

            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            PopupWindow taskFinishedPopup = new PopupWindow(popupView, width, height, true);

            LinearLayout btnDismissPawsome = popupView.findViewById(R.id.btnDismissPawsome);

            btnDismissPawsome.setOnClickListener(x ->{
                taskFinishedPopup.dismiss();
            });
        }
    }

    private void stopLockTaskMode() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (am.getLockTaskModeState() != ActivityManager.LOCK_TASK_MODE_NONE) {
            stopLockTask();
        }
    }

    private void onTimerFinish() {
        stopLockTaskMode();
    }

    private void pauseTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
    }

    private void resumeTimer() {
        if (!isTimerRunning) {
            startTimer();
        }
    }

    private void updateTimerText() {
        long minutes = (timeLeftInMillis / 1000) / 60;
        long seconds = (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
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
                            deleteRandomCat();
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

    private void deleteRandomCat() {
        DocumentReference userRef = firebaseFirestore.collection("users").document(UID);
        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> cats = (List<Map<String, Object>>) documentSnapshot.get("cats");
                        if (cats != null && !cats.isEmpty()) {
                            // Select a random cat to remove
                            Random rand = new Random();
                            int randomIndex = rand.nextInt(cats.size());
                            cats.remove(randomIndex);

                            // Update the document with the new list
                            userRef.update("cats", cats)
                                    .addOnSuccessListener(aVoid -> Log.d("TAG", "Random cat deleted successfully"))
                                    .addOnFailureListener(e -> Log.e("TAG", "Error deleting random cat", e));
                        } else {
                            Log.d("TAG", "No cats to delete");
                        }
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error fetching user cats", e);
                });
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
                        updateUserCoins();
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error fetching user document", e));
    }


    private void updateUserCoins() {
        DocumentReference userRef = firebaseFirestore.collection("users").document(UID);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userRef.update("coins", userCoins+newCoins)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("TAG", "Username updated successfully");
                                })
                                .addOnFailureListener(e -> Log.e("TAG", "Error updating username", e));
                    } else {
                        Log.d("TAG", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error fetching user document", e));
    }
}