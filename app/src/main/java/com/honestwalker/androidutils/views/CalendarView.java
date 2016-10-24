package com.honestwalker.androidutils.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;



@SuppressLint("ClickableViewAccessibility")
public class CalendarView extends LinearLayout {

    public interface OnCalendarItemClickListener{
        void onCalendarItemClick(Calendar calendar, String year, String month, String dayOfMonth, String dayOfWeek);
    }

    private OnCalendarItemClickListener onCalendarItemClickListener;

    public void setOnCalendarItemClickListener(
            OnCalendarItemClickListener onCalendarItemClickListener) {
        this.onCalendarItemClickListener = onCalendarItemClickListener;
    }

    public interface OnSwitchCalendarMonthListener{
        void onMonthSwitch(String year, String month);
    }

    private OnSwitchCalendarMonthListener onSwitchCalendarMonthListener;

    public void setOnSwitchCalendarMonthListener(
            OnSwitchCalendarMonthListener onSwitchCalendarMonthListener) {
        this.onSwitchCalendarMonthListener = onSwitchCalendarMonthListener;
    }

    private Context context;

    private Config config;

    private Calendar currentCalendar;
    private Calendar calendarIterater;

    private Date currentDate;

    private boolean isOnTouch;
    public boolean isOnTouch() {
        return isOnTouch;
    }

    public String getYearOnDisplay(){
        return calendarIterater.get(Calendar.YEAR) + "";
    }

    public String getMonthOnDisplay(){
        return calendarIterater.get(Calendar.MONTH) + 1 + "";
    }

    class Config{//全局设置

        int screenWidth;
        int mTouchSlop;

        String[] weekUnitNames;
        int[] weekUnitDaysOfWeek;
        int[] weekUnitColors;

        int unitSize;
        int borderWidth;
        int rows;
        int unitsInRow;

        int textSizeType;
        float textSizeWeekUnit;
        float textSizeCalendarUnit;
        int textColorActivatedRes;
        int textColorAccessibleRes;
        int textColorInactivatedRes;
        int textBackgroundRes;
        int weekUnitPadding;
        int calendarUnitPadding;

        Config(){
            screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() * 2;
            rows = 6;//6行
            unitsInRow = 7;//每行7个元素
            borderWidth = dip2px(8);//8dp
            unitSize = (screenWidth - borderWidth * (unitsInRow + 1)) / unitsInRow;
            textSizeType = TypedValue.COMPLEX_UNIT_SP;
            textSizeWeekUnit = 18;//14sp
            textSizeCalendarUnit = 16;//16sp
            textColorActivatedRes = Color.parseColor("#fffdfd");
            textColorAccessibleRes = Color.parseColor("#999999");
            textColorInactivatedRes = Color.parseColor("#666666");
            textBackgroundRes = 0;
            weekUnitPadding = dip2px(5);//5dp
            calendarUnitPadding = dip2px(10);//10dp

            weekUnitNames = new String[]{"日","一","二","三","四","五","六"};
            weekUnitDaysOfWeek = new int[]{Calendar.SUNDAY,Calendar.MONDAY,Calendar.TUESDAY,
                    Calendar.WEDNESDAY,Calendar.THURSDAY,Calendar.FRIDAY,Calendar.SATURDAY};
            weekUnitColors = new int[]{parseColor("#df4d19"),parseColor("#ffffff"),
                    parseColor("#ffffff"),parseColor("#ffffff"),parseColor("#ffffff"),
                    parseColor("#ffffff"),parseColor("#df4d19")};
        }

        int parseColor(String hex){
            return Color.parseColor(hex);
        }

        int px2dip(int pxValue) {
            float scale = getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }

        int dip2px(int dpValue) {
            final float scale = getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }
    }

    class CalendarTextView extends TextView {

        int year;
        int month;
        int dayOfMonth;
        int dayOfWeek;
        String dayOfWeekStr;
        Calendar calendar;
        String dateStr;
        boolean isInactive;

        public CalendarTextView(Context context) {
            super(context);
        }

        void displayDate(){
            setText(dayOfMonth + "");
        }

        void inactivate(){
            setTextColor(config.textColorInactivatedRes);
            setClickable(false);
            isInactive = true;
        }

        void setAccessible(){
            if (!isInactive) {
                setTextColor(config.textColorAccessibleRes);
                setOnClickListener(mAutoFlipOnClickListener);
            }
        }

        OnClickListener mAutoFlipOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (month < calendarIterater.get(Calendar.MONTH) + 1){
                    switchToPreviousMonth();
                }else {
                    switchToNextMonth();
                }
                selectDay = dayOfMonth;
            }
        };

        String getDateStr(){
            return String.format("%s-%s-%s %s", year,
                    (month < 10 ? "0" : "") + month,
                    (dayOfMonth < 10 ? "0" : "") + dayOfMonth,
                    dayOfWeekStr);
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
            if (selected) {
                mCurrentCalendarTV = this;
            }
        }
    }

    public void switchToNextMonth(){
        switchSeveralMonth(1);
    }

    public void switchToPreviousMonth(){
        switchSeveralMonth(-1);
    }


    private void switchSeveralMonth(int months){
        calendarIterater.set(Calendar.DAY_OF_MONTH, 1);
        calendarIterater.add(Calendar.MONTH, months);
        iterateComplete = false;
        displayCalendar();
    }

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initCalendar();
        displayCalendar();
        allowAnimationWhileFlip = true;
    }

    private void initCalendar() {
        currentDate = new Date();
        currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);
        calendarIterater = Calendar.getInstance();
        calendarIterater.setTime(currentDate);
        calendarIterater.set(Calendar.DAY_OF_MONTH, 1);
    }

    private void displayCalendar() {
        setOrientation(VERTICAL);
        config = new Config();
        getCalendarDisplay();
    }

    private enum RowType{
        CalendarUnit,WeekLegend
    }

    private void getCalendarDisplay(){
        if (allowAnimationWhileFlip) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    calendarDone();
                }
            }, 150);
        }else {
            calendarDone();
        }
    }

    private void calendarDone(){
        removeAllViews();
        //星期标注
        addView(getRowUnitDisplay(RowType.WeekLegend));
        for (int i = 0; i < config.rows; i++) {
            addView(getRowUnitDisplay(RowType.CalendarUnit));
            addView(getBorderView(HORIZONTAL, config.borderWidth));
        }
        if (onSwitchCalendarMonthListener != null) {
            onSwitchCalendarMonthListener.onMonthSwitch(getYearOnDisplay(), getMonthOnDisplay());
        }
    }

    private View getRowUnitDisplay(RowType rowType){
        LinearLayout rowLayout = new LinearLayout(context);
        rowLayout.setOrientation(HORIZONTAL);
        LayoutParams params = new LayoutParams(MATCH_PARENT, config.unitSize);
        rowLayout.setLayoutParams(params);
        rowLayout.addView(getBorderView(VERTICAL, config.borderWidth));
        for (int i = 0; i < config.unitsInRow; i++) {
            switch (rowType) {
                case WeekLegend:
                    ViewGroup weekUnit = (ViewGroup) getUnitDisplay(RowType.WeekLegend);
                    ((TextView)weekUnit.getChildAt(0)).setText(config.weekUnitNames[i]);
                    ((TextView)weekUnit.getChildAt(0)).setTextColor(config.weekUnitColors[i]);
                    rowLayout.addView(weekUnit);
                    break;
                case CalendarUnit:
                    ViewGroup calendarUnit = (ViewGroup) getUnitDisplay(RowType.CalendarUnit);
                    ((CalendarTextView)calendarUnit.getChildAt(0)).dayOfWeek = config.weekUnitDaysOfWeek[i];
                    ((CalendarTextView)calendarUnit.getChildAt(0)).dayOfWeekStr = "周" + config.weekUnitNames[i];
                    insertDateIntoTextView((CalendarTextView)calendarUnit.getChildAt(0));
                    rowLayout.addView(calendarUnit);
                    break;
            }
            rowLayout.addView(getBorderView(VERTICAL, config.borderWidth));
        }
        return rowLayout;
    }

    private boolean iterateComplete;
    private int selectDay;//点击上月或下月时跳到相应月的天

    /**
     * 填充所有日历单元的值
     */
    private void insertDateIntoTextView(CalendarTextView calendarTextView) {
        if (!iterateComplete){
            /**  填充本月 */
            if (calendarTextView.dayOfWeek == calendarIterater.get(Calendar.DAY_OF_WEEK)) {

                fillCalendarTV(calendarIterater, calendarTextView);
                selectOneDay(calendarTextView);

                if (calendarIterater.get(Calendar.DAY_OF_MONTH) == calendarIterater.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    iterateComplete = true;
                    countRefOfNextMonth = 0;
                }else {
                    calendarIterater.add(Calendar.DAY_OF_MONTH, 1);
                }
            } else {
                /**  填充上个月*/
                fillPreviousMonth(calendarTextView);
            }
        }else{
            /**  填充下个月   */
            fillNextMonth(calendarTextView);
        }
    }

    private void selectOneDay(CalendarTextView calendarTextView){
        if (selectDay != 0){
            if (calendarTextView.dayOfMonth == selectDay) {
                calendarTextView.setSelected(true);
            }
        }else{
            if (calendarTextView.calendar.getTime().equals(currentDate)) {
                calendarTextView.setSelected(true);
            }
            if (calendarTextView.calendar.getTime().after(currentDate) && calendarTextView.dayOfMonth == 1) {
                calendarTextView.setSelected(true);
            }
        }
    }

    private void fillCalendarTV(Calendar calendar,CalendarTextView tv){
        tv.calendar = (Calendar) calendar.clone();
        tv.year = calendar.get(Calendar.YEAR);
        tv.month = calendar.get(Calendar.MONTH) + 1;
        tv.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        tv.displayDate();
        if (tv.calendar.getTime().before(currentDate)) {
            tv.inactivate();
        }
    }

    /**
     * 填充本月中上个月的某几天值
     */
    private void fillPreviousMonth(CalendarTextView calendarTextView){
        Calendar previousMonthCalendar = (Calendar) calendarIterater.clone();
        previousMonthCalendar.add(Calendar.MONTH,-1);
        int days = previousMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        previousMonthCalendar.set(Calendar.DAY_OF_MONTH, days);

        for (int i = 0;i < 7;i++){
            if (calendarTextView.dayOfWeek == previousMonthCalendar.get(Calendar.DAY_OF_WEEK)) {
                fillCalendarTV(previousMonthCalendar, calendarTextView);
                calendarTextView.setAccessible();
                break;
            }
            previousMonthCalendar.add(Calendar.DAY_OF_MONTH, -1);
        }
    }
    /**
     * 填充本月中下个月的某几天值
     */
    private int countRefOfNextMonth;
    private void fillNextMonth(CalendarTextView calendarTextView){
        Calendar nextMonthCalendar = (Calendar) calendarIterater.clone();
        nextMonthCalendar.add(Calendar.MONTH, 1);
        nextMonthCalendar.set(Calendar.DAY_OF_MONTH, ++countRefOfNextMonth);
        fillCalendarTV(nextMonthCalendar, calendarTextView);
        calendarTextView.setAccessible();
    }

    private View getUnitDisplay(RowType rowType) {

        LinearLayout unitLayout = new LinearLayout(context);
        LayoutParams params = new LayoutParams(config.unitSize, config.unitSize);
        unitLayout.setLayoutParams(params);
        unitLayout.setOrientation(HORIZONTAL);
        CalendarTextView unitTV = new CalendarTextView(context);
        unitTV.setGravity(Gravity.CENTER);

        switch (rowType) {
            case WeekLegend:
                unitTV.setClickable(false);
                unitTV.setTextSize(config.textSizeType, config.textSizeWeekUnit);
                unitTV.setPadding(config.weekUnitPadding, config.weekUnitPadding, config.weekUnitPadding, config.weekUnitPadding);
                unitTV.setTypeface(Typeface.DEFAULT_BOLD);
                break;
            case CalendarUnit:
                unitTV.setTextSize(config.textSizeType, config.textSizeCalendarUnit);
                unitTV.setPadding(config.calendarUnitPadding, config.calendarUnitPadding, config.calendarUnitPadding, config.calendarUnitPadding);
                unitTV.setTextColor(config.textColorActivatedRes);
                unitTV.setBackgroundResource(config.textBackgroundRes);
                unitTV.setOnClickListener(onCalendarUnitClickListener);
                calendarTVComplex.add(unitTV);
                break;
        }

        unitLayout.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        unitLayout.addView(unitTV,params);
        unitLayout.addView(getBorderView(VERTICAL,config.borderWidth), params);
        return unitLayout;
    }

    private View getBorderView(int orientation,int width){
        View borderView = new View(context);
        LayoutParams params = null;
        switch (orientation) {
            case VERTICAL:
                params = new LayoutParams(width, MATCH_PARENT);
                break;
            case HORIZONTAL:
                params = new LayoutParams(MATCH_PARENT, width);
                break;
        }
        borderView.setLayoutParams(params);
        return borderView;
    }

    ArrayList<CalendarTextView> calendarTVComplex = new ArrayList<CalendarTextView>();

    CalendarTextView mCurrentCalendarTV;

    public Calendar getCurrentCalendar() {
        return mCurrentCalendarTV.calendar;
    }

    public String getCurrentCalendarStr(){
        return mCurrentCalendarTV.getDateStr();
    }

    private void iterateCalendarAndSetUnselected(){

        for (TextView mTextView : calendarTVComplex) {
            mTextView.setSelected(false);
        }
    }

    private OnClickListener onCalendarUnitClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            iterateCalendarAndSetUnselected();
            mCurrentCalendarTV = (CalendarTextView) v;
            mCurrentCalendarTV.setSelected(true);
            if (onCalendarItemClickListener != null) {
                onCalendarItemClickListener.onCalendarItemClick(
                        mCurrentCalendarTV.calendar,
                        mCurrentCalendarTV.year+"",
                        (mCurrentCalendarTV.month < 10 ? "0" : "") + mCurrentCalendarTV.month,
                        (mCurrentCalendarTV.dayOfMonth < 10 ? "0" : "") + mCurrentCalendarTV.dayOfMonth,
                        mCurrentCalendarTV.dayOfWeekStr);
            }
        }
    };

    private int downX,tempX;
    boolean toRight,toLeft;
    private boolean allowAnimationWhileFlip;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) ev.getRawX();
                isOnTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                if (Math.abs(moveX - downX) > config.mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                int deltaX = tempX - moveX;
                tempX = moveX;
                if (Math.abs(deltaX) > config.mTouchSlop) {
                    if (deltaX < 0) {
                        toLeft = true;
                    }else{
                        toRight = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                selectDay = 0;
                if (toRight) {
                    switchToNextMonth();
                }else if(toLeft){
                    switchToPreviousMonth();
                }
                toRight = toLeft = isOnTouch = false;
                break;

        }
        return true;
    }

}
