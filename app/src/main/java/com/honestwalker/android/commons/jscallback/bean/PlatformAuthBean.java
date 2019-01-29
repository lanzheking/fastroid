package com.honestwalker.android.commons.jscallback.bean;

/**
 * share 类型的参数实体对象。
 * title	分享标题	AA灯饰10月激情大促
 * content	分享内容	豪华灯具5折起，其他品类8折起，期待您的光临
 * url	分享页面链接	http://fosun.com/store/detail?store_id=1
 * img	分享图片链接	http://fosun.com/img/HG23hg567MNBVG.png
 */
public class PlatformAuthBean extends JSActionParamBean {

    /** QQ Wechat SinaWeibo TencentWeibo QZone WechatMoments WechatFavorite */
    private String platform;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
