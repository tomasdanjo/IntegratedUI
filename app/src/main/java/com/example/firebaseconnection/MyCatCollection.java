package com.example.firebaseconnection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.gridlayout.widget.GridLayout;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyCatCollection extends AppCompatActivity {

    public static GridLayout catsGrid;
    public static ConstraintLayout main;
    private static String UID;
    public ArrayList<Cat> cats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_cat_collection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyCatCollection.this, Menu.class);
                startActivity(i);
            }
        });

        CatShopActivity.fetchUserCats();

        main = findViewById(R.id.main);
        catsGrid = findViewById(R.id.catsGrid);
        cats = new ArrayList<>();

        List<Map<String, Object>> ownedCats = CatShopActivity.userCatsList;

        for (Map map : ownedCats) {
            String url = (String) map.get("catImageURL");
            Log.d("Simon", url);
            cats.add(new Cat(url, (String) map.get("catName"), null, null));
        }

        for (Cat cat : cats) {
            catsGrid.addView(cat.generateWithoutButtons(catsGrid.getContext(), main));
        }
    }
}