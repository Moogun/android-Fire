package com.toomtoome.fire.Wordbook;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Utils.FirebaseMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class WordbookActivity extends AppCompatActivity {

    private static final String TAG = "WordbookActivity";
    private Context mContext;

    //View Objects
    ImageView backArrow;
    TabLayout tabLayout;
    ViewPager mPager;

    //Adapter
    WordbookPagerAdapter wordbookPagerAdapter;

    //Passed information
    Bundle chapterBundle;
    String chapterId;

    // keywords data
    static int NUM_PAGES;
    int quotient, modulas;
    HashMap<String, Integer> mKeywordList; // coming from chapter fragment
    private ArrayList<Keyword> mKeywords; //passing to the adapters to show keywords in the list fragment


    //FIREBASE
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String uid;
    private FirebaseMethods mFirebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordbook);

        mContext = WordbookActivity.this;

        backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to 'ProfileActivity'");
                finish();
            }
        });


        mPager = (ViewPager)findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Data coming from chapter fragment
        chapterBundle = getIntent().getExtras();
        chapterId = chapterBundle.getString("chapterId");

        mKeywordList = (HashMap<String, Integer>) chapterBundle.getSerializable("keywordList");
        mKeywords = new ArrayList<>();

        fetchKeywords(mKeywordList);
    }

    private void fetchKeywords(HashMap<String, Integer> keywordIds) {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        Set set = (Set) keywordIds.entrySet();
        Iterator i = (Iterator) set.iterator();

        while (i.hasNext()) {
            Map.Entry keywordId = (Map.Entry) i.next();
            String id = (String) keywordId.getKey();

            mRef.child("keywords").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "fetchKeywords: inside fetching block" + dataSnapshot.getValue());
                    Keyword word = new Keyword();
                    word.setId(dataSnapshot.getKey());

                    word.setKeyword(dataSnapshot.getValue(Keyword.class).getKeyword());
                    word.setPronunciation(dataSnapshot.getValue(Keyword.class).getPronunciation());
                    word.setMeaning(dataSnapshot.getValue(Keyword.class).getKeyword());

                    word.setEg_1(dataSnapshot.getValue(Keyword.class).getEg_1());
                    word.setEg_Pronun_1(dataSnapshot.getValue(Keyword.class).getEg_Pronun_1());
                    word.setEg_meaning_1(dataSnapshot.getValue(Keyword.class).getEg_meaning_1());

                    word.setEg_2(dataSnapshot.getValue(Keyword.class).getEg_2());
                    word.setEg_Pronun_2(dataSnapshot.getValue(Keyword.class).getEg_Pronun_2());
                    word.setEg_meaning_2(dataSnapshot.getValue(Keyword.class).getEg_meaning_2());

                    word.setChapterId(dataSnapshot.getValue(Keyword.class).getChapterId());

                    mKeywords.add(word);
                    Log.d(TAG, "fetchKeywords: " + "keyword lsit size" + mKeywords.size());


                    modulas = mKeywords.size() % 10;
                    quotient = mKeywords.size() / 10;

                    if (modulas == 0) {
                        NUM_PAGES = quotient;
                    } else {
                        NUM_PAGES = quotient + 1;
                    }

                    wordbookPagerAdapter = new WordbookPagerAdapter(getSupportFragmentManager(), NUM_PAGES, mKeywords);
                    mPager.setAdapter(wordbookPagerAdapter);
                    tabLayout.setupWithViewPager(mPager);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

}
