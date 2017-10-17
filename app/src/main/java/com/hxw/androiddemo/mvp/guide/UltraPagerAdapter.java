package com.hxw.androiddemo.mvp.guide;

import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hxw.androiddemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxw on 2017/8/10.
 */

public class UltraPagerAdapter extends PagerAdapter {

    private List<Integer> imgRes;

    public UltraPagerAdapter(@DrawableRes int... resIds) {
        imgRes = new ArrayList<>();
        for (int resId : resIds) {
            imgRes.add(resId);
        }
    }

    @Override
    public int getCount() {
        return imgRes.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(container.getContext())
                .inflate(R.layout.layout_viewpager, null);
        ImageView imageView = (ImageView) linearLayout.findViewById(R.id.img_item);
        imageView.setImageResource(imgRes.get(position));

        container.addView(linearLayout);

        return linearLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LinearLayout view = (LinearLayout) object;
        container.removeView(view);
    }
}
