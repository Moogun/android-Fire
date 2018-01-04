package com.toomtoome.fire.Library;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toomtoome.fire.Model.Announcement;
import com.toomtoome.fire.Model.User;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Utils.AnnouncementAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moogunjung on 11/21/17.
 */

public class NoteFragment extends Fragment {

    private static final String TAG = "NoteFragment";

    private List<Announcement> announcementList = new ArrayList<>();
    private AnnouncementAdapter mAdapter;

    private RecyclerView recyclerView;

    // FIREBASE

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String uid;
    private String mCourseKey;
    private String mInstructorId;
    private User instructor;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        final String courseId = args.getString("courseId");
        final String instructorId = args.getString("instructorId");

        mCourseKey = courseId;
        mInstructorId = instructorId;

        setupNote(mCourseKey, mInstructorId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_note, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mAdapter = new AnnouncementAdapter(announcementList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        recyclerView.setAdapter(mAdapter);
        snapHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: ");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: user Id" + user.getUid());
                    uid = mAuth.getCurrentUser().getUid();
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
            }
        };

    }

    private void setupNote(String courseId, final String instructorId) {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        instructor = new User();

        mRef.child("courses").child(courseId).child("announcement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange note: " + dataSnapshot);

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: note " + ds);

                    mRef.child("announcement").child(ds.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onDataChange: note annouce" + dataSnapshot);

                            final Announcement a = new Announcement();
                            a.setTitle(dataSnapshot.getValue(Announcement.class).getTitle());
                            //a.setAnImgUrl(dataSnapshot.getValue(Announcement.class).getAnImgUrl());
                            //createdAt

                            mRef.child("users").child(instructorId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Log.d(TAG, "onDataChange: answerer" + dataSnapshot);
                                    instructor.setUsername(dataSnapshot.getValue(User.class).getUsername());
                                    instructor.setProfileImageUrl(dataSnapshot.getValue(User.class).getProfileImageUrl());

                                    a.setUsername(instructor.getUsername());
                                    a.setProfileImageUrl(instructor.getProfileImageUrl());

                                    Log.d(TAG, "onDataChange: an text" + a.getTitle());
                                    announcementList.add(a);
                                    mAdapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

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
