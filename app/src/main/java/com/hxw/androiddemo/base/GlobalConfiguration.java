package com.hxw.androiddemo.base;

import android.app.Application;
import android.content.Context;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.hxw.androiddemo.BuildConfig;
import com.hxw.frame.base.App;
import com.hxw.frame.base.delegate.AppLifecycles;
import com.hxw.frame.di.module.AppModule;
import com.hxw.frame.di.module.GlobalConfigModule;
import com.hxw.frame.http.GlobalHttpHandler;
import com.hxw.frame.http.OnResponseErrorListener;
import com.hxw.frame.integration.ConfigModule;
import com.hxw.frame.utils.NullStringToEmptyAdapterFactory;
import com.hxw.frame.utils.UIUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.HttpException;
import timber.log.Timber;

/**
 * Created by hxw on 2017/4/14.
 */

public final class GlobalConfiguration implements ConfigModule {
    /**
     * 使用{@link GlobalConfigModule.Builder}给框架配置一些配置参数
     *
     * @param context
     * @param builder
     */
    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        builder.baseUrl(Constant.BASE_URL)
                .globeHttpHandler(new GlobalHttpHandler() {// 这里可以提供一个全局处理Http请求和响应结果的处理类,
                    // 这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
                    @Override
                    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
                        /* 这里可以先客户端一步拿到每一次http请求的结果,可以解析成json,做一些操作,如检测到token过期后
                           重新请求token,并重新执行请求 */
//                        try {
//                            if (!TextUtils.isEmpty(httpResult) && RequestInterceptor.isJson(response.body().contentType())) {
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
                    public void handleResponseError(Context context, Throwable t) {
                        /* 用来提供处理所有错误的监听
                       rxjava必要要使用ErrorSubscriber(默认实现Subscriber的onError方法),此监听才生效 */
                        Timber.tag("Catch-Error").w(t.getMessage());
                        //这里不光是只能打印错误,还可以根据不同的错误作出不同的逻辑处理
                        String msg = "未知错误";
                        if (t instanceof UnknownHostException) {
                            msg = "网络不可用";
                        } else if (t instanceof SocketTimeoutException) {
                            msg = "请求网络超时";
                        } else if (t instanceof HttpException) {
                            HttpException httpException = (HttpException) t;
                            msg = convertStatusCode(httpException);
                        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
                            msg = "数据解析错误";
                        }
                        UIUtils.showSnackBar(msg, 0);
                    }
                })
                .gsonConfiguration(new AppModule.GsonConfiguration() {
                    @Override
                    public void configGson(Context context, GsonBuilder builder) {
                        builder.setDateFormat("yyyy-MM-dd HH:mm:ss")
                                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<>());
                    }
                });
    }

    /**
     * 使用{@link AppLifecycles}在Application的声明周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     * @return
     */
    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        // AppLifecycles 的所有方法都会在BaseApplication对应的生命周期中被调用,
        // 所以在对应的方法中可以扩展一些自己需要的逻辑
        lifecycles.add(new AppLifecycles() {
            @Override
            public void attachBaseContext(Context base) {
//                MultiDex.install(base);  //这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
            }

            @Override
            public void onCreate(Application application) {
                if (LeakCanary.isInAnalyzerProcess(application)) {
                    // This process is dedicated to LeakCanary for heap analysis.
                    // You should not init your app in this process.
                    return;
                }

                //Timber日志打印
                if (BuildConfig.LOG_DEBUG) {
                    //Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
                    //并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
                    //比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
                    Timber.plant(new Timber.DebugTree());
                    // 如果你想将框架切换为 Logger 来打印日志,请使用下面的代码,如想切换为其他日志框架请根据下面的方式扩展
//                    Logger.addLogAdapter(new AndroidLogAdapter());
//                    Timber.plant(new Timber.DebugTree() {
//                        @Override
//                        protected void log(int priority, String tag, String message, Throwable t) {
//                            Logger.log(priority, tag, message, t);
//                        }
//                    });
                }

                //安装leakCanary检测内存泄露
                ((App) application).getAppComponent().extras().put(RefWatcher.class.getName(), BuildConfig.USE_CANARY ?
                        LeakCanary.install(application) : RefWatcher.DISABLED);

            }

            @Override
            public void onTerminate(Application application) {

            }
        });
    }

    /**
     * 使用{@link Application.ActivityLifecycleCallbacks}在Activity的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {

    }

    /**
     * 使用{@link FragmentManager.FragmentLifecycleCallbacks}在Fragment的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {

            @Override
            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建是重复利用已经创建的Fragment。
                // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
                // 如果在 XML 中使用 <Fragment/> 标签,的方式创建 Fragment 请务必在标签中加上 android:id 或者 android:tag 属性,否则 setRetainInstance(true) 无效
                // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
                f.setRetainInstance(true);
            }

            /**
             * Called after the fragment has returned from the FragmentManager's call to
             * {@link Fragment#onDestroy()}.
             *
             * @param fm Host FragmentManager
             * @param f  Fragment changing state
             */
            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                ((RefWatcher) ((App) f.getActivity()
                        .getApplication())
                        .getAppComponent()
                        .extras()
                        .get(RefWatcher.class.getName()))
                        .watch(f);
            }
        });
    }

    private String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }
}
