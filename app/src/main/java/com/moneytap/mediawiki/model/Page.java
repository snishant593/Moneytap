package com.moneytap.mediawiki.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class Page {

    private String pageid;
    private String title;
    private Thumbnail thumbnail;
    private Terms terms;
    private int viewType;

    public Page() {
    }

    public Page(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public String getPageid() {
        return pageid;
    }

    public String getTitle() {
        return title;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public Terms getTerms() {
        return terms;
    }

}
