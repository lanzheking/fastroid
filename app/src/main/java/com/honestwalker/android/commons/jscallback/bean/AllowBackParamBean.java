package com.honestwalker.android.commons.jscallback.bean;

/**
 * open_url 类型的参数实体对象。
 * title	新页面标题	店铺详情
 * url	新页面链接	http://fosun.com/store/detail?store_id=1
 */
public class AllowBackParamBean extends JSActionParamBean {

    private String allow;

    public String getAllow() {
        return allow;
    }

    public void setAllow(String allow) {
        this.allow = allow;
    }
}
