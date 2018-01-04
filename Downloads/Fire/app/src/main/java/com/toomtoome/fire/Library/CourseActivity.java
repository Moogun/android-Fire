package com.toomtoome.fire.Library;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.toomtoome.fire.Login.LoginActivity;
import com.toomtoome.fire.Model.Course;
import com.toomtoome.fire.Model.User;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Search.SearchActivity;
import com.toomtoome.fire.Utils.BottomNavigationViewHelper;
import com.toomtoome.fire.Utils.CourseAdapter;
import com.toomtoome.fire.Utils.FirebaseMethods;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {

    private static final String TAG = "CourseActivity";
    private Context mContext;
    private static final int ACTIVITY_NUM = 1;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String uid;
    private String mCourseKey;

    private ArrayList<Course> mCourses;
    private FirebaseMethods mFirebaseMethods;

    private ImageView backArrow;
    private TextView title, subTitle, instructor;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        mContext = CourseActivity.this;
        mFirebaseMethods = new FirebaseMethods(mContext);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        //array items
        mCourses = new ArrayList<>();

        backArrow = (ImageView) findViewById(R.id.backArrow);

        title = (TextView) findViewById(R.id.courseTitle);
        subTitle = (TextView) findViewById(R.id.subTitle);
        instructor = (TextView) findViewById(R.id.instructor);

        listView = (ListView) findViewById(R.id.courseList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(CourseActivity.this, DetailActivity.class);

                // Dec 26, 2017 Due to Parcelable not carrying User model, instructor id is getting passed by putString method
                Bundle args = new Bundle();
                args.putParcelable("selectedCourse", mCourses.get(i));
                args.putString("instructorId", mCourses.get(i).getInstructorId());
                args.putString("instructorProfile", mCourses.get(i).getUser().getProfileImageUrl());
                args.putString("instructorName", mCourses.get(i).getUser().getUsername());


                intent.putExtras(args);
                startActivity(intent);
            }
        });

        setupBottomNavigationView();
        setupFirebaseAuth();

        initFCM();

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
        uid = "";
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: 000000" + user.getUid());
                    uid = mAuth.getCurrentUser().getUid();
                    setupCourseList(uid);

                } else {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    private void setupCourseList(String uid) {

        //need to refactor for later use Dec 26

        // method name get following course
        // need to pass uid of current user
        // get course ids and loop under course node
        // get course item and then loop again for info data
        // get instructor with get User method
        // then set adapter

        mRef.child("followingCourses").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String courseStatus = snapshot.getValue().toString();
                    Log.d(TAG, "onDataChange: see removed course status" + courseStatus);
                    if (courseStatus.equals("0")) {
                        Log.d(TAG, "onDataChange: see removed false " + false);

                    } else {


                        mRef.child("courses").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //Log.d(TAG, "d snapshot: " + dataSnapshot);

                                final Course course = new Course();
                                course.setId(dataSnapshot.getKey());

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    if (snapshot.getKey().equals("info")) {

                                        course.setTitle(snapshot.getValue(Course.class).getTitle());
                                        course.setCourseImageUrl(snapshot.getValue(Course.class).getCourseImageUrl());

                                        String instructorId = (String) snapshot.child("instructorId").getValue();
                                        course.setInstructorId(snapshot.getValue(Course.class).getInstructorId());
                                        Log.d(TAG, "instructor Id: " + instructorId);

                                        mRef.child("users").child(instructorId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                User user = mFirebaseMethods.getUser(dataSnapshot);
                                                course.setUser(user);
                                                Log.d(TAG, "onDataChange: setUser " + course.getUser().getUsername());
                                                mCourses.add(course);

                                                CourseAdapter adapter = new CourseAdapter(CourseActivity.this, R.layout.layout_course_item, mCourses);
                                                listView.setAdapter(adapter);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    } else {
                                        Log.d(TAG, "nono ");
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    CourseAdapter adapter = new CourseAdapter(CourseActivity.this, R.layout.layout_course_item, mCourses);
                    listView.setAdapter(adapter);

                }
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

    /**
     * Messaging
     * @param token
     */

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("field_messaging_token")
                .setValue(token);
    }

    private void initFCM(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "initFCM: token: " + token);
        sendRegistrationToServer(token);
    }

}
