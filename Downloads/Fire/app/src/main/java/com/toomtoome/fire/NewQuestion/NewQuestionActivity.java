package com.toomtoome.fire.NewQuestion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.toomtoome.fire.Library.CourseActivity;
import com.toomtoome.fire.Login.LoginActivity;
import com.toomtoome.fire.Model.Question;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Utils.FirebaseMethods;

import java.sql.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by moogunjung on 12/9/17.
 */

public class NewQuestionActivity extends AppCompatActivity {

    private static final String TAG = "NewQuestionActivity";

    private static final int  CAMERA_REQUEST_CODE = 5;

    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String uid;
    private String chapterId;

    private FirebaseMethods mFirebaseMethods;

    private ImageView backArrow;
    private TextView save;
    private ImageView photo;
    private EditText questionText;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        Log.d(TAG, "onCreate: " + getIntent().getStringExtra("chapterId"));
        chapterId = getIntent().getStringExtra("chapterId");

        mContext = NewQuestionActivity.this;
        mFirebaseMethods = new FirebaseMethods(mContext);

        mAuth = FirebaseAuth.getInstance();
        setupFirebaseAuth(); // shared preference?
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save = (TextView) findViewById(R.id.saveQuestion);
        save.setClickable(false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Editable question = questionText.getText();
                String questionText = question.toString();
                mFirebaseMethods.saveQuestion(chapterId, questionText);
                //progress
                //done message
                Log.d(TAG, "onClick: uid " + uid);
                Toast.makeText(mContext, R.string.question_saved, Toast.LENGTH_SHORT).show();

                ((EditText) findViewById(R.id.question_text)).setText("");

                //Todos
                /**
                 * question is not updated, probablly the questions are not being fetched as a list, need to fix question fethcing node
                 * use shared preference to save uid and use globally
                 * permission denied? why
                 * add photo
                 * add progress bar

                 * DONE add toast message
                 * DONE reset edit text field
                 * DONE save enable disable
                 */
            }
        });

        photo = (ImageView) findViewById(R.id.question_image);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //error permision rejecteed?
                Log.d(TAG, "onClick: starting either camera or photo fragment");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            }
        });

        questionText = (EditText) findViewById(R.id.question_text);
        questionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: " + charSequence);

                if (charSequence.length() > 0) {
                    save.setClickable(true);

                } else {
                    save.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    //Firebase
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: ");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: new question auth" + user.getUid());
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

}
