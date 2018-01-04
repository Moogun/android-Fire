package com.toomtoome.fire.Library;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toomtoome.fire.Dialog.ConfirmRemoveDialog;
import com.toomtoome.fire.Model.Chapter;
import com.toomtoome.fire.QuestionList.QuestionListActivity;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Utils.ChapterAdapter;
import com.toomtoome.fire.Utils.RecyclerTouchListener;
import com.toomtoome.fire.Wordbook.WordbookActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by moogunjung on 11/21/17.
 */

public class ChapterFragment extends Fragment {

    private static final String TAG = "ChapterFragment";

    private ArrayList<Chapter> mChapters;
    private ChapterAdapter mAdapter;

    private RecyclerView recyclerView;

    private TextView keyword;

    private HashMap<String, Integer> mKeywordIdList;
    private HashMap<String, Integer> mQuestionIdList;


    // FIREBASE

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String uid;
    private String mCourseKey;
    private String mChapterKey;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        final String courseId = args.getString("courseId");

        mCourseKey = courseId;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        mChapters = new ArrayList<>();
        mAdapter = new ChapterAdapter(mChapters);

        /**
         * RecyclerView Settings
         */

       recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        recyclerView.setAdapter(mAdapter);
        snapHelper.attachToRecyclerView(recyclerView);

        setupFirebaseAuth();

        /**
         * RecyclerView Click Listener
         */

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Toast.makeText(getActivity(), "Hello" + position, Toast.LENGTH_SHORT).show();

                TextView keyword = (TextView) view.findViewById(R.id.keyword);
                keyword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d(TAG, "onClick: " + mChapters.get(position).getId());

                        Intent intent = new Intent(getContext(), WordbookActivity.class);

                        Bundle args = new Bundle();
                        args.putString("chapterId", mChapters.get(position).getId());
                        args.putSerializable("keywordList", mChapters.get(position).getKeywords());
                        intent.putExtras(args);
                        startActivity(intent);
                    }
                });

                TextView question = (TextView) view.findViewById(R.id.question);
                question.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d(TAG, "onClick: " + mChapters.get(position).getId());

                        Intent intent = new Intent(getContext(), QuestionListActivity.class);

                        Bundle args = new Bundle();
                        args.putString("chapterId", mChapters.get(position).getId());
                        args.putSerializable("questionList", mChapters.get(position).getQuestions());
                        intent.putExtras(args);
                        startActivity(intent);

                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;

    }

    /**
     * FIREBASE
     * 1. Get chapter keys from courses - chapter - key:1 node
     * 2. For loop with chapter keys
     * 3. Fetch values from chapters - chapterKey node
     */

    private void setupFirebaseAuth() {

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
                                Log.d(TAG, "onDataChange: e " + e.getMessage());
                            }


                            mKeywordIdList = new HashMap<>();
                            int i = 0;
                            for (DataSnapshot child : dataSnapshot.child("keywords").getChildren()) {
                                mKeywordIdList.put(child.getKey(), i);
                                i += 1;
                                chapter.setKeywords(mKeywordIdList);
                                Log.d(TAG, "onDataChange: " + chapter.getKeywords());
                            }

                            mQuestionIdList = new HashMap<>();
                            int j = 0;

                            for (DataSnapshot child : dataSnapshot.child("questions").getChildren()) {
                                mQuestionIdList.put(child.getKey(), i);
                                j += 1;
                                chapter.setQuestions(mQuestionIdList);
                                Log.d(TAG, "onDataChange: " + chapter.getQuestions());
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
