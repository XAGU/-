package com.xiaolian.amigo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;

/**
 * @author wcm
 * @data 07/10/2018
 * 网络请求错误显示界面
 */
public class ErrorLayout extends RelativeLayout {

    private ReferListener referListener ;
    TextView refer;
    RelativeLayout rlError;
    private Context context;



    public ErrorLayout(Context context) {
        this(context , null);
    }

    public ErrorLayout(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public ErrorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_error ,this ,true);
//        inflate(context ,R.layout.layout_error ,null);
        rlError = findViewById(R.id.rl_error);
        refer = findViewById(R.id.refer);
        refer.setOnClickListener(v -> {
            if (referListener != null) referListener.referData();
        });
    }


    public void setReferListener(ReferListener referListener) {
        this.referListener = referListener;
    }

    public interface ReferListener{
        void referData();
    }
}
