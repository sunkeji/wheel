package com.skj.wheel.swiperecyclerview.loading;

/**
 * Created by 孙科技 on 2017/12/7.
 */


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class LoadingView extends ImageView {
    private LoadingDrawable mLoadingDrawable;
    private LevelLoadingRenderer mLoadingRenderer;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mLoadingRenderer = new LevelLoadingRenderer(context);
        this.mLoadingDrawable = new LoadingDrawable(this.mLoadingRenderer);
        this.setImageDrawable(this.mLoadingDrawable);
    }

    public void setCircleColors(int r1, int r2, int r3) {
        this.mLoadingRenderer.setCircleColors(r1, r2, r3);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.startAnimation();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.stopAnimation();
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == 0) {
            this.startAnimation();
        } else {
            this.stopAnimation();
        }

    }

    private void startAnimation() {
        if (this.mLoadingDrawable != null) {
            this.mLoadingDrawable.start();
        }

    }

    private void stopAnimation() {
        if (this.mLoadingDrawable != null) {
            this.mLoadingDrawable.stop();
        }

    }
}

