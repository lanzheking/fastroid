package com.honestwalker.android.commons.jscallback.bean;

/**
 * open_url 类型的参数实体对象。
 * title	新页面标题	店铺详情
 * url	新页面链接	http://fosun.com/store/detail?store_id=1
 */
public class OpenUrlParamBean extends JSActionParamBean {

    private String title;

    private String url;

    private boolean loading;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
