package com.example.firebaseconnection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    TextView txtHomeSetting;
    ImageView toggleBGM;
    ImageView toggleSFX;

    LinearLayout btnMenu;
    LinearLayout btnUser;

    LinearLayout btnLogout;

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

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        homeSetting = "Rural";
        bgm = true;
        sfx = true;

        btnMenu = findViewById(R.id.btnMenu);
        btnUser = findViewById(R.id.btnUser);
        txtHomeSetting = findViewById(R.id.homeSetting);
        toggleBGM = findViewById(R.id.toggleBGM);
        toggleSFX = findViewById(R.id.toggleSFX);
        btnLogout = findViewById(R.id.btnLogout);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

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
                    toggleBGM.setImageResource(R.drawable.icon_mute);
                } else {
                    toggleBGM.setImageResource(R.drawable.icon_sound);
                }
            }
        });

        toggleSFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfx = !sfx;
                if (!sfx) {
                    toggleSFX.setImageResource(R.drawable.icon_mute);
                } else {
                    toggleSFX.setImageResource(R.drawable.icon_sound);
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(SettingsActivity.this, SignInActivity.class);
                startActivity(i);
            }
        });
    }
}