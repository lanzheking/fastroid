package com.honestwalker.android.commons.jscallback.bean;

/**
 * alert 类型的参数实体对象。
 * title	对话框标题	AA灯饰10月激情大促
 * content	对话框内容	豪华灯具5折起，其他品类8折起，期待您的光临
 */
public class AlertParamBean extends JSActionParamBean {

    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
