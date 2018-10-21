package com.moneytap.mediawiki.util;


import com.moneytap.mediawiki.model.SearchResult;

public class Constant {

    public interface BundleKeys {
        String SEARCH_QUERY = "search_query";
        String PAGE_ID = "search_query";
    }

    public interface SearchListener {
        void onSearchResponse(SearchResult searchResult);

        void onSearchNextResponse(SearchResult searchResult);

        void onSearchError(String errMsg);

    }

    public interface PageDetailListener {
        void onPageUrlResponse(String url);

        void onPageUrlError(String errMsg);
    }
}
