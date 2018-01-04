package com.toomtoome.fire.NewQuestion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toomtoome.fire.R;
import com.toomtoome.fire.Utils.FilePaths;
import com.toomtoome.fire.Utils.FileSearch;
import com.toomtoome.fire.Utils.GridImageAdapter;

import java.util.ArrayList;


/**
 * Created by moogunjung on 12/18/17.
 */

public class GalleryFragment extends Fragment {

    private static final String TAG = "GalleryFragment";

    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;

    private ArrayList<String> directories;
    private String mAppend = "file:/";

    private static final int NUM_GRID_COLUMNS = 3;

    private String mSelectedImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        Log.d(TAG, "onCreateView: gallery fragment");
        directories = new ArrayList<>();

        galleryImage = (ImageView) view.findViewById(R.id.galleryImageView);
        gridView = (GridView) view.findViewById(R.id.gridView);
        directorySpinner = (Spinner) view.findViewById(R.id.spinnerDirectory);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        ImageView shareClose = (ImageView) view.findViewById(R.id.ivCloseShare);

        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the gallery fragment.");
                getActivity().finish();
            }
        });

        TextView nextScreen = (TextView) view.findViewById(R.id.tvNext);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the final share screen.");
//
//                Intent intent = new Intent(getActivity(), NextActivity.class);
//                intent.putExtra("selected_image", mSelectedImage);
//                startActivity(intent);
            }
        });


        init( );

        return view;
    }

    private void init() {
        Log.d(TAG, "init: for file paths" );
        FilePaths filePaths = new FilePaths();

        //check for other folders indide "/storage/emulated/0/pictures"
        if(FileSearch.getDirectoryPaths(filePaths.PICTURES) != null) {
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }

        /**
         *  don't know what this does
         */
//        ArrayList<String> directoryNames = new ArrayList<>();
//        for(int i = 0; i < directories.size(); i++){
//            int index = directories.get(i).lastIndexOf("/");
//                        String string = directories.get(i).substring(index);
//                        directoryNames.add(string);
//        }


        //

        directories.add(filePaths.CAMERA);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, directories);
//                                android.R.layout.simple_spinner_item, directoryNames);
//
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d(TAG, "onItemClick: selected: " + directories.get(position));
                setupGridView(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupGridView(String selectedDirectory){

        Log.d(TAG, "setupGridView: directory chosen: " + selectedDirectory);
        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);

        //set the grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        //use the grid adapter to adapter the images to gridview
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, mAppend, imgURLs);
        gridView.setAdapter(adapter);

        //set the first image to be displayed when the activity fragment view is inflated
        setImage(imgURLs.get(0), galleryImage, mAppend);

        mSelectedImage = imgURLs.get(0);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected an image: " + imgURLs.get(position));

                setImage(imgURLs.get(position), galleryImage, mAppend);
                mSelectedImage = imgURLs.get(position);
            }
        });

    }

    private void setImage(String imgURL, ImageView image, String append){
        Log.d(TAG, "setImage: setting image");
        //mProgressBar.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(imgURL).into(galleryImage);
        mProgressBar.setVisibility(View.GONE);
//        ImageLoader imageLoader = ImageLoader.getInstance();
//
//        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//                mProgressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                mProgressBar.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                mProgressBar.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//                mProgressBar.setVisibility(View.INVISIBLE);
//            }
//        });
    }
}
