package com.xuan.rxjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

public class ReceiverActivity extends AppCompatActivity {

    @Bind(R.id.tv_receiver)
    TextView tv_receiver;

    private Subscription rxSubscription;

    @OnClick(R.id.tv_receiver)
    void receiver() {
        //订阅事件（先订阅再发送）
        Toast.makeText(ReceiverActivity.this, ("已订阅"), Toast.LENGTH_SHORT).show();
        tv_receiver.setEnabled(false);

        rxSubscription=RxBus.getDefault().toObservable(StringEvent.class).subscribe(new Action1<StringEvent>() {
                     @Override
                     public void call(StringEvent event) {
                             Toast.makeText(ReceiverActivity.this,
                                     "接收到事件返回的对象数据 " + event.content, Toast.LENGTH_SHORT).show();
                     }
                 });
    }

    @OnClick(R.id.tv_send)
    void send() {

        startActivity(new Intent(this, RxBusActivity.class));
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

        if(!rxSubscription.isUnsubscribed()) {
            rxSubscription.unsubscribe();
        }
    }
}
