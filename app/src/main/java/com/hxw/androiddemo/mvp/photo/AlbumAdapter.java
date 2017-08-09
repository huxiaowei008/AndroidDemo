package com.hxw.androiddemo.mvp.photo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxw.androiddemo.R;
import com.hxw.frame.loader.media.AlbumEntity;
import com.hxw.frame.loader.media.ImageMedia;
import com.hxw.frame.widget.imageloader.glide.GlideApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by hxw on 2017/5/17.
 */

public class AlbumAdapter extends RecyclerView.Adapter {

    private List<AlbumEntity> mAlums;
    private LayoutInflater mInflater;
    private OnAlbumClickListener mAlbumOnClickListener;
    private int mCurrentAlbumPos;//当前选中的位置

    public AlbumAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mAlums = new ArrayList<>();
        this.mAlums.add(AlbumEntity.createDefaultAlbum());

    }

    public void setAlbumOnClickListener(OnAlbumClickListener albumOnClickListener) {
        this.mAlbumOnClickListener = albumOnClickListener;
    }

    public int getCurrentAlbumPos() {
        return mCurrentAlbumPos;
    }

    public void setCurrentAlbumPos(int currentAlbumPos) {
        mCurrentAlbumPos = currentAlbumPos;
    }

    public AlbumEntity getCurrentAlbum() {
        if (mAlums == null || mAlums.size() <= 0) {
            return null;
        }
        return mAlums.get(mCurrentAlbumPos);
    }

    public void addAllData(List<AlbumEntity> alums) {
        mAlums.clear();
        mAlums.addAll(alums);
        notifyDataSetChanged();
    }

    public List<AlbumEntity> getAlums() {
        return mAlums;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlbumViewHolder(mInflater.inflate(R.layout.item_album, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AlbumViewHolder albumViewHolder = (AlbumViewHolder) holder;
        AlbumEntity album = mAlums.get(position);
        if (album != null && album.hasImages()) {
            albumViewHolder.mNameTxt.setText(album.mBucketName);
            ImageMedia media = album.mImageList.get(0);
            if (media != null) {

                GlideApp.with(albumViewHolder.mCheckedImg.getContext())
                        .load("file://" + media.getPath())
                        .placeholder(R.drawable.ic_placeholder)
                        .centerCrop()
                        .override(48, 48)
                        .into(albumViewHolder.mCoverImg);
            }
            albumViewHolder.mLayout.setTag(position);
            albumViewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAlbumOnClickListener != null) {
                        mAlums.get(mCurrentAlbumPos).mIsSelected=false;
                        mAlbumOnClickListener.onClick(v, (Integer) v.getTag());
                    }
                }
            });
            albumViewHolder.mCheckedImg.setVisibility(album.mIsSelected ? View.VISIBLE : View.GONE);
            albumViewHolder.mSizeTxt.setText(String.format(Locale.CHINESE, "(%d)", album.mCount));
        } else {
            albumViewHolder.mNameTxt.setText("?");
            albumViewHolder.mSizeTxt.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mAlums != null ? mAlums.size() : 0;
    }

    private static class AlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView mCoverImg;
        TextView mNameTxt;
        TextView mSizeTxt;
        View mLayout;
        ImageView mCheckedImg;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            mCoverImg = (ImageView) itemView.findViewById(R.id.img_album_thumbnail);
            mNameTxt = (TextView) itemView.findViewById(R.id.tv_album_name);
            mSizeTxt = (TextView) itemView.findViewById(R.id.tv_album_size);
            mLayout = itemView.findViewById(R.id.rl_album);
            mCheckedImg = (ImageView) itemView.findViewById(R.id.img_album_checked);
        }
    }

    public interface OnAlbumClickListener {
        void onClick(View view, int pos);
    }
}
