package com.honestwalker.android.APICore.API.APIImpl;

import com.honestwalker.android.APICore.API.APIListener;
import com.honestwalker.android.APICore.API.APISupport;
import com.honestwalker.android.APICore.API.req.UpLoadImageReq;
import com.honestwalker.android.APICore.API.req.UpLoadVoiceReq;
import com.honestwalker.android.APICore.API.resp.UploadImageResp;
import com.honestwalker.android.APICore.API.resp.UploadVoiceResp;

import java.util.HashMap;

/**
 * Created by honestwalker on 15-8-22.
 */
public class UpLoadAPI extends APISupport {


    public  void updateVoice(HashMap<String,String> hashMap,APIListener<UploadVoiceResp> apiListener){

        UpLoadVoiceReq req = new UpLoadVoiceReq();

        request(req,hashMap,apiListener,UploadVoiceResp.class);

    }

    public  void updateImage(HashMap<String,String> hashMap,APIListener<UploadImageResp> apiListener){

        UpLoadImageReq req = new UpLoadImageReq();

        request(req,hashMap,apiListener,UploadImageResp.class);

    }
    public  void updateFile(HashMap<String,String> hashMap,APIListener<UploadImageResp> apiListener){

        UpLoadImageReq req = new UpLoadImageReq();

        request(req,hashMap,apiListener,UploadImageResp.class);

    }

}
