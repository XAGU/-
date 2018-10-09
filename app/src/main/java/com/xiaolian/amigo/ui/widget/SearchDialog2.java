package com.xiaolian.amigo.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.lostandfound.adapter.SocialImgAdapter;
import com.xiaolian.amigo.util.SoftInputUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * 搜索框
 *
 * @author caidong
 * @date 17/9/13
 */

public class SearchDialog2 extends Dialog implements TextWatcher {

    @BindView(R.id.et_search_content)
    EditText etSearchContent;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    //    @BindView(R.id.iv_clear)
//    ImageView ivClear;
    @BindView(R.id.rl_result)
    RelativeLayout rlResult;
    @BindView(R.id.fl_result_contain)
    FrameLayout flResultContain;
    @BindView(R.id.tv_no_result_tip)
    TextView tvNoResultTip;
    @BindView(R.id.search_rl)
    RelativeLayout searchRl;
//    @BindView(R.id.ll_container)
//    RelativeLayout llContainer;

    private OnSearchListener listener;


    Context context;

    public SearchDialog2(AppCompatActivity context) {
        super(context, R.style.Search_Dialog);
        this.context = context;
//        setOwnerActivity((Activity)context);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.TOP);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        setCancelable(true);

        setContentView(R.layout.dialog_lost_and_found_search);
        ButterKnife.bind(this);

        etSearchContent.addTextChangedListener(this);

    }


    public void showSearch() {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.dialog_search);
        searchRl.startAnimation(animation);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
//        String input = etSearchContent.getText().toString().trim();
////        ivClear.setVisibility(input.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @OnEditorAction(R.id.et_search_content)
    boolean search(TextView v, int actionId, KeyEvent event) {
        // 判断如果用户输入的是搜索键
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            this.dismiss();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            if (listener != null) {
                listener.onSearch(etSearchContent.getText().toString());
            }
            return true;
        }
        return false;
    }

    @Override
    public void show() {
        super.show();
        showSearch();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
//        TransitionManager.beginDelayedTransition(llContainer, new ChangeBounds().setDuration(1000));
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) etSearchContent.getLayoutParams();
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        etSearchContent.setLayoutParams(lp);
        tvCancel.setVisibility(View.VISIBLE);

        rlResult.setVisibility(View.GONE);
        if (flResultContain.getChildCount() > 0) {
            flResultContain.removeAllViews();
        }
    }


    @Override
    public void dismiss() {
//        TransitionManager.beginDelayedTransition(llContainer);
        SoftInputUtils.hideSoftInputFromWindow((Activity) context, etSearchContent);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) etSearchContent.getLayoutParams();
        lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        etSearchContent.setLayoutParams(lp);
        tvCancel.setVisibility(View.GONE);
        super.dismiss();
    }


    @OnClick(R.id.tv_cancel)
    void cancelSearch() {
        etSearchContent.setText("");
        this.dismiss();
    }

    public void showNoResult(String selectKey) {
        flResultContain.setVisibility(View.GONE);
        rlResult.setVisibility(View.VISIBLE);
    }

    public void showResult(View view) {
        rlResult.setVisibility(View.GONE);
        if (flResultContain.getChildCount() > 0) {
            flResultContain.removeAllViews();
        }
        flResultContain.setVisibility(View.VISIBLE);
        flResultContain.addView(view);
    }

    public void setSearchListener(OnSearchListener listener) {
        this.listener = listener;
    }


    public interface OnSearchListener {
        void onSearch(String searchStr);
    }
}
