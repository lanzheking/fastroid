package com.honestwalker.android.commons.jscallback.actionclass;//package com.honestwalker.android.commons.jscallback.actionclass;
//
//import android.app.Activity;
//import com.honestwalker.android.x5.jscallback.actionclass.JSCallbackAction;
//import com.honestwalker.android.fastroid.R;
//import com.honestwalker.android.commons.jscallback.bean.ShareParamBean;
//import com.honestwalker.android.share.ShareUtil;
//import com.honestwalker.android.x5.views.X5WebView;
//import com.honestwalker.androidutils.IO.LogCat;
//
//import java.util.HashMap;


//
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;
//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.onekeyshare.OnekeyShare;
//import cn.sharesdk.onekeyshare.OnekeyShareTheme;
//import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
//import cn.sharesdk.wechat.friends.Wechat;
//
///**
// * share js callback 业务实现
// * Created by honestwalker on 15-6-2.
// */
//public class ShareAction extends JSCallbackAction<ShareParamBean> {
//
//    private Activity context;
//    private OnekeyShare oks;
//    private ShareParamBean paramBean;
//    private X5WebView webView;
//
//
//
//    @Override
//    protected String doAction(Activity context, ShareParamBean paramBean, X5WebView webView) {
//
//        LogCat.i("callback","callback");
//        if(paramBean.getPlatform() == null || paramBean.getPlatform().equalsIgnoreCase("all")) {
//            this.context = context;
//            this.paramBean = paramBean;
//            this.webView = webView;
//            ShareSDK.initSDK(context);
//            oks = new OnekeyShare();
//            oks.setShareContentCustomizeCallback(shareContentCustomizeCallback);   //修改分享朋友圈时修改标题为内容
//            oks.setImageUrl(paramBean.getImageUrl());	//设置分享内容中图片到链接
//            oks.setUrl(paramBean.getLink());	//设置点击微信分享内容需要跳转到到url
//            oks.setComment(context.getString(R.string.share));	//设置人人网中对分享到评论
//            oks.setSite(context.getString(R.string.app_name));	//设置分享该内容的站点
//            oks.setSiteUrl(paramBean.getLink());	//设置分享该内容站点到url
//            oks.setSilent(false);	//设置是否直接分享
//            oks.setShareFromQQAuthSupport(false);	//是否支持QQ,QZone授权登录后发微博
//            oks.setTheme(OnekeyShareTheme.CLASSIC);	//使用经典到分享界面
//            oks.setCallback(platformActionListener);
//            // 令编辑页面显示为Dialog模式
//            oks.setDialogMode();
//            oks.show(context);
//        } else {
//            /*Platform.ShareParams sp = new Platform.ShareParams();
//            sp.setTitle(paramBean.getTitle());
//            sp.setTitleUrl(paramBean.getLink()); // 标题的超链接
//            sp.setText(paramBean.getDescContent());
//            sp.setImageUrl(paramBean.getImageUrl());
//            sp.setSite(paramBean.getLink());
//            sp.setSiteUrl(paramBean.getLink());
////            sp.setShareType(Platform.SHARE_TEXT);
//            sp.setShareType(Platform.SHARE_WEBPAGE);
//            new ShareUtil().share(paramBean.getPlatform(), sp , platformActionListener);*/
//
//            if (Wechat.NAME.equals(paramBean.getPlatform())) {
//                final Platform weixin = ShareSDK.getPlatform(context, Wechat.NAME);
//                Wechat.ShareParams sp = new Wechat.ShareParams();
//                sp.setShareType(Platform.SHARE_WEBPAGE);
//                sp.setTitle(paramBean.getTitle());
//                sp.setUrl(paramBean.getLink());
//                sp.setText(paramBean.getDescContent());
//                sp.setImageUrl(paramBean.getImageUrl());
//                weixin.setPlatformActionListener (platformActionListener);
//                weixin.share(sp);
//            }
//
//        }
//
//        return "success";
//    }
//
//    private ShareContentCustomizeCallback shareContentCustomizeCallback = new ShareContentCustomizeCallback() {
//        @Override
//        public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
//            if("WechatMoments".equals(platform.getName())){
//                LogCat.d("share", "分享到朋友圈");
//                paramsToShare.setTitle(paramBean.getDescContent());
//                paramsToShare.setText(paramBean.getTitle());
//            }else if("Wechat".equals(platform.getName())){
//                LogCat.d("share", "分享到朋友");
//                paramsToShare.setTitle(paramBean.getTitle());
//                paramsToShare.setText(paramBean.getDescContent());
//            }
//
//        }
//    };
//
//    private PlatformActionListener platformActionListener = new PlatformActionListener() {
//        @Override
//        public void onComplete(Platform platform, int i, HashMap<String, Object> stringObjectHashMap) {
//            LogCat.d("share", "分享成功");
//        }
//
//        @Override
//        public void onError(Platform platform, int i, Throwable throwable) {
//            LogCat.d("share", "分享失败:" + throwable.toString());
//        }
//
//        @Override
//        public void onCancel(Platform platform, int i) {
//            LogCat.d("share", "取消");
//        }
//    };
//
//
//}
