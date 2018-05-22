package com.xiaolian.amigo.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.ViewUtils;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

import com.xiaolian.amigo.util.DimentionUtils;

public class CustomVerticalCenterSpan extends ReplacementSpan {
    private int fontSizeSp;    //字体大小sp
    private Context context;

    public CustomVerticalCenterSpan(int fontSizeSp, Context context){
        this.fontSizeSp = fontSizeSp;
        this.context = context;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        text = text.subSequence(start, end);
        Paint p = getCustomTextPaint(paint);
        return (int) p.measureText(text.toString());
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        text = text.subSequence(start, end);
        Paint p = getCustomTextPaint(paint);
        Paint.FontMetricsInt fm = p.getFontMetricsInt();
        canvas.drawText(text.toString(), x, y - ((y + fm.descent + y + fm.ascent) / 2 - (bottom + top) / 2), p);    //此处重新计算y坐标，使字体居中
    }

    private TextPaint getCustomTextPaint(Paint srcPaint) {
        TextPaint paint = new TextPaint(srcPaint);
        paint.setTextSize(DimentionUtils.convertSpToPixels(fontSizeSp, context));   //设定字体大小, sp转换为px
        return paint;
    }
}
