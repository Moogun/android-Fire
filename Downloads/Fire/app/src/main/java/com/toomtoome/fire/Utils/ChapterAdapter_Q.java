package com.toomtoome.fire.Utils;

import android.support.v7.widget.RecyclerView;
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

public class ChapterAdapter_Q extends RecyclerView.Adapter<ChapterAdapter_Q.ChViewHolder> {

    private static final String TAG = "ChapterAdapter";

    ArrayList<Chapter> chapters = new ArrayList<>();

    public class ChViewHolder extends RecyclerView.ViewHolder {

        String chapterId = "";
        public TextView title, subTitle;
        //public TextView index,

        public ChViewHolder(View itemView) {
            super(itemView);

            //index = (TextView) itemView.findViewById(R.id.index);
            title = (TextView) itemView.findViewById(R.id.title);
            subTitle = (TextView) itemView.findViewById(R.id.subTitle);
        }
    }

    public ChapterAdapter_Q(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    @Override
    public ChViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_preview_question_item, parent, false);
        return new ChViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChViewHolder holder, int position) {

        Chapter chapter = chapters.get(position);
        holder.title.setText(chapter.getTitle());
        holder.subTitle.setText(chapter.getChapterDescription());

    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }
}
