package com.honestwalker.androidutils.net;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;

/**
 * Created by lanzhe on 17-6-21.
 */

public class XutilsHttpResultParser implements ResponseParser {

    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {

        XutilsHttpsResponseEntity responseEntity = new XutilsHttpsResponseEntity();
        responseEntity.setResult(result);
        //返回ResponseEntity对象
        return responseEntity;
    }

}
