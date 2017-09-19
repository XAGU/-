package com.xiaolian.amigo.tmp.component.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by caidong on 2017/9/13.
 */

public class SearchDialog extends Dialog implements TextWatcher {

    @BindView(R.id.et_search_content)
    EditText et_search_content;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.rl_result)
    RelativeLayout rl_result;

    Context context;

    public SearchDialog(AppCompatActivity context) {
        super(context, R.style.Search_Dialog);
        this.context = context;
//        setOwnerActivity((Activity)context);

        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.TOP);  //此处可以设置dialog显示的位置
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        setContentView(R.layout.dialog_lost_and_found_search);
        ButterKnife.bind(this);

        et_search_content.addTextChangedListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String input = et_search_content.getText().toString().trim();
        iv_clear.setVisibility(input.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @OnEditorAction(R.id.et_search_content)
    boolean search(TextView v, int actionId, KeyEvent event) {
        // 判断如果用户输入的是搜索键
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            this.dismiss();
            rl_result.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }
        return false;
    }

    // 点击清除图标
    @OnClick(R.id.iv_clear)
    void clear() {
        et_search_content.setText("");
        iv_clear.setVisibility(View.GONE);
        this.dismiss();
    }

    @OnClick(R.id.tv_cancel)
    void cancelSearch() {
        this.dismiss();
    }
}
