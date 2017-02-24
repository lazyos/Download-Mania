package com.applichic.downloadmusic;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.applichic.downloadmusic.api.DownloadVideo;
import com.applichic.downloadmusic.api.Video;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Lazyos on 2/22/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    private List<Video> videoList;
    private Activity mActivity;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView thumbnail, overflow;

        MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    VideoAdapter(Activity activity, List<Video> videoList) {
        this.mActivity = activity;
        this.videoList = videoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Video currentVideo = videoList.get(position);
        holder.title.setText(currentVideo.getTitle());

        Picasso.with(mActivity).load(currentVideo.getUrlThumbnail()).into(holder.thumbnail);

        // Listener of the click on the dots
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, currentVideo);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, Video currentVideo) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mActivity, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_video, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(currentVideo));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private DownloadVideo downloadVideo;
        private Video currentVideo;

        public MyMenuItemClickListener(Video currentVideo) {
            this.currentVideo = currentVideo;
            downloadVideo = new DownloadVideo();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_download_mp3:
                    downloadVideo.downloadMusic(mActivity, "http://www.youtubeinmp3.com/fetch/?video=https://www.youtube.com/watch?v="+currentVideo.getId(), currentVideo.getTitle());
                    return true;
                case R.id.action_download_mp4:
                    // Ask the URL to the server
                    downloadVideo.getVideoUrl(mActivity, "https://helloacm.com/api/video/?cached&video=https://www.youtube.com/watch?v="+currentVideo.getId(), currentVideo.getTitle());
                    return true;
                case R.id.action_watch_video:
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(mActivity, mActivity.getResources().getString(R.string.api_youtube), currentVideo.getId());
                    mActivity.startActivity(intent);
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    public List<Video> getVideoList() {
        return videoList;
    }
}
