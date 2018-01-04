package com.toomtoome.fire.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.toomtoome.fire.Model.Course;
import com.toomtoome.fire.Model.Question;
import com.toomtoome.fire.Model.User;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Search.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by moogunjung on 11/25/17.
 */

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";
    private Context mContext;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userId;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private StorageReference mStorageReference;

    //var
    private double mPhotoUploadProgress = 0;

    public FirebaseMethods(Context mContext) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        this.mContext = mContext;

        if(mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        }
    }

    public void saveQuestion(String chapterId, String questionText) {

        Question newQuestion = new Question();

        String questionId = mRef.child("questions").push().getKey();

        //newQuestion.setUid();

        newQuestion.setChapterId(chapterId);
        newQuestion.setText(questionText);

        long createdAtLong = System.currentTimeMillis();
        Double createdAtDouble = (double) createdAtLong;
        newQuestion.setCreatedAt(createdAtDouble);

        mRef.child("questions").child(questionId).setValue(newQuestion);

    }


    public void addCourse(String courseId, String uid) {
        Log.d(TAG, "addCourse: " + courseId);

        Map<String, Object> followingUpdates = new HashMap<>();
        followingUpdates.put("followingCourses/" + uid + "/" + courseId, 1);
        followingUpdates.put("courses/" + courseId + "/" + "attendee/" + uid, 1);
        mRef.updateChildren(followingUpdates);

    }

    public void removeCourse(String courseId, String uid) {
        Log.d(TAG, "removeCourse: " + courseId + uid);

        Map<String, Object> followingUpdates = new HashMap<>();
        followingUpdates.put("followingCourses/" + uid + "/" + courseId, 0);
        followingUpdates.put("courses/" + courseId + "/" + "attendee/" + uid, 0);
        mRef.updateChildren(followingUpdates);
    }

    public User getUser(DataSnapshot dataSnapshot) {
        Log.d(TAG, "getUser: retrieving user info from Firebase");

        User user = new User();
        user.setEmail(dataSnapshot.getValue(User.class).getEmail());
        user.setUsername(dataSnapshot.getValue(User.class).getUsername());
        user.setProfileImageUrl(dataSnapshot.getValue(User.class).getProfileImageUrl());

        Log.d(TAG, "getUser: " + user);
        return user;
    }

    public void registerNewEmail (final String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "onComplete: complete with email " + task.isSuccessful() );

                if(!task.isSuccessful()) {
                    Toast.makeText(mContext, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                } else {
                    userId = mAuth.getCurrentUser().getUid();
                    Log.d(TAG, "onComplete: Authstate changed: " + userId);
                }
            }
        });
    }

    public void addUser(String email, String username, String profileImageUrl) {
        Log.d(TAG, "addUser: " + email + username + profileImageUrl);

        User user = new User(email, profileImageUrl, username);
        mRef.child("users").child(userId).setValue(user);
    }

    public void updateUsername(String username) {
        Log.d(TAG, "updateUsername to: " + username);
        mRef.child("users").child(userId).child("username").setValue(username);
    }

    /**
     * update the email to user node only? without authenticating the new email address  for log in?
     * @param email
     */
    public void updateEmail(String email) {
        Log.d(TAG, "updateEmail: updating email to " + email);
        mRef.child("users").child(userId).child("email").setValue(email);
    }

}
