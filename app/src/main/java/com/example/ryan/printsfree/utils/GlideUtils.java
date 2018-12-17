package com.example.ryan.printsfree.utils;

import android.content.Context;
import android.media.Image;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ryan.printsfree.R;

/**
 * Created by ryan on 2018/12/10.
 */

public class GlideUtils {


    public static void load(Context context, String url, ImageView iv) {
        load(context, url, iv, R.mipmap.ic_placeholder);
    }

    public static void load(Context context, String url, ImageView iv, int placeholderId) {
        Glide.with(context)
                .load(url)
                .placeholder(placeholderId)
                .into(iv);
    }


}
