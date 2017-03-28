package com.lzy.heartset.fragment;

/**
 * Created by yuqing on 2017/3/4.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.lzy.heartset.R;
import com.lzy.heartset.activity.LoginActivity;
import com.lzy.heartset.activity.MyInformationActivity;
import com.lzy.heartset.ui.RoundImageView;
import com.lzy.heartset.utils.FileUtil;
import com.lzy.heartset.utils.GlobalData;
import com.lzy.heartset.utils.Tools;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;


/**
 * 个人信息界面
 */
public class MineFragment extends Fragment {
    private View view;
    // 组件
    private RoundImageView mIvUimage = null;// 头像图片
    private TextView mTvName;// 昵称

    // 用户缓存
    private SharedPreferences preferences;

    // 我的订单、我的收藏、账号管理
    private RelativeLayout mRlAppoint;
    private RelativeLayout mRlCollects;
    private RelativeLayout mRlInfo;
    private RelativeLayout mRlQuit;

    private boolean mIsLogin;

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_mine, null);
        // 初始化控件
        initView();
        // 登录成功之后，获得用户头像
        preferences = getActivity().getSharedPreferences("Login",
                Context.MODE_PRIVATE);
//        int uimage = preferences.getInt("uimage", 0);
        mIsLogin = preferences.getBoolean("mIsLogin", false);
        return view;
    }

    /**
     * 初始化控件方法
     */
    private void initView() {
        mIvUimage = (RoundImageView) view.findViewById(R.id.iv_uimage);// 头像


        mTvName = (TextView) view.findViewById(R.id.tv_name);// 立即登录
        // 我的订单、我的收藏、账号管理
        mRlAppoint = (RelativeLayout) view.findViewById(R.id.rl_appoint);
        mRlCollects = (RelativeLayout) view.findViewById(R.id.rl_collects);
        mRlInfo = (RelativeLayout) view.findViewById(R.id.rl_info);
        mRlQuit = (RelativeLayout) view.findViewById(R.id.rl_quit);


        // 监听事件
        mIvUimage.setOnClickListener(iv_listener);
        mTvName.setOnClickListener(iv_listener);

        mRlAppoint.setOnClickListener(iv_listener);
        mRlCollects.setOnClickListener(iv_listener);
        mRlInfo.setOnClickListener(iv_listener);
        mRlQuit.setOnClickListener(iv_listener);
    }

    OnClickListener iv_listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_uimage:

                    // 跳转到修改信息界面
                    startActivity(new Intent(mContext, MyInformationActivity.class));
                    break;
                case R.id.tv_name:
                    // 跳转到修改信息界面
                    startActivity(new Intent(mContext, MyInformationActivity.class));
                    break;

                case R.id.rl_appoint:
                    // 跳转至已完成订单页面
//                        startActivity(new Intent(getActivity(), MyIndent.class));
                    break;
                case R.id.rl_collects:
                    // 跳转至我的收藏页面
//                        startActivity(new Intent(getActivity(), ShowCollect.class));

                    break;
                case R.id.rl_info:
                    // 跳转至账号管理界面
//                    if (mIsLogin == true) {
//                        startActivity(new Intent(getActivity(),
//                                UserInfoManage.class));
//                    } else {
//                        Toast.makeText(getActivity(), "请先登录", 1).show();
//                    }

                    break;
                case R.id.rl_quit:
                    // 跳转至登录界面
//                    if (mIsLogin == true) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
//                    GlobalData.clearUserInfo();
                    getActivity().finish();
                    clearSP();

                    break;
                default:
                    break;
            }

        }
    };


    // 清空SharedPreferences中的数据
    private void clearSP() {
        SharedPreferences sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", "");
        editor.putString("password", "");
        // editor.putBoolean()、editor.putInt()、editor.putFloat()……
        editor.apply();
    }

    @Override
    public void onResume() {
        Bitmap photo = FileUtil.readImageFromLocal(mContext, GlobalData.getTel()+"photo.png");
        if (photo != null) {
            mIvUimage.setImageBitmap(photo);
        } else {
            mIvUimage.setImageResource(R.drawable.user_image);
        }
        super.onResume();
    }
}

