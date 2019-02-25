package com.example.dong.meishi.foodUtil;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;

import com.example.dong.meishi.httpUtil.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FoodData {
    private static List<Food> foodList = new ArrayList<>();;
    private static FoodAdapter adapter;
    public static void setDate(String address){
        HttpUtil.sendHttpWithOkhttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                foodList = ParseJSON.parseJSONWithGson(responseData);
                adapter = new FoodAdapter(foodList);
            }
        });
    }
}
