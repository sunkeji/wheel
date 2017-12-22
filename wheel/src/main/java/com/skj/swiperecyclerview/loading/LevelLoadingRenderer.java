package com.skj.swiperecyclerview.loading;

/**
 * Created by 孙科技 on 2017/12/7.
 */


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.skj.util.DisplayUtil;

public class LevelLoadingRenderer extends LoadingRenderer {
    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator MATERIAL_INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final Interpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final int NUM_POINTS = 5;
    private static final int DEGREE_360 = 360;
    private static final float MAX_SWIPE_DEGREES = 288.0F;
    private static final float FULL_GROUP_ROTATION = 1080.0F;
    private static final float[] LEVEL_SWEEP_ANGLE_OFFSETS = new float[]{1.0F, 0.875F, 0.625F};
    private static final float START_TRIM_DURATION_OFFSET = 0.5F;
    private static final float END_TRIM_DURATION_OFFSET = 1.0F;
    private static final float DEFAULT_CENTER_RADIUS = 12.5F;
    private static final float DEFAULT_STROKE_WIDTH = 2.5F;
    private static final int[] DEFAULT_LEVEL_COLORS = new int[]{Color.parseColor("#55ffffff"), Color.parseColor("#b1ffffff"), Color.parseColor("#ffffffff")};
    private final Paint mPaint = new Paint();
    private final RectF mTempBounds = new RectF();
    private final AnimatorListener mAnimatorListener = new AnimatorListenerAdapter() {
        public void onAnimationRepeat(Animator animator) {
            super.onAnimationRepeat(animator);
            LevelLoadingRenderer.this.storeOriginals();
            LevelLoadingRenderer.this.mStartDegrees = LevelLoadingRenderer.this.mEndDegrees;
            LevelLoadingRenderer.this.mRotationCount = (LevelLoadingRenderer.this.mRotationCount + 1.0F) % 5.0F;
        }

        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            LevelLoadingRenderer.this.mRotationCount = 0.0F;
        }
    };
    private int[] mLevelColors;
    private float[] mLevelSwipeDegrees;
    private float mStrokeInset;
    private float mRotationCount;
    private float mGroupRotation;
    private float mEndDegrees;
    private float mStartDegrees;
    private float mOriginEndDegrees;
    private float mOriginStartDegrees;
    private float mStrokeWidth;
    private float mCenterRadius;

    public LevelLoadingRenderer(Context context) {
        super(context);
        this.init(context);
        this.setupPaint();
        this.addRenderListener(this.mAnimatorListener);
    }

    private void init(Context context) {
        this.mStrokeWidth = DisplayUtil.dip2px(context, 2.5F);
        this.mCenterRadius = DisplayUtil.dip2px(context, 12.5F);
        this.mLevelSwipeDegrees = new float[3];
        this.mLevelColors = DEFAULT_LEVEL_COLORS;
    }

    private void setupPaint() {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeWidth(this.mStrokeWidth);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.initStrokeInset((float) ((int) this.mWidth), (float) ((int) this.mHeight));
    }

    public void setCircleColors(int r1, int r2, int r3) {
        this.mLevelColors = new int[]{r1, r2, r3};
    }

    protected void draw(Canvas canvas) {
        int saveCount = canvas.save();
        this.mTempBounds.set(this.mBounds);
        this.mTempBounds.inset(this.mStrokeInset, this.mStrokeInset);
        canvas.rotate(this.mGroupRotation, this.mTempBounds.centerX(), this.mTempBounds.centerY());

        for (int i = 0; i < 3; ++i) {
            if (this.mLevelSwipeDegrees[i] != 0.0F) {
                this.mPaint.setColor(this.mLevelColors[i]);
                canvas.drawArc(this.mTempBounds, this.mEndDegrees, this.mLevelSwipeDegrees[i], false, this.mPaint);
            }
        }

        canvas.restoreToCount(saveCount);
    }

    protected void computeRender(float renderProgress) {
        float endTrimProgress;
        float mSwipeDegrees;
        float levelSwipeDegreesProgress;
        if (renderProgress <= 0.5F) {
            endTrimProgress = renderProgress / 0.5F;
            this.mStartDegrees = this.mOriginStartDegrees + 288.0F * MATERIAL_INTERPOLATOR.getInterpolation(endTrimProgress);
            mSwipeDegrees = this.mEndDegrees - this.mStartDegrees;
            levelSwipeDegreesProgress = Math.abs(mSwipeDegrees) / 288.0F;
            float level1Increment = DECELERATE_INTERPOLATOR.getInterpolation(levelSwipeDegreesProgress) - LINEAR_INTERPOLATOR.getInterpolation(levelSwipeDegreesProgress);
            float level3Increment = ACCELERATE_INTERPOLATOR.getInterpolation(levelSwipeDegreesProgress) - LINEAR_INTERPOLATOR.getInterpolation(levelSwipeDegreesProgress);
            this.mLevelSwipeDegrees[0] = -mSwipeDegrees * LEVEL_SWEEP_ANGLE_OFFSETS[0] * (1.0F + level1Increment);
            this.mLevelSwipeDegrees[1] = -mSwipeDegrees * LEVEL_SWEEP_ANGLE_OFFSETS[1] * 1.0F;
            this.mLevelSwipeDegrees[2] = -mSwipeDegrees * LEVEL_SWEEP_ANGLE_OFFSETS[2] * (1.0F + level3Increment);
        }

        if (renderProgress > 0.5F) {
            endTrimProgress = (renderProgress - 0.5F) / 0.5F;
            this.mEndDegrees = this.mOriginEndDegrees + 288.0F * MATERIAL_INTERPOLATOR.getInterpolation(endTrimProgress);
            mSwipeDegrees = this.mEndDegrees - this.mStartDegrees;
            levelSwipeDegreesProgress = Math.abs(mSwipeDegrees) / 288.0F;
            if (levelSwipeDegreesProgress > LEVEL_SWEEP_ANGLE_OFFSETS[1]) {
                this.mLevelSwipeDegrees[0] = -mSwipeDegrees;
                this.mLevelSwipeDegrees[1] = 288.0F * LEVEL_SWEEP_ANGLE_OFFSETS[1];
                this.mLevelSwipeDegrees[2] = 288.0F * LEVEL_SWEEP_ANGLE_OFFSETS[2];
            } else if (levelSwipeDegreesProgress > LEVEL_SWEEP_ANGLE_OFFSETS[2]) {
                this.mLevelSwipeDegrees[0] = 0.0F;
                this.mLevelSwipeDegrees[1] = -mSwipeDegrees;
                this.mLevelSwipeDegrees[2] = 288.0F * LEVEL_SWEEP_ANGLE_OFFSETS[2];
            } else {
                this.mLevelSwipeDegrees[0] = 0.0F;
                this.mLevelSwipeDegrees[1] = 0.0F;
                this.mLevelSwipeDegrees[2] = -mSwipeDegrees;
            }
        }

        this.mGroupRotation = 216.0F * renderProgress + 1080.0F * (this.mRotationCount / 5.0F);
    }

    protected void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    protected void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    protected void reset() {
        this.resetOriginals();
    }

    private void initStrokeInset(float width, float height) {
        float minSize = Math.min(width, height);
        float strokeInset = minSize / 2.0F - this.mCenterRadius;
        float minStrokeInset = (float) Math.ceil((double) (this.mStrokeWidth / 2.0F));
        this.mStrokeInset = strokeInset < minStrokeInset ? minStrokeInset : strokeInset;
    }

    private void storeOriginals() {
        this.mOriginEndDegrees = this.mEndDegrees;
        this.mOriginStartDegrees = this.mEndDegrees;
    }

    private void resetOriginals() {
        this.mOriginEndDegrees = 0.0F;
        this.mOriginStartDegrees = 0.0F;
        this.mEndDegrees = 0.0F;
        this.mStartDegrees = 0.0F;
        this.mLevelSwipeDegrees[0] = 0.0F;
        this.mLevelSwipeDegrees[1] = 0.0F;
        this.mLevelSwipeDegrees[2] = 0.0F;
    }

    private void apply(LevelLoadingRenderer.Builder builder) {
        this.mWidth = builder.mWidth > 0 ? (float) builder.mWidth : this.mWidth;
        this.mHeight = builder.mHeight > 0 ? (float) builder.mHeight : this.mHeight;
        this.mStrokeWidth = builder.mStrokeWidth > 0 ? (float) builder.mStrokeWidth : this.mStrokeWidth;
        this.mCenterRadius = builder.mCenterRadius > 0 ? (float) builder.mCenterRadius : this.mCenterRadius;
        this.mDuration = builder.mDuration > 0 ? (long) builder.mDuration : this.mDuration;
        this.mLevelColors = builder.mLevelColors != null ? builder.mLevelColors : this.mLevelColors;
        this.setupPaint();
        this.initStrokeInset(this.mWidth, this.mHeight);
    }

    public static class Builder {
        private Context mContext;
        private int mWidth;
        private int mHeight;
        private int mStrokeWidth;
        private int mCenterRadius;
        private int mDuration;
        private int[] mLevelColors;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public LevelLoadingRenderer.Builder setWidth(int width) {
            this.mWidth = width;
            return this;
        }

        public LevelLoadingRenderer.Builder setHeight(int height) {
            this.mHeight = height;
            return this;
        }

        public LevelLoadingRenderer.Builder setStrokeWidth(int strokeWidth) {
            this.mStrokeWidth = strokeWidth;
            return this;
        }

        public LevelLoadingRenderer.Builder setCenterRadius(int centerRadius) {
            this.mCenterRadius = centerRadius;
            return this;
        }

        public LevelLoadingRenderer.Builder setDuration(int duration) {
            this.mDuration = duration;
            return this;
        }

        public LevelLoadingRenderer.Builder setLevelColors(int[] colors) {
            this.mLevelColors = colors;
            return this;
        }

        public LevelLoadingRenderer.Builder setLevelColor(int color) {
            return this.setLevelColors(new int[]{this.oneThirdAlphaColor(color), this.twoThirdAlphaColor(color), color});
        }

        public LevelLoadingRenderer build() {
            LevelLoadingRenderer loadingRenderer = new LevelLoadingRenderer(this.mContext);
            loadingRenderer.apply(this);
            return loadingRenderer;
        }

        private int oneThirdAlphaColor(int colorValue) {
            int startA = colorValue >> 24 & 255;
            int startR = colorValue >> 16 & 255;
            int startG = colorValue >> 8 & 255;
            int startB = colorValue & 255;
            return startA / 3 << 24 | startR << 16 | startG << 8 | startB;
        }

        private int twoThirdAlphaColor(int colorValue) {
            int startA = colorValue >> 24 & 255;
            int startR = colorValue >> 16 & 255;
            int startG = colorValue >> 8 & 255;
            int startB = colorValue & 255;
            return startA * 2 / 3 << 24 | startR << 16 | startG << 8 | startB;
        }
    }
}

