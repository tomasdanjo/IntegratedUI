package com.example.firebaseconnection;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    TextView txtHomeSetting;
    ImageView toggleBGM;
    ImageView toggleSFX;

    String homeSetting;
    boolean bgm;
    boolean sfx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        homeSetting = "Rural";
        bgm = true;
        sfx = true;

        txtHomeSetting = findViewById(R.id.homeSetting);
        toggleBGM = findViewById(R.id.toggleBGM);
        toggleSFX = findViewById(R.id.toggleSFX);

        txtHomeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeSetting.equals("Rural")) {
                    homeSetting = "Urban";
                } else {
                    homeSetting = "Rural";
                }
                txtHomeSetting.setText(homeSetting);
            }
        });

        toggleBGM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgm = !bgm;
                if (!bgm) {
                    toggleBGM.setImageResource(R);
                } else {

                }
            }
        });

        toggleSFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfx = !sfx;

            }
        });
    }
}