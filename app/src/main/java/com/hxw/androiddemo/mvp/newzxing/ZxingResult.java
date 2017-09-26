package com.hxw.androiddemo.mvp.newzxing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;

import java.io.ByteArrayOutputStream;

/**
 * Created by hxw on 2017/9/25.
 */

public class ZxingResult {
    public Result result;
    public float scaleFactor = 1.0f;
    public Bitmap barcode;

    public void bundleThumbnail(PlanarYUVLuminanceSource source) {
        int[] pixels = source.renderThumbnail();
        int width = source.getThumbnailWidth();
        int height = source.getThumbnailHeight();
        Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);

        byte[] compressedBitmap = out.toByteArray();
        if (compressedBitmap != null) {
            barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
            // Mutable copy:
            barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
        }
        scaleFactor = (float) width / source.getWidth();
    }
}
