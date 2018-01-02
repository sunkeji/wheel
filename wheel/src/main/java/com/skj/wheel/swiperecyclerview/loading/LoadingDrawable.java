package com.skj.wheel.swiperecyclerview.loading;

/**
 * Created by 孙科技 on 2017/12/7.
 */


import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

public class LoadingDrawable extends Drawable implements Animatable {
    private final LoadingRenderer mLoadingRender;
    private final Callback mCallback = new Callback() {
        public void invalidateDrawable(Drawable d) {
            LoadingDrawable.this.invalidateSelf();
        }

        public void scheduleDrawable(Drawable d, Runnable what, long when) {
            LoadingDrawable.this.scheduleSelf(what, when);
        }

        public void unscheduleDrawable(Drawable d, Runnable what) {
            LoadingDrawable.this.unscheduleSelf(what);
        }
    };

    public LoadingDrawable(LoadingRenderer loadingRender) {
        this.mLoadingRender = loadingRender;
        this.mLoadingRender.setCallback(this.mCallback);
    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mLoadingRender.setBounds(bounds);
    }

    public void draw(Canvas canvas) {
        if (!this.getBounds().isEmpty()) {
            this.mLoadingRender.draw(canvas);
        }

    }

    public void setAlpha(int alpha) {
        this.mLoadingRender.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mLoadingRender.setColorFilter(cf);
    }

    public int getOpacity() {
        return -3;
    }

    public void start() {
        this.mLoadingRender.start();
    }

    public void stop() {
        this.mLoadingRender.stop();
    }

    public boolean isRunning() {
        return this.mLoadingRender.isRunning();
    }

    public int getIntrinsicHeight() {
        return (int) this.mLoadingRender.mHeight;
    }

    public int getIntrinsicWidth() {
        return (int) this.mLoadingRender.mWidth;
    }
}

