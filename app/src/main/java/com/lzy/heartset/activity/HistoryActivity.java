package com.lzy.heartset.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lzy.heartset.R;
import com.lzy.heartset.bean.ResponseBean;
import com.lzy.heartset.fragment.DayHistoryFragment;
import com.lzy.heartset.fragment.MonthHistoryFragment;
import com.lzy.heartset.fragment.WeekHistoryFragment;
import com.lzy.heartset.fragment.YearHistoryFragment;
import com.lzy.heartset.utils.GlobalData;
import com.lzy.heartset.utils.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by yuqing on 2017/3/15.
 */
public class HistoryActivity extends Activity {

    private static final String URL_HISTORY = "http://10.108.224.77:8080/detect3/HistoryServlet";
    // 临时测试用
//    private static final String URL_HISTORY = "http://101.200.89.170:9000/capp/login/normal";
    RadioGroup mRadioGroup;
    RadioButton mRbDay;
    RadioButton mRbWeek;
    RadioButton mRbMonth;
    RadioButton mRbYear;

    DayHistoryFragment mDayHistoryFragment;
    WeekHistoryFragment mWeekHistoryFragment;
    MonthHistoryFragment mMonthHistoryFragment;
    YearHistoryFragment mYearHistoryFragment;

//    FragmentManager mManager;

    ListView mListView;
    private String mDate;
    private int mType;
    private static final int DAY = 1;
    private static final int WEEK = 2;
    private static final int MONTH = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_history);
        super.onCreate(savedInstanceState);


        Bundle bundle = getIntent().getExtras();
        mDate = bundle.getString("date");
        initView();
        setDefaultFragment();

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager manager = getFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = manager.beginTransaction();
                switch (checkedId) {

                    case R.id.rb_day:
                        mType = DAY;
//                        if (mDayHistoryFragment == null) {
//                            mDayHistoryFragment = new DayHistoryFragment();
//                        }
//                        transaction.replace(R.id.fl_history, mDayHistoryFragment);

                        break;
                    case R.id.rb_week:
                        mType = WEEK;
//                        if (mWeekHistoryFragment == null) {
//                            mWeekHistoryFragment = new WeekHistoryFragment();
//                        }
//                        transaction.replace(R.id.fl_history, mWeekHistoryFragment);
                        break;
                    case R.id.rb_month:
                        mType = MONTH;
//                        if (mMonthHistoryFragment == null) {
//                            mMonthHistoryFragment = new MonthHistoryFragment();
//                        }
//                        transaction.replace(R.id.fl_history, mMonthHistoryFragment);
                        break;
                    case R.id.rb_year:
//                        if (mYearHistoryFragment == null) {
//                            mYearHistoryFragment = new YearHistoryFragment();
//                        }
//                        transaction.replace(R.id.fl_history, mYearHistoryFragment);
                        break;
                    default:
                        break;

                }
                postToServer(HistoryActivity.this,mType,transaction);

            }
        });


    }

    private void setDefaultFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        postToServer(HistoryActivity.this,DAY,transaction);
    }

    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_choose);

        mRbDay = (RadioButton) findViewById(R.id.rb_day);
        mRbWeek = (RadioButton) findViewById(R.id.rb_week);
        mRbMonth = (RadioButton) findViewById(R.id.rb_month);
        mRbYear = (RadioButton) findViewById(R.id.rb_year);
        mRbDay.setChecked(true);

        mListView = (ListView) findViewById(R.id.lv_history);
        SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.list_item1,
                new String[]{"day", "time", "value", "suggestion"},
                new int[]{R.id.item_tv_day, R.id.item_tv_time, R.id.item_tv_value, R.id.item_tv_suggetion});
        mListView.setAdapter(adapter);


    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("day", "2017-01-01");
        map.put("time", "12:00:00");
        map.put("value", "80");
        map.put("suggestion", "心率正常");
        list.add(map);

        map.put("day", "2017-01-01");
        map.put("time", "12:00:00");
        map.put("value", "80");
        map.put("suggestion", "心率正常");
        list.add(map);

        map.put("day", "2017-01-01");
        map.put("time", "12:00:00");
        map.put("value", "80");
        map.put("suggestion", "心率正常");
        list.add(map);

        map.put("day", "2017-01-01");
        map.put("time", "12:00:00");
        map.put("value", "80");
        map.put("suggestion", "心率正常");
        list.add(map);

        map.put("day", "2017-01-01");
        map.put("time", "12:00:00");
        map.put("value", "80");
        map.put("suggestion", "心率正常");
        list.add(map);

        map.put("day", "2017-01-01");
        map.put("time", "12:00:00");
        map.put("value", "80");
        map.put("suggestion", "心率正常");
        list.add(map);

        map.put("day", "2017-01-01");
        map.put("time", "12:00:00");
        map.put("value", "80");
        map.put("suggestion", "心率正常");
        list.add(map);

        map.put("day", "2017-01-01");
        map.put("time", "12:00:00");
        map.put("value", "80");
        map.put("suggestion", "心率正常");
        list.add(map);
        return list;
    }

    /**
     * 请求服务器 返回历史数据
     *
     * @param context
     */
    private void postToServer(final Context context, final int type, final FragmentTransaction transaction) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //将JSONObject作为，将上一步得到的JSONObject对象作为参数传入
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("请求成功", "response -> " + response);
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();

                        Gson gson = new Gson();

                        ResponseBean responseBean = gson.fromJson(response, ResponseBean.class);

                        if (responseBean.getCode() == 0) {
//                        if (true) {
                            Toast.makeText(HistoryActivity.this, responseBean.toString(), Toast.LENGTH_SHORT).show();

                            switch (type) {
                                case DAY:
                                    if (mDayHistoryFragment == null) {
                                        mDayHistoryFragment = new DayHistoryFragment();
                                    }
                                    transaction.replace(R.id.fl_history, mDayHistoryFragment);
                                    break;
                                case WEEK:
                                    if (mWeekHistoryFragment == null) {
                                        mWeekHistoryFragment = new WeekHistoryFragment();
                                    }
                                    transaction.replace(R.id.fl_history, mWeekHistoryFragment);
                                    break;
                                case MONTH:
                                    if (mMonthHistoryFragment == null) {
                                        mMonthHistoryFragment = new MonthHistoryFragment();
                                    }
                                    transaction.replace(R.id.fl_history, mMonthHistoryFragment);
                                    break;

                            }
                            transaction.commit();
                        }


                    }


                }

                , new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("请求失败", error.getMessage(), error);
            }
        }

        )

        {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();

                map.put("userid", GlobalData.getUserid() + "");
                map.put("date", mDate);
                map.put("type", String.valueOf(type));
                map.put("sort_type","1");
                return map;
            }
        };
        // 将请求添加到请求队列
        requestQueue.add(stringRequest);
    }
}
