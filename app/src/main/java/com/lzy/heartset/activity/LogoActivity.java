package com.lzy.heartset.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.heartset.R;
import com.lzy.heartset.bean.LoginInfo;
import com.lzy.heartset.bean.ResponseBean;

import java.util.HashMap;
import java.util.Map;


public class LogoActivity extends Activity {

    private String mUsername;
    private String mPassword;
    private boolean mIsLoginSuccess = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            login();


                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    void login() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlString = "http://101.200.89.170:9000/capp/login/normal";

        //将JSONObject作为，将上一步得到的JSONObject对象作为参数传入
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("请求成功", "response -> " + response);

                        Gson gson = new Gson();
//                        JsonObject jsonObject=new JsonObject();

                        ResponseBean responseBean = gson.fromJson(response, ResponseBean.class);

                        // TODO: 2017/3/20 加一个GlobalData

                        if (responseBean.getCode()==0) {
                            // 跳转到主界面
                            startActivity(new Intent(LogoActivity.this, MainActivity.class));
                        } else {
                            // 跳转到登录界面
                            startActivity(new Intent(LogoActivity.this, LoginActivity.class));
                        }
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("请求失败", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                // 从sp中读出账号密码
                load();
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
//                map.put("phone", "15210035181");
                map.put("phone", mUsername);
//                map.put("password", "123456");
                map.put("password", mPassword);
                return map;
            }
        };
//        requestQueue.add(stringRequest);
        //添加唯一标识
//        objectRequest.setTag("lhdpost");
        //将请求添加到请求队列
        requestQueue.add(stringRequest);
    }

    void load() {
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        if (sp!=null)
        {
            mUsername = sp.getString("username", ""); // 第二个参数为默认值
            mPassword = sp.getString("password", ""); // 第二个参数为默认值
        }


    }
}
