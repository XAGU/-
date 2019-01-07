package com.xiaolian.amigo.ui.widget.marqueeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug;
import android.widget.TextView;

public class WaterTextView extends TextView {

    public WaterTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    public WaterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    public WaterTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    @Override
    @ViewDebug.ExportedProperty(category = "focus")
    public boolean isFocused() {
        // TODO Auto-generated method stub
        return true;
    }
}
