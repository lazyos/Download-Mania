package com.applichic.downloadmusic.api;

import com.applichic.downloadmusic.api.Video;

import java.util.List;

/**
 * Created by Lazyos on 2/23/2017.
 */

public interface YoutubeInformationsListener {
    void updateData(List<Video> videoList);
}
