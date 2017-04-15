package com.hxw.frame.widget.imageloader;

import android.content.Context;

/**
 * Created by hxw on 2017/3/15.
 */

public interface IImageLoaderStrategy <T extends ImageConfig>{

    void loadImage(Context context, T config);
    void clear(Context context, T config);
}
