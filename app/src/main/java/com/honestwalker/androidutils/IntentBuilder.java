package com.honestwalker.androidutils;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;

/**
 * 简化Intent 建立过程
 * @author honestwalker
 *
 */
public class IntentBuilder {
	
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	
	public IntentBuilder buildIntent(String key , Object value ) {
		if(value != null) {
			if(value instanceof Integer) {
				bundle.putInt(key, Integer.parseInt(value + ""));
			} else if (value instanceof String) {
				bundle.putString(key, value.toString());
			} else if (value instanceof Long) {
				bundle.putLong(key, Long.parseLong(value.toString()));
			} else if (value instanceof Boolean) {
				bundle.putBoolean(key, Boolean.parseBoolean(value.toString().toLowerCase()));
			} else if (value instanceof Float) {
				bundle.putFloat(key, Float.parseFloat(value.toString()));
			} else if (value instanceof Double) {
				bundle.putDouble(key, Double.parseDouble(value.toString()));
			} else if (value instanceof Serializable) {
				bundle.putSerializable(key, (Serializable)value);
			}
			intent.putExtras(bundle);
		}
		return this;
	}
	
	public Intent getIntent() {
		return intent;
	}
	
}
