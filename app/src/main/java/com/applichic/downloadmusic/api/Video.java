package com.applichic.downloadmusic.api;

/**
 * Created by Lazyos on 2/22/2017.
 */

public class Video {
    private String id;
    private String title;
    private String urlThumbnail;

    public Video() {

    }

    public Video(String id, String title, String urlThumbnail) {
        this.id = id;
        this.title = title;
        this.urlThumbnail = urlThumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }
}
