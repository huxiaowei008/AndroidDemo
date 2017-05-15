package com.hxw.androiddemo.mvp.photo;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;
import com.hxw.frame.loader.LoaderCallbacks;
import com.hxw.frame.loader.LocalMediaLoader;
import com.hxw.frame.loader.media.BaseMedia;

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

    private MediaAdapter adapter;

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
     * @param appComponent
     */
    @Override
    public void componentInject(AppComponent appComponent) {

    }

    /**
     * 初始化，会在onCreate中执行
     */
    @Override
    public void init() {

        createToolbar();
        initRecyclerView();
        this.getSupportLoaderManager().initLoader(0, null, new LocalMediaLoader(this,
                LocalMediaLoader.TYPE_IMAGE, new LoaderCallbacks<BaseMedia>() {
            @Override
            public void onResult(List<BaseMedia> data) {
                adapter.addAllData(data);
            }
        }));
    }

    private void initRecyclerView() {
        adapter = new MediaAdapter(this,3);

        rvPhoto.setLayoutManager(new GridLayoutManager(this, 3));
        rvPhoto.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelOffset(R.dimen.media_margin), 3));
        rvPhoto.setAdapter(adapter);
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
}
