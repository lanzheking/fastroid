package com.honestwalker.android.commons.jscallback.bean;

/**
 * open_url 类型的参数实体对象。
 * title	新页面标题	店铺详情
 * url	新页面链接	http://fosun.com/store/detail?store_id=1
 */
public class ShowHomeBackParamBean extends JSActionParamBean {

    private Boolean show;

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }
}
