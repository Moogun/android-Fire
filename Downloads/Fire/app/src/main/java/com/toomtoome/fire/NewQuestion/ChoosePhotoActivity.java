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

public class ChoosePhotoActivity extends AppCompatActivity {

    private static final String TAG = "ChoosePhotoActivity";
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_question_photo);

        mViewPager = (ViewPager) findViewById(R.id.question_photo_viewPager);

        if (checkPermissionsArray(Permissions.PERMISSIONS)) {
            Log.d(TAG, "onCreate: checkPermissionsArray");
            setupViewPager();
        } else {
            verifyPermissions(Permissions.PERMISSIONS);
            Log.d(TAG, "onCreate: verifyPermissions");
        }

    }

    private void setupViewPager() {
        Log.d(TAG, "setupViewPager: for fragments ");
        Bundle bundle = new Bundle();
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(), bundle);
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new CameraFragment());


        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
        tabLayout.getTabAt(1).setText(getString(R.string.camera));
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

        int permissionRequest = ActivityCompat.checkSelfPermission(ChoosePhotoActivity.this, permission);

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
                ChoosePhotoActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    public int getCurrentTabNumber(){
        return mViewPager.getCurrentItem();
    }
}
