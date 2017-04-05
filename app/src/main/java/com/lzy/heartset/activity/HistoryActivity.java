package com.lzy.heartset.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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
import com.lzy.heartset.bean.HistoryDataItemBean;
import com.lzy.heartset.bean.ResponseBean;
import com.lzy.heartset.fragment.DayHistoryFragment;
import com.lzy.heartset.fragment.MonthHistoryFragment;
import com.lzy.heartset.fragment.WeekHistoryFragment;
import com.lzy.heartset.fragment.YearHistoryFragment;
import com.lzy.heartset.utils.GlobalData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by yuqing on 2017/3/15.
 */
public class HistoryActivity extends Activity {

    private static final String URL_HISTORY = GlobalData.URL_HEAD+":9000/detect3/HistoryServlet";
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
    private int mTimeType;

    public enum MeasureType {HEART_RATE, BLOOD_OXYGEN, PRESSURE}

    ;
    private MeasureType mMeasureType;
    private static final int DAY = 1;
    private static final int WEEK = 2;
    private static final int MONTH = 3;
    private static final int YEAR = 4;

    List<HistoryDataItemBean> mHistoryDataItemList = new ArrayList<>();

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
                        mTimeType = DAY;
//                        if (mDayHistoryFragment == null) {
//                            mDayHistoryFragment = new DayHistoryFragment();
//                        }
//                        transaction.replace(R.id.fl_history, mDayHistoryFragment);

                        break;
                    case R.id.rb_week:
                        mTimeType = WEEK;
//                        if (mWeekHistoryFragment == null) {
//                            mWeekHistoryFragment = new WeekHistoryFragment();
//                        }
//                        transaction.replace(R.id.fl_history, mWeekHistoryFragment);
                        break;
                    case R.id.rb_month:
                        mTimeType = MONTH;
//                        if (mMonthHistoryFragment == null) {
//                            mMonthHistoryFragment = new MonthHistoryFragment();
//                        }
//                        transaction.replace(R.id.fl_history, mMonthHistoryFragment);
                        break;
                    case R.id.rb_year:
                        mTimeType = YEAR;
//                        if (mYearHistoryFragment == null) {
//                            mYearHistoryFragment = new YearHistoryFragment();
//                        }
//                        transaction.replace(R.id.fl_history, mYearHistoryFragment);
                        break;
                    default:
                        break;

                }
                postToServer(HistoryActivity.this, mTimeType, transaction);

            }
        });


    }

    private void setDefaultFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        postToServer(HistoryActivity.this, DAY, transaction);
    }

    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_choose);

        mRbDay = (RadioButton) findViewById(R.id.rb_day);
        mRbWeek = (RadioButton) findViewById(R.id.rb_week);
        mRbMonth = (RadioButton) findViewById(R.id.rb_month);
        mRbYear = (RadioButton) findViewById(R.id.rb_year);
        mRbDay.setChecked(true);

        mListView = (ListView) findViewById(R.id.lv_history);


    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String[] pressure_suggestions = {"心理压力低", "心理压力中等", "心理压力高"};
        String[] blood_oxygten_suggestions = {"血氧含量低", "血氧含量中等", "血压含量高"};
        String[] heartrate_suggestions = {"心率慢", "心率正常", "心率快"};
        for (int i = 0; i < mHistoryDataItemList.size(); i++) {
            HistoryDataItemBean item = mHistoryDataItemList.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("day", item.getDate());
            map.put("time", item.getTime());


            switch (mMeasureType) {
                case HEART_RATE:
                    int heartrate = item.getHeart_rate();
                    map.put("value", heartrate);

                    if (heartrate < 60) {
                        map.put("suggestion", heartrate_suggestions[0]);
                    } else if (heartrate < 90) {
                        map.put("suggestion", heartrate_suggestions[1]);

                    } else {
                        map.put("suggestion", heartrate_suggestions[2]);
                    }
                    break;
                case BLOOD_OXYGEN:
                    int bloodoxygen = item.getBlood_oxygen();
                    map.put("value", bloodoxygen);
                    if (bloodoxygen < 93) {
                        map.put("suggestion", heartrate_suggestions[0]);
                    } else if (bloodoxygen < 98) {
                        map.put("suggestion", heartrate_suggestions[1]);

                    } else {
                        map.put("suggestion", heartrate_suggestions[2]);
                    }
                    break;
                case PRESSURE:
                    int pressure = item.getPressure();
                    map.put("value", pressure);
                    if (pressure < 93) {
                        map.put("suggestion", heartrate_suggestions[0]);
                    } else if (pressure < 98) {
                        map.put("suggestion", heartrate_suggestions[1]);

                    } else {
                        map.put("suggestion", heartrate_suggestions[2]);
                    }
                    break;

            }


            list.add(map);
        }
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
                            // TODO: 2017/4/4  解析服务器返回的历史数据，并存到 mHistoryDataItemList

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
                                case YEAR:
                                    if (mYearHistoryFragment == null) {
                                        mYearHistoryFragment = new YearHistoryFragment();
                                    }
                                    transaction.replace(R.id.fl_history, mYearHistoryFragment);
                                    break;

                            }
//                            GlobalData.historyDataItemBeanList = mHistoryDataItemList;
//                            SimpleAdapter adapter = new SimpleAdapter(HistoryActivity.this, getData(), R.layout.list_item1,
//                                    new String[]{"day", "time", "value", "suggestion"},
//                                    new int[]{R.id.item_tv_day, R.id.item_tv_time, R.id.item_tv_value, R.id.item_tv_suggetion});
//                            mListView.setAdapter(adapter);
                            transaction.commit();
                        }


                    }


                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("请求失败", error.getMessage(), error);
                switch (type) {
                    case DAY:
                        mHistoryDataItemList.clear();
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "03:20:30", 50, 90, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "03:46:30", 50, 99, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "05:20:30", 50, 86, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "08:10:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "08:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "08:50:30", 50, 63, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "09:20:30", 50, 65, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "12:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "12:40:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "12:45:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "13:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "14:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "15:20:30", 50, 75, 95));
                        if (mDayHistoryFragment == null) {
                            mDayHistoryFragment = new DayHistoryFragment();
                        }
                        transaction.replace(R.id.fl_history, mDayHistoryFragment);

                        break;
                    case WEEK:
                        mHistoryDataItemList.clear();

                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-01", "03:20:30", 50, 90, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-01", "03:46:30", 50, 99, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-03", "05:20:30", 50, 86, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-03", "08:10:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-03", "08:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-04", "08:50:30", 50, 63, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-05", "09:20:30", 50, 65, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-05", "12:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-06", "12:40:30", 50, 90, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-07", "12:45:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-07", "13:20:30", 50, 79, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-07", "14:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-07", "15:20:30", 50, 70, 95));


                        if (mWeekHistoryFragment == null) {
                            mWeekHistoryFragment = new WeekHistoryFragment();
                        }
                        transaction.replace(R.id.fl_history, mWeekHistoryFragment);
                        break;
                    case MONTH:
                        mHistoryDataItemList.clear();

                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-01", "03:20:30", 50, 90, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-01", "03:46:30", 50, 99, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-03", "05:20:30", 50, 86, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-03", "08:10:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-03", "08:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-04", "08:50:30", 50, 63, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-05", "09:20:30", 50, 65, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-05", "12:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-06", "12:40:30", 50, 90, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-07", "12:45:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-07", "13:20:30", 50, 79, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-07", "14:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-07", "15:20:30", 50, 70, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-11", "03:20:30", 50, 90, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-11", "03:46:30", 50, 99, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-13", "05:20:30", 50, 86, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-13", "08:10:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-13", "08:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-14", "08:50:30", 50, 63, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-15", "09:20:30", 50, 65, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-15", "12:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-26", "12:40:30", 50, 90, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-27", "12:45:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-27", "13:20:30", 50, 79, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-27", "14:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-27", "15:20:30", 50, 70, 95));
                        if (mMonthHistoryFragment == null) {
                            mMonthHistoryFragment = new MonthHistoryFragment();
                        }
                        transaction.replace(R.id.fl_history, mMonthHistoryFragment);
                        break;
                    case YEAR:
                        mHistoryDataItemList.clear();

                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "03:20:30", 50, 90, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "03:46:30", 50, 99, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "05:20:30", 50, 86, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "08:10:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "08:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "08:50:30", 50, 63, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "09:20:30", 50, 65, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "12:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "12:40:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "12:45:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "13:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "14:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "15:20:30", 50, 75, 95));
                        if (mYearHistoryFragment == null) {
                            mYearHistoryFragment = new YearHistoryFragment();
                        }
                        transaction.replace(R.id.fl_history, mYearHistoryFragment);
                        break;
                }
                GlobalData.historyDataItemBeanList = mHistoryDataItemList;
                SimpleAdapter adapter = new SimpleAdapter(HistoryActivity.this, getData(), R.layout.list_item1,
                        new String[]{"day", "time", "value", "suggestion"},
                        new int[]{R.id.item_tv_day, R.id.item_tv_time, R.id.item_tv_value, R.id.item_tv_suggetion});
                mListView.setAdapter(adapter);
                transaction.commit();
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
                map.put("sort_type", "1");
                return map;
            }
        };
        // 将请求添加到请求队列
        requestQueue.add(stringRequest);
    }
}
