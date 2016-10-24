package com.honestwalker.android.commons.jscallback.bean;

/**
 * open_url 类型的参数实体对象。
 * title	新页面标题	店铺详情
 * url	新页面链接	http://fosun.com/store/detail?store_id=1
 */
public class OpenPageParamBean extends JSActionParamBean {

    private String title;

    private String name;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
