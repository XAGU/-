package com.xiaolian.amigo.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zcd
 * @date 18/6/29
 */
public class BathroomOperationStatusView extends LinearLayout {

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.iv_status)
    ImageView ivStatus;

    @BindView(R.id.tv_right)
    TextView tvRight;

    public BathroomOperationStatusView(Context context) {
        super(context);
        init(context);
    }

    public BathroomOperationStatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BathroomOperationStatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_bathroom_operation_status,
                this, true);
        ButterKnife.bind(this, view);
    }
}
