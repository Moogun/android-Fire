package com.toomtoome.fire.QuestionList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toomtoome.fire.Model.Question;
import com.toomtoome.fire.Model.User;
import com.toomtoome.fire.NewQuestion.NewQuestionActivity;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Utils.FirebaseMethods;
import com.toomtoome.fire.Utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by moogunjung on 12/8/17.
 */

public class QuestionListFragment extends Fragment {

    private static final String TAG = "QuestionListFragment";

    private String chapterId;
    private HashMap<String, Integer> mQuestionList; // receiving map data from the question activity
    private List<Question> mQuestions; // question type list to pass on to the adampter
    private QuestionAdapter mAdapter; // adapter

    private HashMap<String, Integer> mAnswers;

    private RecyclerView recyclerView;

    private TextView newQuestion, comment, likes, scraps;
    private ImageView backArrow;

    private User questioner;


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

        final Bundle args = getArguments();
        chapterId = args.getString("chapterId");
        Log.d(TAG, "onCreate: chapterId" + chapterId);
        mQuestionList = (HashMap<String, Integer>) args.getSerializable("questionList");
        //Log.d(TAG, "onCreate: " + mQuestionList); //.entrySet()
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        /**
         * Toolbar
         */

        backArrow = (ImageView) view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        newQuestion = (TextView) view.findViewById(R.id.newQuestion);
        newQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Here this comes", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), NewQuestionActivity.class);
                intent.putExtra("chapterId", chapterId);
                startActivity(intent);
            }
        });

        /**
         * Recycler view settings
         */
        mQuestions= new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new QuestionAdapter(mQuestions);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        recyclerView.setAdapter(mAdapter);
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Bundle args = new Bundle();
                        args.putParcelable("question", mQuestions.get(position));

                        Fragment fragment = new QuestionDetailFragment();
                        fragment.setArguments(args);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                        transaction.replace(R.id.container, fragment);
                        transaction.addToBackStack("Question detail Fragment");
                        transaction.commit();
                    }
                })
        );

        /**
         * Recycler View items
         */

        comment = (TextView) view.findViewById(R.id.comment);
        likes = (TextView) view.findViewById(R.id.like);
        scraps = (TextView) view.findViewById(R.id.follow);

        /**
         *  Gettings data
         */

        setupQuestions();
        return view;
    }

    /**
     * FIREBASE
     */

    private void setupQuestions() {
        //Log.d(TAG, "setupQuestions: 1");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        questioner = new User();

        if (mQuestionList != null) {
            Set set = (Set) mQuestionList.entrySet();
            Iterator i = (Iterator) set.iterator();

            while (i.hasNext()) {
                Map.Entry qId = (Map.Entry)i.next();
                String id = (String) qId.getKey();
                //Log.d(TAG, "setupQuestions: key " + id);

                mRef.child("questions").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: " + dataSnapshot);

                        final Question q = new Question();
                        q.setId(dataSnapshot.getKey());
                        q.setUid(dataSnapshot.getValue(Question.class).getUid());
                        q.setText(dataSnapshot.getValue(Question.class).getText());;
                        q.setCreatedAt(dataSnapshot.getValue(Question.class).getCreatedAt());

                        mAnswers = new HashMap<>();

                        int i = 0;
                        for (DataSnapshot child : dataSnapshot.child("answers").getChildren()) {
                            //Log.d(TAG, "onDataChange: child " + child.getKey());
                            mAnswers.put(child.getKey(), i);
                            //Log.d(TAG, "onDataChange: answers child " + child.getKey());
                            i += 1;
                            //Log.d(TAG, "onDataChange: answers i " + i);
                            q.setAnswers(mAnswers);
                            //Log.d(TAG, "onDataChange: answers " + q.getAnswers());
                        }

                        mRef.child("users").child(q.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG, "onDataChange: " + dataSnapshot);
                                questioner.setUsername(dataSnapshot.getValue(User.class).getUsername());
                                questioner.setProfileImageUrl(dataSnapshot.getValue(User.class).getProfileImageUrl());

                                q.setUsername(questioner.getUsername());
                                q.setProfileImageUrl(questioner.getProfileImageUrl());

                                mQuestions.add(q);
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


    }
}

