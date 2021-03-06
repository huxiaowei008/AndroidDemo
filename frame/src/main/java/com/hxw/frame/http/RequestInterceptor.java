package com.hxw.frame.http;

import android.support.annotation.Nullable;

import com.hxw.frame.utils.StringUtils;
import com.hxw.frame.utils.ZipHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import timber.log.Timber;

/**
 * 请求拦截器,主要打印返回结果
 * Created by hxw on 2017/2/8.
 */
@Singleton
public class RequestInterceptor implements Interceptor {
    private GlobalHttpHandler mHandler;

    @Inject
    public RequestInterceptor(@Nullable GlobalHttpHandler handler) {
        this.mHandler = handler;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        boolean hasRequestBody = (request.body() != null);

        //打印请求信息
        Timber.tag(getTag(request, "Request_Info"))
                .w("Params : 「 %s 」%nConnection : 「 %s 」%nHeaders : %n「 %s 」",
                        hasRequestBody ? parseParams(request.newBuilder().build().body()) : "Null",
                        chain.connection(),
                        request.headers());

        long t1 = System.nanoTime();

        Response originalResponse;
        try {
            originalResponse = chain.proceed(request);
        } catch (Exception e) {
            Timber.w("Http Error: " + e);
            throw e;
        }
        long t2 = System.nanoTime();

        String bodySize = originalResponse.body().contentLength() != -1 ?
                originalResponse.body().contentLength() + "-byte" : "unknown-length";

        //打印响应时间以及响应头
        Timber.tag(getTag(request, "Response_Info"))
                .w("Received response in [ %d-ms ] , [ %s ]%n%s",
                        TimeUnit.NANOSECONDS.toMillis(t2 - t1),
                        bodySize,
                        originalResponse.headers());

//        //如果是下载文件就不拦截打印信息
//        if (originalResponse.headers().get("Content-Type")
//                .equals("application/vnd.android.package-archive")) {
//            return originalResponse;
//        }

        //打印响应结果
        String bodyString = printResult(request, originalResponse.newBuilder().build());

        if (mHandler != null)//这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
        {
            return mHandler.onHttpResultResponse(bodyString, chain, originalResponse);
        }

        return originalResponse;
    }

    private String getTag(Request request, String tag) {
        return String.format(" [%s] 「 %s 」>>> %s", request.method(), request.url().toString(), tag);
    }

    /**
     * 打印响应结果
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @Nullable
    private String printResult(Request request, Response response) throws IOException {
        //读取服务器返回的结果
        ResponseBody responseBody = response.body();
        String bodyString = null;
        if (isParseable(responseBody.contentType())) {
            try {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                //获取content的压缩类型
                String encoding = response
                        .headers()
                        .get("Content-Encoding");

                Buffer clone = buffer.clone();


                //解析response content
                bodyString = parseContent(responseBody, encoding, clone);
            } catch (IOException e) {
                e.printStackTrace();
            }


            Timber.tag(getTag(request, "Response_Result")).w(isJson(responseBody.contentType()) ?
                    StringUtils.jsonFormat(bodyString) : isXml(responseBody.contentType()) ?
                    StringUtils.xmlFormat(bodyString) : bodyString);

        } else {
            Timber.tag(getTag(request, "Response_Result"))
                    .w("This result isn't parsed");
        }
        return bodyString;
    }

    /**
     * 解析服务器响应的内容
     *
     * @param responseBody
     * @param encoding
     * @param clone
     * @return
     */
    private String parseContent(ResponseBody responseBody, String encoding, Buffer clone) {
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        if (encoding != null && "gzip".equalsIgnoreCase(encoding)) {//content使用gzip压缩
            return ZipHelper.decompressForGzip(clone.readByteArray(), convertCharset(charset));//解压
        } else if (encoding != null && "zlib".equalsIgnoreCase(encoding)) {//content使用zlib压缩
            return ZipHelper.decompressToStringForZlib(clone.readByteArray(), convertCharset(charset));//解压
        } else {//content没有被压缩
            return clone.readString(charset);
        }
    }


    public String parseParams(RequestBody body) throws UnsupportedEncodingException {
        if (isParseable(body.contentType())) {
            try {
                Buffer requestBuffer = new Buffer();
                body.writeTo(requestBuffer);
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = body.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }
                return URLDecoder.decode(requestBuffer.readString(charset), convertCharset(charset));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "This params isn't parsed";
    }

    public static boolean isParseable(MediaType mediaType) {
        if (mediaType == null) {
            return false;
        }
        return mediaType.toString().toLowerCase().contains("text")
                || isJson(mediaType) || isForm(mediaType)
                || isHtml(mediaType) || isXml(mediaType);
    }

    public static boolean isJson(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("json");
    }

    public static boolean isXml(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("xml");
    }

    public static boolean isHtml(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("html");
    }

    public static boolean isForm(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("x-www-form-urlencoded");
    }

    private String convertCharset(Charset charset) {
        String s = charset.toString();
        int i = s.indexOf("[");
        if (i == -1) {
            return s;
        }
        return s.substring(i + 1, s.length() - 1);
    }
}
