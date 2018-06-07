package com.xiaolian.amigo.util;

import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

/**
 * View相关util
 *
 * @author zcd
 * @date 17/9/25
 */

public final class ViewUtil {
    private ViewUtil() {
    }

    public static void setEditHintAndSize(String hint, int dp, TextView textView) {
        SpannableString span = new SpannableString(hint);
        span.setSpan(new AbsoluteSizeSpan(dp, true), 0, hint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setHint(span);
    }

    public static void setEditPasswordInputFilter(TextView textView) {
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };

        textView.setFilters(new InputFilter[] {filter});
    }
}
