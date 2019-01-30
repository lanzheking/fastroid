package com.honestwalker.androidutils;

import java.util.UUID;

/**
 * Created by lanzhe on 18-5-11.
 */
public class UUIDUtil {

    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
