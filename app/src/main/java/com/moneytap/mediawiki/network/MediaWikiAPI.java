package com.moneytap.mediawiki.network;


import com.moneytap.mediawiki.model.DetailResponse;
import com.moneytap.mediawiki.model.SearchResult;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface MediaWikiAPI {

    interface QueryParam {
        String ACTION = "action";
        String FORMAT = "format";
        String PROP = "prop";
        String GENERATOR = "generator";
        String REDIRECTS = "redirects";
        String FORMAT_VERSION = "formatversion";
        String PI_PROP = "piprop";
        String PI_THUMB_SIZE = "pithumbsize";
        String PI_LIMIT = "pilimit";
        String WBPT_TERMS = "wbptterms";
        String GPS_SEARCH = "gpssearch";
        String GPS_LIMIT = "gpslimit";
        String PROP_INFO = "inprop";
        String PAGE_IDS = "pageids";
    }

    interface QueryValues {
        String QUERY = "query";
        String JSON = "json";
        String PROP_VALUE = "pageimages%7Cpageterms";
        String GENERATOR_VALUE = "prefixsearch";
        String REDIRECTS_VALUE = "1";
        String FORMAT_VERSION_VALUE = "2";
        String PI_PROP_VALUE = "thumbnail";
        String PI_THUMB_SIZE_VALUE = "50";
        String PI_LIMIT_VALUE = "10";
        String WBPT_TERMS_VALUE = "description";
        String PROP_INFO_URL = "url";
        String INFO = "info";
    }

    @GET("api.php")
    Observable<Response<SearchResult>> getSearchResult(@QueryMap(encoded = true) Map<String, String> options);

    @GET("api.php")
    Observable<Response<DetailResponse>> getDetailUrl(@QueryMap(encoded = true) Map<String, String> options);

}
