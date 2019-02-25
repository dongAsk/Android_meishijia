package com.example.dong.meishi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dong.meishi.foodUtil.Food;
import com.example.dong.meishi.foodUtil.FoodAdapter;
import com.example.dong.meishi.httpUtil.HttpUtil;
import com.example.dong.meishi.httpUtil.ParseJSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private List<Food> foodList = new ArrayList<>();
    private FoodAdapter adapter;
    String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView naView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.list);
        }

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        address = "http://47.93.252.249/data.php?search=%E6%97%A9%E9%A4%90&&page=1";

        naView.setCheckedItem(R.id.item1);
        naView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item1:address = "http://47.93.252.249/data.php?search=%E6%97%A9%E9%A4%90&&page=1";
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(adapter);
                                    }
                                });
                            }
                        });
                        Toast.makeText(MainActivity.this,"item1",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item2:
                        address = "http://47.93.252.249/data.php";
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(adapter);
                                    }
                                });
                            }
                        });
                        Toast.makeText(MainActivity.this,"item2",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item3:
                        Toast.makeText(MainActivity.this,"item3",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item4:
                        Toast.makeText(MainActivity.this,"item4",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item5:
                        Toast.makeText(MainActivity.this,"item5",Toast.LENGTH_SHORT).show();
                        break;

                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
///////////////////////////////////////////////////////////////////////////////////////////

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
///////////////////////////////////////////////////////////////////////////////////////////////




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            //the menu of right
        }
        return true;
    }
}
