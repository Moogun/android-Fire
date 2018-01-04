package com.toomtoome.fire.QuestionList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toomtoome.fire.Model.Answer;
import com.toomtoome.fire.R;

import java.util.List;

/**
 * Created by moogunjung on 12/9/17.
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private static final String TAG = "AnswerAdapter";
    private List<Answer> answerList;


    public class AnswerViewHolder extends RecyclerView.ViewHolder {
        public TextView uid, username, text;
        public ImageView profileImage;

        public AnswerViewHolder(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.text1);

        }
    }

    public AnswerAdapter(List<Answer> answerList) {
        this.answerList = answerList;
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_answer_item, parent, false);
        return new AnswerAdapter.AnswerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AnswerViewHolder holder, int position) {
        Answer a = answerList.get(position);

        Glide.with(holder.profileImage).load(a.getProfileImageUrl()).into(holder.profileImage);
        holder.username.setText(a.getUsername());
        holder.text.setText(a.getText());
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }
}
