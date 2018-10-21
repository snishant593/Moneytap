package com.moneytap.mediawiki.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class DetailQuery {
    @SerializedName("pages")
    @Expose
    private Map<String, DetailPage> pages;

    public Map<String, DetailPage> getPages() {
        return pages;
    }
}
