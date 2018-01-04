package com.toomtoome.fire.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.toomtoome.fire.Dialog.ConfirmPasswordDialog;
import com.toomtoome.fire.Model.User;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Utils.FirebaseMethods;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by moogunjung on 11/23/17.
 */

public class EditProfileFragment extends Fragment implements ConfirmPasswordDialog.OnConfirmPasswordListener {

    @Override
    public void onComfirmPassword(String password) {

        Log.d(TAG, "onComfirmPassword: " + password);
        bar.setVisibility(View.VISIBLE);

        AuthCredential credential = EmailAuthProvider.getCredential(mAuth.getCurrentUser().getEmail(),password);

        ///////////////////// Prompt the user to re-provide their sign-in credentials
        mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: user r e authenticated");

                    ///////////////////////check to see if the email is not already present in the database
                    mAuth.fetchProvidersForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                            if (task.isSuccessful()) {
                                try {
                                    if (task.getResult().getProviders().size() == 1) {

                                        bar.setVisibility(View.GONE);

                                        Log.d(TAG, "onComplete: that email is already in use");
                                        Toast.makeText(getActivity(), getString(R.string.email_in_use), Toast.LENGTH_SHORT).show();
                                    } else {

                                        bar.setVisibility(View.GONE);
                                        Log.d(TAG, "onComplete: The email is available");

                                        mAuth.getCurrentUser().updateEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG, "onComplete: email updated");
                                            Toast.makeText(getActivity(), getString(R.string.email_updated), Toast.LENGTH_SHORT).show();
                                            mFirebaseMethods.updateEmail(mEmail.getText().toString());
                                            }
                                        });

                                    }
                                } catch ( NullPointerException e ) {
                                    Log.d(TAG, "onComplete: Null Pointer Exception" + e.getMessage());
                                }
                            }
                        }
                    });
                } else {
                    bar.setVisibility(View.GONE);
                    Log.d(TAG, "onComplete: re- authentication failed");
                    Toast.makeText(getActivity(), getString(R.string.wrong_password), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static final String TAG = "EditProfileFragment";

    private EditText mUsername, mEmail;
    private CircleImageView mProfilePhoto;
    private TextView mChangeProfilePhoto;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String uid;
    private FirebaseMethods mFirebaseMethods;

    private User mUser;
    private String append;

    private ProgressBar bar;

    public interface OnBackArrowSelectedListener{
        void onBackArrowSelected();
    }
    OnBackArrowSelectedListener mOnBackArrowSelectedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        Log.d(TAG, "onCreateView: ");

        /**
         * Toolbar
         */
        
        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to 'ProfileActivity'");
                mOnBackArrowSelectedListener.onBackArrowSelected();
                //getActivity().finish();
            }
        });

        ImageView checkmark = (ImageView) view.findViewById(R.id.saveChanges);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save changes.");
                saveProfileSettings();
            }
        });

        
        /**
         * Center
         */

        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        mChangeProfilePhoto = (TextView) view.findViewById(R.id.changeProfilePhoto);
      
        mUsername = (EditText) view.findViewById(R.id.username);
        mEmail = (EditText) view.findViewById(R.id.email);

        //Progress Bar
        bar = (ProgressBar) view.findViewById(R.id.progressBar);
        bar.setVisibility(View.GONE);

        mFirebaseMethods = new FirebaseMethods(getActivity());
        setupFirebaseAuth();
        return view;
    }

    @Override
    public void onAttach(Context context) {

        try {
            mOnBackArrowSelectedListener = (OnBackArrowSelectedListener) getActivity();
        } catch (ClassCastException e){
            Log.e(TAG, "onAttach: class exception"+ e.getMessage() );
        }
        super.onAttach(context);
    }

    /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: ");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        uid = mAuth.getCurrentUser().getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
            }
        };

        mRef.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setupProfilewidgets(mFirebaseMethods.getUser(dataSnapshot));
                Log.d(TAG, "onDataChange: " + dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupProfilewidgets(User user) {
        Log.d(TAG, "setupProfilewidgets: " + user);
        mUsername.setText(user.getUsername());
        mEmail.setText(user.getEmail());
        if (user.getProfileImageUrl().equals("")) {
            mProfilePhoto.setImageResource(R.drawable.ic_profile);
        } else {
            Glide.with(mProfilePhoto).load(user.getProfileImageUrl()).into(mProfilePhoto);
        }

        mChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: changing profile photo");
            }
        });

        mUser = user;
    }

    /**
     * Editing profile info
     */

    private void saveProfileSettings() {
        Log.d(TAG, "saveProfileSettings: ");
        final String username = mUsername.getText().toString();
        final String email = mEmail.getText().toString();

        mRef = mDatabase.getReference();

        //case1: if the user made a change to their username
        if(!mUser.getUsername().equals(username)) {
            checkIfUsernameExists(username);
        }

        //case2: if the user made a change to their email
        if(!mUser.getEmail().equals(email)) {
            Log.d(TAG, "email change");
            ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
            dialog.show(getFragmentManager(), getString(R.string.confirm_password_dialog));
            dialog.setTargetFragment(EditProfileFragment.this, 1);
        }

        //step1) Reauthenticate
        //          -Confirm the password and email
        // step2) check if the email already is registered
        //          -'fetchProvidersForEmail(String email)'
        // step3) change the email
        //          -submit the new email to the database and authentication
//        }
    }

    private void checkIfUsernameExists(final String username) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("users").orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()) {
                    mFirebaseMethods.updateUsername(username);
                    Toast.makeText(getActivity(), getString(R.string.username_saved), Toast.LENGTH_SHORT).show();

                }

                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {
                    if(singleSnapshot.exists()) {
                        Log.d(TAG, "check if the username exists FOUND A MATCH" + singleSnapshot);
                        Toast.makeText(getActivity(), getString(R.string.username_exists) , Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        final String email = mEmail.getText().toString();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
