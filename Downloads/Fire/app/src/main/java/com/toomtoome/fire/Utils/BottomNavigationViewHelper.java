package com.toomtoome.fire.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.toomtoome.fire.Library.CourseActivity;
import com.toomtoome.fire.Profile.ProfileActivity;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Search.SearchActivity;

/**
 * Created by moogunjung on 11/3/17.
 */

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationHelper";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: 1");
        //came here
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);

    }

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view) {

        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "setupBottomNavigationView: 5");
                switch (item.getItemId()) {


                    case R.id.search:
                        Intent intent1 = new Intent(context, SearchActivity.class);
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;


                    case R.id.library:
                        Intent intent2 = new Intent(context, CourseActivity.class);
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.profile:
                        Intent intent3 = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent3);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                }

                return false;

            }
        });

    }
}