package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Vo Ngoc Hanh on 5/13/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public ViewPagerAdapter(FragmentManager fm, Context context,
                            @NonNull ArrayList<Fragment> fragments, @NonNull ArrayList<String> titles) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.titles = titles;
    }

    public void addFragment(String title, Fragment fragment) {
        fragments.add(fragment);
        titles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void destroy() {
        context = null;
    }
}
