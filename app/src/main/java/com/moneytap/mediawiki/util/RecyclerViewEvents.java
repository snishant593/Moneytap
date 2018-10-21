package com.moneytap.mediawiki.util;

import android.view.View;

public class RecyclerViewEvents {

    /**
     * Callback interface for delivering item clicks.
     */
    public interface Listener<T> {
        /**
         * Called when a item is clicked.
         */
        public void onItemClick(T item, View v, int position);
    }


    public static final int ITEM = 0;

    public static final int FOOTER = 1;
}
