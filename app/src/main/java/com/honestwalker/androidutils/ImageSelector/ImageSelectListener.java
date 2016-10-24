package com.honestwalker.androidutils.ImageSelector;

import java.io.Serializable;
import java.util.ArrayList;

public interface ImageSelectListener extends Serializable {
	public void onSelect();
	public void onSelected(ImageSelectType type, ArrayList<String> imagePath);
	public void onCancel();
	public void onComplete();
}