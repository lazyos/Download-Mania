package com.applichic.downloadmusic.api;

import android.content.Context;
import android.os.AsyncTask;

import com.applichic.downloadmusic.MainActivity;
import com.applichic.downloadmusic.R;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Lazyos on 2/22/2017.
 */

public class SearchVideos {
    private YouTube youtube;
    private Context mContext;
    private static final long NUMBER_OF_VIDEOS_RETURNED = 24;

    private YoutubeInformationsListener mCallback;

    public SearchVideos(MainActivity activity) {
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("downloadMusic").build();

        mCallback = activity;
        mContext = activity;
    }

    public void search(String query) {
        new RetrieveVideoList().execute(query);
    }

    private List<Video> loadYoutubeVideosQuery(String query) throws Throwable {
        List<Video> result = new ArrayList<>();

        // Define the API request for retrieving search results.
        YouTube.Search.List search = youtube.search().list("id,snippet");

        // Set all informations about the query
        search.setKey(mContext.getResources().getString(R.string.api_youtube));
        search.setQ(query);
        search.setType("video");
        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
        search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

        // Call the API and print results.
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        if (searchResultList != null) {

            // For each videos sent by api we parse informations
            Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();
            while (iteratorSearchResults.hasNext()) {
                SearchResult singleVideo = iteratorSearchResults.next();
                result.add(fillVideoInfo(singleVideo));
            }
        }

        return result;
    }

    private Video fillVideoInfo(SearchResult singleVideo) {
        Video result = new Video();

        // Get youtube informations about the video
        Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

        // Fill the informations
        result.setId(singleVideo.getId().getVideoId());
        result.setTitle(singleVideo.getSnippet().getTitle());
        result.setUrlThumbnail(thumbnail.getUrl());

        return result;
    }

    private class RetrieveVideoList extends AsyncTask<String, Void, List<Video>> {

        protected List<Video> doInBackground(String... queries) {
            List<Video> result = new ArrayList<>();

            try {
                result = loadYoutubeVideosQuery(queries[0]);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return result;
        }

        protected void onPostExecute(List<Video> videoList) {
            mCallback.updateData(videoList);
        }
    }
}
