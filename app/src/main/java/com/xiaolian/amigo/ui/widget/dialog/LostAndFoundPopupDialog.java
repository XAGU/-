package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.ScreenUtils;

import lombok.NonNull;

/**
 * @author zcd
 * @date 18/5/13
 */
public class LostAndFoundPopupDialog extends Dialog {
    private Context context;
    public LostAndFoundPopupDialog(@NonNull Context context) {
        super(context, R.style.LostAndFoundPopupDialogStyle);
        this.context = context;
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.END | Gravity.TOP);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.y = ScreenUtils.dpToPxInt(context, 45);
        lp.x = 24;
        window.setAttributes(lp);
        this.context = context;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_lost_and_found);
    }

    public void show(int x, int y) {

        super.show();
    }
}
