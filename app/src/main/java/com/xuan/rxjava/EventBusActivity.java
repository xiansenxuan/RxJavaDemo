package com.xuan.rxjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EventBusActivity extends AppCompatActivity {

    @Bind(R.id.tv_receiver)
    TextView tv_receiver;

    @OnClick(R.id.tv_send)
    void send() {
        startActivity(new Intent(this,EventBusReceiverActivity.class));
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onShowMessageEvent(StringEvent messageEvent) {
        tv_receiver.setText("接收到事件返回的对象数据 " + messageEvent.content);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbus);
        ButterKnife.bind(this);

        //订阅事件（先订阅再发送）
        Toast.makeText(this, ("已订阅"), Toast.LENGTH_SHORT).show();
        //注册事件
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //订阅事件（先订阅再发送）
        Toast.makeText(this, ("已取消"), Toast.LENGTH_SHORT).show();
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }
}
