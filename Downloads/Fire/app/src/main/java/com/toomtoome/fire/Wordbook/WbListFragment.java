package com.toomtoome.fire.Wordbook;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.toomtoome.fire.R;

import java.util.ArrayList;

/**
 * Created by moogunjung on 12/12/17.
 */

public class WbListFragment extends ListFragment {

    private static final String TAG = "WbListFragment";

    int mNum; // position info

    ArrayList<Keyword> mKeywords;

    static WbListFragment newInstance(int num, ArrayList<Keyword> mKeywords) {

        WbListFragment f = new WbListFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putParcelableArrayList("keywords", mKeywords);
        f.setArguments(args);

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNum = getArguments() != null ? getArguments().getInt("num") : 1;

        if (getArguments() == null) {
            mKeywords = new ArrayList<>();

        } else {
            mKeywords = getArguments().getParcelableArrayList("keywords");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_wordbook_list, container, false);

//        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Log.d(TAG, "onCheckedChanged: " + compoundButton);
//                //checkbox.setSolved(isChecked)?
//            }
//        });

        TextView keyword = (TextView) view.findViewById(R.id.keyword);
        TextView meaning = (TextView) view.findViewById(R.id.meaning);
        ImageButton more = (ImageButton) view.findViewById(R.id.more);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new WbAdapter(getActivity(), R.layout.layout_wordbook_list_item, mKeywords));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "Item clicked: " + id);
    }


}