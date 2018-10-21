package com.moneytap.mediawiki.view.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.moneytap.mediawiki.R;
import com.moneytap.mediawiki.model.DetailResponse;
import com.moneytap.mediawiki.network.MediaWikiAPI;
import com.moneytap.mediawiki.network.NetworkService;
import com.moneytap.mediawiki.util.Constant;
import com.moneytap.mediawiki.util.Network;
import com.moneytap.mediawiki.viewmodel.SearchViewModel;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageDetailFragment extends Fragment implements Constant.PageDetailListener {

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_detail, container, false);
        webView = view.findViewById(R.id.web_page);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebClient());
        progressBar = view.findViewById(R.id.progress_bar);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Network.isConnected(getActivity())) {
            Network.showAlert(getActivity());
            return;
        }
        SearchViewModel searchViewModel = new SearchViewModel(this);
        Observable<Response<DetailResponse>> responseObservable = NetworkService.create(getActivity()).getDetailUrl(getQueryMap());
        searchViewModel.getDetailUrl(responseObservable);
    }

    @Override
    public void onPageUrlResponse(String url) {
        webView.loadUrl(url);
    }

    @Override
    public void onPageUrlError(String errMsg) {
    }


    private class WebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }
    }

    private Map<String, String> getQueryMap() {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put(MediaWikiAPI.QueryParam.ACTION, MediaWikiAPI.QueryValues.QUERY);
        queryMap.put(MediaWikiAPI.QueryParam.FORMAT, MediaWikiAPI.QueryValues.JSON);
        queryMap.put(MediaWikiAPI.QueryParam.PROP, MediaWikiAPI.QueryValues.INFO);
        queryMap.put(MediaWikiAPI.QueryParam.PROP_INFO, MediaWikiAPI.QueryValues.PROP_INFO_URL);
        queryMap.put(MediaWikiAPI.QueryParam.PAGE_IDS, "" + getArguments().get(Constant.BundleKeys.PAGE_ID));
        return queryMap;
    }

}
