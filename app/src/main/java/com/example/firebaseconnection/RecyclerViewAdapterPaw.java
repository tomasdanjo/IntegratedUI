package com.example.firebaseconnection;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RecyclerViewAdapterPaw extends RecyclerView.Adapter<RecyclerViewAdapterPaw.MyViewHolder> {
    Context context;
    ArrayList<Paw> paws;

    public RecyclerViewAdapterPaw(Context context, ArrayList<Paw> paws) {
        this.context = context;
        this.paws = paws;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterPaw.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_paw, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterPaw.MyViewHolder holder, int position) {
        holder.name.setText(paws.get(position).name);
        holder.image.setImageResource(context.getResources().getIdentifier(paws.get(position).imageURL.replace(".svg", "").toLowerCase(), "drawable", context.getPackageName()));
        holder.price.setText(String.valueOf(paws.get(position).price));
        holder.rarity.setText(String.valueOf(paws.get(position).rarity));
    }

    @Override
    public int getItemCount() {
        return paws.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        TextView price;
        TextView rarity;
        LinearLayout btnPurchase;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pawName);
            image = itemView.findViewById(R.id.pawImage);
            price = itemView.findViewById(R.id.pawPrice);
            rarity = itemView.findViewById(R.id.pawRarity);

            btnPurchase = itemView.findViewById(R.id.btnPurchase);

            btnPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = LayoutInflater.from(btnPurchase.getContext());
                    View popupView = inflater.inflate(R.layout.popup_purchase_paw, null);
                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    PopupWindow purchasePawPopUp = new PopupWindow(popupView, width, height, true);
                    purchasePawPopUp.showAtLocation(btnPurchase.getRootView(), Gravity.CENTER_VERTICAL, 0, 0);

                    TextView pawName = popupView.findViewById(R.id.pawName);
                    ImageView pawImage = popupView.findViewById(R.id.pawImage);
                    TextView pawPrice = popupView.findViewById(R.id.pawPrice);
                    TextView pawRarity = popupView.findViewById(R.id.pawRarity);

                    pawName.setText(name.getText().toString());
                    pawImage.setImageDrawable(image.getDrawable());
                    pawPrice.setText(price.getText().toString());
                    pawRarity.setText(rarity.getText().toString());

                    LinearLayout purchasePawButton = popupView.findViewById(R.id.purchasePawButton);

                    if (contains(PawShopActivity.userCatsList, name.getText().toString())) {
                        ((TextView) purchasePawButton.getChildAt(0)).setText("Purchased");
                        purchasePawButton.setBackgroundResource(R.drawable.button_blue);
                        purchasePawButton.setEnabled(false);
                    } else {
                        purchasePawButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PawShopActivity.getUserCoins(name.getText().toString());
                                purchasePawPopUp.dismiss();
                            }
                        });
                    }
                }
            });
        }

        public boolean contains(List<Map<String, Object>> list, String name) {
            for (Map stringObject : list) {
                if (Objects.equals(stringObject.get("catName"), name)) {
                    return true;
                }
            }
            return false;
        }
    }
}