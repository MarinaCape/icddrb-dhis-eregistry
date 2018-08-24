package org.icddrb.dhis.client.sdk.ui.views;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class DividerDecoration extends ItemDecoration {
    private final Drawable mDivider;

    public DividerDecoration(Drawable drawable) {
        this.mDivider = drawable;
    }

    public void onDrawOver(Canvas canvas, RecyclerView parent, State state) {
        drawVertical(canvas, parent);
    }

    public void drawVertical(Canvas canvas, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int top = child.getBottom() + ((LayoutParams) child.getLayoutParams()).bottomMargin;
            this.mDivider.setBounds(left, top, right, top + this.mDivider.getIntrinsicHeight());
            this.mDivider.draw(canvas);
        }
    }
}
