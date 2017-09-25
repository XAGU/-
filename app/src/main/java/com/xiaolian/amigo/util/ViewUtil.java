package com.xiaolian.amigo.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

/**
 * View相关util
 * <p>
 * Created by zcd on 9/25/17.
 */

public final class ViewUtil {
    private ViewUtil() {
    }

    public static void setEditHintAndSize(String hint, int dp, TextView textView) {
        SpannableString span = new SpannableString(hint);
        span.setSpan(new AbsoluteSizeSpan(dp, true), 0, hint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setHint(span);
    }
}
