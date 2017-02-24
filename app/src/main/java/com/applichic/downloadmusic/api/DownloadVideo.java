package com.applichic.downloadmusic.api;


import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Properties;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Lazyos on 2/23/2017.
 */

public class DownloadVideo {

    private DownloadManager downloadManager;

    private void download(Activity activity, String url, String name, String folder) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            // Create request for android download manager
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            downloadManager = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);

            //Set the local destination for the downloaded file to a path within the application's external files directory
            request.setDestinationInExternalPublicDir(folder, name);

            //Enqueue download and save into referenceId
            downloadManager.enqueue(request);
        }
    }

    public void downloadMusic(Activity activity, String url, String name) {
        download(activity, url, name+".mp3", "Music");
    }

    private void downloadVideo(Activity activity, String url, String name) {
        download(activity, url, name+".mp4", "Movies");
    }

    public void getVideoUrl(final Activity activity, String url, final String name) {
        RequestQueue queue = Volley.newRequestQueue(activity);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parsing the answer
                        Gson gson = new Gson();
                        Properties data = gson.fromJson(response, Properties.class);

                        // Send the informations
                        downloadVideo(activity, data.getProperty("url"), name);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Couldn't find the video URL");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
