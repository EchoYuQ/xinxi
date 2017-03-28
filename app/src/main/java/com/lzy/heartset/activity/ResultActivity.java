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
    int mCircleGreen;
    int mCircleYellow;
    int mCircleRed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cicle_view);

        UserDataBean userDataBean = (UserDataBean) getIntent().getSerializableExtra("userdatabean");
        mHeartRate=userDataBean.getHeartrate();
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
        mCircleGreen = getResources().getColor(R.color.circle_green);
        mCircleYellow = getResources().getColor(R.color.circle_yellow);
        mCircleRed = getResources().getColor(R.color.circle_red);
    }


    private void testLineProgress() {
        String leftAlert = "慢";
        String leftContent = "0";
        String rightAlert = "快";
        String rightContent = "150";
        if (mHeartRate > 100) {
            liProgress.setProgressColor(mCircleRed);
            liProgress.setIndicatorBackground(mCircleRed);
        } else {
            if (mHeartRate > 65) {
                liProgress.setProgressColor(mCircleGreen);
                liProgress.setIndicatorBackground(mCircleGreen);

            } else {
                liProgress.setProgressColor(mCircleYellow);
                liProgress.setIndicatorBackground(mCircleYellow);

            }
        }
        liProgress.setContent(leftAlert, leftContent, rightAlert, rightContent);
        liProgress.setIndicator(40, 150, mHeartRate, mHeartRate + "BMP");
    }
//
//    private void testProgress() {
//        String title = "身体年龄";
//        String content = "23";
//        String unit = "岁";
//        String alert = "显年轻4岁";
//        cp1.setContent(title, content, unit, alert);
//        cp1.setIndicatorValue(10f, 60f, 33f, "实际年龄", 29f, 20, 30, 40, 50);
//        cp2.setContent(title, content, unit, alert);
//        cp2.setIndicatorValue(10f, 60f, 33f, 29f, 20, 30, 40, 50);
//        cp3.setContent(title, content, unit, alert);
//        cp3.setIndicatorValue(10f, 60f, 33f, 29f, 20, 30, 40, 50);
//    }

    private void testIndicator() {

        List<IndicatorItem> dividerIndicator = new ArrayList<>();
        IndicatorItem item1 = new IndicatorItem();
        item1.start = 0;
        item1.end = 30;
        item1.value = "过低";
        item1.color = mCircleYellow;
        dividerIndicator.add(item1);

        IndicatorItem item2 = new IndicatorItem();
        item2.start = 30;
        item2.end = 70;
        item2.value = "正常";
        item2.color = mCircleGreen;
        dividerIndicator.add(item2);

        IndicatorItem item3 = new IndicatorItem();
        item3.start = 70;
        item3.end = 100;
        item3.value = "过高";
        item3.color = mCircleRed;
        dividerIndicator.add(item3);


        mFatigue = 50;
        String title = "疲劳度";
        String content = mFatigue + "";
        String unit = "";
        String alert = "愉快的心情";
        if (mFatigue < 30) {

            ci1.setContentColor(mCircleGreen, mCircleGreen);
            ci1.setmAlertColor(mCircleGreen);
        } else {
            if (mFatigue < 70) {
                ci1.setContentColor(mCircleYellow, mCircleYellow);
                ci1.setmAlertColor(mCircleYellow);
            } else {
                ci1.setContentColor(mCircleRed, mCircleRed);
                ci1.setmAlertColor(mCircleRed);
            }
        }
        ci1.setContent(title, content, unit, alert);
        ci1.setIndicatorValue(dividerIndicator, mFatigue);
    }
}
