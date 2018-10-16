package com.bowen.tcm.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.bowen.commonlib.utils.EmptyUtils;
import com.bowen.commonlib.utils.ScreenSizeUtil;
import com.bowen.tcm.R;
import com.bowen.tcm.common.bean.DateNum;
import com.bowen.tcm.common.bean.ReservationBean;
import com.bowen.tcm.common.bean.network.AppointmentInfo;
import com.bowen.tcm.common.model.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Describe:预约控件
 * Created by AwenZeng on 2018/6/26.
 */
public class ReservationView extends View {

    private Context mContext;
    private Paint paint;
    private Calendar mCalendar;
    private int mItemWidth, mItemHeight;
    private int mSelectRow, mSelectColumn;
    private int width, height;

    private int downX = 0, downY = 0;

    private boolean isSelected = false;

    private int lineSize = ScreenSizeUtil.dp2px(1);//线的大小
    private int lineColor = Color.parseColor("#e3e3e3");
    private int mWeekdayColor;
    private ArrayList<ReservationBean> mReservationBeans;

    private String[] weekString = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private List<DateNum> dateList = new ArrayList<>();
    private String[] timeString = new String[]{"上午", "下午", "晚上"};

    private static final int NUM_COLUMNS = 8;
    private static final int NUM_ROWS = 4;

    public ReservationTimeListener mListener;

    public interface ReservationTimeListener {
        void onReservationTime(DateNum dateNum, int pos);
    }

    public void setListener(ReservationTimeListener mListener) {
        this.mListener = mListener;
    }

    public ReservationView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ReservationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();

    }

    public ReservationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    /**
     * 初始化列宽行高
     */
    private void init() {
        mWeekdayColor = mContext.getResources().getColor(R.color.color_main_black);
        paint = new Paint();
        paint.setAntiAlias(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        width = getWidth();
        height = getHeight();
        mItemWidth = width / NUM_COLUMNS;
        mItemHeight = height / NUM_ROWS;

        for (int row = 0; row < NUM_ROWS; row++) {//行循环

            drawRowLine(canvas, row);//绘制行线

            if (row == 0) {//第一行，绘制日期
                for (int column = 0; column < NUM_COLUMNS; column++) {

                    drawColumnLine(canvas, column);//绘制列线

                    if (column != 0) {//第一格不设置任何信息
                        drawDateAndWeek(canvas, column);
                    }

                }
            } else {

                drawDayTime(canvas, row);

                for (int column = 0; column < NUM_COLUMNS; column++) {

                    drawColumnLine(canvas, column);//绘制列线
                    if (column != 0) {//第一列不绘制
                        if(EmptyUtils.isNotEmpty(mReservationBeans)){
                            ReservationBean bean = mReservationBeans.get(column-1);
                            int status = bean.getReserveStatus()[row-1];
                            drawStatusView(canvas, row, column,status);
                        }
                    }
                }
            }
        }
        //绘制边界线
        drawFrame(canvas);
    }

    private void drawStatusView(Canvas canvas, int row, int column,int status){
        switch (status){
            case Constants.STATUS_APPIONTMENT:
                drawReservation(canvas, row, column);
                break;
            case Constants.STATUS_APPIONTMENT_FULL:
                drawReserveFull(canvas, row, column);
                break;
            case Constants.STATUS_APPIONTMENT_NOT:
                drawNotReception(canvas, row, column);
                break;
            case Constants.STATUS_APPIONTMENT_OUTDATE:
                drawOutDate(canvas, row, column);
                break;
            case Constants.STATUS_APPIONTMENT_SELECTED:
                drawChooseItemBg(canvas, row, column);
                break;
        }
    }

    /**
     * 绘制周几和日期
     *
     * @param canvas
     * @param column
     */
    private void drawDateAndWeek(Canvas canvas, int column) {
        //绘制周一至周日
        String text = weekString[column - 1];
        paint.setColor(mContext.getResources().getColor(R.color.color_main_black));
        paint.setTextSize(ScreenSizeUtil.sp2px(15));
        int fontWidth = (int) paint.measureText(text);
        int startX = mItemWidth * column + (mItemWidth - fontWidth) / 2;
        int startY = mItemHeight / 2;
        canvas.drawText(text, startX, startY, paint);

        //绘制一周的日期
        paint.setTextSize(ScreenSizeUtil.sp2px(14));
        paint.setColor(mContext.getResources().getColor(R.color.color_main_gray_01));
        if(EmptyUtils.isNotEmpty(mReservationBeans)){
            ReservationBean bean = mReservationBeans.get(column - 1);
            if(EmptyUtils.isNotEmpty(bean)&&EmptyUtils.isNotEmpty(bean.getDate())){
                fontWidth = (int) paint.measureText(bean.getDate());
                startX = mItemWidth * column + (mItemWidth - fontWidth) / 2;
                canvas.drawText(bean.getDate(), startX, startY + mItemHeight / 3, paint);
            }
        }
    }

    /**
     * 绘制一天的时间（上午，下午，晚上）
     *
     * @param canvas
     * @param row
     */
    private void drawDayTime(Canvas canvas, int row) {
        paint.setTextSize(ScreenSizeUtil.sp2px(15));
        paint.setColor(mWeekdayColor);
        String text = timeString[row - 1];
        int fontWidth = (int) paint.measureText(text);
        int startX = (mItemWidth - fontWidth) / 2;
        int startY = (int) (mItemHeight / 2 - (paint.ascent() + paint.descent()) / 2);
        canvas.drawText(text, startX, startY + mItemHeight * row, paint);
    }


    /**
     * 绘制预约
     *
     * @param canvas
     * @param row
     * @param column
     */
    private void drawReservation(Canvas canvas, int row, int column) {
        //进行画格子左边的竖线
        paint.setColor(mContext.getResources().getColor(R.color.color_white));
        paint.setStrokeWidth(lineSize);
        canvas.drawLine(mItemWidth * column, mItemHeight * row, mItemWidth * column, mItemHeight * (row + 1), paint);

        //绘制预约背景色矩形
        paint.setColor(mContext.getResources().getColor(R.color.color_main));
        int startRecX = mItemWidth * column;
        int startRecY = mItemHeight * row;
        int endRecX = startRecX + mItemWidth + lineSize;
        int endRecY = startRecY + mItemHeight;
        canvas.drawRect(startRecX, startRecY, endRecX, endRecY, paint);

        //绘制预约
        paint.setTextSize(ScreenSizeUtil.sp2px(14));
        paint.setColor(mContext.getResources().getColor(R.color.color_white));
        canvas.drawText("预约", (endRecX - startRecX) / 5 + startRecX, (endRecY - startRecY) / 2 + startRecY + 13, paint);
    }

    /**
     * 绘制约满
     *
     * @param canvas
     * @param row
     * @param column
     */
    private void drawReserveFull(Canvas canvas, int row, int column) {
        //进行画格子左边的竖线
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineSize);
        canvas.drawLine(mItemWidth * column, mItemHeight * row, mItemWidth * column, mItemHeight * (row + 1), paint);

        //绘制预约背景色矩形
        paint.setColor(Color.parseColor("#f0f0f0"));
        int startRecX = mItemWidth * column;
        int startRecY = mItemHeight * row;
        int endRecX = startRecX + mItemWidth + lineSize;
        int endRecY = startRecY + mItemHeight;
        canvas.drawRect(startRecX, startRecY, endRecX, endRecY, paint);

        //绘制预约
        paint.setTextSize(ScreenSizeUtil.sp2px(14));
        paint.setColor(Color.parseColor("#a4a4a4"));
        canvas.drawText("约满", (endRecX - startRecX) / 5 + startRecX, (endRecY - startRecY) / 2 + startRecY + 13, paint);
    }

    /**
     * 绘制不接诊
     *
     * @param canvas
     * @param row
     * @param column
     */
    private void drawNotReception(Canvas canvas, int row, int column) {
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineSize);
        canvas.drawLine(mItemWidth * column, mItemHeight * row, mItemWidth * column, mItemHeight * (row + 1), paint);

        //绘制预约背景色矩形
        paint.setColor(mContext.getResources().getColor(R.color.color_white));
        int startRecX = mItemWidth * column;
        int startRecY = mItemHeight * row;
        int endRecX = startRecX + mItemWidth;
        int endRecY = startRecY + mItemHeight;
        canvas.drawRect(startRecX, startRecY, endRecX, endRecY, paint);

        //绘制预约
        paint.setTextSize(ScreenSizeUtil.sp2px(11));
        paint.setColor(Color.parseColor("#a4a4a4"));
        canvas.drawText("不接诊", (endRecX - startRecX) / 5 + startRecX - ScreenSizeUtil.dp2px(3), (endRecY - startRecY) / 2 + startRecY + 13, paint);
    }

    /**
     * 绘制预约
     *
     * @param canvas
     * @param row
     * @param column
     */
    private void drawAlreadySetReervation(Canvas canvas, int row, int column) {
        //进行画格子左边的竖线
        paint.setColor(mContext.getResources().getColor(R.color.color_white));
        paint.setStrokeWidth(lineSize);
        canvas.drawLine(mItemWidth * column, mItemHeight * row, mItemWidth * column, mItemHeight * (row + 1), paint);

        //绘制预约背景色矩形
        paint.setColor(mContext.getResources().getColor(R.color.color_main));
        int startRecX = mItemWidth * column;
        int startRecY = mItemHeight * row;
        int endRecX = startRecX + mItemWidth + lineSize;
        int endRecY = startRecY + mItemHeight;
//        canvas.drawRect(startRecX, startRecY, endRecX, endRecY, paint);

        //绘制预约
        paint.setTextSize(ScreenSizeUtil.sp2px(14));
        paint.setColor(mContext.getResources().getColor(R.color.color_red));
        canvas.drawText("√", (endRecX - startRecX) / 5 + startRecX, (endRecY - startRecY) / 2 + startRecY + 13, paint);
    }

    /**
     * 绘制过期
     *
     * @param canvas
     * @param row
     * @param column
     */
    private void drawOutDate(Canvas canvas, int row, int column) {
        //进行画格子左边的竖线
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineSize);
        canvas.drawLine(mItemWidth * column, mItemHeight * row, mItemWidth * column, mItemHeight * (row + 1), paint);

        //绘制预约背景色矩形
        paint.setColor(Color.parseColor("#f0f0f0"));
        int startRecX = mItemWidth * column;
        int startRecY = mItemHeight * row;
        int endRecX = startRecX + mItemWidth + lineSize;
        int endRecY = startRecY + mItemHeight;
        canvas.drawRect(startRecX, startRecY, endRecX, endRecY, paint);

        //绘制预约
        paint.setTextSize(ScreenSizeUtil.sp2px(14));
        paint.setColor(Color.parseColor("#a4a4a4"));
        canvas.drawText("", (endRecX - startRecX) / 5 + startRecX, (endRecY - startRecY) / 2 + startRecY + 13, paint);
    }


    /**
     * 绘制预约
     *
     * @param canvas
     * @param row
     * @param column
     */
    private void drawChooseItemBg(Canvas canvas, int row, int column) {
        //进行画格子左边的竖线
        paint.setColor(mContext.getResources().getColor(R.color.color_white));
        paint.setStrokeWidth(lineSize);
        canvas.drawLine(mItemWidth * column, mItemHeight * row, mItemWidth * column, mItemHeight * (row + 1), paint);

        //绘制预约背景色矩形
        paint.setColor(mContext.getResources().getColor(R.color.color_main));
        int startRecX = mItemWidth * column;
        int startRecY = mItemHeight * row;
        int endRecX = startRecX + mItemWidth + lineSize;
        int endRecY = startRecY + mItemHeight;
        canvas.drawRect(startRecX, startRecY, endRecX, endRecY, paint);

        //绘制预约
        paint.setTextSize(ScreenSizeUtil.sp2px(14));
        paint.setColor(mContext.getResources().getColor(R.color.color_red));
        canvas.drawText("√", (endRecX - startRecX) / 5 + startRecX, (endRecY - startRecY) / 2 + startRecY + 13, paint);
    }

    /**
     * 绘制行线
     *
     * @param canvas
     * @param row
     */
    private void drawRowLine(Canvas canvas, int row) {
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineSize);
        canvas.drawLine(0, mItemHeight * row, width, mItemHeight * row, paint);
    }

    /**
     * 绘制列线
     *
     * @param canvas
     * @param column
     */
    private void drawColumnLine(Canvas canvas, int column) {
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineSize);
        canvas.drawLine(mItemWidth * column, 0, mItemWidth * column, height, paint);
    }


    /**
     * 绘制边框线
     *
     * @param canvas
     */
    private void drawFrame(Canvas canvas) {
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineSize);
        canvas.drawLine(0, 0, width, 0, paint);
        canvas.drawLine(0, 0, 0, height, paint);
        canvas.drawLine(width, 0, width, height, paint);
        canvas.drawLine(0, height, width, height, paint);
    }


    public ArrayList<ReservationBean> getReservationBeans() {
        return mReservationBeans;
    }

    public void setReservationBeans(ArrayList<ReservationBean> reservationBeans) {
        this.mReservationBeans = reservationBeans;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode = event.getAction();
        switch (eventCode) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {//点击事件
                    doClickAction((upX + downX) / 2, (upY + downY) / 2);
                }
                break;
        }
        return true;
    }

    /**
     * 执行点击事件
     *
     * @param x
     * @param y
     */
    private void doClickAction(int x, int y) {
        int row = y / mItemHeight;
        int column = x / mItemWidth;

        if (row != 0 && column != 0) {
            //保存用户选中的item
            setCheckedItem(row, column);
        }
        invalidate();
    }

    private void setCheckedItem(int row, int column){
        if(EmptyUtils.isEmpty(mReservationBeans)){
            return;
        }
        this.mSelectRow = row;
        this.mSelectColumn = column;
        DateNum dateNum = new DateNum();
//        DateNum dateNum = dateList.get(column - 1);
        dateNum.setDayType(row);//行为：上午，下午，晚上
        ReservationBean bean = mReservationBeans.get(column - 1);
        dateNum.setDateTime(bean.getDateTime());
        int status = bean.getReserveStatus()[row - 1];
        if (mListener != null&&status != Constants.STATUS_APPIONTMENT_NOT&&status!=Constants.STATUS_APPIONTMENT_OUTDATE) {
            mListener.onReservationTime(dateNum, column-1);//列为数据
//            int[] temp = bean.getReserveStatus();
//            temp[row -1] = 4;
//            bean.setReserveStatus(temp);
//            reservationBeans.set(column - 1,bean);
        }
    }


    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * 计算当前一周日期并显示在界面
     *
     * @param week 0：为当前周
     */
    public void caculateWeekDates(int week) {
        mCalendar = Calendar.getInstance();
//        mCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        mCalendar.add(Calendar.WEEK_OF_YEAR, week);
        Date date = mCalendar.getTime();
//        this.dateList = dateToWeek(date);
        invalidate();
    }

    /**
     * 根据日期获得所在周的日期列表
     *
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
    public static List<DateNum> dateToWeek(Date date) {
        int b = date.getDay();
        Date fDate;
        List<DateNum> list = new ArrayList<>();
        Long fTime = date.getTime() - b * 24 * 3600000;
        for (int i = 0; i < 7; i++) {
            fDate = new Date();
            fDate.setTime(fTime + ((i + 1) * 24 * 3600000)); //一周从周日开始算
            DateNum dateNum = new DateNum();
            dateNum.setDateTime(fDate.getTime());
            switch (i) {
                case 0:
                    dateNum.setWeek("周一");
                    break;
                case 1:
                    dateNum.setWeek("周二");
                    break;
                case 2:
                    dateNum.setWeek("周三");
                    break;
                case 3:
                    dateNum.setWeek("周四");
                    break;
                case 4:
                    dateNum.setWeek("周五");
                    break;
                case 5:
                    dateNum.setWeek("周六");
                    break;
                case 6:
                    dateNum.setWeek("周日");
                    break;

                default:

                    break;
            }
//            dateNum.setMonth(fDate.getMonth() + 1);
//            dateNum.setDay(fDate.getDate());
            list.add(dateNum);
        }
        return list;
    }


}