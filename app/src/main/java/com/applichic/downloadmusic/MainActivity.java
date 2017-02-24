package com.applichic.downloadmusic;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.applichic.downloadmusic.api.SearchVideos;
import com.applichic.downloadmusic.api.Video;
import com.applichic.downloadmusic.api.YoutubeInformationsListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lazyos on 2/22/2017.
 */

public class MainActivity extends AppCompatActivity implements YoutubeInformationsListener {
    private SearchVideos mSearchVideos;
    private EditText mSearchInput;
    private RecyclerView mRecyclerViewVideos;
    private ProgressBar mProgressBar;

    private VideoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the views
        mSearchInput = (EditText) findViewById(R.id.search_input);
        mRecyclerViewVideos = (RecyclerView) findViewById(R.id.recycler_view_videos);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Initialization
        mSearchVideos = new SearchVideos(this);

        adapter = new VideoAdapter(this, new ArrayList<Video>());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerViewVideos.setLayoutManager(mLayoutManager);
        mRecyclerViewVideos.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        mRecyclerViewVideos.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewVideos.setAdapter(adapter);
    }

    public void onSearching(View v) {
        mProgressBar.setVisibility(View.VISIBLE);
        mSearchVideos.search(mSearchInput.getText().toString());

        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void updateData(List<Video> videoList) {
        adapter.setVideoList(videoList);
        adapter.notifyDataSetChanged();

        // Set invisible the loading bar
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override public void onSaveInstanceState(Bundle savedInstanceState) {
        Gson gson = new Gson();

        savedInstanceState.putString("search_text", mSearchInput.getText().toString());
        savedInstanceState.putString("video_list", gson.toJson(adapter.getVideoList()));
    }

    @Override public void onRestoreInstanceState(Bundle savedInstanceState) {
        Gson gson = new Gson();

        // Recover the searched text
        mSearchInput.setText(savedInstanceState.getString("search_text"));

        // Recover the list of videos in the list
       List<Video> videoList = gson.fromJson(savedInstanceState.getString("video_list"), new TypeToken<ArrayList<Video>>(){}.getType());
        adapter.setVideoList(videoList);
        adapter.notifyDataSetChanged();
    }
}