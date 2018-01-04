package com.toomtoome.fire.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toomtoome.fire.Model.Announcement;
import com.toomtoome.fire.R;

import java.util.List;

/**
 * Created by moogunjung on 12/8/17.
 */

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnViewHolder> {

    private static final String TAG = "AnnouncementAdapter";
    private List<Announcement> announcementList;

    public class AnViewHolder extends RecyclerView.ViewHolder {

        public TextView username, title;
        public ImageView profileImage, anImage;

        public AnViewHolder(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            username = itemView.findViewById(R.id.username);
            //anImage = itemView.findViewById(R.id.anImage1);
            title = itemView.findViewById(R.id.title);

        }
    }

    public AnnouncementAdapter(List<Announcement> announcementList) {
        this.announcementList = announcementList;
    }

    @Override
    public AnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_preview_note_item, parent, false);
        return new AnViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AnViewHolder holder, int position) {

        Announcement announcement = announcementList.get(position);

        holder.username.setText(announcement.getUsername());
        holder.title.setText(announcement.getTitle());
        Glide.with(holder.profileImage).load(announcement.getProfileImageUrl()).into(holder.profileImage);

        if (announcement.getAnImgUrl() != null) {
            Glide.with(holder.anImage).load(announcement.getAnImgUrl()).into(holder.anImage);
        }

    }

    @Override
    public int getItemCount() {
        return announcementList.size();
    }
}
