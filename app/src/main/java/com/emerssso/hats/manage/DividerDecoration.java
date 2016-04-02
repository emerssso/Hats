package com.emerssso.hats.manage;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView ItemDecoration that adds a divider to the bottom of each item
 */
public class DividerDecoration extends RecyclerView.ItemDecoration {

    private final Paint paint;

    public DividerDecoration(@ColorInt int dividerColor) {
        paint = new Paint();
        paint.setColor(dividerColor);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        RecyclerView.LayoutManager lm = parent.getLayoutManager();

        for (int i = 0; i < lm.getChildCount(); i++) {
            View child = lm.getChildAt(i);

            c.drawLine(lm.getDecoratedLeft(child), lm.getDecoratedBottom(child),
                    lm.getDecoratedRight(child), lm.getDecoratedBottom(child), paint);
        }
    }
}
