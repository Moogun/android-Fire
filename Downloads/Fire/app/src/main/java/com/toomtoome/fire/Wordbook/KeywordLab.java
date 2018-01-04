package com.toomtoome.fire.Wordbook;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toomtoome.fire.Utils.FirebaseMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by moogunjung on 12/21/17.
 */

public class KeywordLab {

    private static final String TAG = "KeywordLab";

    private ArrayList<Keyword> mKeywords;
    private static KeywordLab sKeywordLab;

    private Context mContext;

    private HashMap<String, Integer> keywordIds;
    //FIREBASE

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String uid;
    private FirebaseMethods mFirebaseMethods;


    public KeywordLab(Context mContext, HashMap<String, Integer> keywordIds) {
        this.mContext = mContext;
        this.keywordIds = keywordIds;
        this.mKeywords = new ArrayList<Keyword>();

        setupWordbook(keywordIds);
    }

    //get keyword ids as hasMap from here
    // passing ids to constructor
    // constructor runs

    public static KeywordLab get(Context c, HashMap<String, Integer> keywordIds) {
        if (sKeywordLab == null) {
            sKeywordLab = new KeywordLab(c.getApplicationContext(), keywordIds);
        }
        return sKeywordLab;
    }

    public ArrayList<Keyword> getKeywords() {
        return mKeywords;
    }



    private void setupWordbook(HashMap<String, Integer> keywordIds) {
        Log.d(TAG, "setupWordbook: 1");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        Set set = (Set) keywordIds.entrySet();
        Iterator i = (Iterator) set.iterator();

        while (i.hasNext()) {
            Map.Entry keywordId = (Map.Entry) i.next();
            String id = (String) keywordId .getKey();
            Log.d(TAG, "setup keyword id: " + id);

            mRef.child("keywords").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: inside fetching block" + dataSnapshot.getValue());
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
//                    Log.d(TAG, "setupWordbook: 3 mkeyword list size " +mKeywordList.size());
//                    Log.d(TAG, "setupWordbook: 4 " + keywords.size());
//                    Log.d(TAG, "onDataChange: keywords fetched " + word.getKeyword());
//
//                    //wordbookPagerAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }


}
