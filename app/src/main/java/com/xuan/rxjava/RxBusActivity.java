package com.xuan.rxjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RxBusActivity extends AppCompatActivity {

    @OnClick(R.id.tv_send)
    void send() {
        StringEvent event=new StringEvent();
        event.content="test";
        RxBus.getDefault().send(1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbus);

        ButterKnife.bind(this);

    }


}
