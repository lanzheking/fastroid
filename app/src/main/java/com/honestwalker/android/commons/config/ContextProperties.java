package com.honestwalker.android.commons.config;

/**
 * Created by honestwalker on 15-4-23.
 */
public class ContextProperties {
    private static ContextProperties config;

    public static ContextProperties getConfig() {
        if(config == null) {
            config = new ContextProperties();
        }
        return config;
    }

    /** 是否输出日志 */
    public boolean 	showLog;

    /** 是否发送错误报告 */
    public boolean sendErrorReport;

    /** 请求重试次数 */
    public int maxRequestTime;

    /** 请求超时时间 */
    public int requestTimeout;

    /** 缓存相对路径 */
    public String cachePath;

    /** 图片压缩最大宽度(像素) */
    public int imageMaxWidth;

    /** sd卡根目录下的缓存文件夹 */
    public String sdcartCacheName;

    public String webHost;

    public String appKey;
    public String appSecret;

}
