package com.xiaolian.amigo.ui.widget;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class CustomCharacterSpan extends MetricAffectingSpan {
    private double ratio = 0.5;

    public CustomCharacterSpan() {
    }

    public CustomCharacterSpan(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        paint.baselineShift += (int) (paint.ascent() * ratio);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        paint.baselineShift += (int) (paint.ascent() * ratio);
    }
}