package com.lab.fx.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.lab.fx.library.R;

/**
 * Created by febri on 11/04/17.
 */

public class MediaUtil {

    public static Bitmap getImage(Context p_context, String p_uri) {
        return null;
    }

    public static Bitmap getImageCircle(Context p_context, String p_uri) {
        return null;
    }

    public static Drawable getDrawable(Context p_context, int p_res_drawable, int p_res_dimen) {
        Drawable drawable = p_context.getResources().getDrawable(p_res_drawable);
        int      size     = p_context.getResources().getDimensionPixelSize(p_res_dimen);
        drawable.setBounds(0, 0, size, size);
        return drawable;
    }

}
