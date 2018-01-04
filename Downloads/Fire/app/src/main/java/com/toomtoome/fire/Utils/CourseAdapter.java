package com.toomtoome.fire.Utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toomtoome.fire.Model.Course;
import com.toomtoome.fire.Model.User;
import com.toomtoome.fire.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by moogunjung on 11/21/17.
 */

public class CourseAdapter extends ArrayAdapter<Course> {

    private static final String TAG = "CourseAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    public CourseAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Course> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    private static class ViewHolder {
        TextView title;
        ImageView courseImage;
        TextView instructor;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String courseId = "";
        String title = getItem(position).getTitle();
        String imageUrl = getItem(position).getCourseImageUrl();

        User user = getItem(position).getUser();
        String instructor = getItem(position).getUser().getUsername();
        String instructorId = getItem(position).getUser().getEmail();


        HashMap<String, Integer> chapters = getItem(position).getChapters();

        Course course = new Course(courseId, title, instructor, instructorId, user, imageUrl, chapters);

        //create the view result for showing the animation
        final View result;

        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ViewHolder();

            holder.title = (TextView) convertView.findViewById(R.id.courseTitle);
            holder.instructor = (TextView) convertView.findViewById(R.id.instructor);
            holder.courseImage = (ImageView) convertView.findViewById(R.id.courseImage);

            result = convertView;
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        holder.title.setText(title);
        holder.instructor.setText(instructor);

        Glide.with(mContext).load(imageUrl).into(holder.courseImage);
        return convertView;
    }
}
