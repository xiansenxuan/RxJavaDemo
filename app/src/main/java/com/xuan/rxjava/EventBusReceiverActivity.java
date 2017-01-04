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
import rx.Subscription;
import rx.functions.Action1;

public class EventBusReceiverActivity extends AppCompatActivity {

    @Bind(R.id.tv_receiver)
    TextView tv_receiver;

    @OnClick(R.id.tv_receiver)
    void receiver() {

    }

    @OnClick(R.id.tv_send)
    void send() {
        StringEvent event = new StringEvent();
        event.content = "test";

        EventBus.getDefault().post(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
