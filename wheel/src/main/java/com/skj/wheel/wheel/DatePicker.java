package com.skj.wheel.wheel;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.skj.wheel.R;
import com.skj.wheel.wheel.widget.CanShow;
import com.skj.wheel.wheel.widget.OnWheelChangedListener;
import com.skj.wheel.wheel.widget.WheelView;
import com.skj.wheel.wheel.widget.adapters.NumericWheelAdapter;

import java.util.Calendar;

/**
 * Created by 孙科技 on 2017/7/13.
 */

public class DatePicker implements CanShow, OnWheelChangedListener {

    private Context context;

    private PopupWindow popwindow;

    private View popview;

    private WheelView mYearPicker, mMothPicker, mDayPicker;

    private TextView mTvOK, mTvCancle;

    private int mYear = 1990;
    private int mMonth = 0;
    private int mDay = 1;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onSelected(String date);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

    /**
     * Default text size
     */
    public static final int DEFAULT_TEXT_SIZE = 18;

    // Text settings
    private int textColor = DEFAULT_TEXT_COLOR;

    private int textSize = DEFAULT_TEXT_SIZE;


    /**
     * 设置popwindow的背景
     */

    private DatePicker(DatePicker.Builder builder) {
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.context = builder.mContext;

        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR);

        int curYear = mYear;
        int curMonth = mMonth + 1;
        int curDate = mDay;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_datepicker, null);

        mYearPicker = (WheelView) popview.findViewById(R.id.id_year);
        mMothPicker = (WheelView) popview.findViewById(R.id.id_moth);
        mDayPicker = (WheelView) popview.findViewById(R.id.id_day);
        mTvOK = (TextView) popview.findViewById(R.id.tv_confirm);
        mTvCancle = (TextView) popview.findViewById(R.id.tv_cancel);
        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(false);
        popwindow.setFocusable(true);

        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(context, 1950, norYear);
        numericWheelAdapter1.setLabel("年");
        mYearPicker.setViewAdapter(numericWheelAdapter1);
        mYearPicker.setCyclic(true);

        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(context, 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        mMothPicker.setViewAdapter(numericWheelAdapter2);
        mMothPicker.setCyclic(true);

        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context, 1, getDay(curYear, curMonth), "%02d");
        numericWheelAdapter.setLabel("日");
        mDayPicker.setCyclic(true);
        mDayPicker.setViewAdapter(numericWheelAdapter);


        mYearPicker.setVisibleItems(7);
        mMothPicker.setVisibleItems(7);
        mDayPicker.setVisibleItems(7);

        mYearPicker.setCurrentItem(curYear - 1950);
        mMothPicker.setCurrentItem(curMonth - 1);
        mDayPicker.setCurrentItem(curDate - 1);

        // 添加change事件
        mYearPicker.addChangingListener(this);
        mMothPicker.addChangingListener(this);
        mDayPicker.addChangingListener(this);
        // 添加onclick事件

        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelected(mYear + "-" + (mMonth+1) + "-" + mDay);
                hide();
            }
        });
        mTvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

    }

    public static class Builder {
        /**
         * Default text color
         */
        public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

        /**
         * Default text size
         */
        public static final int DEFAULT_TEXT_SIZE = 16;

        // Text settings
        private int textColor = DEFAULT_TEXT_COLOR;

        private int textSize = DEFAULT_TEXT_SIZE;
        /**
         * item间距
         */
        private int padding = 10;
        private Context mContext;

        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         * item文字颜色
         *
         * @param textColor
         * @return
         */
        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        /**
         * item文字大小
         *
         * @param textSize
         * @return
         */
        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }


        /**
         * item间距
         *
         * @param itemPadding
         * @return
         */
        public Builder itemPadding(int itemPadding) {
            this.padding = itemPadding;
            return this;
        }

        public DatePicker build() {
            DatePicker cityPicker = new DatePicker(this);
            return cityPicker;
        }

    }


    @Override
    public void setType(int type) {
    }

    @Override
    public void show() {
        if (!isShow()) {
            popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void hide() {
        if (isShow()) {
            popwindow.dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return popwindow.isShowing();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mYearPicker) {
            mYear = mYearPicker.getCurrentItem() + 1950;
        }
        if (wheel == mMothPicker) {
            mMonth = mMothPicker.getCurrentItem() ;
            NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context, 1, getDay(mYear, mMonth + 1), "%02d");
            numericWheelAdapter.setLabel("日");
            mDayPicker.setCyclic(true);
            mDayPicker.setViewAdapter(numericWheelAdapter);
        }
        if (wheel == mDayPicker) {
            mDay = mDayPicker.getCurrentItem();
        }
    }

    /**
     * 不同的年份和月份天数
     *
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }


}
