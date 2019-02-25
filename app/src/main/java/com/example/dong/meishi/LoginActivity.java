package com.example.dong.meishi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dong.meishi.bean.User;
import com.example.dong.meishi.httpUtil.HttpUtil;
import com.example.dong.meishi.httpUtil.ParseJSON;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    //声明控件
    private EditText edit_account;
    private EditText edit_password;
    private CheckBox chk_remember_pass;
    private ImageView bingPicImg;      //背景图片
    //声明存储数据的相关类
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.layout_login);

        //实例化控件
        bingPicImg = findViewById(R.id.bing_pic_img);
        edit_account = findViewById(R.id.edit_account);
        edit_password = findViewById(R.id.edit_password);
        chk_remember_pass = findViewById(R.id.chk_rememberPass);
        Button btn_login = findViewById(R.id.btn_login);
        Button btn_visitor = findViewById(R.id.btn_visitor);
        Button btn_register = findViewById(R.id.btn_register);

        //对按钮注册点击事件
        btn_login.setOnClickListener(this);
        btn_visitor.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        //加载背景图片
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = pref.getString("bing_pic",null);
        if (bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }

        //记住密码功能
        boolean isRemember = pref.getBoolean("remember_password",false);
        if (isRemember){
            String username = pref.getString("account","");
            String password = pref.getString("password","");
            edit_account.setText(username);
            edit_password.setText(password);
            chk_remember_pass.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //当用户登录时,连接网络查询用户名和密码是否正确
            case R.id.btn_login:
                final String username = edit_account.getText().toString();
                final String password = edit_password.getText().toString();
                if (!username.equals("") && !password.equals("")){
                    userLoginAction(username,password);
                }else if (username.equals("")){
                    Toast.makeText(LoginActivity.this,"请输入账号",Toast.LENGTH_SHORT).show();
                }else if (password.equals("")){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                }
                break;
            //当用户使用游客预览时，进行页面跳转
            case R.id.btn_visitor:
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("isLogin", false);
                intent.putExtra("username", "未登录");
                startActivity(intent);
                break;
            //用户注册
            case R.id.btn_register:


        }
    }

    private void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendHttpWithOkhttp(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(LoginActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    private void userLoginAction(final String username,final String password){
        Gson gson = new Gson();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        String jsonString = gson.toJson(user);
        String address = "http://192.168.43.250:8080/meishijia/UserServlet?action=login";
        HttpUtil.sendHttpWithOkhttp_POST(address,jsonString, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                final boolean result = ParseJSON.parseJSONWithJSONObject(responseData,"login");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result){
                            editor = pref.edit();
                            if (chk_remember_pass.isChecked()){
                                editor.putBoolean("remember_password",true);
                                editor.putString("account",username);
                                editor.putString("password",password);
                            }else {
                                editor.clear();
                            }
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("isLogin",true);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
