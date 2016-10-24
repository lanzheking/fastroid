package com.honestwalker.android.commons.jscallback.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by honestwalker on 15-8-5.
 */
public class SearchHistoryToWebBean implements Serializable {

    private List<String> storeHistory;
    private List<String> activityHistory;
    private List<String> discountHistory;

    public List<String> getDiscountHistory() {
        return discountHistory;
    }

    public void setDiscountHistory(List<String> discountHistory) {
        this.discountHistory = discountHistory;
    }

    public List<String> getStoreHistory() {
        return storeHistory;
    }

    public void setStoreHistory(List<String> storeHistory) {
        this.storeHistory = storeHistory;
    }

    public List<String> getActivityHistory() {
        return activityHistory;
    }

    public void setActivityHistory(List<String> activityHistory) {
        this.activityHistory = activityHistory;
    }

}
