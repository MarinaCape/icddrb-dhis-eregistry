package org.icddrb.dhis.android.sdk.ui.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import org.icddrb.dhis.android.sdk.R;

public class FloatingActionButton extends ImageButton {
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator();
    private static final int SHADOW_LAYER_INSET_INDEX = 1;
    public static final int TYPE_MINI = 1;
    public static final int TYPE_NORMAL = 0;
    private int mColorNormal;
    private int mColorPressed;
    private boolean mHidden;
    private boolean mShadow;
    private int mShadowSize;
    private int mType;

    public FloatingActionButton(Context context) {
        super(context);
        init(null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        setClickable(true);
        this.mType = 0;
        this.mColorNormal = getColor(R.color.navy_blue);
        this.mColorPressed = getColor(R.color.dark_navy_blue);
        this.mShadow = true;
        this.mShadowSize = getDimension(R.dimen.floating_action_button_shadow_size);
        if (attributeSet != null) {
            TypedArray attrs = getContext().obtainStyledAttributes(attributeSet, R.styleable.FloatingActionButton);
            if (attrs != null) {
                try {
                    this.mColorNormal = attrs.getColor(R.styleable.FloatingActionButton_colorNormal, this.mColorNormal);
                    this.mColorPressed = attrs.getColor(R.styleable.FloatingActionButton_colorPressed, this.mColorPressed);
                    this.mShadow = attrs.getBoolean(R.styleable.FloatingActionButton_shadow, this.mShadow);
                    this.mType = attrs.getInt(R.styleable.FloatingActionButton_type, 0);
                } finally {
                    attrs.recycle();
                }
            }
        }
        updateBackground();
    }

    private Drawable createDrawable(int color) {
        Drawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(color);
        if (!this.mShadow) {
            return shapeDrawable;
        }
        Drawable shadowDrawable;
        if (this.mType == 0) {
            shadowDrawable = getResources().getDrawable(R.drawable.shadow);
        } else {
            shadowDrawable = getResources().getDrawable(R.drawable.shadow_mini);
        }
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shadowDrawable, shapeDrawable});
        layerDrawable.setLayerInset(1, this.mShadowSize, this.mShadowSize, this.mShadowSize, this.mShadowSize);
        return layerDrawable;
    }

    private void updateBackground() {
        StateListDrawable stateList = new StateListDrawable();
        int[] unpressedState = new int[0];
        stateList.addState(new int[]{16842919}, createDrawable(this.mColorPressed));
        stateList.addState(unpressedState, createDrawable(this.mColorNormal));
        if (VERSION.SDK_INT >= 16) {
            setBackground(stateList);
        } else {
            setBackgroundDrawable(stateList);
        }
    }

    public void setColorNormalResId(int colorResId) {
        setColorNormal(getColor(colorResId));
    }

    public int getColorNormal() {
        return this.mColorNormal;
    }

    public void setColorNormal(int color) {
        if (color != this.mColorNormal) {
            this.mColorNormal = color;
            updateBackground();
        }
    }

    public void setColorPressedResId(int colorResId) {
        setColorPressed(getColor(colorResId));
    }

    public int getColorPressed() {
        return this.mColorPressed;
    }

    public void setColorPressed(int color) {
        if (color != this.mColorPressed) {
            this.mColorPressed = color;
            updateBackground();
        }
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        if (type != this.mType) {
            this.mType = type;
            updateBackground();
        }
    }

    public void setShadow(boolean shadow) {
        if (shadow != this.mShadow) {
            this.mShadow = shadow;
            updateBackground();
        }
    }

    public boolean hasShadow() {
        return this.mShadow;
    }

    private int getColor(int id) {
        return getResources().getColor(id);
    }

    private int getDimension(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    public void hide() {
        if (!this.mHidden) {
            setVisibility(8);
            this.mHidden = true;
        }
    }

    public void show() {
        if (this.mHidden) {
            setVisibility(0);
            this.mHidden = false;
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", new float[]{0.0f, 1.0f});
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", new float[]{0.0f, 1.0f});
            AnimatorSet animSetXY = new AnimatorSet();
            animSetXY.playTogether(new Animator[]{scaleX, scaleY});
            animSetXY.setInterpolator(OVERSHOOT_INTERPOLATOR);
            animSetXY.setDuration(200);
            animSetXY.start();
        }
    }
}
