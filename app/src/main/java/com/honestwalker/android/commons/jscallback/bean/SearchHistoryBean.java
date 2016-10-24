package com.honestwalker.android.commons.jscallback.bean;

/**
 * alert 类型的参数实体对象。
 * title	对话框标题	AA灯饰10月激情大促
 * content	对话框内容	豪华灯具5折起，其他品类8折起，期待您的光临
 */
public class SearchHistoryBean extends JSActionParamBean {

    private String intent;
    private String type;
    private String value;

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
