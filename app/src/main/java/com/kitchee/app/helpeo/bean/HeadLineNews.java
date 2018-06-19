package com.kitchee.app.helpeo.bean;

import java.io.Serializable;

public class HeadLineNews implements Serializable {
    //标签
    private String id;
    //标题
    private String title;
    //路径
    private String url;
    //分类
    private String category;

    public HeadLineNews(String id, String title, String url, String category) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.category = category;
    }

    public HeadLineNews() {
        super();
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
