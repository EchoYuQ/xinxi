package com.lzy.heartset.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzy.heartset.R;
import com.lzy.heartset.activity.MeasureActivity;
import com.lzy.heartset.ui.RippleImageView;
import com.lzy.heartset.ui.RoundImageView;

/**
 * Created by yuqing on 2017/3/5.
 */
public class MeasureFragment extends Fragment {
    private View view;
    private RoundImageView imageView;
    private ImageView hintImageView;
    private AnimationDrawable animationDrawable;
    private RippleImageView rippleImageView;
    private ImageView startImageView;
    private Context mContext;

    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        mContext=getActivity();
        view=inflater.inflate(R.layout.fragment_two,container,false);
        initView();
        return view;
    }

    void initView()
    {
        hintImageView= (ImageView) view.findViewById(R.id.iv_hintanimation);

        hintImageView.setImageResource(R.drawable.animation1);
        animationDrawable = (AnimationDrawable) hintImageView.getDrawable();
        animationDrawable.start();

        rippleImageView= (RippleImageView) view.findViewById(R.id.rippleImageView);
        rippleImageView.startWaveAnimation();

        startImageView=rippleImageView.getImageView();
        startImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MeasureActivity.class);
                startActivity(intent);
            }
        });
    }


}
