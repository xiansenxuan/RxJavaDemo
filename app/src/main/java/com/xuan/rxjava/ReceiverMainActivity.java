package com.xuan.rxjava;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Title :
 * Description :
 * Author : Jerry xu    Data : 2016/11/28 0028 11:22
 * Updater :            Data : 2016/11/28 0028 11:22
 * Version : 1.0.0
 * Copyright : Copyright(c) 浙江蘑菇加电子商务有限公司 2015 ~ 2016 版权所有
 */

public class ReceiverMainActivity extends AppCompatActivity {
    @Bind(R.id.view_pager)
    ViewPager view_pager;
    @Bind(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;

    private ArrayList<Fragment> fragments;
    private String mTitles[] = {"消息", "通讯录", "应用", "我的"};
    private int mImages[] = {
        R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
    };
    //二次点击间隔小于2000毫秒才会退出应用
    private long waitTime = 2000;
    //点击时的时间
    private long touchTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setBottomTab();

    }

    private void setBottomTab() {
        fragments = new ArrayList<>();

        fragments.add(ReceiverFragment.newInstance(0));
        fragments.add(ReceiverFragment.newInstance(1));
        fragments.add(ReceiverOtherFragment.newInstance(2));
        fragments.add(ReceiverOtherFragment.newInstance(3));

        view_pager.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragments, mTitles));

        view_pager.setOffscreenPageLimit(fragments.size());


        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigation.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        for (int i = 0; i < mTitles.length; i++) {
            bottomNavigation.addItem(new AHBottomNavigationItem(mTitles[i], mImages[i]));
        }

        bottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.base_blue));//选中的颜色
        bottomNavigation.setInactiveColor(ContextCompat.getColor(this, R.color.disable_gray));//默认的颜色
        bottomNavigation.setNotificationBackgroundColor(ContextCompat.getColor(this, R.color.base_red));//消息的颜色

        bottomNavigation.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.base_white));//底色

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                view_pager.setCurrentItem(position);
            }
        });

//        bottomNavigation.setNotification(999, 2);// 数量 角标   数量0移除

        bottomNavigation.setForceTitlesDisplay(true);//全部显示底部文字

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
