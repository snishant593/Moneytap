package com.moneytap.mediawiki.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.moneytap.mediawiki.R;
import java.util.Random;



public class BindingUtils {

    private static final String TAG = BindingUtils.class.getSimpleName();

    @BindingAdapter({"android:src"})
    public static void loadImage(ImageView view, String url) {
        int position = new Random().nextInt(12);
        int[] rainbow = view.getContext().getResources().getIntArray(R.array.place_holder);
        Glide.with(view.getContext()).load(url)
                .placeholder(new ColorDrawable(rainbow[position]))
                .centerCrop()
                .dontAnimate()
                .dontTransform()
                .into(view);
    }
}
