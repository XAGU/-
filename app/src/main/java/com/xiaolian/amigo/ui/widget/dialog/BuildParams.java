package com.xiaolian.amigo.ui.widget.dialog;

/**
 * dialog传递参数
 * <p>
 * Created by zcd on 9/25/17.
 */

public class BuildParams {
    public int mIconId = 0;

    public int themeResId;

    public CharSequence title;

    public CharSequence message;

    public CharSequence positiveText;

    public CharSequence neutralText;

    public CharSequence negativeText;

    public CharSequence[] items;

    public boolean[] checkedItems;

    public boolean isMultiChoice;

    public boolean isSingleChoice;

    public int checkedItem;
}
