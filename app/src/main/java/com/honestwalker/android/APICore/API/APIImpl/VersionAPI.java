package com.honestwalker.android.APICore.API.APIImpl;

import android.content.Context;

import com.honestwalker.android.APICore.API.APIListener;
import com.honestwalker.android.APICore.API.APISupport;
import com.honestwalker.android.APICore.API.req.CheckverisonReq;
import com.honestwalker.android.APICore.API.resp.CheckversionResp;

/**
 * Created by honestwalker on 15-8-22.
 */
public class VersionAPI extends APISupport {

    private static VersionAPI instance;
    private VersionAPI(){}

    public static VersionAPI getInstance(Context context) {
        if (instance == null) {
            instance = new VersionAPI();
        } else {
            instance.context = context;
        }
        return instance;
    }

    public void checkVersion(APIListener<CheckversionResp> apiListener){
        CheckverisonReq req = new CheckverisonReq();
        request(req,apiListener,CheckversionResp.class);
    }

}
