package com.toomtoome.fire.Wordbook;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moogunjung on 12/13/17.
 */

public class
WordbookPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "WordbookPagerAdapter";

    int NUM_PAGES;
    List<Keyword> keywords;

    public WordbookPagerAdapter(FragmentManager fm, int numItems, ArrayList<Keyword> keywords) {
        super(fm);
        this.NUM_PAGES = numItems;
        this.keywords = keywords;
    }

    int startIndex;
    int endIndex;

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new WbListFragment();

        count(position);
        return WbListFragment.newInstance(position, count(position));
    }

    private ArrayList<Keyword> count(int position) {

        ArrayList<Keyword> sub;

        startIndex = 0 + position * 10;
        endIndex = 10 + position * 10;

        int lastIndex = keywords.size() % 10;

        if (endIndex > keywords.size()) {
            sub = new ArrayList<Keyword>(keywords.subList(startIndex, startIndex + lastIndex));
        } else {
            sub = new ArrayList<Keyword>(keywords.subList(startIndex, endIndex));
        }
            Log.d(TAG, "count: " + sub);
        return sub;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
//        return super.getItemPosition(object);
        return POSITION_NONE;
    }
}
