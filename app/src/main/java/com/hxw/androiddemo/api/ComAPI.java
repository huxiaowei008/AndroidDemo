package com.hxw.androiddemo.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 放网络的接口
 * Created by hxw on 2017/2/11.
 */

public interface ComAPI {
    //套餐获取
    @GET("api/getServiceSet")
    Observable<String> getSetMeal(
            @Query("cno") String cno//二维码编号
    );
}
