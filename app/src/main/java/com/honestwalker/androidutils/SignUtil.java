package com.honestwalker.androidutils;

import com.honestwalker.androidutils.IO.LogCat;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

public class SignUtil {
	public static String getMySign(Context context , String packageName)  {
		
		try {
			PackageInfo pis = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			Signature[] sigs = pis.signatures;
			String sig = new String(sigs[0].toChars());
			LogCat.d("SIGN", "sign=" + sig);
		} catch (NameNotFoundException e) {
			return packageName + " NameNotFound";
		}
		
		return "";
	}
}
