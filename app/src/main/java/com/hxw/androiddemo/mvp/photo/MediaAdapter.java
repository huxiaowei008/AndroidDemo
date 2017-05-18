package com.hxw.androiddemo.mvp.photo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hxw.androiddemo.R;
import com.hxw.frame.loader.media.ImageMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxw on 2017/5/15.
 */

public class MediaAdapter extends RecyclerView.Adapter {

    private List<ImageMedia> mMedias;
    private LayoutInflater mInflater;
    private int imageSize;//图片的大小
    private View.OnClickListener mOnMediaClickListener;

    public MediaAdapter(Context context, int count) {
        this.mInflater = LayoutInflater.from(context);
        this.mMedias = new ArrayList<>();
        this.imageSize = getImageItemWidth(context, count);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_media_show, parent, false);
        //把视图变成正方行并适配屏幕的大小
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = imageSize;
        params.height = imageSize;

        return new MediaHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MediaHolder mediaHolder = (MediaHolder) holder;
        ImageMedia imageMedia = mMedias.get(position);
        Glide.with(mediaHolder.img_media.getContext())
                .load("file://" + imageMedia.getThumbnailPath())
                .placeholder(R.drawable.ic_placeholder)
                .crossFade()
                .centerCrop()
                .override(imageSize, imageSize)
                .into(mediaHolder.img_media);

        mediaHolder.img_media.setTag(imageMedia);
        mediaHolder.img_media.setOnClickListener(mOnMediaClickListener);
    }

    @Override
    public int getItemCount() {
        return mMedias.size();
    }

    public void addAllData(@NonNull List<ImageMedia> data) {
        this.mMedias.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.mMedias.clear();
    }

    private static class MediaHolder extends RecyclerView.ViewHolder {
        ImageView img_media;

        MediaHolder(View itemView) {
            super(itemView);
            img_media = (ImageView) itemView.findViewById(R.id.img_media);

        }
    }

    public void setOnMediaClickListener(View.OnClickListener onMediaClickListener) {
        mOnMediaClickListener = onMediaClickListener;
    }

    public static int getImageItemWidth(Context context, int count) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
//        int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
//        float density = context.getResources().getDisplayMetrics().density;
        //(屏幕宽度-每一列的间隔宽度*(列数+1))/列数
        return (screenWidth - context.getResources()
                .getDimensionPixelOffset(R.dimen.media_margin) * (count + 1)) / count;
    }
}
