package com.honestwalker.android.gradletask;

import java.io.IOException;

/**
 * Created by honestwalker on 16-1-20.
 */
public class InitMain {

    public static void main(String[] args) throws IOException {
//        System.out.println(" ===================== main run ====================== ");
        Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c" ,
                "mkdir /home/honestwalker/asd"});
    }

}
