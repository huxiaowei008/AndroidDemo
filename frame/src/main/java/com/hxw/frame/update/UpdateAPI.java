package com.hxw.frame.update;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * Created by hxw on 2017/2/14.
 */

public interface UpdateAPI {

    /**
     * 检查更新的接口
     *
     * @param url  检查更新的地址
     * @param json 附带的json参数
     * @return
     */
    @GET()
    Observable<UpdateItem> checkUpdate(
            @Url String url,
            @Query("json") String json
    );

    /**
     * 下载的接口
     *
     * @param url 下载的地址
     * @return
     */
    @Streaming
    @GET()
    Flowable<Response<ResponseBody>> download(@Url String url);
}
