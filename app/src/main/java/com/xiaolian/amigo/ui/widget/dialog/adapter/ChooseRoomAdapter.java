package com.xiaolian.amigo.ui.widget.dialog.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bathroom.BuildingTrafficDTO;
import com.xiaolian.amigo.intf.OnItemClickListener;
import com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomAdapter;
import com.xiaolian.amigo.ui.widget.popWindow.ChooseBathroomPop;

import java.util.HashMap;
import java.util.List;

import static android.text.Spanned.SPAN_EXCLUSIVE_INCLUSIVE;
import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class ChooseRoomAdapter extends RecyclerView.Adapter<ChooseRoomAdapter.RoomHodler> {
    private static final String TAG = ChooseBathroomAdapter.class.getSimpleName() ;
    public HashMap<Integer, Boolean> states = new HashMap<Integer, Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个
    private Context context ;
    private List<BuildingTrafficDTO.FloorsBean> floorsBeans ;
    private OnItemClickListener onItemClickListener ;

    private ChooseBathroomPop.PopClickableListener popClickableListener ;

    private int  index ;
    public ChooseRoomAdapter(Context context , List<BuildingTrafficDTO.FloorsBean> floorsBeans){
        this.context =  context ;
        this.floorsBeans = floorsBeans ;
    }

    public void setPopClickableListener(ChooseBathroomPop.PopClickableListener popClickableListener) {
        this.popClickableListener = popClickableListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index){
        this.index = index ;
    }

    @Override
    public RoomHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        RoomHodler roomHodler = new RoomHodler(LayoutInflater.from(context).inflate(R.layout.item_choosebathroom , parent , false));
        return roomHodler ;
    }

    @Override
    public void onBindViewHolder(RoomHodler holder, int position) {
        holder.bathroom_status.setTextColor(context.getResources().getColor(R.color.colorFullRed));
        BuildingTrafficDTO.FloorsBean floorsBean = floorsBeans.get(position);
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position ;
                notifyDataSetChanged();
                if (onItemClickListener != null) onItemClickListener.click(position);

                if (popClickableListener != null) popClickableListener.clickable(true);
            }
        });

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position ;
                notifyDataSetChanged();

                if (onItemClickListener != null) onItemClickListener.click(position);

                if (popClickableListener != null) popClickableListener.clickable(true);
            }
        });
        if (index != -1) {
            if (index == position) {
                holder.radioButton.setChecked(true);
            } else {
                holder.radioButton.setChecked(false);
            }
        }else{
            holder.radioButton.setChecked(false);
        }



        if (floorsBean != null){
            String content = "" ;
            holder.textView.setText(floorsBean.getName());
            if (floorsBean.getAvailableCount() > 0){
                String  num = String.valueOf(floorsBean.getAvailableCount());
                content = "[空闲："+floorsBean.getAvailableCount() + "间浴室]" ;
                SpannableString spannable = new SpannableString(content);
                spannable.setSpan(new StyleSpan(Typeface.NORMAL) ,0 ,3 ,SPAN_INCLUSIVE_INCLUSIVE);
                spannable.setSpan(new StyleSpan(Typeface.BOLD) ,4 ,4 +num.length() ,SPAN_EXCLUSIVE_INCLUSIVE);
                spannable.setSpan(new StyleSpan(Typeface.NORMAL) ,4 + num.length() ,content.length() ,SPAN_EXCLUSIVE_INCLUSIVE);
                holder.bathroom_status.setText(spannable);
                holder.bathroom_status.setTextColor(context.getResources().getColor(R.color.colorGreen));
                holder.radioButton.setBackgroundResource(R.drawable.bathroom_radio_color_bg);
            }else if (floorsBean.getWaitCount() >= 0){

                content = "[排队："+floorsBean.getWaitCount() + "人]" ;
                String  num = String.valueOf(floorsBean.getWaitCount());
                SpannableString spannable = new SpannableString(content);
                spannable.setSpan(new StyleSpan(Typeface.NORMAL) ,0 ,3 ,SPAN_INCLUSIVE_INCLUSIVE);
                spannable.setSpan(new StyleSpan(Typeface.BOLD) ,4 ,4 + num.length() ,SPAN_EXCLUSIVE_INCLUSIVE);
                spannable.setSpan(new StyleSpan(Typeface.NORMAL) ,4 + num.length() ,content.length() ,SPAN_EXCLUSIVE_INCLUSIVE);
                holder.bathroom_status.setText(spannable);
                holder.bathroom_status.setTextColor(context.getResources().getColor(R.color.colorFullRed));
                holder.radioButton.setBackgroundResource(R.drawable.bathroom_radio_color_bg);
            }


        }


        if (!TextUtils.isEmpty(floorsBean.getDeviceNo())){
            holder.textView.setText(floorsBean.getName());
            holder.bathroom_status.setText("");
            holder.radioButton.setChecked(true);
            holder.radioButton.setBackgroundResource(R.drawable.bathroom_radio_bg);
            if (onItemClickListener != null) onItemClickListener.click(position);
            if (popClickableListener != null) popClickableListener.clickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return  floorsBeans == null ? 0 : floorsBeans.size() ;
    }

    public class RoomHodler extends RecyclerView.ViewHolder {
        private TextView textView  , bathroom_status;
        private RadioButton radioButton ;
        private RelativeLayout rl ;
        public RoomHodler(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            radioButton = itemView.findViewById(R.id.button);
            bathroom_status = itemView.findViewById(R.id.bathroom_status);
            rl = itemView.findViewById(R.id.rl);
        }
    }
}
