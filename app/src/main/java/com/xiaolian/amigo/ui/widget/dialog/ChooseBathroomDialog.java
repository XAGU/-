package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.xiaolian.amigo.R;

public class ChooseBathroomDialog  extends PopupWindow{
    private Context context ;

    public ChooseBathroomDialog(@NonNull Context context) {
        super(context);
        init();
    }

//    public ChooseBathroomDialog(@NonNull Context context, int themeResId) {
//        super(context, themeResId);
//        init();
//    }


    private void init(){
//        this.context = context ;
//        Window window = this.getWindow();
//        window.requestFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.dialog_changehost);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT ;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT ;
//
//        lp.gravity = Gravity.BOTTOM ;
//        window.setAttributes(lp);
//
    }





}
