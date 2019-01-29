package com.honestwalker.android.APICore.API.APIImpl;

import android.content.Context;

import com.honestwalker.android.APICore.API.APIListener;
import com.honestwalker.android.APICore.API.APISupport;
import com.honestwalker.android.APICore.API.req.XGTokenReq;
import com.honestwalker.android.APICore.API.resp.BaseResp;

/**
 * Created by lanzhe on 16-8-5.
 */
public class XGAPI extends APISupport {

    /**
     * 注册token
     * @param token
     * @param deviceID
     * @param listener
     */
    public void registerToken(String token , String deviceID, APIListener<BaseResp> listener) {
        XGTokenReq req = new XGTokenReq();
        req.token = token;
        req.device_id = deviceID;
        request(req, listener , BaseResp.class);
    }

}
