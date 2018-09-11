package com.xiaolian.amigo.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.ScreenUtils;

import lombok.NonNull;

/**
 * @author zcd
 * @date 18/5/13
 */
public class LostAndFoundPopupDialog extends Dialog {
    private Context context;
    private OnLostAndFoundClickListener listener;
    private TextView tvNoticeCount;
    private RelativeLayout rlMyNotice;
    private View vLine1;
    private View vLine2;
    private TextView tvMyFavorite;

    public LostAndFoundPopupDialog(@NonNull Context context) {
        super(context, R.style.LostAndFoundPopupDialogStyle);
        this.context = context;
        // 设置pop透明效果

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
        TextView tvMyNotice = findViewById(R.id.tv_my_notice);
        tvMyFavorite = findViewById(R.id.tv_my_favorite);
        TextView tvMyPublish = findViewById(R.id.tv_my_publish);
        tvMyNotice.setOnClickListener(v -> {
            listener.onMyNoticeClick();
            dismiss();
        });
        tvMyFavorite.setOnClickListener(v -> {
            listener.onMyFavoriteClick();
            dismiss();
        });
        tvMyPublish.setOnClickListener(v -> {
            listener.onMyPublishClick();
            dismiss();
        });
        tvNoticeCount = findViewById(R.id.tv_notice_count);

        rlMyNotice = findViewById(R.id.rl_my_notice);
        vLine1 = findViewById(R.id.v_line1);
        vLine2 = findViewById(R.id.v_line2);
    }

    public void setLostAndFoundListener(OnLostAndFoundClickListener listener) {
        this.listener = listener;
    }

    public void setNoticeVisible(boolean visible) {
        rlMyNotice.setVisibility(visible ? View.VISIBLE : View.GONE);
        tvMyFavorite.setVisibility(visible ? View.VISIBLE : View.GONE);
        vLine1.setVisibility(visible ? View.VISIBLE : View.GONE);
        vLine2.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setNoticeCount(int count) {
        if (count > 0) {
            tvNoticeCount.setVisibility(View.VISIBLE);
            tvNoticeCount.setText(String.valueOf(count));
        } else {
            tvNoticeCount.setVisibility(View.GONE);
        }
    }

    public interface OnLostAndFoundClickListener {
        void onMyNoticeClick();

        void onMyFavoriteClick();

        void onMyPublishClick();
    }

    public void show(int x, int y) {

        super.show();
    }
}
