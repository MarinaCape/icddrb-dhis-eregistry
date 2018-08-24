package org.icddrb.dhis.android.eregistry.fragments.programoverview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {
    static final /* synthetic */ boolean $assertionsDisabled = (!FlowLayout.class.desiredAssertionStatus());
    private int line_height;

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        public final int horizontal_spacing;
        public final int vertical_spacing;

        public LayoutParams(int horizontal_spacing, int vertical_spacing) {
            super(0, 0);
            this.horizontal_spacing = horizontal_spacing;
            this.vertical_spacing = vertical_spacing;
        }
    }

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if ($assertionsDisabled || MeasureSpec.getMode(widthMeasureSpec) != 0) {
            int childHeightMeasureSpec;
            int width = (MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()) - getPaddingRight();
            int height = (MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop()) - getPaddingBottom();
            int count = getChildCount();
            int line_height = 0;
            int xpos = getPaddingLeft();
            int ypos = getPaddingTop();
            if (MeasureSpec.getMode(heightMeasureSpec) == Integer.MIN_VALUE) {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, Integer.MIN_VALUE);
            } else {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
            }
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != 8) {
                    LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    child.measure(MeasureSpec.makeMeasureSpec(width, Integer.MIN_VALUE), childHeightMeasureSpec);
                    int childw = child.getMeasuredWidth();
                    line_height = Math.max(line_height, child.getMeasuredHeight() + lp.vertical_spacing);
                    if (xpos + childw > width) {
                        xpos = getPaddingLeft();
                        ypos += line_height;
                    }
                    xpos += lp.horizontal_spacing + childw;
                }
            }
            this.line_height = line_height;
            if (MeasureSpec.getMode(heightMeasureSpec) == 0) {
                height = ypos + line_height;
            } else if (MeasureSpec.getMode(heightMeasureSpec) == Integer.MIN_VALUE && ypos + line_height < height) {
                height = ypos + line_height;
            }
            setMeasuredDimension(width, height);
            return;
        }
        throw new AssertionError();
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(1, 1);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (p instanceof LayoutParams) {
            return true;
        }
        return false;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int width = r - l;
        int xpos = getPaddingLeft();
        int ypos = getPaddingTop();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                int childw = child.getMeasuredWidth();
                int childh = child.getMeasuredHeight();
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (xpos + childw > width) {
                    xpos = getPaddingLeft();
                    ypos += this.line_height;
                }
                child.layout(xpos, ypos, xpos + childw, ypos + childh);
                xpos += lp.horizontal_spacing + childw;
            }
        }
    }
}
