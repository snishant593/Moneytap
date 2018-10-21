package com.moneytap.mediawiki.viewmodel;


import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import com.moneytap.mediawiki.model.DetailPage;
import com.moneytap.mediawiki.model.DetailResponse;
import com.moneytap.mediawiki.model.SearchResult;
import com.moneytap.mediawiki.util.Constant;
import com.moneytap.mediawiki.viewmodel.ViewModel;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class SearchViewModel extends ViewModel {

    private Constant.SearchListener searchListener;
    private Constant.PageDetailListener pageDetailListener;

    public SearchViewModel(Constant.SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public SearchViewModel(Constant.PageDetailListener searchListener) {
        this.pageDetailListener = searchListener;
    }

    public void search(Observable<Response<SearchResult>> observable, int pageSize) {
        getSearchResult(observable, pageSize);
    }

    public void getDetailUrl(Observable<Response<DetailResponse>> observable) {
        getUrl(observable);
    }

    private void getUrl(Observable<Response<DetailResponse>> observable) {
        new CompositeDisposable().add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<DetailResponse>>() {
                    @Override
                    public void onNext(Response<DetailResponse> value) {
                        if (value.code() == HttpURLConnection.HTTP_OK) {
                            Log.e("", "onNext: ");
                            Map<String, DetailPage> map = new HashMap<>();
                            map.putAll(value.body().getQuery().getPages());
                            Map.Entry<String, DetailPage> entry = map.entrySet().iterator().next();
                            pageDetailListener.onPageUrlResponse(entry.getValue().getFullurl());

                        } else {
                            try {
                                JSONObject errorObj = new JSONObject(value.errorBody().string());
                            } catch (Exception e) {
                                Log.e("", "onNext: ");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("onComplete", "Taddaaaaaaaa......!!!!");
                    }
                }));

    }

    private void getSearchResult(Observable<Response<SearchResult>> observable, final int pageSize) {
        new CompositeDisposable().add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<SearchResult>>() {
                    @Override
                    public void onNext(Response<SearchResult> value) {
                        if (value.code() == HttpURLConnection.HTTP_OK) {
                            if (pageSize > 20)
                                searchListener.onSearchNextResponse(value.body());
                            else
                                searchListener.onSearchResponse(value.body());
                        } else {
                            try {
                                JSONObject errorObj = new JSONObject(value.errorBody().string());
                            } catch (Exception e) {
                                Log.e("", "onNext: ");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("onComplete", "Taddaaaaaaaa......!!!!");
                    }
                }));
    }

}
