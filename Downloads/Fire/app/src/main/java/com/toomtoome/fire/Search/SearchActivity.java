package com.toomtoome.fire.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.toomtoome.fire.Library.CourseActivity;
import com.toomtoome.fire.Login.LoginActivity;
import com.toomtoome.fire.Model.Chapter;
import com.toomtoome.fire.Model.Course;
import com.toomtoome.fire.Model.User;
import com.toomtoome.fire.Preview.PreviewActivity;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Utils.BottomNavigationViewHelper;
import com.toomtoome.fire.Utils.CourseAdapter;
import com.toomtoome.fire.Utils.FirebaseMethods;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by moogunjung on 12/21/17.
 */

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private static final int ACTIVITY_NUM = 0;

    private Context mContext = SearchActivity.this;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String uid;
    private String mCourseKey;
    private HashMap<String, Integer> mChapterList;

    private ArrayList<Course> mCourses;
    private FirebaseMethods mFirebaseMethods;

    private TextView mTitle, mSubTitle, mInstructor;
    private ListView listView;

    CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        mContext = SearchActivity.this;
        mFirebaseMethods = new FirebaseMethods(mContext);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        //array items
        mCourses = new ArrayList<>();
        mChapterList = new HashMap<>();

        mTitle = (TextView) findViewById(R.id.courseTitle);
        mSubTitle = (TextView) findViewById(R.id.subTitle);
        mInstructor = (TextView) findViewById(R.id.instructor);

        listView = (ListView) findViewById(R.id.courseList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(SearchActivity.this, PreviewActivity.class);

                Bundle args = new Bundle();
                args.putParcelable("selectedCourse", mCourses.get(i));
                args.putString("instructorProfile", mCourses.get(i).getUser().getProfileImageUrl());
                args.putString("instructorName", mCourses.get(i).getUser().getUsername());
                args.putString("uid", uid);

                intent.putExtras(args);
                startActivity(intent);
            }
        });

        setupCourseList();
        setupBottomNavigationView();
        setupFirebaseAuth();
        Log.d(TAG, "onCreate: ");

    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: ");

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);

        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

    /**
     * Firebase Auth ------------------------------------------------------------------------
     */

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: ");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: 000000" + user.getUid());
                    uid = mAuth.getCurrentUser().getUid();

                } else {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    //Faild test Dec 23 00: 26 as mFirebaseMethods.getCourseList()) produced 0 item. Need to come up how to pass course value wihtin fethcing block to outside
//    private void setupCourseList() {
//        Log.d(TAG, "setupCourseList: " + mFirebaseMethods.getCourseList());
//        mCourses = mFirebaseMethods.getCourseList();
//        adapter = new CourseAdapter(SearchActivity.this, R.layout.layout_search_course_item, mCourses);
//        listView.setAdapter(adapter);
//    }

    private void setupCourseList() {
        Log.d(TAG, "setupCourseList: ");

        mRef.child("courses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: what is a key " + dataSnapshot.getKey());
                Log.d(TAG, "onChildAdded: get value " + dataSnapshot.getValue());

                final Course course = new Course();
                course.setId(dataSnapshot.getKey());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.getKey().equals("info")) {

                        Log.d(TAG, "onChildAdded: snapshot " + snapshot.getValue());
                        Log.d(TAG, "onChildAdded: instructor " + snapshot.child("instructorId"));

                        course.setTitle(snapshot.getValue(Course.class).getTitle());
                        course.setCourseImageUrl(snapshot.getValue(Course.class).getCourseImageUrl());

                        String instructorId = (String) snapshot.child("instructorId").getValue();
                        Log.d(TAG, "onChildAdded: " + instructorId);
                        mRef.child("users").child(instructorId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = mFirebaseMethods.getUser(dataSnapshot);
                                course.setUser(user);
                                Log.d(TAG, "onDataChange: setUser " + course.getUser().getUsername());
                                mCourses.add(course);
                                adapter = new CourseAdapter(SearchActivity.this, R.layout.layout_search_course_item, mCourses);
                                listView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    } else if (snapshot.getKey().equals("chapters")) {
                        Log.d(TAG, "onChildAdded: snapshot " + snapshot.getValue());
                        int i = 0;
                        for (DataSnapshot child : dataSnapshot.child("chapters").getChildren()) {
                            mChapterList.put(child.getKey(), i);
                            i += 1;
                            course.setChapters(mChapterList);
                            Log.d(TAG, "onDataChange:  " + course.getId() + mChapterList);

                            //not sure if this will update the adpater with the mChapterList
                            // produced an null pointer error
                            //adapter.notifyDataSetChanged();
                        }
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + "no need to gets called");
    }
}