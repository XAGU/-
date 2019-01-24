package com.xiaolian.amigo.ui.widget.popWindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;

import java.util.List;

public class BillFilterTypePopupWindow extends PopupWindow {
    private static final String TAG = BillFilterStatusPopupWindow.class.getSimpleName();
    private Context context ;

    private TextView filterAllTextView;

    private TextView filterRechargeTextView;

    private TextView filterWithdrawTextView;

    private TextView filterBillTotalTextView;

    private TextView filterBillItem1TextView;
    private TextView filterBillItem2TextView;
    private TextView filterBillItem3TextView;
    private TextView filterBillItem4TextView;
    private TextView filterBillItem5TextView;


    private BillFilterTypePopupWindow.PopFilterClickListener popFilterClickListener;

    private int popupWidth ;
    private int popupHeight ;

    public BillFilterTypePopupWindow(Context context) {
        super(context);
        this.context = context ;
        init();
    }

    private void init(){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = layoutInflater.inflate(R.layout.pop_bill_filter_type, null , false);
        filterAllTextView = contentView.findViewById(R.id.filter_type_all);
        filterRechargeTextView = contentView.findViewById(R.id.filter_type_recharge);
        filterWithdrawTextView = contentView.findViewById(R.id.filter_type_withdraw);
        filterBillTotalTextView = contentView.findViewById(R.id.filter_type_bill);

        filterBillItem1TextView = contentView.findViewById(R.id.filter_type_bill_item1);
        filterBillItem2TextView = contentView.findViewById(R.id.filter_type_bill_item2);
        filterBillItem3TextView = contentView.findViewById(R.id.filter_type_bill_item3);
        filterBillItem4TextView = contentView.findViewById(R.id.filter_type_bill_item4);
        filterBillItem5TextView = contentView.findViewById(R.id.filter_type_bill_item5);

        filterAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFilterClickListener.click(0);
                dismiss();
            }
        });

        filterRechargeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFilterClickListener.click(1);
                dismiss();
            }
        });

        filterWithdrawTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFilterClickListener.click(2);
                dismiss();
            }
        });

        filterBillTotalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFilterClickListener.click(9);
                dismiss();
            }
        });

        filterBillItem1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFilterClickListener.click(((Long)v.getTag()).intValue());
                dismiss();
            }
        });

        filterBillItem2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFilterClickListener.click(((Long)v.getTag()).intValue());
                dismiss();
            }
        });

        filterBillItem3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFilterClickListener.click(((Long)v.getTag()).intValue());
                dismiss();
            }
        });

        filterBillItem4TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFilterClickListener.click(((Long)v.getTag()).intValue());
                dismiss();
            }
        });

        filterBillItem5TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFilterClickListener.click(((Long)v.getTag()).intValue());
                dismiss();
            }
        });

        setContentView(contentView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        setAnimationStyle(R.style.DialogDownAnimation);
        setBackgroundDrawable(new ColorDrawable(0xf0ff0000));
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

    public void setBillItems(List<BriefSchoolBusiness> businessesList) {
        for (BriefSchoolBusiness briefSchoolBusiness: businessesList) {
            TextView itemView = new TextView(context);
            if (filterBillItem1TextView.getVisibility() != View.VISIBLE) /*加载第一个*/{
                itemView = filterBillItem1TextView;
            } else if (filterBillItem2TextView.getVisibility() != View.VISIBLE) /*加载第二个*/{
                itemView = filterBillItem2TextView;
            } else if (filterBillItem3TextView.getVisibility() != View.VISIBLE) /*加载第三个*/{
                itemView = filterBillItem3TextView;
            } else if (filterBillItem4TextView.getVisibility() != View.VISIBLE) /*加载第四个*/{
                itemView = filterBillItem4TextView;
            } else if (filterBillItem5TextView.getVisibility() != View.VISIBLE) /*加载第五个*/{
                itemView = filterBillItem5TextView;
            }
            itemView.setVisibility(View.VISIBLE);
            itemView.setTag(briefSchoolBusiness.getBusinessId()+2);
            if ((Long)itemView.getTag() == 3) /*热水澡*/{
                itemView.setText("热水澡消费");
            } else if ((Long)itemView.getTag() == 4) /*饮水机*/{
                itemView.setText("饮水机消费");
            } else if ((Long)itemView.getTag() == 5) /*洗衣机*/{
                itemView.setText("洗衣机消费");
            } else if ((Long)itemView.getTag() == 6) /*吹风机*/{
                itemView.setText("吹风机消费");
            } else if ((Long)itemView.getTag() == 7) /*烘干机*/{
                itemView.setTag(briefSchoolBusiness.getBusinessId()+3); //手动把烘干机设为8，和服务器同步
                itemView.setText("烘干机消费");
            }
        }
    }

    public void setPopFilterClickListener(BillFilterTypePopupWindow.PopFilterClickListener popFilterClickListener) {
        this.popFilterClickListener = popFilterClickListener;
    }

    public interface PopFilterClickListener {
        void click(int type);
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
