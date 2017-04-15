package com.hxw.androiddemo.base;

import android.content.Context;

import com.hxw.androiddemo.api.ComAPI;
import com.hxw.androiddemo.api.ComCache;
import com.hxw.frame.di.module.GlobeConfigModule;
import com.hxw.frame.http.GlobeHttpHandler;
import com.hxw.frame.http.OnResponseErrorListener;
import com.hxw.frame.integration.ConfigModule;
import com.hxw.frame.integration.IRepositoryManager;
import com.hxw.frame.utils.UIUtils;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by hxw on 2017/4/14.
 */

public class GlobalConfiguration implements ConfigModule {
    /**
     * 使用{@link GlobeConfigModule.Builder}给框架配置一些配置参数
     *
     * @param context
     * @param builder
     */
    @Override
    public void applyOptions(Context context, GlobeConfigModule.Builder builder) {
        builder.baseUrl(Constant.BASE_URL)
                .globeHttpHandler(new GlobeHttpHandler() {// 这里可以提供一个全局处理Http请求和响应结果的处理类,
                    // 这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
                    @Override
                    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
                        /* 这里可以先客户端一步拿到每一次http请求的结果,可以解析成json,做一些操作,如检测到token过期后
                           重新请求token,并重新执行请求 */
//                        try {
//                            if (!TextUtils.isEmpty(httpResult) && RequestInterceptor.isJson(response.body())) {
//                                JSONArray array = new JSONArray(httpResult);
//                                JSONObject object = (JSONObject) array.get(0);
//                                String login = object.getString("login");
//                                String avatar_url = object.getString("avatar_url");
//                                Timber.w("Result ------> " + login + "    ||   Avatar_url------> " + avatar_url);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            return response;
//                        }


                     /* 这里如果发现token过期,可以先请求最新的token,然后在拿新的token放入request里去重新请求
                        注意在这个回调之前已经调用过proceed,所以这里必须自己去建立网络请求,如使用okhttp使用新的request去请求
                        create a new request and modify it accordingly using the new token
                        Request newRequest = chain.request().newBuilder().header("token", newToken)
                                             .build();

                        retry the request

                        response.body().close();
                        如果使用okhttp将新的请求,请求成功后,将返回的response  return出去即可
                        如果不需要返回新的结果,则直接把response参数返回出去 */

                        return response;
                    }

                    // 这里可以在请求服务器之前可以拿到request,做一些操作比如给request统一添加token或者header以及参数加密等操作
                    @Override
                    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                        /* 如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的requeat如增加header,不做操作则直接返回request参数
                           return chain.request().newBuilder().header("token", tokenId)
                                  .build(); */
                        return request;
                    }
                })
                .responseErrorListener(new OnResponseErrorListener() {
                    @Override
                    public void handleResponseError(Exception e) {
                             /* 用来提供处理所有错误的监听
                       rxjava必要要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法),此监听才生效 */
                        Timber.w("------------>" + e.getMessage());
                        UIUtils.showSnackBar("net error", 0);
                    }
                });
    }

    /**
     * 使用{@link IRepositoryManager}给框架注入一些网络请求和数据缓存等服务
     *
     * @param context
     * @param repositoryManager
     */
    @Override
    public void registerComponents(Context context, IRepositoryManager repositoryManager) {
        repositoryManager.injectRetrofitService(ComAPI.class);
        repositoryManager.injectCacheService(ComCache.class);

    }
}
