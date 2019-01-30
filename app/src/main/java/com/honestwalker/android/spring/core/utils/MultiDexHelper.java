package com.honestwalker.android.spring.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dalvik.system.DexFile;

/**
 * Created by lanzhe on 18-5-10.
 */

public class MultiDexHelper {

    private static final String EXTRACTED_NAME_EXT = ".classes";
    private static final String EXTRACTED_SUFFIX = ".zip";

    private static final String SECONDARY_FOLDER_NAME = "code_cache" + File.separator +
            "secondary-dexes";

    private static final String PREFS_FILE = "multidex.version";
    private static final String KEY_DEX_NUMBER = "dex.number";

    private static SharedPreferences getMultiDexPreferences(Context context) {
        return context.getSharedPreferences(PREFS_FILE, Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
                Context.MODE_PRIVATE :
                Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }

    /**
     * get all the dex path
     *
     * @param context the application context
     * @return all the dex path
     * @throws PackageManager.NameNotFoundException
     * @throws IOException
     */
    public static List<String> getSourcePaths(Context context) throws PackageManager.NameNotFoundException, IOException {
        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        File sourceApk = new File(applicationInfo.sourceDir);
        File dexDir = new File(applicationInfo.dataDir, SECONDARY_FOLDER_NAME);

            LogCat.d("MultiDexHelper",
                    "getSourcePaths sourceDir=" + applicationInfo.sourceDir + ", dataDir=" + applicationInfo.dataDir);

        List<String> sourcePaths = new ArrayList<String>();
        sourcePaths.add(applicationInfo.sourceDir); //add the default apk path

        //the prefix of extracted file, ie: test.classes
        String extractedFilePrefix = sourceApk.getName() + EXTRACTED_NAME_EXT;
        //the total dex numbers
        int totalDexNumber = getMultiDexPreferences(context).getInt(KEY_DEX_NUMBER, 1);

            LogCat.d("MultiDexHelper", "getSourcePaths totalDexNumber=" + totalDexNumber);

        for (int secondaryNumber = 2; secondaryNumber <= totalDexNumber; secondaryNumber++) {
            //for each dex file, ie: test.classes2.zip, test.classes3.zip...
            String fileName = extractedFilePrefix + secondaryNumber + EXTRACTED_SUFFIX;
            File extractedFile = new File(dexDir, fileName);
            if (extractedFile.isFile()) {
                sourcePaths.add(extractedFile.getAbsolutePath());
                //we ignore the verify zip part
            } else {
                throw new IOException("Missing extracted secondary dex file '" +
                        extractedFile.getPath() + "'");
            }
        }
        try {
            // handle dex files built by instant run
            File instantRunFilePath = new File(applicationInfo.dataDir,
                    "files" + File.separator + "instant-run" + File.separator + "dex");
                LogCat.d("MultiDexHelper", "getSourcePaths instantRunFile exists=" + instantRunFilePath.exists() + ", isDirectory="
                        + instantRunFilePath.isDirectory() + ", getAbsolutePath=" + instantRunFilePath.getAbsolutePath());
            if (instantRunFilePath.exists() && instantRunFilePath.isDirectory()) {
                File[] sliceFiles = instantRunFilePath.listFiles();
                for (File sliceFile : sliceFiles) {
                    if (null != sliceFile && sliceFile.exists() && sliceFile.isFile() && sliceFile.getName().endsWith(".dex")) {
                        sourcePaths.add(sliceFile.getAbsolutePath());
                    }
                }
            }
        } catch (Throwable e) {
            ExceptionUtil.showException("MultiDexHelper", e);
        }

        return sourcePaths;
    }

    /**
     * scan parent class's sub classes
     *
     * @param context
     * @param packageName
//     * @param parentClass , Class<T> parentClass
     * @param <T>
     * @return
     */
    public static <T> Set<Class<? extends T>> scanClasses(Context context, String packageName) {
        Set<Class<? extends T>> classes = new HashSet<Class<? extends T>>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            for (String path : getSourcePaths(context)) {
                    LogCat.d("MultiDexHelper", "scanClasses path=" + path);
                try {
                    DexFile dexfile = null;
                    if (path.endsWith(EXTRACTED_SUFFIX)) {
                        //NOT use new DexFile(path), because it will throw "permission error in /data/dalvik-cache"
                        dexfile = DexFile.loadDex(path, path + ".tmp", 0);
                    } else {
                        dexfile = new DexFile(path);
                    }
                    Enumeration<String> dexEntries = dexfile.entries();
                    while (dexEntries.hasMoreElements()) {
                        String className = dexEntries.nextElement();
                        if(className.startsWith("com.honestwalker.android.spring")) {
                            LogCat.d("MultiDexHelper", "scanClasses className=" + className);
                        }
                        if (className.toLowerCase().startsWith(packageName.toLowerCase())) {
                            Class clazz = classLoader.loadClass(className);
//                                LogCat.d("MultiDexHelper",
//                                        "scanClasses clazz=" + clazz + ", parentClass=" + parentClass + ", equals=" + clazz
//                                                .getSuperclass().equals(parentClass));
//                            if (clazz.getSuperclass().equals(parentClass)) {
//                                classes.add(clazz);
//                            }
                            classes.add(clazz);
                        }
                    }
                } catch (Throwable e) {
                    ExceptionUtil.showException("MultiDexHelper", e);
                }
            }
        } catch (Throwable e) {
            ExceptionUtil.showException("MultiDexHelper", e);
        }
        return classes;
    }
    
}
