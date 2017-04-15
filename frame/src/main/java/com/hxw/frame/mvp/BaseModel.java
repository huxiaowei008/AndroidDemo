package com.hxw.frame.mvp;

import com.hxw.frame.integration.IRepositoryManager;

/**
 * Created by hxw on 2017/2/9.
 */

public class BaseModel implements IModel {
    protected final String TAG = this.getClass().getSimpleName();
    protected IRepositoryManager mRepositoryManager;//用于管理网络请求层,以及数据缓存层

    public BaseModel(IRepositoryManager repositoryManager) {
        this.mRepositoryManager = repositoryManager;
    }

    /**
     * 销毁，释放资源
     */
    @Override
    public void onDestroy() {
        mRepositoryManager = null;
    }
}
