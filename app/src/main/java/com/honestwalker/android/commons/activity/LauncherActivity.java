package com.honestwalker.android.commons.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

import com.honestwalker.androidutils.IO.LogCat;

public class LauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String mainClassName = null;
		try {
			ActivityInfo info = this.getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
			mainClassName =info.metaData.getString("MainActivity");

			Intent intent = new Intent(this, Class.forName(mainClassName));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();

		} catch (Exception e) {
			Log.e("AndroidRuntime", "AndroidManifest LauncherActivty meta-data named MainActivity messing. ");
		}

	}

}
