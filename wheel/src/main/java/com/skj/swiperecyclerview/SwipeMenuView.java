/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.skj.swiperecyclerview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yan Zhenjie on 2016/7/26.
 */
public class SwipeMenuView extends LinearLayout implements View.OnClickListener {

    private RecyclerView.ViewHolder mAdapterVIewHolder;
    private SwipeSwitch mSwipeSwitch;
    private SwipeMenuItemClickListener mItemClickListener;
    @SwipeMenuRecyclerView.DirectionMode
    private int mDirection;

    public SwipeMenuView(Context context) {
        this(context, null);
    }

    public SwipeMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void createMenu(SwipeMenu swipeMenu, SwipeSwitch swipeSwitch,
                           SwipeMenuItemClickListener swipeMenuItemClickListener,
                           @SwipeMenuRecyclerView.DirectionMode int direction) {
        removeAllViews();

        this.mSwipeSwitch = swipeSwitch;
        this.mItemClickListener = swipeMenuItemClickListener;
        this.mDirection = direction;

        List<SwipeMenuItem> items = swipeMenu.getMenuItems();
        for (int i = 0; i < items.size(); i++) {
            SwipeMenuItem item = items.get(i);

            LayoutParams params = new LayoutParams(item.getWidth(), item.getHeight());
            params.weight = item.getWeight();
            LinearLayout parent = new LinearLayout(getContext());
            parent.setId(i);
            parent.setGravity(Gravity.CENTER);
            parent.setOrientation(VERTICAL);
            parent.setLayoutParams(params);
            ViewCompat.setBackground(parent, item.getBackground());
            parent.setOnClickListener(this);
            addView(parent);

            SwipeMenuBridge menuBridge = new SwipeMenuBridge(mDirection, i, mSwipeSwitch, parent);
            parent.setTag(menuBridge);

            if (item.getImage() != null) {
                ImageView iv = createIcon(item);
                menuBridge.mImageView = iv;
                parent.addView(iv);
            }

            if (!TextUtils.isEmpty(item.getText())) {
                TextView tv = createTitle(item);
                menuBridge.mTextView = tv;
                parent.addView(tv);
            }
        }
    }

    public void bindViewHolder(RecyclerView.ViewHolder adapterVIewHolder) {
        this.mAdapterVIewHolder = adapterVIewHolder;
    }

    private ImageView createIcon(SwipeMenuItem item) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(item.getImage());
        return imageView;
    }

    private TextView createTitle(SwipeMenuItem item) {
        TextView textView = new TextView(getContext());
        textView.setText(item.getText());
        textView.setGravity(Gravity.CENTER);
        int textSize = item.getTextSize();
        if (textSize > 0)
            textView.setTextSize(textSize);
        ColorStateList textColor = item.getTitleColor();
        if (textColor != null)
            textView.setTextColor(textColor);
        int textAppearance = item.getTextAppearance();
        if (textAppearance != 0)
            TextViewCompat.setTextAppearance(textView, textAppearance);
        Typeface typeface = item.getTextTypeface();
        if (typeface != null)
            textView.setTypeface(typeface);
        return textView;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null && mSwipeSwitch.isMenuOpen()) {
            SwipeMenuBridge menuBridge = (SwipeMenuBridge) v.getTag();
            menuBridge.mAdapterPosition = mAdapterVIewHolder.getAdapterPosition();
            mItemClickListener.onItemClick(menuBridge);
        }
    }
}