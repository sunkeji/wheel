package com.skj.wheel;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skj.wheel.model.CityModel;
import com.skj.wheel.model.DistrictModel;
import com.skj.wheel.model.ProvinceModel;
import com.skj.wheel.widget.wheel.OnWheelChangedListener;
import com.skj.wheel.widget.wheel.WheelView;
import com.skj.wheel.widget.wheel.adapters.ArrayWheelAdapter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by 孙科技 on 2017/5/22.
 */

public class SexPicker implements CanShow, OnWheelChangedListener {

    private Context context;

    private PopupWindow popwindow;

    private View popview;

    private WheelView mSexPicker;

    private TextView mTvOK;
    private ArrayWheelAdapter sexStatusAdapter;
    private int mCurrentSexStatus = 1;// 1-男士;0-女士
    private String value = "男";
    private String[] data = new String[]{"男", "女"};


    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onSelected(String value, int sex);

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

    private SexPicker(Builder builder) {
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.context = builder.mContext;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_sexpicker, null);

        mSexPicker = (WheelView) popview.findViewById(R.id.wheelview_sex);
        mTvOK = (TextView) popview.findViewById(R.id.tv_confirm);
        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(false);
        popwindow.setFocusable(true);

        sexStatusAdapter = new ArrayWheelAdapter<String>(context, data);
        mSexPicker.setViewAdapter(sexStatusAdapter);
        mSexPicker.setCurrentItem(0);

        // 添加change事件
        mSexPicker.addChangingListener(this);
        // 添加onclick事件

        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelected(value, mCurrentSexStatus);
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
        private int padding = 5;
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

        public SexPicker build() {
            SexPicker cityPicker = new SexPicker(this);
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
        if (wheel == mSexPicker) {
            int pCurrent = mSexPicker.getCurrentItem();
            value = data[pCurrent];
            if ("男".equals(value)) {
                mCurrentSexStatus = 1;
            } else if ("女".equals(value)) {
                mCurrentSexStatus = 0;
            }
        }
    }
}
