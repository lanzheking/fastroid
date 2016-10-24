package com.honestwalker.android.commons.menu;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.honestwalker.androidutils.exception.ExceptionUtil;

import java.lang.reflect.Field;

/**
 * 
 * @author terry
 *
 */
public class BlurViewPager extends ViewPager {

	private int screenWitdh;
	/**
	 * the tabPageIndicator that guides the viewPager. 
	 */
	private TabPageIndicator tabPageIndicator;

	/**
	 * True if the viewPager cannot scroll ,false otherwise.False by default.
	 */
	private boolean disableDragScroll;

	/**
	 * True if snap when switch the tab,false otherwise.True by default.
	 */
	private boolean snapWhenSwitchTab = true;

    private MyScroller mScroller;

    int mCurrentIndex;

    /**
     * True if there is change in alpha when scroll the viewPager(true by default)
     */
    boolean enableBlurWhenScroll = true;

    public void setEnableBlurWhenScroll(boolean enableBlurWhenScroll) {
        this.enableBlurWhenScroll = enableBlurWhenScroll;
    }

    /**
     * Whether to disable the viewPager to scroll.True to disable,false otherwise.False by default.
     */
	public void setDisableDragScroll(boolean disableDragScroll) {
		this.disableDragScroll = disableDragScroll;
	}

	/**
	 * Whether snap to pages of the viewPager.True to snap,false otherwise.True by default.
	 */
	public void setSnapWhenSwitchTab(boolean snapWhenSwitchTab) {
		this.snapWhenSwitchTab = snapWhenSwitchTab;
	}

	/**
	 * Set the tabPageIndicator to guide the viewPager
	 */
	public void setTabPageIndicator(TabPageIndicator tabPageIndicator) {
		this.tabPageIndicator = tabPageIndicator;
		if (tabPageIndicator != null) {
			tabPageIndicator.setViewPager(this);
		}
	}
    
	public BlurViewPager(Context context) {
		this(context,null);
	}

	public BlurViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		screenWitdh = getResources().getDisplayMetrics().widthPixels;
		alterScrollRate();
		setOnPageChangeListener(onPageChangeListener);
	}
	
	void alterScrollRate() {
		try {  
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);  
            mScroller = new MyScroller(getContext(),new DecelerateInterpolator());  
            mField.set(this, mScroller);  
        } catch (Exception e) {  
        	ExceptionUtil.showException(e);
        }  
	}

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if(!disableDragScroll)
            return super.onInterceptTouchEvent(arg0);
        else
            return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if(!disableDragScroll)
            return super.onTouchEvent(arg0);
        else
            return false;
    }

    @Override
	public void setCurrentItem(int item){
		setCurrentItem(item,!snapWhenSwitchTab);
//        for (int i = 0; i < getChildCount(); i++) {
//            if(getChildAt(i) != null){
//                getChildAt(i).setAlpha(1f);
//            }
//        }
	}
	
	
	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageScrollStateChanged(int state) {
            if (!disableDragScroll){
                if (tabPageIndicator != null) {
                    tabPageIndicator.onPageScrollStateChanged(state);
                }
            }
		}

		@Override
		public void onPageSelected(int position) {
            mCurrentIndex = position;
            if (tabPageIndicator != null) {
                tabPageIndicator.onPageSelected(position);
            }
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (!disableDragScroll){
                if (tabPageIndicator != null) {
                    tabPageIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }
		}
	};
	
	public class MyScroller extends Scroller {  
		  
	    public MyScroller(Context context) {  
	        super(context);  
	    }  
	  
	    public MyScroller(Context context, Interpolator interpolator) {  
	        super(context, interpolator);  
	    }  
	  
	    @Override  
	    public void startScroll(int startX, int startY, int dx, int dy, int duration) {  
        	super.startScroll(startX, startY, dx, dy, duration);  
	    }  
	}  
	
	int scrollQuotient;
	int scrollRemainder;
	float scrollRatio;

	@Override
	public void computeScroll() {
		super.computeScroll();

//        if (!disableDragScroll){
//            scrollQuotient = getScrollX() / screenWitdh;
//            scrollRemainder = getScrollX() % screenWitdh;
//            scrollRatio = (float)scrollRemainder / (float)screenWitdh;
//
//            Log.d("q", "scrollQuotient="+scrollQuotient+",getCurrentItem()="+getCurrentItem()+",scrollRemainder="+scrollRemainder+",scrollRatio="+scrollRatio+",scrollX="+getScrollX());
//
//            if(scrollRatio == 0f) return;
//
//            if (enableBlurWhenScroll) {
//                //左页
//                if(getChildAt(scrollQuotient) != null){
//                    getChildAt(scrollQuotient).setAlpha(1 - scrollRatio);
//                }
//                //右页
//                if(getChildAt(scrollQuotient + 1) != null){
//                    getChildAt(scrollQuotient + 1).setAlpha(scrollRatio);
//                }
//            }
//            tabPageIndicator.gradientAlpha(scrollQuotient, scrollQuotient + 1, 1 - scrollRatio, scrollRatio);
//        }
	}
	
}
