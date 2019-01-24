package com.xiaolian.amigo.ui.widget.popWindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bathroom.BuildingTrafficDTO;
import com.xiaolian.amigo.ui.widget.dialog.adapter.ChooseRoomAdapter;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;

import butterknife.OnClick;

public class BillFilterStatusPopupWindow extends PopupWindow {
    private static final String TAG = BillFilterStatusPopupWindow.class.getSimpleName();
    private Context context ;

    private TextView filterAllTextView;

    private TextView filterOngoingTextView;

    private TextView filterEndTextView;

    private PopFilterClickListener popFilterClickListener;

    private int popupWidth ;
    private int popupHeight ;

    public BillFilterStatusPopupWindow(Context context) {
        super(context);
        this.context = context ;
        init();
    }

    private void init(){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = layoutInflater.inflate(R.layout.pop_bill_filter_status, null , false);
        filterAllTextView = contentView.findViewById(R.id.filter_status_all);
        filterOngoingTextView = contentView.findViewById(R.id.filter_status_ongoing);
        filterEndTextView = contentView.findViewById(R.id.filter_status_end);

        filterAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFilterClickListener.click(0);
                dismiss();
            }
        });

        filterOngoingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFilterClickListener.click(1);
                dismiss();
            }
        });

        filterEndTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFilterClickListener.click(2);
                dismiss();
            }
        });

        setContentView(contentView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        setAnimationStyle(R.style.DialogDownAnimation);
        setBackgroundDrawable(new ColorDrawable(0xffff0000));
        setFocusable(true);
        setOutsideTouchable(true);
        setTouchable(true);
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE){
                    return true ;
                }
                return  false ;
            }
        });
        //获取自身的长宽高
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = contentView.getMeasuredHeight();
        popupWidth = contentView.getMeasuredWidth();
    }

    public void setPopFilterClickListener(PopFilterClickListener popFilterClickListener) {
        this.popFilterClickListener = popFilterClickListener;
    }

    public interface PopFilterClickListener {
        void click(int status);
    }


    /**
     * 设置显示在v上方(以v的左边距为开始位置)
     * @param v
     */
    public void showUp(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        showAsDropDown(v);
//        showAtLocation(v ,Gravity.TOP ,0 ,location[1] + v.getHeight() );
        //在控件上方显示
//        showAtLocation(v, Gravity.TOP, 0 , ScreenUtils.dpToPxInt(context ,20 ) );
//        showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight-70);
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

}
