package com.lzy.heartset.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import com.lzy.heartset.utils.GlobalData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by yuqing on 2017/3/15.
 */
public class HistoryActivity extends Activity implements View.OnClickListener {

    private static final String URL_HISTORY = GlobalData.URL_HEAD + ":9000/detect3/HistoryServlet";
    // 临时测试用
    RadioGroup mRadioGroup;
    RadioButton mRbDay;
    RadioButton mRbWeek;
    RadioButton mRbMonth;

    WeekHistoryFragment mWeekHistoryFragment;
    MonthHistoryFragment mMonthHistoryFragment;
    DayHistoryFragment mDayHistoryFragment;

    ImageView mIvLast;
    ImageView mIvNext;
    TextView mTvDataType;

//    FragmentManager mManager;

    ListView mListView;
    private String mDate;
    private int mTimeType;


    private int mDrawables[] = {R.drawable.user_appoint, R.drawable.heart_icon, R.drawable.icon_main};
    private String mDataTypeTexts[] = {"心率", "血氧", "心理压力"};
    private int mIndex = 0;


    private GlobalData.MeasureType mMeasureType;
    private static final int DAY = 1;
    private static final int WEEK = 2;
    private static final int MONTH = 3;
    private static final int YEAR = 4;

    List<HistoryDataItemBean> mHistoryDataItemList = new ArrayList<>();
    FragmentManager mManager;
    FragmentTransaction mTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_history);
        super.onCreate(savedInstanceState);
        mManager = getFragmentManager();
        // 开启Fragment事务
        mTransaction = mManager.beginTransaction();

        Bundle bundle = getIntent().getExtras();
        mDate = bundle.getString("date");
        initView();
        setDefaultFragment();

        // 初始化当前历史数据类型（心率、血氧等）
        GlobalData.currenttype = GlobalData.MeasureType.HEART_RATE;

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager mManager = getFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = mManager.beginTransaction();
                switch (checkedId) {

                    case R.id.rb_day:
                        mTimeType = DAY;

                        break;
                    case R.id.rb_week:
                        mTimeType = WEEK;
                        break;
                    case R.id.rb_month:
                        mTimeType = MONTH;
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
//        mRbYear = (RadioButton) findViewById(R.id.rb_year);
        mRbDay.setChecked(true);

        mIvLast = (ImageView) findViewById(R.id.id_iv_last);
        mIvNext = (ImageView) findViewById(R.id.id_iv_next);
        mIvLast.setOnClickListener(this);
        mIvNext.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.lv_history);
        mTvDataType = (TextView) findViewById(R.id.id_tv_history_data_type);
        mTvDataType.setText(mDataTypeTexts[mIndex]);


    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String[] pressure_suggestions = {"心理压力低", "心理压力中等", "心理压力高"};
        String[] blood_oxygten_suggestions = {"血氧含量低", "血氧含量中等", "血压含量高"};
        String[] heartrate_suggestions = {"心率慢", "心率正常", "心率快"};
        Log.i("HistoryList size", mHistoryDataItemList.size() + "");
        for (int i = 0; i < mHistoryDataItemList.size(); i++) {
            HistoryDataItemBean item = mHistoryDataItemList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("image", mDrawables[mIndex]);
            map.put("day", item.getDate());
            map.put("time", item.getTime());

            mMeasureType = GlobalData.measuretypes[mIndex];
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
                        map.put("suggestion", blood_oxygten_suggestions[0]);
                    } else if (bloodoxygen < 98) {
                        map.put("suggestion", blood_oxygten_suggestions[1]);

                    } else {
                        map.put("suggestion", blood_oxygten_suggestions[2]);
                    }
                    break;
                case PRESSURE:
                    int pressure = item.getPressure();
                    map.put("value", pressure);
                    if (pressure < 93) {
                        map.put("suggestion", pressure_suggestions[0]);
                    } else if (pressure < 98) {
                        map.put("suggestion", pressure_suggestions[1]);

                    } else {
                        map.put("suggestion", pressure_suggestions[2]);
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

                                    GlobalData.historyDataItemBeanList.clear();
                                    GlobalData.historyDataItemBeanList = new ArrayList<>(mHistoryDataItemList);

                                    mDayHistoryFragment = new DayHistoryFragment();
                                    transaction.replace(R.id.fl_history, mDayHistoryFragment);
                                    break;
                                case WEEK:

                                    GlobalData.historyDataItemBeanList.clear();
                                    GlobalData.historyDataItemBeanList = new ArrayList<>(mHistoryDataItemList);

                                    mWeekHistoryFragment = new WeekHistoryFragment();
                                    transaction.replace(R.id.fl_history, mWeekHistoryFragment);
                                    break;
                                case MONTH:

                                    GlobalData.historyDataItemBeanList.clear();
                                    GlobalData.historyDataItemBeanList = new ArrayList<>(mHistoryDataItemList);

                                    mMonthHistoryFragment = new MonthHistoryFragment();
                                    transaction.replace(R.id.fl_history, mMonthHistoryFragment);

                                    break;

                            }
                            SimpleAdapter adapter = new SimpleAdapter(HistoryActivity.this, getData(), R.layout.list_item1,
                                    new String[]{"image", "day", "time", "value", "suggestion"},
                                    new int[]{R.id.item_iv_image, R.id.item_tv_day, R.id.item_tv_time, R.id.item_tv_value, R.id.item_tv_suggetion});
                            mListView.setAdapter(adapter);
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
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "08:10:30", 50, 75, 99));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "08:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "08:50:30", 50, 63, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "09:20:30", 45, 65, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "12:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "12:40:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "12:45:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "13:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "14:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "15:20:30", 50, 75, 95));
                        mHistoryDataItemList.add(new HistoryDataItemBean("2015-10-08", "16:20:30", 50, 75, 95));

                        GlobalData.historyDataItemBeanList.clear();
                        GlobalData.historyDataItemBeanList = new ArrayList<>(mHistoryDataItemList);

                        mDayHistoryFragment = new DayHistoryFragment();
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
                        mHistoryDataItemList.add(new HistoryDataItemBean("2017-02-07", "19:20:30", 50, 70, 95));

                        GlobalData.historyDataItemBeanList.clear();
                        GlobalData.historyDataItemBeanList = new ArrayList<>(mHistoryDataItemList);

                        mWeekHistoryFragment = new WeekHistoryFragment();
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

                        GlobalData.historyDataItemBeanList.clear();
                        GlobalData.historyDataItemBeanList = new ArrayList<>(mHistoryDataItemList);
                        mMonthHistoryFragment = new MonthHistoryFragment();
                        transaction.replace(R.id.fl_history, mMonthHistoryFragment);
                        break;
                }


                Log.i(" size", mHistoryDataItemList.size() + "");
                SimpleAdapter adapter = new SimpleAdapter(HistoryActivity.this, getData(), R.layout.list_item1,
                        new String[]{"image", "day", "time", "value", "suggestion"},
                        new int[]{R.id.item_iv_image, R.id.item_tv_day, R.id.item_tv_time, R.id.item_tv_value, R.id.item_tv_suggetion});
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_iv_last:
                mIndex = (mIndex - 1 + GlobalData.measuretypes.length) % GlobalData.measuretypes.length;
//                mMeasureType=measuretypes[(mIndex-1+measuretypes.length)%measuretypes.length];
                break;
            case R.id.id_iv_next:
                mIndex = (mIndex + 1) % GlobalData.measuretypes.length;

                break;
        }
        // 更改当前历史数据类型（心率、血氧等）
        GlobalData.currenttype = GlobalData.measuretypes[mIndex];

        // 更新TextView
        mTvDataType.setText(mDataTypeTexts[mIndex]);
        // 更新ListView
        SimpleAdapter adapter = new SimpleAdapter(HistoryActivity.this, getData(), R.layout.list_item1,
                new String[]{"image", "day", "time", "value", "suggestion"},
                new int[]{R.id.item_iv_image, R.id.item_tv_day, R.id.item_tv_time, R.id.item_tv_value, R.id.item_tv_suggetion});
        mListView.setAdapter(adapter);

        // 更新Fragment

        FragmentManager mManager = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = mManager.beginTransaction();
        switch (mTimeType) {
            case DAY:
                mDayHistoryFragment = new DayHistoryFragment();
                transaction.replace(R.id.fl_history, mDayHistoryFragment);
                break;
            case WEEK:
                mWeekHistoryFragment = new WeekHistoryFragment();
                transaction.replace(R.id.fl_history, mWeekHistoryFragment);
                break;
            case MONTH:
                mMonthHistoryFragment = new MonthHistoryFragment();
                transaction.replace(R.id.fl_history, mMonthHistoryFragment);
                break;
//            case YEAR:
//                mDayHistoryFragment = new DayHistoryFragment();
//                transaction.replace(R.id.fl_history, mDayHistoryFragment);
//                break;
        }
        transaction.commit();

        System.out.println("点击了下一个");
    }
}
