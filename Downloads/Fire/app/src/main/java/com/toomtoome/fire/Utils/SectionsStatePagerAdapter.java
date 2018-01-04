package com.toomtoome.fire.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by moogunjung on 11/23/17.
 */

public class SectionsStatePagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SectionsStatePagerAdapt";

    // List type array
    /*
    pager adapter also has a list of fragments but state pager adapter has a hashmap data
     */
    private final List<Fragment> mFragmentList = new ArrayList<>();

    private final HashMap<Fragment, Integer> mFragments = new HashMap<>();
    private final HashMap<Integer, String> mFragmentNames = new HashMap<>();
    private final HashMap<String, Integer> mFragmentNumbers = new HashMap<>();

    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /*
    get item and get count methods are what needs to override
    get item returns fragment, get count returns integer
     */
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /*
        Custom methods
     */

    public void addFragment(Fragment fragment, String fragmentName) {
        mFragmentList.add(fragment);
        mFragments.put(fragment, mFragmentList.size()-1);
        mFragmentNumbers.put(fragmentName, mFragmentList.size()-1);
        mFragmentNames.put(mFragmentList.size()-1, fragmentName);
    }

    /**
     *
     * @param fragmentName
     * @return
     */
    public Integer getFragmentNumber(String fragmentName) {
        if(mFragmentNumbers.containsKey(fragmentName)) {
            return mFragmentNumbers.get(fragmentName);
        } else {
            return null;
        }
    }

    public Integer getFragmentNumber(Fragment fragment) {
        if (mFragmentNumbers.containsKey(fragment)) {
            return mFragmentNumbers.get(fragment);
        } else {
            return null;
        }
    }

    public String getFragmentName (Integer fragmentNumber) {
        if (mFragmentNames.containsKey(fragmentNumber)) {
            return mFragmentNames.get(fragmentNumber);
        } else {
            return null;
        }
    }


}
