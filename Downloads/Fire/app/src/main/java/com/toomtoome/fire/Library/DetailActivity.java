package com.toomtoome.fire.Library;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toomtoome.fire.Dialog.ConfirmPasswordDialog;
import com.toomtoome.fire.Dialog.ConfirmRemoveDialog;
import com.toomtoome.fire.Model.Chapter;
import com.toomtoome.fire.Model.Course;
import com.toomtoome.fire.Profile.EditProfileFragment;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Utils.FirebaseMethods;
import com.toomtoome.fire.Utils.SectionsPagerAdapter;

import java.util.ArrayList;

/**
 * Created by moogunjung on 11/21/17.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private Context mContext;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String uid;
    private ArrayList<Chapter> mChapters;

    private String mInstructorId;
    private String mInstructorName;
    private String mInstructorProfileImageUrl;

    private FirebaseMethods mFirebaseMethods;

    private Course mCourse;

    private ImageView mCourseImage;
    private TextView mTitle, mInstructor;
    private String mChapterKey;

    private Bundle bundlePassed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate: ");

        mContext = DetailActivity.this;

        bundlePassed = getIntent().getExtras();
        mCourse = bundlePassed.getParcelable("selectedCourse");
        mInstructorId = bundlePassed.getString("instructorId");
        mInstructorName = bundlePassed.getString("instructorName");
        mInstructorProfileImageUrl = bundlePassed.getString("instructorProfile");
        //Log.d(TAG, "get course Id " + mCourse.getId() + mCourse.getInstructorId());

        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to 'ProfileActivity'");
                backButtonPressed();
            }
        });

        ImageView more = (ImageView) findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: more " + " do some to remove this course");

                String title = getString(R.string.remove_course);
                String msg = getString(R.string.remove_message);
                String yes = "yes";
                String no = "no";

                AlertDialog dialog = makeDialog(title, msg, yes, no);
            }
        });


        setupToolbar();
        setupCourseInfo(mCourse);
        setupViewPager(mCourse);
        setupFirebaseAuth(mCourse);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backButtonPressed();
    }

    private void setupToolbar() {

    }

    private void backButtonPressed() {
        Intent intent = new Intent(getApplicationContext(), CourseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private void setupCourseInfo(Course course) {

        mTitle = findViewById(R.id.courseTitle);
        mCourseImage = findViewById(R.id.courseImage);
        mInstructor = findViewById(R.id.instructor);

        mTitle.setText(course.getTitle());
        Glide.with(this).load(course.getCourseImageUrl()).into(mCourseImage);

        mInstructor.setText(mInstructorName);

    }

    private void setupViewPager(Course course) {

        Bundle bundleToPass = new Bundle();
        bundleToPass.putString("courseId", course.getId());
        bundleToPass.putString("instructorId", mInstructorId);

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(), bundleToPass);
        adapter.addFragment(new NoteFragment());
        adapter.addFragment(new ChapterFragment());
        adapter.addFragment(new QnaFragment());

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(getString(R.string.preview_note));
        tabLayout.getTabAt(1).setText(getString(R.string.preview_chapter));
        tabLayout.getTabAt(2).setText(getString(R.string.preview_question));

    }

    /**
     * Firebase ------------------------------------------------------------------------
     */

    private void setupFirebaseAuth(Course course) {

        //FIREBASE
        mFirebaseMethods = new FirebaseMethods(mContext);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();


        Log.d(TAG, "setupFirebaseAuth: ");
        Log.d(TAG, "Course Key: " + mCourse.getId());
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: current user" + user.getUid());
                    uid = mAuth.getCurrentUser().getUid();

                } else {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private AlertDialog makeDialog(String title, String msg, CharSequence yes, CharSequence no) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setPositiveButton(yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: yy ");
                mFirebaseMethods.removeCourse(mCourse.getId(), uid);
                backButtonPressed();
            }
        });

        dialog.setNegativeButton(no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: nn");
            }
        });

        return dialog.show();
    }

}
