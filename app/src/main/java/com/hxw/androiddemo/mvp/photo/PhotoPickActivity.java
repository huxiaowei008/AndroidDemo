package com.hxw.androiddemo.mvp.photo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;
import com.hxw.frame.loader.IAlbumTaskCallback;
import com.hxw.frame.loader.LoaderCallbacks;
import com.hxw.frame.loader.MediaLoaderManager;
import com.hxw.frame.loader.media.AlbumEntity;
import com.hxw.frame.loader.media.BaseMedia;
import com.hxw.frame.loader.media.ImageMedia;
import com.hxw.frame.utils.WindowManagerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hxw on 2017/5/15.
 */

public class PhotoPickActivity extends BaseActivity {
    @BindView(R.id.tb_title)
    Toolbar tbTitle;
    @BindView(R.id.rv_photo)
    RecyclerView rvPhoto;
    @BindView(R.id.tv_pick_album)
    TextView tvPickAlbum;

    private MediaAdapter mediaAdapter;
    private AlbumAdapter albumAdapter;
    private PopupWindow mAlbumPopWindow;
    private LoaderCallbacks<ImageMedia> callbacks = new LoaderCallbacks<ImageMedia>() {//图片加载的回调
        @Override
        public void onResult(List<ImageMedia> data) {
            mediaAdapter.clearData();
            mediaAdapter.addAllData(data);
        }
    };

    /**
     * @return 返回布局资源ID
     */
    @Override
    public int getLayoutId() {
        return R.layout.acticity_photo_pick;
    }

    /**
     * 依赖注入的入口,提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
     *
     * @param appComponent 基础注入器
     */
    @Override
    public void componentInject(AppComponent appComponent) {

    }

    /**
     * 初始化，会在onCreate中执行
     */
    @Override
    public void init(Bundle savedInstanceState) {

        createToolbar();
        initRecyclerView();
        albumAdapter = new AlbumAdapter(PhotoPickActivity.this);//这需要先初始化
        createAlbumView();
        MediaLoaderManager.getInstance().loaderImage(this, callbacks);
        MediaLoaderManager.getInstance().loaderAlbum(this.getApplicationContext().getContentResolver(),
                new IAlbumTaskCallback() {
                    @Override
                    public void postAlbumList(@Nullable List<AlbumEntity> albums) {
                        if ((albums == null || albums.isEmpty())
                                && tvPickAlbum != null) {
                            tvPickAlbum.setCompoundDrawables(null, null, null, null);
                            tvPickAlbum.setOnClickListener(null);
                            return;
                        }
                        albumAdapter.addAllData(albums);
                    }
                });

    }

    private void initRecyclerView() {
        mediaAdapter = new MediaAdapter(this, 3);
        mediaAdapter.setOnMediaClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageMedia imageMedia=(ImageMedia)v.getTag();
                List<ImageMedia> list=new ArrayList<ImageMedia>();
                list.add(imageMedia);
                onFinish(list);
            }
        });
        rvPhoto.setLayoutManager(new GridLayoutManager(this, 3));
        rvPhoto.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelOffset(R.dimen.media_margin), 3));
        rvPhoto.setAdapter(mediaAdapter);
    }

    private void createToolbar() {

        setSupportActionBar(tbTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void createAlbumView() {
        tvPickAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAlbumPopWindow == null) {
                    int height = WindowManagerUtils.getScreenHeight(v.getContext()) -
                            (WindowManagerUtils.getToolbarHeight(v.getContext())
                                    + WindowManagerUtils.getStatusBarHeight(v.getContext()));
                    View windowView = createWindowView();
                    mAlbumPopWindow = new PopupWindow(windowView, ViewGroup.LayoutParams.MATCH_PARENT,
                            height, true);
                    mAlbumPopWindow.setOutsideTouchable(true);
                }
                mAlbumPopWindow.showAsDropDown(v, 0, 0);
            }

            @NonNull
            private View createWindowView() {
                View view = LayoutInflater.from(PhotoPickActivity.this).inflate(R.layout.view_album_list, null);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_album);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.addItemDecoration(new SpacesItemDecoration(2, 1));

                View albumShadowLayout = view.findViewById(R.id.album_shadow);
                albumShadowLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissAlbumWindow();
                    }
                });
                albumAdapter.setAlbumOnClickListener(new AlbumAdapter.OnAlbumClickListener() {
                    @Override
                    public void onClick(View view, int pos) {
                        if (albumAdapter != null && albumAdapter.getCurrentAlbumPos() != pos) {
                            List<AlbumEntity> albums = albumAdapter.getAlums();
                            albumAdapter.setCurrentAlbumPos(pos);
                            AlbumEntity albumMedia = albums.get(pos);
                            MediaLoaderManager.getInstance().loaderImage(PhotoPickActivity.this,
                                    albumMedia.mBucketId, callbacks);

                            tvPickAlbum.setText(albumMedia.mBucketName);
                            albumMedia.mIsSelected = true;
                            albumAdapter.notifyDataSetChanged();
                        }
                        dismissAlbumWindow();
                    }
                });
                recyclerView.setAdapter(albumAdapter);
                return view;
            }
        });
    }

    /**
     * 关闭相册窗口
     */
    private void dismissAlbumWindow() {
        if (mAlbumPopWindow != null && mAlbumPopWindow.isShowing()) {
            mAlbumPopWindow.dismiss();
        }
    }

    private void onFinish(List<ImageMedia> medias){
        Intent intent =new Intent();
        intent.putParcelableArrayListExtra("result",(ArrayList<ImageMedia>) medias);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
