package com.toomtoome.fire.Wordbook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.toomtoome.fire.R;

import java.util.List;

import static android.graphics.Color.YELLOW;

/**
 * Created by moogunjung on 12/13/17.
 */

public class WbAdapter extends ArrayAdapter<Keyword> {

    private static final String TAG = "WbAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    public WbAdapter(@NonNull Context context, int resource, @NonNull List<Keyword> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    private class ViewHolder {
        CheckBox checkBox;
        TextView keyword;
        TextView meaning;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String index = "10";

//        try {
            String keyword = getItem(position).getKeyword();
            String pronunciation = getItem(position).getPronunciation();
            String meaning = getItem(position).getMeaning();


  //
        final View result;

        final ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ViewHolder();

            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.keyword = (TextView) convertView.findViewById(R.id.keyword);
            holder.meaning = (TextView) convertView.findViewById(R.id.meaning);



            final View finalConvertView = convertView;
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: abc " + position);

                    // do something here
                    holder.keyword.setHighlightColor(YELLOW); // this is not working Dec 13

                }
            });

            holder.keyword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: keyword" + position);
                }
            });

            result = convertView;

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

            result = convertView;
        }

        lastPosition = position;

        holder.keyword.setText(keyword);
        holder.meaning.setText(meaning);

        return convertView;
    }
}
