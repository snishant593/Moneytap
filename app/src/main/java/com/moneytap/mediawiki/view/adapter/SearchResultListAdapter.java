package com.moneytap.mediawiki.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.moneytap.mediawiki.R;
import com.moneytap.mediawiki.BR;
import com.moneytap.mediawiki.databinding.AdapterSerchResultListItemBinding;
import com.moneytap.mediawiki.model.Page;
import com.moneytap.mediawiki.util.FooterViewHolder;
import com.moneytap.mediawiki.util.RecyclerViewEvents;
import com.moneytap.mediawiki.util.RecyclerViewHolder;
import com.moneytap.mediawiki.util.RecyclerViewOnItemClickHandler;

import java.util.List;

public class SearchResultListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Page> list;
    private final RecyclerViewEvents.Listener<Page> listener;

    public SearchResultListAdapter(LayoutInflater layoutInflater, List<Page> list,
                                   RecyclerViewEvents.Listener<Page> listener) {
        this.layoutInflater = layoutInflater;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        Page item = list.get(position);
        return item.getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case RecyclerViewEvents.FOOTER:
                return new FooterViewHolder(layoutInflater.inflate(R.layout.footer_progress_bar, parent, false));
            default:
                AdapterSerchResultListItemBinding binding = DataBindingUtil.inflate(layoutInflater,
                        R.layout.adapter_serch_result_list_item, parent, false);
                return new RecyclerViewHolder<>(binding);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            Page item = list.get(position);
            RecyclerViewHolder pageViewHolder = (RecyclerViewHolder) holder;
            pageViewHolder.getBinding().setVariable(BR.page, item);
            pageViewHolder.getBinding().setVariable(BR.clickListener,
                    new RecyclerViewOnItemClickHandler<>(item, position, listener));
            pageViewHolder.getBinding().executePendingBindings();

        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
