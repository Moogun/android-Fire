package com.toomtoome.fire.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toomtoome.fire.R;
import com.toomtoome.fire.Utils.SectionsStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by moogunjung on 11/23/17.
 */

public class AccountSettingsActivity extends AppCompatActivity implements EditProfileFragment.OnBackArrowSelectedListener {

    private static final String TAG = "AccountSettingsActivity";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext;

    //--------------------------------------------------------
    private RelativeLayout mRelLayout1;
    private ViewPager mViewPager;
    public SectionsStatePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        mContext = AccountSettingsActivity.this;
        /**
         * Toolbar
         */
        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to 'ProfileActivity'");
                finish();
            }
        });

        TextView text = (TextView) findViewById(R.id.text1);

        /**
         * Center
         */
        mViewPager = (ViewPager) findViewById(R.id.container);
        mRelLayout1= (RelativeLayout) findViewById(R.id.relLayout1);

        setupSettingsList();

        pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile));
        pagerAdapter.addFragment(new SignOutFragment(), getString(R.string.sign_out));
    }

    private void setupSettingsList() {

        ListView listView = (ListView) findViewById(R.id.lvAccountSettings);

        ArrayList<String> settings = new ArrayList();
        settings.add(getString(R.string.edit_profile));
        settings.add(getString(R.string.sign_out));

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, settings);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: navigating to settings");
                //setViewPager();
                setViewPager(position);
            }
        });
    }

    public void setViewPager(int fragmentNumber) {

            mRelLayout1.setVisibility(View.GONE);
            Log.d(TAG, "setViewPager: navigating to fragment #:" + fragmentNumber);
            mViewPager.setAdapter(pagerAdapter);
            mViewPager.setCurrentItem(fragmentNumber);

     }

    @Override
    public void onBackArrowSelected() {
        Log.d(TAG, "onBackArrowSelected: called from edit profile");
        mRelLayout1.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: 1");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 2");
    }
}

