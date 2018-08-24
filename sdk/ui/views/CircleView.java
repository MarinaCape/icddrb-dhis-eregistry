package org.icddrb.dhis.client.sdk.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import org.icddrb.dhis.client.sdk.ui.C0935R;

public class CircleView extends View {
    private int circleGap;
    private int circleRadius;
    private int fillColor;
    private int strokeColor;
    private int strokeWidth;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, C0935R.style.AppTheme_Base);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ars = context.obtainStyledAttributes(attrs, C0935R.styleable.CircleView, defStyleAttr, 0);
        this.circleRadius = ars.getDimensionPixelSize(C0935R.styleable.CircleView_circle_radius, -1);
        this.strokeWidth = ars.getDimensionPixelSize(C0935R.styleable.CircleView_stroke_width, -1);
        this.circleGap = ars.getDimensionPixelSize(C0935R.styleable.CircleView_circle_gap, -1);
        this.fillColor = ars.getColor(C0935R.styleable.CircleView_fill_color, 0);
        this.strokeColor = ars.getColor(C0935R.styleable.CircleView_stroke_color, 0);
        setMinimumHeight((this.circleRadius * 2) + (this.strokeWidth * 2));
        setMinimumWidth((this.circleRadius * 2) + (this.strokeWidth * 2));
        setSaveEnabled(true);
        ars.recycle();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int ox = getWidth() / 2;
        int oy = getHeight() / 2;
        if (this.strokeWidth > 0 && this.strokeColor != 0) {
            canvas.drawCircle((float) ox, (float) oy, (float) this.circleRadius, getStroke());
        }
        if (this.circleRadius > 0 && this.fillColor != 0) {
            canvas.drawCircle((float) ox, (float) oy, (float) (this.circleRadius - this.circleGap), getFill());
        }
    }

    private Paint getStroke() {
        float adjustedStrokeWidth = this.strokeWidth + -2 > 0 ? (float) (this.strokeWidth - 2) : (float) this.strokeWidth;
        Paint paint = new Paint(1);
        paint.setStrokeWidth(adjustedStrokeWidth);
        paint.setColor(this.strokeColor);
        paint.setStyle(Style.STROKE);
        return paint;
    }

    private Paint getFill() {
        Paint paint = new Paint(1);
        paint.setColor(this.fillColor);
        paint.setStyle(Style.FILL);
        return paint;
    }

    public int getCircleRadius() {
        return this.circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeColor(@ColorInt int strokeColor) {
        this.strokeColor = strokeColor;
        invalidate();
    }

    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(@ColorInt int fillColor) {
        this.fillColor = fillColor;
    }

    public int getCircleGap() {
        return this.circleGap;
    }

    public void setCircleGap(int circleGap) {
        this.circleGap = circleGap;
    }
}
