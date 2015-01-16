package com.imagesearch.app;

/**
 * Created by aimango on 15-01-16.
 */
public class GoogleImage {
    String thumbUrl;
    String actualUrl;
    String title;

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String url) {
        this.thumbUrl = url;
    }

    public String getTitle()  {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActualUrl() {
        return actualUrl;
    }

    public void setActualUrl(String actualUrl) {
        this.actualUrl = actualUrl;
    }
}
