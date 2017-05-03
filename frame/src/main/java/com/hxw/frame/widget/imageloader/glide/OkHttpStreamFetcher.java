package com.hxw.frame.widget.imageloader.glide;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Fetches an {@link InputStream} using the okhttp3library.
 * Created by hxw on 2017/2/13.
 */

public class OkHttpStreamFetcher implements DataFetcher<InputStream> {

    private final Call.Factory client;
    private final GlideUrl url;
    private InputStream stream;
    private ResponseBody responseBody;
    private volatile Call call;

    public OkHttpStreamFetcher(Call.Factory client, GlideUrl url) {
        this.client = client;
        this.url = url;
    }

    /**
     * Asynchronously fetch data from which a resource can be decoded. This will always be called on
     * background thread so it is safe to perform long running tasks here. Any third party libraries called
     * must be thread safe since this method will be called from a thread in a
     * {@link ExecutorService} that may have more than one background thread.
     * <p>
     * This method will only be called when the corresponding resource is not in the cache.
     * <p>
     * <p>
     * Note - this method will be run on a background thread so blocking I/O is safe.
     * </p>
     *
     * @param priority The priority with which the request should be completed.
     * @see #cleanup() where the data retuned will be cleaned up
     */
    @Override
    public InputStream loadData(Priority priority) throws Exception {
        Request.Builder requestBuilder = new Request.Builder().url(url.toStringUrl());

        for (Map.Entry<String, String> headerEntry : url.getHeaders().entrySet()) {
            String key = headerEntry.getKey();
            requestBuilder.addHeader(key, headerEntry.getValue());
        }
        Request request = requestBuilder.build();

        Response response;
        call = client.newCall(request);
        response = call.execute();
        responseBody = response.body();
        if (!response.isSuccessful()) {
            throw new IOException("Request failed with code: " + response.code());
        }

        long contentLength = responseBody.contentLength();
        stream = ContentLengthInputStream.obtain(responseBody.byteStream(), contentLength);
        return stream;
    }

    /**
     * Cleanup or recycle any resources used by this data fetcher. This method will be called in a finally block
     * after the data returned by {@link #loadData(Priority)} has been decoded by the
     * {@link ResourceDecoder}.
     * <p>
     * <p>
     * Note - this method will be run on a background thread so blocking I/O is safe.
     * </p>
     */
    @Override
    public void cleanup() {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            // Ignored
        }
        if (responseBody != null) {
            responseBody.close();
        }
    }

    /**
     * Returns a string uniquely identifying the data that this fetcher will fetch including the specific size.
     * <p>
     * <p>
     * A hash of the bytes of the data that will be fetched is the ideal id but since that is in many cases
     * impractical, urls, file paths, and uris are normally sufficient.
     * </p>
     * <p>
     * <p>
     * Note - this method will be run on the main thread so it should not perform blocking operations and should
     * finish quickly.
     * </p>
     */
    @Override
    public String getId() {
        return url.getCacheKey();
    }

    /**
     * A method that will be called when a load is no longer relevant and has been cancelled. This method does not need
     * to guarantee that any in process loads do not finish. It also may be called before a load starts or after it
     * finishes.
     * <p>
     * <p>
     * The best way to use this method is to cancel any loads that have not yet started, but allow those that are in
     * process to finish since its we typically will want to display the same resource in a different view in
     * the near future.
     * </p>
     * <p>
     * <p>
     * Note - this method will be run on the main thread so it should not perform blocking operations and should
     * finish quickly.
     * </p>
     */
    @Override
    public void cancel() {
        Call local = call;
        if (local != null) {
            local.cancel();
        }
    }
}
