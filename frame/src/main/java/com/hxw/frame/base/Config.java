package com.hxw.frame.base;

/**
 * Created by hxw on 2017/2/8.
 */

public class Config {
    //超时时间,默认单位为秒
    public static final int TIME_OUT = 10;
    //图片缓存文件最大值为100Mb
    public static final int IMAGE_DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024;
    //通用运营系统接口
    public static final String CHECK_VERSION_WIDE_NET_URL =
            "http://123.159.193.22:7444/yyxt/CommonQueryObject.action";
    //文件下载路径
    public static final String DOWNLOAD_APK_WIDE_NET_URL =
            "http://123.159.193.22:1080/userfile/default/attach/";
    /**
     * 对应远端的键
     * 能耗原生 93bfe0ea901340a88123fde7c800b0a2
     * 企业点对点 287d4256a34742d6497f0b68f4824b49
     * 疲劳管家 9a20953243d779427d3e6a0acee03c9f
     */
    public static final String APP_CODE = "287d4256a34742d6497f0b68f4824b49";


}
