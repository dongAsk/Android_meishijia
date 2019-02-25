package com.example.dong.meishi.foodUtil;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dong.meishi.R;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Context mContext;
    private List<Food> mFoodList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView foodImage;
        TextView foodName;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView;
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
        }
    }

    public FoodAdapter(List<Food> foodList) {
        mFoodList = foodList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.food_item_1,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Food food = mFoodList.get(position);
        holder.foodName.setText(food.getName());
        Glide.with(mContext).load(food.getUrl()).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }
}
