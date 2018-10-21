package com.moneytap.mediawiki.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.moneytap.mediawiki.util.Constant;
import com.moneytap.mediawiki.util.FragmentHelper;
import com.moneytap.mediawiki.R;
import com.moneytap.mediawiki.util.Utility;
import com.moneytap.mediawiki.view.fragment.SearchResultFragment;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) SearchActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQuery("", true);
        searchView.setFocusable(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchActivity.this.getComponentName()));
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    public void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Fragment fragment = new SearchResultFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.BundleKeys.SEARCH_QUERY, query);
            fragment.setArguments(bundle);
            FragmentHelper.replaceFragment(this, R.id.search_container, fragment);
            Toast.makeText(this, "" + query, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Utility.hideKeyboard(this);
        super.onBackPressed();
    }
}
