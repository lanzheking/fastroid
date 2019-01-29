package com.honestwalker.androidutils.net;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by hkyy on 16-10-14.
 */
@HttpResponse(parser = XutilsHttpResultParser.class)
public class XutilsHttpsResponseEntity {

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}