package com.moneytap.mediawiki.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moneytap.mediawiki.R;
import com.moneytap.mediawiki.model.Page;
import com.moneytap.mediawiki.network.MediaWikiAPI;
import com.moneytap.mediawiki.network.NetworkService;
import com.moneytap.mediawiki.util.Constant;
import com.moneytap.mediawiki.util.FragmentHelper;
import com.moneytap.mediawiki.util.Network;
import com.moneytap.mediawiki.util.RecyclerViewEvents;
import com.moneytap.mediawiki.util.RecyclerViewOnItemClickHandler;
import com.moneytap.mediawiki.view.activity.SearchDetailActivity;
import com.moneytap.mediawiki.view.adapter.SearchResultListAdapter;
import com.moneytap.mediawiki.databinding.FragmentSearchResultBinding;
import com.moneytap.mediawiki.model.SearchResult;
import com.moneytap.mediawiki.viewmodel.SearchViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends Fragment implements RecyclerViewEvents.Listener<Page>,
        Constant.SearchListener, SwipeRefreshLayout.OnRefreshListener {

    private FragmentSearchResultBinding binding;
    private List<Page> pages;
    private int pageLimit = 20;
    private boolean isLoading;
    private SearchResult searchResult;
    private Page footerView;
    private Observable<Response<SearchResult>> responseObservable;
    private SearchViewModel searchViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        pages = new ArrayList<>();
        footerView = new Page(RecyclerViewEvents.FOOTER);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_result, container, false);
        binding.recyclerView.setAdapter(new SearchResultListAdapter(inflater, pages, this));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        searchViewModel = new SearchViewModel(this);
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.green, R.color.colorPrimaryDark);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        responseObservable = NetworkService.create(getActivity()).getSearchResult(getQueryMap(pageLimit));
        searchViewModel.search(responseObservable, pageLimit);
        binding.swipeRefreshLayout.setRefreshing(true);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (canLoadMoreItems()) {
                    showFooterView();
                    Observable<Response<SearchResult>> responseObservable = NetworkService.create(getActivity()).getSearchResult(getQueryMap(pageLimit += 20));
                    searchViewModel.search(responseObservable, pageLimit);
                    isLoading = true;
                    binding.swipeRefreshLayout.setRefreshing(true);
                }

            }
        });
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onItemClick(Page item, View v, int position) {
        Intent intent = new Intent(getActivity(), SearchDetailActivity.class);
        intent.putExtra(Constant.BundleKeys.PAGE_ID, item.getPageid());
        startActivity(intent);
    }

    @Override
    public void onSearchResponse(SearchResult searchResult) {
        if (searchResult != null && searchResult.getQuery() != null) {
            this.searchResult = searchResult;
            pages.addAll(searchResult.getQuery().getPages());
            binding.recyclerView.getAdapter().notifyDataSetChanged();
            binding.swipeRefreshLayout.setRefreshing(false);
            return;
        }
        binding.emptyView.setVisibility(searchResult == null || searchResult.getQuery() == null ? View.VISIBLE : View.GONE);
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSearchNextResponse(SearchResult searchResult) {
        hideFooterView();
        isLoading = false;
        final int insertPositionStart = pages.size();
        List<Page> newPages = new ArrayList<>();
        for (int i = insertPositionStart; i < searchResult.getQuery().getPages().size() - 1; i++)
            newPages.add(searchResult.getQuery().getPages().get(i));
        pages.addAll(newPages);
        binding.recyclerView.getAdapter().notifyItemRangeInserted(insertPositionStart, pages.size() - 1);
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSearchError(String errMsg) {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    private Map<String, String> getQueryMap(int pageLimit) {
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put(MediaWikiAPI.QueryParam.ACTION, MediaWikiAPI.QueryValues.QUERY);
        queryMap.put(MediaWikiAPI.QueryParam.FORMAT, MediaWikiAPI.QueryValues.JSON);
        queryMap.put(MediaWikiAPI.QueryParam.GENERATOR, MediaWikiAPI.QueryValues.GENERATOR_VALUE);
        queryMap.put(MediaWikiAPI.QueryParam.PROP, MediaWikiAPI.QueryValues.PROP_VALUE);
        queryMap.put(MediaWikiAPI.QueryParam.REDIRECTS, MediaWikiAPI.QueryValues.REDIRECTS_VALUE);
        queryMap.put(MediaWikiAPI.QueryParam.FORMAT_VERSION, MediaWikiAPI.QueryValues.FORMAT_VERSION_VALUE);
        queryMap.put(MediaWikiAPI.QueryParam.PI_PROP, MediaWikiAPI.QueryValues.PI_PROP_VALUE);
        queryMap.put(MediaWikiAPI.QueryParam.PI_LIMIT, MediaWikiAPI.QueryValues.PI_LIMIT_VALUE);
        queryMap.put(MediaWikiAPI.QueryParam.PI_THUMB_SIZE, MediaWikiAPI.QueryValues.PI_THUMB_SIZE_VALUE);
        queryMap.put(MediaWikiAPI.QueryParam.WBPT_TERMS, MediaWikiAPI.QueryValues.WBPT_TERMS_VALUE);
        queryMap.put(MediaWikiAPI.QueryParam.GPS_SEARCH, getArguments().getString(Constant.BundleKeys.SEARCH_QUERY));
        queryMap.put(MediaWikiAPI.QueryParam.GPS_LIMIT, "" + pageLimit);
        return queryMap;
    }

    private boolean canLoadMoreItems() {
        if (!Network.isConnected(getActivity()) || searchResult == null)
            return false;
        RecyclerView.LayoutManager layoutManager = binding.recyclerView.getLayoutManager();
        int visibleItemCount = binding.recyclerView.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        boolean isLastItem = totalItemCount == firstVisibleItem + visibleItemCount;
        boolean hasMoreItems = totalItemCount < 500;
        return isLastItem && hasMoreItems && !isLoading;
    }

    private void showFooterView() {
        if (pages.add(footerView))
            binding.recyclerView.getAdapter().notifyItemInserted(pages.size() - 1);
    }

    private void hideFooterView() {
        if (pages.remove(footerView))
            binding.recyclerView.getAdapter().notifyItemRemoved(pages.size());
    }

    @Override
    public void onRefresh() {
        if (responseObservable == null)
            return;
        isLoading = true;
        searchViewModel.search(responseObservable, 20);

    }
}
