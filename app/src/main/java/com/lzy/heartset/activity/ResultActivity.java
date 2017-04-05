package com.lzy.heartset.activity;

import android.app.Activity;
import android.os.Bundle;

import com.lzy.heartset.R;
import com.lzy.heartset.bean.UserDataBean;
import com.lzy.heartset.ui.CircleIndicator;
import com.lzy.heartset.ui.IndicatorItem;
import com.lzy.heartset.ui.LineIndicator;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends Activity {

    CircleIndicator ci1;
    LineIndicator liProgress;

    private int mHeartRate = 0;
    private int mFatigue = 0;
    int mMiddleColor;
    int mLowColor;
    int mHighColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cicle_view);


        Bundle bundle=getIntent().getExtras();
        mHeartRate =  bundle.getInt("heart_rate");
        mFatigue =  bundle.getInt("pressure");

        initColor();
        initView();
        testLineProgress();
        testIndicator();
    }

    private void initView() {
        liProgress = (LineIndicator) findViewById(R.id.li_progress);
        ci1 = (CircleIndicator) findViewById(R.id.ci_1);
    }

    private void initColor() {
        mMiddleColor = getResources().getColor(R.color.pressure_middle);
        mLowColor = getResources().getColor(R.color.pressure_low);
        mHighColor = getResources().getColor(R.color.pressure_high);
    }


    private void testLineProgress() {
        String leftAlert = "慢";
        String leftContent = "0";
        String rightAlert = "快";
        String rightContent = "150";
        if (mHeartRate > 100) {
            liProgress.setProgressColor(mHighColor);
            liProgress.setIndicatorBackground(mHighColor);
        } else {
            if (mHeartRate > 65) {
                liProgress.setProgressColor(mMiddleColor);
                liProgress.setIndicatorBackground(mMiddleColor);

            } else {
                liProgress.setProgressColor(mLowColor);
                liProgress.setIndicatorBackground(mLowColor);

            }
        }
        liProgress.setContent(leftAlert, leftContent, rightAlert, rightContent);
        liProgress.setIndicator(40, 150, mHeartRate, mHeartRate + " BMP");
    }

    private void testIndicator() {

        List<IndicatorItem> dividerIndicator = new ArrayList<>();
        IndicatorItem item1 = new IndicatorItem();
        item1.start = 0;
        item1.end = 30;
        item1.value = "过低";
        item1.color = mLowColor;
        dividerIndicator.add(item1);

        IndicatorItem item2 = new IndicatorItem();
        item2.start = 30;
        item2.end = 70;
        item2.value = "正常";
        item2.color = mMiddleColor;
        dividerIndicator.add(item2);

        IndicatorItem item3 = new IndicatorItem();
        item3.start = 70;
        item3.end = 100;
        item3.value = "过高";
        item3.color = mHighColor;
        dividerIndicator.add(item3);


        String title = "疲劳度";
        String content = mFatigue + "";
        String unit = "";
        String alert = "愉快的心情";
        if (mFatigue < 30) {

            ci1.setContentColor(mLowColor, mLowColor);
            ci1.setmAlertColor(mLowColor);

        } else {
            if (mFatigue < 70) {
                ci1.setContentColor(mMiddleColor, mMiddleColor);
                ci1.setmAlertColor(mMiddleColor);
            } else {
                ci1.setContentColor(mHighColor, mHighColor);
                ci1.setmAlertColor(mHighColor);
            }
        }
        ci1.setContent(title, content, unit, alert);
        ci1.setIndicatorValue(dividerIndicator, mFatigue);
    }
}
