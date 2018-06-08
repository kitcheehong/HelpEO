package com.kitchee.app.helpeo.bean;



/**
 * Created by kitchee on 2018/5/31.
 * desc :
 */

public class Poster  {
    private int versionCode;
    private String posterUrl;

    public Poster() {
    }

    public Poster(String tableName, int versionCode, String posterUrl) {

        this.versionCode = versionCode;
        this.posterUrl = posterUrl;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
