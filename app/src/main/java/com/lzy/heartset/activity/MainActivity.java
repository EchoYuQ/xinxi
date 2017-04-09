package com.lzy.heartset.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lzy.heartset.R;
import com.lzy.heartset.fragment.HistroyFragment;
import com.lzy.heartset.fragment.MainPageFragment;
import com.lzy.heartset.fragment.MeasureFragment;
import com.lzy.heartset.fragment.MineFragment;
import com.lzy.heartset.fragment.NewsFragment;
import com.lzy.heartset.ui.AlphaIndicator;
import com.lzy.heartset.ui.AlphaView;

import java.util.ArrayList;
import java.util.List;

//import com.lzy.widget.AlphaIndicator;

public class MainActivity extends FragmentActivity implements View.OnClickListener {


    AlphaView mTab1;
    AlphaView mTab2;
    AlphaView mTab3;
    AlphaView mTab4;
    AlphaView mTab5;
    ViewPager viewPager;
    AlphaIndicator alphaIndicator;
//    MainAdapter mainAdapter;
//    public static MainActivity instance = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("MainActivity onCreate()");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT>19)
        {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        alphaIndicator = (AlphaIndicator) findViewById(R.id.alphaIndicator);
        mTab1 = (AlphaView) findViewById(R.id.av_tab1);
        mTab2 = (AlphaView) findViewById(R.id.av_tab2);
        mTab3 = (AlphaView) findViewById(R.id.av_tab3);
        mTab4 = (AlphaView) findViewById(R.id.av_tab4);
        mTab5 = (AlphaView) findViewById(R.id.av_tab5);

//        mainAdapter=new MainAdapter(getSupportFragmentManager());

        initEvent();
    }


    private void initEvent() {
        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        alphaIndicator.setViewPager(viewPager);
        mTab1.setOnClickListener(this);
        mTab2.setOnClickListener(this);
        mTab3.setOnClickListener(this);
        mTab4.setOnClickListener(this);
        mTab5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.av_tab1:
                alphaIndicator.setPagerNum(0);
                break;
            case R.id.av_tab2:
                alphaIndicator.setPagerNum(1);
                break;
            case R.id.av_tab3:
                alphaIndicator.setPagerNum(2);
                break;
            case R.id.av_tab4:
                alphaIndicator.setPagerNum(3);
                break;
            case R.id.av_tab5:
                alphaIndicator.setPagerNum(4);
                break;


        }
    }

    private class MainAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
//        private String[] titles = {//
//                "第一页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度",//
//                "第二页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度",//
//                "第三页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度", //
//                "第四页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度"};

        public MainAdapter(FragmentManager fm) {
            super(fm);
                fragments.add(new MainPageFragment());
                fragments.add(new MeasureFragment());
                fragments.add(new HistroyFragment());
//            fragments.add(new TextFragment());
                fragments.add(new NewsFragment());
                fragments.add(new MineFragment());

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
