package com.moneytap.mediawiki.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.v4.app.Fragment;

import com.moneytap.mediawiki.R;
import com.moneytap.mediawiki.util.FragmentHelper;
import com.moneytap.mediawiki.view.fragment.PageDetailFragment;

public class SearchDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);
        Bundle bundle = getIntent().getExtras();
        Fragment fragment = new PageDetailFragment();
        fragment.setArguments(bundle);
        fragment.setArguments(getIntent().getExtras());
        FragmentHelper.addFragment(this, R.id.detail_container, fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

