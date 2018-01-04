package com.toomtoome.fire.Library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toomtoome.fire.Model.Chapter;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Utils.ChapterAdapter_Q;

import java.util.ArrayList;

/**
 * Created by moogunjung on 11/21/17.
 */

public class QnaFragment extends Fragment {

    private ArrayList<Chapter> chapters = new ArrayList<>();
    private ChapterAdapter_Q mAdapter;

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mAdapter = new ChapterAdapter_Q(chapters);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        recyclerView.setAdapter(mAdapter);
        snapHelper.attachToRecyclerView(recyclerView);

//        prepareMovieData();
        return view;

    }


    private void prepareMovieData() {

//        Chapter chapter1 = new Chapter(1, "This is the first chapter", "Learn the new way of building a house");
//        Chapter chapter2 = new Chapter(2, "This is the 2nd chapter", "Learn the 2nd way of building a house");

//        chapters.add(chapter1);
//        chapters.add(chapter2);
//        chapters.add(chapter1);
//        chapters.add(chapter2);
//        chapters.add(chapter1);
//        chapters.add(chapter2);
//        chapters.add(chapter1);
//        chapters.add(chapter2);

        mAdapter.notifyDataSetChanged();
    }
}
