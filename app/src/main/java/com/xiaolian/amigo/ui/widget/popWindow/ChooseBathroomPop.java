package com.xiaolian.amigo.ui.widget.popWindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.dialog.adapter.ChooseRoomAdapter;
import com.xiaolian.amigo.util.ScreenUtils;

public class ChooseBathroomPop  extends PopupWindow{

    private Context context ;

    private RecyclerView recyclerView ;

    private Button button ;

    private ChooseRoomAdapter adapter ;

    private PopButtonClickListener clickListener ;

    private int popupWidth ;
    private int popupHeight ;


    public ChooseBathroomPop(Context context) {
        super(context);
        this.context = context ;
        init();
    }

    public void setPopClickListtener(PopButtonClickListener clickListtener){
        this.clickListener = clickListtener ;
    }

    private void init(){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = layoutInflater.inflate(R.layout.pop_chose_bathroom, null , false);
        recyclerView = contentView.findViewById(R.id.bathroom_recy);
        button = contentView.findViewById(R.id.pre_bathroom) ;
        adapter = new ChooseRoomAdapter(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        setContentView(contentView);
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.DialogAnimation);
        setBackgroundDrawable(new ColorDrawable());
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null){
                    clickListener.click();
                }
            }
        });
        //获取自身的长宽高
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = contentView.getMeasuredHeight();
        popupWidth = contentView.getMeasuredWidth();
    }


    /**
     * 设置显示在v上方(以v的左边距为开始位置)
     * @param v
     */
    public void showUp(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        showAtLocation(v, Gravity.BOTTOM, 0 , ScreenUtils.dpToPxInt(context ,20));
    }


    /**
     * 设置data
     */
    public void setAdapter(){

    }

    /**
     * pop 中 点击事件
     */
    interface PopButtonClickListener{
        void click();
    }

}
