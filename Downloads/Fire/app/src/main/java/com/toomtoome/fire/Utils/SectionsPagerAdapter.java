package com.toomtoome.fire.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moogunjung on 11/21/17.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SectionsPagerAdapter";

    private final List<Fragment> mFragmentList = new ArrayList<>();

    private final Bundle args;
//    private final Course mCourse;

    public SectionsPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        args = bundle;
        //        mCourse = course;
        //Log.d(TAG, "SectionsPagerAdapter: bundle " + bundle);
        //Log.d(TAG, "SectionsPagerAdapter: args" + args.getParcelable("course"));
    }

    @Override
    public Fragment getItem(int position) {

        mFragmentList.get(position).setArguments(args);
        //Log.d(TAG, "getItem: " + mFragmentList.get(position).getArguments().getParcelable("course"));
        //Log.d(TAG, "getItem: " + mFragmentList.get(position).getArguments().getString("courseId"));
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }



}
