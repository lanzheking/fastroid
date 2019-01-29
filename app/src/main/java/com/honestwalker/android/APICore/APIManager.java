package com.honestwalker.android.APICore;
//
//import com.honestwalker.android.APICore.config.ServerContext;
//
//import org.xutils.http.HttpMethod;
//
///**
// * API初始化和环境信息获取
// * Created by honestwalker on 16-1-29.
// */
//public class APIManager {
//
//    private static ServerContext serverContext;
//
//    public static void init(String host, HttpMethod defaultRequestMethod) {
//        init(host, defaultRequestMethod, null);
//    }
//
//    public static void init(String host, HttpMethod defaultRequestMethod, String actionKey) {
//        serverContext = new ServerContext();
//        serverContext.setHost(host);
//        serverContext.setMethod(defaultRequestMethod);
//        serverContext.setActionKey(actionKey);
//    }
//
//    public static ServerContext getServerContext() {
//        return serverContext;
//    }
//
//}
