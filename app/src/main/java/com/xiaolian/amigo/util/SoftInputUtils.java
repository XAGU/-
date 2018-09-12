package com.xiaolian.amigo.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SoftInputUtils {

        /**
         * EditText获取焦点并显示软键盘
         */
        public static void showSoftInputFromWindow(Activity activity, EditText editText) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            InputMethodManager inputManager =
                    (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
        }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void hideSoftInputFromWindow(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


}
