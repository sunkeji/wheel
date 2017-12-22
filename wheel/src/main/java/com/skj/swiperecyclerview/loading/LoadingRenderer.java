package com.skj.swiperecyclerview.loading;

/**
 * Created by 孙科技 on 2017/12/7.
 */


import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.view.animation.LinearInterpolator;

import com.skj.util.DisplayUtil;

public abstract class LoadingRenderer {
    private static final long ANIMATION_DURATION = 1333L;
    private final AnimatorUpdateListener mAnimatorUpdateListener = new AnimatorUpdateListener() {
        public void onAnimationUpdate(ValueAnimator animation) {
            LoadingRenderer.this.computeRender(((Float) animation.getAnimatedValue()).floatValue());
            LoadingRenderer.this.invalidateSelf();
        }
    };
    protected final Rect mBounds = new Rect();
    private Callback mCallback;
    private ValueAnimator mRenderAnimator;
    protected long mDuration;
    protected float mWidth;
    protected float mHeight;

    public LoadingRenderer(Context context) {
        this.mWidth = this.mHeight = DisplayUtil.dip2px(context, 56.0F);
        this.mDuration = 1333L;
        this.setupAnimators();
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected void draw(Canvas canvas, Rect bounds) {
    }

    protected void draw(Canvas canvas) {
        this.draw(canvas, this.mBounds);
    }

    protected abstract void computeRender(float var1);

    protected abstract void setAlpha(int var1);

    protected abstract void setColorFilter(ColorFilter var1);

    protected abstract void reset();

    protected void addRenderListener(AnimatorListener animatorListener) {
        this.mRenderAnimator.addListener(animatorListener);
    }

    void start() {
        this.reset();
        this.mRenderAnimator.addUpdateListener(this.mAnimatorUpdateListener);
        this.mRenderAnimator.setRepeatCount(-1);
        this.mRenderAnimator.setDuration(this.mDuration);
        this.mRenderAnimator.start();
    }

    void stop() {
        this.mRenderAnimator.removeUpdateListener(this.mAnimatorUpdateListener);
        this.mRenderAnimator.setRepeatCount(0);
        this.mRenderAnimator.setDuration(0L);
        this.mRenderAnimator.end();
    }

    boolean isRunning() {
        return this.mRenderAnimator.isRunning();
    }

    void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    void setBounds(Rect bounds) {
        this.mBounds.set(bounds);
    }

    @SuppressLint("WrongConstant")
    private void setupAnimators() {
        this.mRenderAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
        this.mRenderAnimator.setRepeatCount(-1);
        this.mRenderAnimator.setRepeatMode(1);
        this.mRenderAnimator.setDuration(this.mDuration);
        this.mRenderAnimator.setInterpolator(new LinearInterpolator());
        this.mRenderAnimator.addUpdateListener(this.mAnimatorUpdateListener);
    }

    private void invalidateSelf() {
        this.mCallback.invalidateDrawable((Drawable) null);
    }
}

