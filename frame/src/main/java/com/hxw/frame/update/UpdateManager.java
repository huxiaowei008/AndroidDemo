package com.hxw.frame.update;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.hxw.frame.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.base.Config;
import com.hxw.frame.utils.AppUtils;
import com.hxw.frame.utils.FileUtils;
import com.hxw.frame.utils.UIUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by hxw on 2017/2/14.
 */

public class UpdateManager {
    private static final String TAG = "UpdateManager";
    private Context mContext;
    private UpdateAPI updateAPI;
    private File cacheFile;//缓存文件
    //远端,服务器端信息
    private String mAppName;
    private String mUpdateContent;
    private int mRemoteVersionCode;
    private String mDownUrl;
    private String mRemoteVersionName;
    private String mRemoteResourceName;
    private OnUpdateListener update;
    private ProgressDialog progressDialog;

    @Inject
    public UpdateManager(Retrofit retrofit, File file) {
        this.updateAPI = retrofit.create(UpdateAPI.class);
        this.cacheFile = file;
        mDownUrl = Config.DOWNLOAD_APK_WIDE_NET_URL;
    }

    /**
     * 执行检查更新
     */
    public void checkUpdate(Context aContext, OnUpdateListener onUpdateListener) {
        this.mContext = aContext;
        this.update = onUpdateListener;
        check();
    }

    private void check() {
        String jsonContent = "{\n" +
                "  \"accessSession\": {\n" +
                "    \"username\": \"胡晓伟\",\n" +
                "    \"password\": \"49c5ab6e26430dde5a43b74531d7e154\",\n" +
                "    \"versionid\": \"100.0.16\"\n" +
                "  },\n" +
                "  \"cordition\": [\n" +
                "    {\n" +
                "      \"sfield\": \"czrjid\",\n" +
                "      \"scomparison\": \"eqref\",\n" +
                "      \"svalue\": \"" + Config.APP_CODE + "\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"curPageNo\": \"0\",\n" +
                "  \"idquery\": \"\",\n" +
                "  \"object\": \"appslc\",\n" +
                "  \"objectJsonName\": \"appxm01_1469501521591\",\n" +
                "  \"pageSize\": \"1\",\n" +
                "  \"userid\": \"8a57527145e8d4f6008538f70e38fcfc\",\n" +
                "}";

        /**1、subscribeOn的调用切换之前的线程。
         2、observeOn的调用切换之后的线程。
         3、observeOn之后,不可再调用subscribeOn切换线程
         扔物线 大神给的总结:
         1、下面提到的“操作”包括产生事件、用操作符操作事件以及最终的通过 subscriber 消费事件；
         2、只有第一subscribeOn() 起作用（所以多个 subscribeOn() 无意义）；
         3、这个 subscribeOn() 控制从流程开始的第一个操作，直到遇到第一个 observeOn()；
         4、observeOn() 可以使用多次，每个 observeOn() 将导致一次线程切换()，
         这次切换开始于这次 observeOn() 的下一个操作；
         5、不论是 subscribeOn() 还是 observeOn()，每次线程切换如果不受到下一个 observeOn() 的干预，
         线程将不再改变，不会自动切换到其他线程。
         subscribeOn(): 指定 subscribe() 所发生的线程，
         即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
         observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
         **/
        updateAPI
                .checkUpdate(Config.CHECK_VERSION_WIDE_NET_URL, jsonContent)
                //主要改变的是订阅的线程，即call()执行的线程,只执行一次,后面在调用无效
                .subscribeOn(Schedulers.io())
                //告诉取消订阅在子线程中
                .unsubscribeOn(Schedulers.io())
                //主要改变的是发送的线程，即onNext()执行的线程,可多次,改变后面代码运行的线程
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((BaseActivity) mContext)
                        .<UpdateItem>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Consumer<UpdateItem>() {
                    @Override
                    public void accept(UpdateItem updateItem) throws Exception {
                        List<UpdateItem.CommonQueryObjectBean> list = updateItem
                                .getCommonQueryObject().get(0);
                        for (UpdateItem.CommonQueryObjectBean lItem : list) {
                            if (lItem.getKey().equals("rjbbh")) {
                                String lSvalue = lItem.getValue();
                                mRemoteVersionName = lSvalue;
                                mRemoteVersionCode = Integer.parseInt(lSvalue.replace(".", ""));
                            }
                            if (lItem.getKey().equals("gxnr")) {
                                String lSvalue = lItem.getValue();
                                mUpdateContent = lSvalue;
                            }
                            if (lItem.getKey().equals("czrjidname")) {
                                String lSvalue = lItem.getValue();
                                mAppName = lSvalue;
                            }
                            if (lItem.getKey().equals("appwj")) {
                                String lSvalue = lItem.getValue();
                                lSvalue.replace(";", "");
                                mRemoteResourceName = lSvalue;
                                mDownUrl = mDownUrl + mRemoteResourceName;
                            }
                        }

                        if (hasNewVersion()) {
                            //有新版本
                            update.isUpdate();
                        } else {
                            update.noUpdate();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.tag(TAG).e(throwable, "自动更新检查失败");
                        UIUtils.makeText("检查更新失败");
                        update.error();
                    }
                });

    }

    /**
     * 比对版本号，判断是否是低版本
     *
     * @return
     */
    private boolean hasNewVersion() {
        if (mRemoteVersionCode > AppUtils.getVersionCode(mContext)) {
            Timber.tag(TAG).d("hasNewVersion");
            return true;
        }
        Timber.tag(TAG).d("noNewVersion");
        return false;
    }

    /**
     * 通知更新对话框
     */
    public void showNoticeUpdate() {

        new AlertDialog.Builder(mContext)
                .setTitle(R.string.update)
                .setMessage(mUpdateContent)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        downloadAPK();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();

    }

    /**
     * 下载apk
     */
    private void downloadAPK() {
        //建立更新progressDialog
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle(R.string.update);
        progressDialog.setContentMessage(R.string.updating);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        updateAPI.download(mDownUrl)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<Response<ResponseBody>, Publisher<DownloadStatus>>() {
                    @Override
                    public Publisher<DownloadStatus> apply
                            (final Response<ResponseBody> responseBodyResponse) throws Exception {
                        return Flowable.create(new FlowableOnSubscribe<DownloadStatus>() {
                            @Override
                            public void subscribe(FlowableEmitter<DownloadStatus> e) throws Exception {
                                saveFile(e, responseBodyResponse);
                            }
                        }, BackpressureStrategy.LATEST);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .compose(((BaseActivity) mContext)
                        .<DownloadStatus>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Observer<DownloadStatus>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DownloadStatus status) {
                        progressDialog.setDate(status.getDownloadSize(),
                                status.getTotalSize());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.tag(TAG).e(e);
                    }

                    @Override
                    public void onComplete() {
                        Timber.tag(TAG).d("下载完成");
                        progressDialog.dismiss();
                        AppUtils.installApp(mContext, new File(cacheFile, mRemoteVersionCode + mAppName + ".apk"));

                    }
                });
    }

    /**
     * 保存文件
     *
     * @param emitter
     * @param responseBodyResponse
     */
    private void saveFile(FlowableEmitter<? super DownloadStatus> emitter,
                          Response<ResponseBody> responseBodyResponse) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File apk = new File(cacheFile, mRemoteVersionCode + mAppName + ".apk");
        if (apk.exists()) {//判断文件是否存在
            apk.delete();
        }

        int readLength;//读取的长度
        int downloadSize = 0;//已下载的大小
        byte[] buffer = new byte[8192];//缓冲数据,最大8kb
        DownloadStatus status = new DownloadStatus();
        long contentLength = responseBodyResponse.body().contentLength();//获取返回内容总大小
        status.setTotalSize(contentLength);
        inputStream = responseBodyResponse.body().byteStream();//打开输入流

        try {
            outputStream = new FileOutputStream(apk);//打开输出流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            emitter.onError(e);
        }

        try {
            //read( byte b[ ] )
            //读取多个字节，放置到字节数组b中，通常读取的字节数量为b的长度，
            //返回值为实际读取的字节的数量,到未尾时都返回-1

            while ((readLength = inputStream.read(buffer)) != -1) {
                //write( byte b[ ], int off, int len )
                //把字节数组b中从下标off开始，长度为len的字节写入流中
                outputStream.write(buffer, 0, readLength);
                downloadSize += readLength;
                status.setDownloadSize(downloadSize);
                emitter.onNext(status);

            }

            //刷空输出流，并输出所有被缓存的字节，由于某些流支持缓存功能，
            //该方法将把缓存中所有内容强制输出到流中
            outputStream.flush(); // This is important!!!

            emitter.onComplete();

        } catch (IOException e) {
            e.printStackTrace();
            emitter.onError(e);
        } finally {
            //关闭流
            FileUtils.closeQuietly(inputStream);
            FileUtils.closeQuietly(outputStream);
            FileUtils.closeQuietly(responseBodyResponse.body());
        }

    }

}
