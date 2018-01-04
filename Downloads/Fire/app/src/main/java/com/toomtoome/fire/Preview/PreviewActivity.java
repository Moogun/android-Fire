package com.toomtoome.fire.Preview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toomtoome.fire.Login.LoginActivity;
import com.toomtoome.fire.Model.Chapter;
import com.toomtoome.fire.Model.Course;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Search.SearchActivity;
import com.toomtoome.fire.Utils.ChapterAdapter;
import com.toomtoome.fire.Utils.FirebaseMethods;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by moogunjung on 12/11/17.
 */

public class PreviewActivity extends AppCompatActivity {

    private static final String TAG = "PreviewActivity";
    private Context mContext = PreviewActivity.this;

    private Bundle bundlePassed;
    private Course mCourse;
    private String mInstructorName;
    private String mInstructorProfileImageUrl;

    private ImageView backArrow, courseImage, instructorImage;
    private TextView title, instructorName;
    private Button add; 
    
    private ArrayList<Chapter> mChapters;
    private ChapterAdapter mAdapter;

    private RecyclerView recyclerView;

    // FIREBASE

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseMethods mFirebaseMethods;
    private String uid;
    private String mCourseKey;
    private String mChapterKey;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        mFirebaseMethods = new FirebaseMethods(mContext);

        //bundle sent user obj but this bundle dosen't have one
        bundlePassed = getIntent().getExtras();
        mCourse = bundlePassed.getParcelable("selectedCourse");
        mCourseKey = mCourse.getId();
        mInstructorName = bundlePassed.getString("instructorName");
        mInstructorProfileImageUrl = bundlePassed.getString("instructorProfile");
        uid = bundlePassed.getString("uid");
        Log.d(TAG, "onCreate: " + uid);


        backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to 'ProfileActivity'");
                backButtonPressed();
            }
        });

        setupCourseInfo(mCourse, mInstructorName, mInstructorProfileImageUrl);

        mChapters = new ArrayList<>();
        mAdapter = new ChapterAdapter(mChapters);

        /**
         * RecyclerView Settings
         */

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        recyclerView.setAdapter(mAdapter);
        snapHelper.attachToRecyclerView(recyclerView);

        setupChapters(mCourse, mCourseKey);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backButtonPressed();
    }

    private void backButtonPressed() {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private void setupCourseInfo(Course course, String name, String profileImageUrl) {

        courseImage = findViewById(R.id.courseImage);
        title = findViewById(R.id.courseTitle);
        instructorName = findViewById(R.id.instructor);
        instructorImage = findViewById(R.id.profileImage);

        Glide.with(this).load(course.getCourseImageUrl()).into(courseImage);
        title.setText(course.getTitle());

        instructorName.setText(mInstructorName);
        Glide.with(this).load(mInstructorProfileImageUrl).into(instructorImage);

        add = findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ready to add to firebase");
                add.setText("Added");
                mFirebaseMethods.addCourse(mCourseKey, uid);
            }
        });

    }

    private void setupChapters(Course course, String mCourseKey) {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        mRef.child("courses")
                .child(mCourseKey).child("chapters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> chapterKeys = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mChapterKey = ds.getKey();

                    ValueEventListener valueEventListener = mRef.child("chapters").child(mChapterKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Log.d(TAG, "onDataChange: value " + dataSnapshot.getValue());

                            Chapter chapter = new Chapter();
                            chapter.setId(dataSnapshot.getKey());

                            try {
                                chapter.setTitle(dataSnapshot.getValue(Chapter.class).getTitle());
                                chapter.setChapterDescription(dataSnapshot.getValue(Chapter.class).getChapterDescription());
                            } catch (NullPointerException e) {
                                Log.d(TAG, "onDataChange: e " + e);
                            }

                            mChapters.add(chapter);
                            mAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
