package com.toomtoome.fire.NewQuestion;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.toomtoome.fire.R;
import com.toomtoome.fire.Utils.Permissions;
import com.toomtoome.fire.Utils.SectionsPagerAdapter;

/**
 * Created by moogunjung on 12/18/17.
 */

public class QuestionPhotoActivity extends AppCompatActivity {

    private static final String TAG = "QuestionPhotoActivity";
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_question_photo);

        if (checkPermissionsArray(Permissions.PERMISSIONS)) {
            setupViewPager();
        } else {

            verifyPermissions(Permissions.PERMISSIONS);
        }

    }

    private void setupViewPager() {
        Bundle bundle = new Bundle();
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(), bundle);
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
        tabLayout.getTabAt(1).setText(getString(R.string.photo));
    }

    public boolean checkPermissionsArray(String[] permissions) {
        Log.d(TAG, "checkPermissionsArray: ");
        for (int i =0; i<permissions.length; i++) {
            String check = permissions[i];
            if(!checkPermissions(check)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkPermissions(String permission) {
        Log.d(TAG, "checkPermissions: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(QuestionPhotoActivity.this, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        } else {
            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }


    private void verifyPermissions(String[] permissions) {
        Log.d(TAG, "verifyPermissions: ");
        ActivityCompat.requestPermissions(
                QuestionPhotoActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    public int getCurrentTabNumber(){
        return mViewPager.getCurrentItem();
    }
}
