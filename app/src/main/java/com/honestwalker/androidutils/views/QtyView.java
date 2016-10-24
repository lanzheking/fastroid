package com.honestwalker.androidutils.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;

/**
 * 数量提示控件
 * @author honestwalker
 *
 */
public class QtyView extends BaseMyViewRelativeLayout {

	/** inflater布局 */
	private View contentView;
	/** 用于控制控件显示 */
	private View layout;
	/** 显示数量 */
	private TextView qtyTV;
	/** 显示背景红色圆圈 */
	private ImageView bgIV;
	
	private int qty;
	
	public QtyView(Context context) {
		super(context);
		init();
	}
	public QtyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void init() {
		contentView = inflater.inflate(R.layout.view_qty, null);
		layout = contentView.findViewById(R.id.qtyview);
		bgIV   = (ImageView) contentView.findViewById(R.id.qtyiv);
		qtyTV  = (TextView) contentView.findViewById(R.id.qtytv);
		this.addView(contentView);
	}
	
	/**
	 * 设置数量，根据数量变更背景
	 * @param qty
	 */
	public void setQty(int qty) {
		this.qty = qty;
		qtyTV.setText(qty + "");
		if(qty < 1) {
			layout.setVisibility(View.GONE);
		} else {
			layout.setVisibility(View.VISIBLE);
//			// 数量大于10采用较长的背景图
			if(qty > 99) {
//				bgIV.setImageResource(R.drawable.quantity_bg3);
				ViewSizeHelper.getInstance(context).setWidth(bgIV, 
						(int) getResources().getDimension(R.dimen.qty_view_size_b2));
			} else  if(qty > 10) {
//				bgIV.setImageResource(R.drawable.quantity_bg2);
				ViewSizeHelper.getInstance(context).setWidth(bgIV, 
						(int) getResources().getDimension(R.dimen.qty_view_size_b1));
			} else {
//				bgIV.setImageResource(R.drawable.quantity_bg1);
				ViewSizeHelper.getInstance(context).setWidth(bgIV, 
						(int) getResources().getDimension(R.dimen.qty_view_size));
			}
			bgIV.setImageResource(R.drawable.xmlbg_qtybg);
		}
	}
	
	public int getQty() {
		return qty;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
}
