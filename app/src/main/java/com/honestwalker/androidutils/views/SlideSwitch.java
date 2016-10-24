package com.honestwalker.androidutils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import com.honestwalker.android.fastroid.R;

public class SlideSwitch extends RelativeLayout implements Checkable {
	
	public interface OnCheckedChangeListener {
		void onCheckedChanged(SlideSwitch view, boolean checked);
	}

	Thread m_thread;
	Handler m_handler = new Handler();
	float m_add = 0.0f;
	boolean m_checked;
	View m_view_bar;
	View m_view_background_off;
	View m_view_background_on;
	int m_orientation = HORIZONTAL;
	String m_text;

	static final float m_accele = 0.05f;
	static final long m_intarval = 50L;
	static final int m_height_bar = 70;
	static final int m_width_bar = 100;
	static final int HORIZONTAL = 0;
	static final int VERTICAL = 1;
	final float scale = getContext().getResources().getDisplayMetrics().density;

	OnTouchListener m_listnerOnTouch = new OnTouchListener() {

		int m_left;
		int m_top;
		float m_x_org;
		float m_y_org;
		float m_x_prev;
		float m_y_prev;
		long m_t_prev;

		public boolean onTouch(View v, MotionEvent me) {

			boolean bResult = false;

			float x = me.getRawX();
			float y = me.getRawY();
			long t = System.currentTimeMillis();

			int action = me.getAction();

			switch (action) {
			case MotionEvent.ACTION_DOWN: {
				stopTimer();
				m_x_org = x;
				m_y_org = y;
				m_left = v.getLeft();
				m_top = v.getTop();
				bResult = true;
				m_view_bar
						.setBackgroundResource(R.drawable.slideswitch_bar_touch);
			}
				break;
			case MotionEvent.ACTION_UP:
				if (Math.abs(m_left - v.getLeft()) < 2) {
					stopTimer();
					m_add = (x - m_x_prev) / (t - m_t_prev);
					float x_move = x - m_x_org;
					moveBarX(m_left + (int) x_move);
					startTimer(true);
				} else
					startTimer(false);
				m_view_bar.setBackgroundResource(R.drawable.slideswitch_bar);
				break;
			case MotionEvent.ACTION_MOVE: {
				stopTimer();
				m_add = (x - m_x_prev) / (t - m_t_prev);
				float x_move = x - m_x_org;
				moveBarX(m_left + (int) x_move);
			}
				break;
			default:
				// do nothing.
			}

			m_t_prev = t;
			m_x_prev = x;
			m_y_prev = y;

			return bResult;
		}

	};

	void moveBarX(int left) {

		int width = m_view_bar.getWidth();
		int widthParent = getWidth();
		int height = m_view_bar.getHeight();
		int leftMax = widthParent - width;

		int leftNew = left;
		if (leftNew <= 0) {
			stopTimer();
			leftNew = 0;
			updateState(false);
		} else if (leftMax <= leftNew) {
			stopTimer();
			leftNew = leftMax;
			updateState(true);
		}

		int top = m_view_bar.getTop();
		int rightNew = leftNew + width;
		int bottom = top + height;

		m_view_bar.layout(leftNew, top, rightNew, bottom);

		int centerNew = leftNew + (width / 2);
		m_view_background_on.layout(centerNew - (int) (76 * scale + 0.5f), top,
				centerNew - (int) (18 * scale + 0.5f), bottom);
		m_view_background_off.layout(centerNew + (int) (18 * scale + 0.5f),
				top, (int) (76 * scale + 0.5f) + centerNew, bottom);

	}

	void moveBarY(int top) {

		// int height = m_view_bar.getHeight();
		// int heightParent = getHeight();
		// int width = m_view_bar.getWidth();
		// int topMax = heightParent - height;
		//		
		// int topNew = top;
		// if( topNew <= 0) {
		// stopTimer();
		// topNew = 0;
		// updateState(true);
		// } else if( topMax <= topNew ) {
		// stopTimer();
		// topNew = topMax;
		// updateState(false);
		// }
		//	
		// int left = m_view_bar.getLeft();
		// int bottomNew = topNew + height;
		// int right = left + width;
		// m_view_bar.layout(left, topNew, right, bottomNew);
		//
		// int centerNew = topNew + ( height / 2 );
		// m_view_background_on.layout(left, centerNew, right, heightParent);
		// m_view_background_off.layout(left, 0, right, centerNew);

	}

	void updateState(boolean checked) {

		if (m_checked == checked) {
			return;
		}

		m_checked = checked;

		if (m_listener != null) {
			m_listener.onCheckedChanged(SlideSwitch.this, m_checked);
		}

	}

	public SlideSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		{
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.SlideSwitch);

			m_checked = a.getBoolean(R.styleable.SlideSwitch_checked, false);
			m_checked = this.isChecked();
		}

		{
			int idRes;
			idRes = R.layout.slide_switch_horizontal_layout;

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(idRes, this);
		}
		// init button
		//setOn(false);
	}

	public void setOn(boolean on) {
		if (!on) {
			{
				View v = findViewById(R.id.ViewOff);
				LayoutParams params = new LayoutParams(
						(int) (55 * scale + 0.5f), (int) (37 * scale + 0.5f));
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				v.setLayoutParams(params);

				m_view_background_off = v;
			}
			{
				View v = findViewById(R.id.ViewOn);
				LayoutParams params = new LayoutParams(
						(int) (0 * scale + 0.5f), (int) (37 * scale + 0.5f));
				v.setLayoutParams(params);
				m_view_background_on = v;
			}
			
			{
				int gravity;
				gravity = Gravity.LEFT;
				Button v = (Button) findViewById(R.id.ButtonBar);
				{
					LayoutParams params = (LayoutParams) v
							.getLayoutParams();
					// params.gravity = gravity;
//					v.setGravity(gravity);
					params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					v.setLayoutParams(params);
					v.setOnTouchListener(m_listnerOnTouch);
					m_view_bar = v;
				}
			}
		}
		else if (on)
		{
			{
				View v = findViewById(R.id.ViewOff);
				LayoutParams params = new LayoutParams(
						(int) (0 * scale + 0.5f), (int) (37 * scale + 0.5f));
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				v.setLayoutParams(params);

				m_view_background_off = v;
			}
			{
				View v = findViewById(R.id.ViewOn);
				LayoutParams params = new LayoutParams(
						(int) (55 * scale + 0.5f), (int) (37 * scale + 0.5f));
				v.setLayoutParams(params);
				m_view_background_on = v;
			}
			{
				int gravity;
				gravity = Gravity.RIGHT;
				Button v = (Button) findViewById(R.id.ButtonBar);
				{
					LayoutParams params = (LayoutParams) v
							.getLayoutParams();
					//params.gravity = gravity;
					//v.setGravity(gravity);
					params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					v.setLayoutParams(params);
					v.setOnTouchListener(m_listnerOnTouch);
					m_view_bar = v;
				}
			}
		}
	}

	public void setChecked(boolean checked) {

		m_checked = checked;
			if (checked) {
				setOn(true);
			} else {
				setOn(false);
			}
	}

	void stopTimer() {
		if (m_thread == null) {
			return;
		}

		m_thread.interrupt();
		m_thread = null;
	}

	void startTimer(final boolean goSwitch) {

		stopTimer();

		m_thread = new Thread(new Runnable() {

			public void run() {

				while (true) {
					try {
						Thread.sleep(m_intarval);
					} catch (InterruptedException e) {
						return;
					}

					m_handler.post(new Runnable() {

						public void run() {
							onTimer(goSwitch);

						}

					});
				}

			}

		});

		m_thread.start();
	}

	void onTimer(boolean goSwitch) {

		int left = m_view_bar.getLeft();
		int width = m_view_bar.getWidth();
		int widthParent = SlideSwitch.this.getWidth();
		int centerParent = widthParent / 2;
		int center = left + (width / 2);

		if (!goSwitch) {
			if (center < centerParent) {
				m_add -= m_accele;
			} else {
				m_add += m_accele;
			}
		} else {
			if (m_checked)
				m_add -= m_accele;
			else
				m_add += m_accele;
		}

		int move = (int) (m_add * m_intarval);
		int leftNew = left + move;
		moveBarX(leftNew);
	}

	public boolean isChecked() {
		return m_checked;
	}

	public void toggle() {
		boolean checkedNew = !m_checked;
		setChecked(checkedNew);
	}

	OnCheckedChangeListener m_listener;

	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		m_listener = listener;
	}

}
