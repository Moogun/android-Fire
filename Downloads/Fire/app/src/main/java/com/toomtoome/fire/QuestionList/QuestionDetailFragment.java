package com.toomtoome.fire.QuestionList;

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
import android.widget.Button;
import android.widget.EditText;
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
import com.toomtoome.fire.Model.Answer;
import com.toomtoome.fire.Model.Question;
import com.toomtoome.fire.Model.User;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Utils.FirebaseMethods;
import com.toomtoome.fire.Utils.SquareImageView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by moogunjung on 12/8/17.
 */


public class QuestionDetailFragment extends Fragment {

    private static final String TAG = "QuestionDetailFragment";

    private HashMap<String, Integer> mAnswerList; // holding answer list from q list fragment
    private ArrayList<Answer> mAnswers; // hold answers to popuate the recycler view
    //private Answer answer; // save 1 answer for mAnswers

    private AnswerAdapter mAdapter;
    private RecyclerView recyclerView;

    private Question question;

    private User answerer;

    private TextView username, createdAt, questionText;
    private ImageView profileImage, questionImage;
    private EditText editText;
    private Button button;


    //FIREBASE

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String uid;
    private FirebaseMethods mFirebaseMethods;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle received = getArguments();
        question = received.getParcelable("question");
        Log.d(TAG, "onCreate: question recieved " + question.getText() + question.getUsername() + question.getProfileImageUrl() + question.getAnswers());
        mAnswerList = question.getAnswers();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_detail, container, false);

        /**
         * Toolbar
         */

        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to question list");

                ((QuestionListActivity)getActivity()).getSupportFragmentManager().popBackStack();
            }
        });


        /**
         * Question
         */

        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        username = (TextView) view.findViewById(R.id.username);
        createdAt = (TextView) view.findViewById(R.id.createdAt);

        questionImage = (SquareImageView) view.findViewById(R.id.question_image);
        questionText = (TextView) view.findViewById(R.id.question_text);

        setupQuestion();


        /**
         * Recycler view settings
         */
        mAnswers = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new AnswerAdapter(mAnswers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        recyclerView.setAdapter(mAdapter);
        snapHelper.attachToRecyclerView(recyclerView);

        setupAnswer();

        /**
         * Submiting Answer
         */

        editText = (EditText) view.findViewById(R.id.editAnswer);
        button = (Button) view.findViewById(R.id.buttonSend);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = editText.getText().toString();
                Log.d(TAG, "onClick: " + editText.getText().toString());
                Log.d(TAG, "onClick: " + uid);

                Answer newAnswer = new Answer();
                newAnswer.setUid(uid);
                newAnswer.setText(text);
                newAnswer.setQuestionId(question.getId());
                //newAnswer.setCreatedAt(ServerValue.TIMESTAMP);

                Log.d(TAG, "onClick: " + newAnswer.getUid() + newAnswer.getText() + newAnswer.getQuestionId());

                sendAnswer(newAnswer);

            }
        });


        setupFirebaseAuth();

        return view;
    }

    /**
     * FIREBASE
     */

    private void setupFirebaseAuth() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: " + user.getUid());
                    uid = mAuth.getCurrentUser().getUid();

                } else {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
            }
        };
    }

    private void setupQuestion() {

        Glide.with(getContext()).load(question.getProfileImageUrl()).into(profileImage);
        username.setText(question.getUsername());

        Double createdAtDouble = question.getCreatedAt() * 1000;
        long time = (new Double(createdAtDouble)).longValue();
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a HH:mm", Locale.KOREA);
        sdf.setTimeZone(TimeZone.getTimeZone("ROK")); //google 'android list of timezones'
        String newFormat = sdf.format(date);

        createdAt.setText(newFormat);

        if (question.getqImageUrl() != null) {
            Glide.with(getContext()).load(question.getqImageUrl()).into(questionImage);
        } else {
            questionImage.setVisibility(View.GONE);
        }

        //Log.d(TAG, "setupQuestion: q text" + question.getText());
        questionText.setText(question.getText());
    }


    private void setupAnswer() {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        //answer = new Answer();

        //Log.d(TAG, "setupQuestions: " + mQuestionList.entrySet());
        Set set = (Set) mAnswerList.entrySet();
        Iterator i = (Iterator) set.iterator();

        answerer = new User();

        while (i.hasNext()) {
            Map.Entry aId = (Map.Entry) i.next();
            String id = (String) aId.getKey();
            Log.d(TAG, "setupAnswer: id " + id);

            mRef.child("answers").child(question.getId()).child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.d(TAG, "onDataChange: answer" + dataSnapshot.getKey() + dataSnapshot.getValue());
                    final Answer a = new Answer();

                    a.setUid(dataSnapshot.getValue(Answer.class).getUid());
                    a.setText(dataSnapshot.getValue(Answer.class).getText());
                    Log.d(TAG, "onDataChange: createdAt " + dataSnapshot.getValue(Answer.class).getCreatedAt());
                    //a.setCreatedAt(dataSnapshot.getValue(Answer.class).getCreatedAt());

                    //TimeConverter converter = new TimeConverter();

                    Log.d(TAG, "onDataChange: time converter " + dataSnapshot.getValue(Answer.class).getCreatedAt().intValue());
                    DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE);


                    mRef.child("users").child(a.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Log.d(TAG, "onDataChange: answerer" + dataSnapshot);
                            answerer.setUsername(dataSnapshot.getValue(User.class).getUsername());
                            answerer.setProfileImageUrl(dataSnapshot.getValue(User.class).getProfileImageUrl());

                            a.setUsername(answerer.getUsername());
                            a.setProfileImageUrl(answerer.getProfileImageUrl());

                            mAnswers.add(a);
                            mAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, "onCancelled: " + databaseError);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: " + databaseError);
                }
            });
        }
    }

    private void sendAnswer(Answer answer) {

        Log.d(TAG, "sendAnswer: uid " + uid);

        mRef = mDatabase.getReference();
        final String key = mRef.child("answers").child(question.getId()).push().getKey();
        Log.d(TAG, "sendAnswer: + new child key " + key);
        Log.d(TAG, "sendAnswer: " + answer.getUid() + answer.getQuestionId());
        //mRef.child("answers").child(question.getId()).child(key).setValue();

    }



}
