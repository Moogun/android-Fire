package com.toomtoome.fire.QuestionList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toomtoome.fire.Model.Question;
import com.toomtoome.fire.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by moogunjung on 12/8/17.
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QViewHolder> {

    private static final String TAG = "QuestionAdapter";
    private List<Question> questionList;


    public class QViewHolder extends RecyclerView.ViewHolder {

        public TextView username, text, createdAt;
        public ImageView profileImage, qImage;

        public QViewHolder(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.text1);
            createdAt = itemView.findViewById(R.id.createdAt);

        }
    }

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @Override
    public QViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_question_list_item, parent, false);
        return new QuestionAdapter.QViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(QViewHolder holder, int position) {

        Question q = questionList.get(position);
        Glide.with(holder.profileImage).load(q.getProfileImageUrl()).into(holder.profileImage);

        holder.username.setText(q.getUsername());
        holder.text.setText(q.getText());

        Double createdAt = q.getCreatedAt() * 1000;
        long time = (new Double(createdAt)).longValue();
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a HH:mm", Locale.KOREA);
        sdf.setTimeZone(TimeZone.getTimeZone("ROK")); //google 'android list of timezones'
        String newFormat = sdf.format(date);
        Log.d(TAG, "onCreate: " + date);

        holder.createdAt.setText(newFormat);

//        //set the time it was posted
//        String timestampDifference = getTimestampDifference(q);
//        if(!timestampDifference.equals("0")){
//            holder.createdAt.setText(timestampDifference + " DAYS AGO");
//        }else{
//            holder.createdAt.setText("TODAY");
//        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    /**
     * Returns a string representing the number of days ago the post was made
     * @return
//     */
//    private String getTimestampDifference(Question question){
//        Log.d(TAG, "getTimestampDifference: getting timestamp difference.");
//
//        String difference = "";
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
//        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));//google 'android list of timezones'
//        Date today = c.getTime();
//        sdf.format(today);
//        Date timestamp;
//        //final String photoTimestamp = comment.getDate_created();
//        final String questionTimeStamp =  question.getCreatedAt();
//        try{
//            timestamp = sdf.parse(questionTimeStamp);
//            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24 )));
//        }catch (ParseException e){
//            Log.e(TAG, "getTimestampDifference: ParseException: " + e.getMessage() );
//            difference = "0";
//        }
//        return difference;
//    }

}
