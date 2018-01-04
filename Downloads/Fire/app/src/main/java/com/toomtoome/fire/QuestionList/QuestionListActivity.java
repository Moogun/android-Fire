package com.toomtoome.fire.QuestionList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.toomtoome.fire.R;

import java.util.HashMap;

/**
 * Created by moogunjung on 12/8/17.
 */

public class QuestionListActivity extends AppCompatActivity {

    private static final String TAG = "QuestionListActivity";

    //private static final int ACTIVITY_NUM = 2;
    private Context mContext = QuestionListActivity.this;

    Bundle bundle;
    HashMap<String, Integer> mQuestionList; // coming from chapter fragment

    QuestionListFragment qListFragment;
    QuestionDetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        bundle = getIntent().getExtras();
        String chapterId = bundle.getString("chapterId");
        mQuestionList = (HashMap<String, Integer>) bundle.getSerializable("questionList");

        init(chapterId);
    }

    private void init(String chapterId) {
        Log.d(TAG, "init: inflating" + "Question list fragment");

        Bundle bundle = new Bundle();
        bundle.putString("chapterId", chapterId);
        bundle.putSerializable("questionList", mQuestionList);
        Log.d(TAG, "init: " + mQuestionList);

        qListFragment = new QuestionListFragment();
        qListFragment.setArguments(bundle);
        FragmentTransaction transaction = QuestionListActivity.this.getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.container, qListFragment);
        transaction.addToBackStack("qListFragment");
        transaction.commit();

    }

}
