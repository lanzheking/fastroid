package com.honestwalker.android.spring.core.bean;

import java.util.ArrayList;
import java.util.List;

class Config {

    private List<SpringBean> springBeans = new ArrayList<>();

    public List<SpringBean> getSpringBeans() {
        return springBeans;
    }

    public void setSpringBeans(List<SpringBean> springBeans) {
        this.springBeans = springBeans;
    }
}
