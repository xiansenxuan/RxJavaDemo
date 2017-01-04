package com.xuan.rxjava;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Title :
 * Description :
 * Author : Jerry xu    Data : 2016/11/28 0028 11:23
 * Updater :            Data : 2016/11/28 0028 11:23
 * Version : 1.0.0
 * Copyright : Copyright(c) 浙江蘑菇加电子商务有限公司 2015 ~ 2016 版权所有
 */

public class ReceiverFragment extends Fragment {

    @Bind(R.id.tv_receiver)
    TextView tv_receiver;

    @OnClick(R.id.tv_receiver)void actionUp(){
        startActivity(new Intent(getActivity(),EventBusReceiverActivity.class));
    }

    private int index;

    public ReceiverFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public ReceiverFragment(int index) {
        this.index = index;
    }

    public static ReceiverFragment newInstance(int index) {
        ReceiverFragment fragment = new ReceiverFragment(index);
        return fragment;
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onShowMessageEvent(StringEvent messageEvent) {
        tv_receiver.setText("接收到事件返回的对象数据 " + messageEvent.content + index);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_receiver, container, false);
        ButterKnife.bind(this, view);

        //订阅事件（先订阅再发送）
        Toast.makeText(getActivity(), ("已订阅"), Toast.LENGTH_SHORT).show();
        //注册事件
        EventBus.getDefault().register(this);


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //订阅事件（先订阅再发送）
        Toast.makeText(getActivity(), ("已取消"), Toast.LENGTH_SHORT).show();
        //取消事件注册
        EventBus.getDefault().unregister(this);

        ButterKnife.unbind(this);
    }
}
