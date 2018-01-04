package com.toomtoome.fire.Utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toomtoome.fire.Model.Chapter;
import com.toomtoome.fire.R;

import java.util.ArrayList;

/**
 * Created by moogunjung on 12/8/17.
 */

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChViewHolder> {

    private static final String TAG = "ChapterAdapter";

    ArrayList<Chapter> chapters = new ArrayList<>();

    public class ChViewHolder extends RecyclerView.ViewHolder {

        public TextView index, title, chapterDescription;

        public ChViewHolder(View itemView) {
            super(itemView);

            index = (TextView) itemView.findViewById(R.id.index);
            title = (TextView) itemView.findViewById(R.id.title);
            chapterDescription = (TextView) itemView.findViewById(R.id.desc);
        }
    }

    public ChapterAdapter(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    @Override
    public ChViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_preview_chapter_item, parent, false);
        return new ChViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChViewHolder holder, int position) {

        Chapter chapter = chapters.get(position);

        holder.index.setText(String.valueOf(position + 1));
        holder.title.setText(chapter.getTitle());
        holder.chapterDescription.setText(chapter.getChapterDescription());
        Log.d(TAG, "onBindViewHolder: 2 = " + chapter.getTitle());

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: 1 = " + chapters.size());
        return chapters.size();
    }
}
