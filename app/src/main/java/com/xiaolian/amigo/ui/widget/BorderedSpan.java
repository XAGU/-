package com.xiaolian.amigo.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * @author zcd
 * @date 18/5/15
 */
public class BorderedSpan extends ReplacementSpan {
    final Paint mPaintBorder, mPaintBackground;
    int mWidth;
    Resources r;
    int mTextColor;
    int top;
    int bottom;

    public BorderedSpan(Context context, int top, int bottom) {
        this.top = top;
        this.bottom = bottom;
        mPaintBorder = new Paint();
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setAntiAlias(true);

        mPaintBackground = new Paint();
        mPaintBackground.setStyle(Paint.Style.FILL);
        mPaintBackground.setAntiAlias(true);

        r = context.getResources();

        mPaintBorder.setColor(Color.parseColor("#999999"));
        mPaintBackground.setColor(Color.WHITE);
        mTextColor = Color.parseColor("#999999");
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        mWidth = (int) paint.measureText(text, start, end);
        return 75;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
//        canvas.drawRect(x, top, x + mWidth, bottom, mPaintBackground);
        Rect bound = new Rect();
        canvas.drawRect(x, top + this.top, x + 75, bottom-this.bottom, mPaintBorder);
        paint.setColor(mTextColor); //use the default text paint to preserve font size/style
        int padding = (int) ((75 - paint.getTextSize() * 2)/2);
        canvas.drawText(text, start, end, x + padding, y, paint);
    }
}
