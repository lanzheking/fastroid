package com.honestwalker.androidutils.ImageSelector;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.ImageSelector.ImageScan.MultiImageSelectorActivity;
import com.honestwalker.androidutils.ImageSelector.utils.BroadcaseManager;
import com.honestwalker.androidutils.ImageSelector.utils.PictureUtil;
import com.honestwalker.androidutils.ImageUtil;
import com.honestwalker.androidutils.net.URIUtil;
import com.honestwalker.androidutils.system.ProcessUtil;

import java.io.File;
import java.util.ArrayList;

public class ImageSelector {

	private Activity mContext;

	private final String TAG = "ImageSelector";

	public final static int REQUEST_CAMERA = 71;
	public final static int REQUEST_IMAGE_SELECT = 72;
	public final static int REQUEST_IMAGE_CUT = 73;
	public final static int REQUEST_SINGLE_IMAGE_SELECT = 74;
	public final static int REQUEST_MULTI_IMAGE_SELECT = 75;
//	public final static int REQUEST_CAMERA = 15479401;
//	public final static int REQUEST_IMAGE_SELECT = 15479402;
//	public final static int REQUEST_IMAGE_CUT = 15479403;
//	public final static int REQUEST_SINGLE_IMAGE_SELECT = 15479406;
//	public final static int REQUEST_MULTI_IMAGE_SELECT = 15479404;

	private ImageSelectListener imageSelectListener;

	public static final String ACTION = "com.honestwalker.models.ImageSelector";

	/** 是否需要切割图片 */
	private boolean needCut = false;
	private String  outputPath = "";

	/**
	 * 记录图片最大宽度,图片剪切时用到
	 */
	private int maxWidthPx;

	// 裁剪比例
	private int aspectX = 1;
	private int aspectY = 1;

	public ImageSelector(Activity context) {
		this.mContext = context;
	}

	private void buildOutputDir() {
		Log.d(TAG, " outputPath=" + outputPath);
		if(!"".equals(outputPath)) {
			File outputFile = new File(outputPath);
			if(!outputFile.getParentFile().exists()) {
				Log.d(TAG, outputFile.getParentFile().toString() + " 不存在， 建立");
				outputFile.getParentFile().mkdirs();
			}
		}
	}

	public void openCameraAndCrop(String outputPath , int maxWidth , int aspectX , int aspectY) {
		openCamera(true, outputPath, maxWidth, aspectX, aspectY);
	}

	public void openCamera(String outputPath , int maxWidth , int aspectX , int aspectY) {
		openCamera(false, outputPath, maxWidth, aspectX, aspectY);
	}

	/**
	 * 打开摄像头拍照基本方法
	 * @param crop         是否剪切
	 * @param outputPath   拍照后输出路径，只能是SD卡，不能使程序内部路径
	 * @param maxWidth     拍照像素
	 * @param aspectX
     * @param aspectY
     */
	private void openCamera(boolean crop, String outputPath , int maxWidth , int aspectX , int aspectY) {
		this.needCut = crop;
		this.outputPath = outputPath;
		this.aspectX = aspectX;
		this.aspectY = aspectY;
		this.maxWidthPx = maxWidth;

		buildOutputDir();

		BroadcaseManager.registerReceiver(mContext, ACTION, imageSelectorReceiver);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, Configuration.ORIENTATION_PORTRAIT);

		Uri cameraOutputPath = null;
		try {
			cameraOutputPath = Uri.fromFile(new File(outputPath));
		} catch (Exception e) {}

		LogCat.d("Main", "outputPath=" + outputPath);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutputPath);
		keepIntent(intent);

		mContext.startActivityForResult(intent, ImageSelector.REQUEST_CAMERA);

	}

	/** 添加一些通用参数到intent */
	private void keepIntent(Intent intent) {
		intent.putExtra("maxWidth" , maxWidthPx);
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
	}

	/** 打开图片剪切工具 */
	private void toImageCut(Uri resultUri) {
		Intent intentCut = new Intent("com.android.camera.action.CROP");
		//需要裁减的图片格式

		intentCut.setDataAndType(resultUri, "image/*");

		//允许裁减
		intentCut.putExtra("crop", "true");
		if(maxWidthPx > 0) {
			//剪裁后ImageView显时图片的宽
			intentCut.putExtra("outputX", maxWidthPx);
			int maxHeightPx = maxWidthPx * aspectY / aspectX;
			//剪裁后ImageView显时图片的高
			intentCut.putExtra("outputY", maxHeightPx);
			LogCat.d(TAG , "剪切最大像素 " + maxWidthPx);
		}
		//设置剪裁框的宽高比例
		intentCut.putExtra("aspectX", aspectX);
		intentCut.putExtra("aspectY", aspectY);
		intentCut.putExtra("scale", true);
//		intentCut.putExtra(MediaStore.EXTRA_OUTPUT, outputPath);                // 剪切工具保存到这个uri
		//intentCut.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/storage/emulated/0/jingxinwei/image/20160722_00000.jpeg")));                // 剪切工具保存到这个uri
		intentCut.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(outputPath)));                // 剪切工具保存到这个uri
//		intentCut.putExtra("return-data", true);                    // 加这个 小米2 会报错
		intentCut.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());//返回格式
		Log.d(TAG, "剪切图片将要输出到:" + outputPath);

		keepIntent(intentCut);

		mContext.startActivityForResult(intentCut, ImageSelector.REQUEST_IMAGE_CUT);
	}

	public void selectImageAndCrop(String outputPath , int aspectX , int aspectY) {
		this.selectImage(true, outputPath, aspectX , aspectY);
	}

	public void selectImage() {
		this.selectImage(false, "" , 0 , 0);
	}

	private void selectImage(boolean needCut , String outputPath , int aspectX , int aspectY) {
		this.needCut = needCut;
		this.outputPath = outputPath;
		this.aspectX = aspectX;
		this.aspectY = aspectY;

		buildOutputDir();

		BroadcaseManager.registerReceiver(mContext, ACTION, imageSelectorReceiver);

		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		mContext.startActivityForResult(
				Intent.createChooser(intent, "Select Picture"), ImageSelector.REQUEST_IMAGE_SELECT);

	}

	/**
	 * 单图选择
	 */
	public void singleSelectImage() {
		singleSelectImage(false, "", 0 , 0);
	}

	/**
	 * 单图选择并且剪切
	 * @param outputPath   剪切后输出路径
	 * @param aspectX      比例X
	 * @param aspectY      比例Y
     */
	public void singleSelectImageAndCrop(String outputPath, int aspectX, int aspectY) {
		singleSelectImage(true, outputPath, aspectX , aspectY);
	}

	/**
	 *
	 * @param crop        是否剪切
	 * @param outputPath  输出文件路径，包括文件名和后缀， 仅在crop = true时有效
	 * @param aspectX     比例X
	 * @param aspectY     比例Y
	 */
	private void singleSelectImage(boolean crop , String outputPath , int aspectX , int aspectY) {
		this.customImageSelector(true, crop, outputPath, 1 , aspectX, aspectY);
	}

	/**
	 * 多图选择
	 * @param maxSelect 最多允许选择多少张图
     */
	public void multiSelectImage(int maxSelect) {
		this.customImageSelector(false, false, "", maxSelect , 0, 0);
	}

	/**
	 * 自定义图片选择器
	 * @param singleSelect	 单选还是多选
	 * @param crop           是否剪切，仅在单选时有效
	 * @param outputPath     剪切后输出路径
	 * @param maxSelect      最多选几张，仅在多选时有效(singleSelect = false)
	 * @param aspectX        剪切后比例X，仅在剪切时有效
     * @param aspectY        剪切后比例Y，仅在剪切时有效
     */
	private void customImageSelector(boolean singleSelect , boolean crop, String outputPath, int maxSelect, int aspectX, int aspectY) {
		this.needCut = singleSelect && crop;
		this.outputPath = outputPath;
		this.aspectX = aspectX;
		this.aspectY = aspectY;

		BroadcaseManager.registerReceiver(mContext, ACTION, imageSelectorReceiver);

		Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
		intent.putExtra("signleSelect", singleSelect);
		intent.putExtra("maxSelect", singleSelect ? 1 : maxSelect);
		mContext.startActivityForResult(intent, singleSelect ? ImageSelector.REQUEST_SINGLE_IMAGE_SELECT : ImageSelector.REQUEST_MULTI_IMAGE_SELECT);
	}

//	public void singleSelectImage() {
//
//		buildOutputDir();
//
//		BroadcaseManager.registerReceiver(mContext, ACTION, imageSelectorReceiver);
//
//		Intent intent = new Intent(mContext , ImageSelectorAgentActivity.class);
//		intent.putExtra("signleSelect", true);
//		intent.putExtra(ImageSelectType.class.getSimpleName(), ImageSelectType.TYPE_SINGLE_IMAGE_SELECTOR);
//		intent.putExtra("aspectX" , aspectX);
//		intent.putExtra("aspectY" , aspectY);
//		mContext.startActivity(intent);
//	}

//	public void multiSelectImage(int maxSelect) {
//
//		BroadcaseManager.registerReceiver(mContext, ACTION , imageSelectorReceiver);
//
//		Intent intent = new Intent(mContext , ImageSelectorAgentActivity.class);
//		intent.putExtra(ImageSelectType.class.getSimpleName(), ImageSelectType.TYPE_MULTI_IMAGE_SELECTOR);
//		intent.putExtra("maxSelect", maxSelect);
//		mContext.startActivity(intent);
//	}

	public void setImageSelectListener(ImageSelectListener imageSelectListener) {
		this.imageSelectListener = imageSelectListener;
	}

	/** 图片选择广播接收者 */
	private BroadcastReceiver imageSelectorReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			LogCat.d(TAG, "接收到广播");
			if(intent != null) {
				int resultCode = intent.getIntExtra("resultCode" , mContext.RESULT_CANCELED);
				int requestCode = intent.getIntExtra("requestCode", -1);

				ArrayList<String> selectedImages = new ArrayList<String>();

				if(resultCode == Activity.RESULT_CANCELED) {
					unregisterReceiver();
					if(imageSelectListener != null) {
						imageSelectListener.onCancel();
						imageSelectListener.onComplete();
					}
				} else if((requestCode == REQUEST_IMAGE_SELECT || requestCode == REQUEST_CAMERA || requestCode == REQUEST_SINGLE_IMAGE_SELECT)) {
					String selectedImagePath = intent.getStringExtra("imgPath");
					if(selectedImagePath == null) {
						return;
					}
					selectedImages.add(selectedImagePath);

					if(ImageSelector.this.needCut) {  // 如果需要剪切

						// 去图片剪切
						Uri resultUri = Uri.fromFile(new File(selectedImagePath));

						Uri mUri = Uri.parse("content://media/external/images/media");

						Cursor cursor = mContext.managedQuery(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
								null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
						cursor.moveToFirst();

						while (!cursor.isAfterLast()) {
							String data = cursor.getString(cursor
									.getColumnIndex(MediaStore.MediaColumns.DATA));
							if (selectedImagePath.equals(data)) {
								int ringtoneID = cursor.getInt(cursor
										.getColumnIndex(MediaStore.MediaColumns._ID));
								resultUri = Uri.withAppendedPath(mUri, ""
										+ ringtoneID);
								break;
							}
							cursor.moveToNext();
						}

						if(resultUri != null) {
							toImageCut(resultUri);
							return;
						}
					}

					if(!needCut) {
						if(imageSelectListener != null) {
							ImageSelectType type = (ImageSelectType) intent.getSerializableExtra(ImageSelectType.class.getSimpleName());
							imageSelectListener.onSelected(type, selectedImages);
							unregisterReceiver();
							imageSelectListener.onComplete();
						}
					}

				} else if(requestCode == REQUEST_MULTI_IMAGE_SELECT) {
					ArrayList<String> imgPaths = intent.getStringArrayListExtra("imgPaths");
					selectedImages.addAll(imgPaths);

					if(imageSelectListener != null) {
						ImageSelectType type = (ImageSelectType) intent.getSerializableExtra(ImageSelectType.class.getSimpleName());
						imageSelectListener.onSelected(type, selectedImages);
						unregisterReceiver();
						imageSelectListener.onComplete();
					}

				} else if(requestCode == REQUEST_IMAGE_CUT) {
					String selectedImagePath = intent.getStringExtra("imgPath");
					selectedImages.add(selectedImagePath);

					if(imageSelectListener != null) {
						ImageSelectType type = (ImageSelectType) intent.getSerializableExtra(ImageSelectType.class.getSimpleName());
						imageSelectListener.onSelected(type, selectedImages);
						unregisterReceiver();
						imageSelectListener.onComplete();
					}

				}

			}
		}
	};

	/**
	 * 反注册广播
	 */
	private void unregisterReceiver() {
		try {
			BroadcaseManager.unregisterReceiver(mContext, imageSelectorReceiver);
		} catch (Exception e) {
			LogCat.d(TAG , "广播已经回收");
		}
	}

	private void sendCallbackBroadcast(Intent intent) {
		LogCat.d(TAG , "发送广播");
		BroadcaseManager.sendBroadcast(mContext, ImageSelector.ACTION, intent);
	}

	public boolean isImageSelect(int requestCode) {
		return requestCode >= ImageSelector.REQUEST_CAMERA && requestCode <= REQUEST_MULTI_IMAGE_SELECT;
	}

	private ImageSelectType getTypeByRequestCode(int requestCode) {
		switch (requestCode) {
			case ImageSelector.REQUEST_CAMERA :  return ImageSelectType.TYPE_CAMERA;
			case ImageSelector.REQUEST_IMAGE_SELECT :  return ImageSelectType.TYPE_IMAGE_SELECTOR;
			case ImageSelector.REQUEST_SINGLE_IMAGE_SELECT :  return ImageSelectType.TYPE_SINGLE_IMAGE_SELECTOR;
			case ImageSelector.REQUEST_MULTI_IMAGE_SELECT :  return ImageSelectType.TYPE_MULTI_IMAGE_SELECTOR;
		}
		return ImageSelectType.TYPE_UNKNOW;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.d(TAG, "ImageSelectorAgentActivity OnActivityResult requestCode=" + requestCode + " resultCode=" + resultCode);

		if(resultCode == mContext.RESULT_CANCELED) {
			LogCat.d(TAG, "取消操作");
			Intent intent = new Intent();
			intent.putExtra("requestCode", requestCode);
			intent.putExtra("resultCode", resultCode);
			intent.putExtra(ImageSelectType.class.getSimpleName(), getTypeByRequestCode(requestCode));
			sendCallbackBroadcast(intent);
			return;
		}

		Intent intent = new Intent();

		intent.putExtra(ImageSelectType.class.getSimpleName(), getTypeByRequestCode(requestCode));

		if(data == null) data = new Intent();

		intent.putExtra("requestCode", requestCode);
		intent.putExtra("resultCode", resultCode);

		maxWidthPx = intent.getIntExtra("maxWidth" , maxWidthPx);

		if(intent.hasExtra("aspectX")) {
			aspectX = intent.getIntExtra("aspectX" , 1);
		}
		if(intent.hasExtra("aspectY")) {
			aspectY = intent.getIntExtra("aspectY" , 1);
		}

		LogCat.d(TAG , "本地 比例 " + aspectX + " : " + aspectY);

		if(ImageSelector.REQUEST_CAMERA == requestCode) {

			Uri cameraOutputPath = Uri.fromFile(new File(outputPath));
			intent.putExtra("imgPath", cameraOutputPath.getPath());

			int degree = PictureUtil.readPictureDegree(cameraOutputPath.getPath());

			if(maxWidthPx != 0) {
				ImageUtil imageUtl = new ImageUtil();
				imageUtl.imageZip(cameraOutputPath.getPath(), maxWidthPx);
				LogCat.d(TAG, "拍照完毕 图片压缩到 " + maxWidthPx);
			}

			LogCat.d(TAG , "拍照后图片方向: " + degree);
			if(degree != 0) {
				boolean rotateResult = PictureUtil.rotate(cameraOutputPath.getPath() , degree);
				if(rotateResult) {
					LogCat.d(TAG , "照片转正成功");
				} else {
					LogCat.d(TAG , "照片转正失败");
				}
			}

		} else if (ImageSelector.REQUEST_IMAGE_SELECT == requestCode) {

			Uri selectedImageUri = data.getData();

			String picturePath = "";
			if(selectedImageUri.toString().startsWith("content://")) {

				picturePath = URIUtil.getPathByUri4kitkat(mContext, selectedImageUri);
			} else {
				picturePath = selectedImageUri.toString().replace("file://" , "");
			}

			Log.d(TAG, "图片选择完成 picturePath=" + picturePath);

			intent.putExtra("imgPath", picturePath);

		} else if(ImageSelector.REQUEST_SINGLE_IMAGE_SELECT == requestCode) {
			ArrayList<String> imgPaths = data.getStringArrayListExtra("imgPaths");
			Log.d(TAG, "单图选择 " + imgPaths);
			if(imgPaths != null && imgPaths.size() >= 1) {
				intent.putExtra("imgPath", imgPaths.get(0));
			}
			if(data != null) {
				intent.setData(data.getData());
			}
		} else if(ImageSelector.REQUEST_MULTI_IMAGE_SELECT == requestCode) {
			ArrayList<String> imgPaths = data.getStringArrayListExtra("imgPaths");
			Log.d(TAG, "多图选择 " + imgPaths);
			intent.putExtra("imgPaths", imgPaths);
		} else if(ImageSelector.REQUEST_IMAGE_CUT == requestCode) {
			Log.d(TAG, "图片剪切完毕，outputPath=" + outputPath);
			intent.putExtra("imgPath", outputPath);
		}
		sendCallbackBroadcast(intent);

	}

}
