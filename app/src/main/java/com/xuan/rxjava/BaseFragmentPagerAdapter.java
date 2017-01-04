package com.xuan.rxjava;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Title : Fragment组成的多个pager页面适配器
 * Description :
 * Author : xiansenxuan   Data : 2016/11/17 14:26
 * Updater : Jerry xu     Data : 2016/11/22 11:36
 * Version : 1.0.0
 * Copyright : Copyright(c) 浙江蘑菇加电子商务有限公司 2015 ~ 2016 版权所有
 */
public class BaseFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;
    private String[]            mTitles;

    public BaseFragmentPagerAdapter(FragmentManager fm,
                                    ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public BaseFragmentPagerAdapter(FragmentManager fm,
                                    ArrayList<Fragment> fragments, ArrayList<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    public BaseFragmentPagerAdapter(FragmentManager fm,
                                    ArrayList<Fragment> fragments, String[] mTitles) {
        super(fm);
        this.fragments = fragments;
        this.mTitles = mTitles;
    }

    @Override
    // 返回的fragment页面
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override//返回的tabs标签文本
    public CharSequence getPageTitle(int position) {
        if (titles == null || titles.size() == 0) {
            return mTitles == null ? "" : mTitles[position];
        } else if (mTitles == null || mTitles.length == 0) {
            return titles == null ? "" : titles.get(position);
        } else {
            return "";
        }
    }
}
