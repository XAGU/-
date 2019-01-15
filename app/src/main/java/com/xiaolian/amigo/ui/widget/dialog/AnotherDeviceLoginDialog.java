package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.ScreenUtils;

public class AnotherDeviceLoginDialog extends Dialog {

    private Context context ;

    private TextView tip ;

    private View line ;
        
    private TextView confirm ;
    public AnotherDeviceLoginDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(ScreenUtils.dpToPxInt(context ,24), 0, ScreenUtils.dpToPxInt(context , 24), 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        this.context = context;
        initView();
    }

    private void initView(){
        setContentView(R.layout.dialog_another_device_login);
        tip = findViewById(R.id.tip);
        line = findViewById(R.id.line);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(v -> dismiss());
    }


}
