package com.xiaolian.amigo.ui.widget.dialog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bathroom.BuildingTrafficDTO;
import com.xiaolian.amigo.intf.OnItemClickListener;
import com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomAdapter;
import com.xiaolian.amigo.ui.widget.popWindow.ChooseBathroomPop;

import java.util.HashMap;
import java.util.List;

public class ChooseRoomAdapter extends RecyclerView.Adapter<ChooseRoomAdapter.RoomHodler> {
    private static final String TAG = ChooseBathroomAdapter.class.getSimpleName() ;
    public HashMap<Integer, Boolean> states = new HashMap<Integer, Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个
    private Context context ;
    private List<BuildingTrafficDTO.FloorsBean> floorsBeans ;
    private OnItemClickListener onItemClickListener ;

    private ChooseBathroomPop.PopClickableListener popClickableListener ;

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

    @Override
    public RoomHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        RoomHodler roomHodler = new RoomHodler(LayoutInflater.from(context).inflate(R.layout.item_choosebathroom , parent , false));
        return roomHodler ;
    }

    @Override
    public void onBindViewHolder(RoomHodler holder, int position) {
        boolean isCheck = false ;
        holder.bathroom_status.setTextColor(context.getResources().getColor(R.color.colorFullRed));
        BuildingTrafficDTO.FloorsBean floorsBean = floorsBeans.get(position);
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0 ; i < getItemCount() ; i++){
                    states.put(i , false);
                }
                if (onItemClickListener != null) onItemClickListener.click(position);
                states.put(position , true);
                notifyDataSetChanged();
            }
        });

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0 ; i < getItemCount() ; i++){
                    states.put(i , false);
                }
                if (onItemClickListener != null) onItemClickListener.click(position);
                states.put(position , true);
                notifyDataSetChanged();
            }
        });

        if (states.get((Integer) position) == null || states.get((Integer) position) == false) {  //true说明被选中
            holder.radioButton.setChecked(false);
        } else {
            holder.radioButton.setChecked(true);
        }


        if (holder.radioButton.isChecked()){
            Log.e(TAG, "onBindViewHolder: >>>>>" );
            isCheck = true ;
        }else{
            Log.e(TAG, "onBindViewHolder: >>>>>>" );
            isCheck = false ;
        }


        if (floorsBean != null) {
            if (TextUtils.isEmpty(floorsBean.getDeviceNo())) {
                if (popClickableListener != null) popClickableListener.clickable(isCheck);
            }
        }

        if (floorsBean != null){
            holder.textView.setText(floorsBean.getName());
            if (floorsBean.getAvailableCount() > 0){
                holder.bathroom_status.setText("[空闲："+floorsBean.getAvailableCount() + "间浴室]");
                holder.radioButton.setBackgroundResource(R.drawable.bathroom_radio_bg);
            }else if (floorsBean.getWaitCount() >= 0){
                holder.bathroom_status.setText("[排队："+floorsBean.getWaitCount() + "人]");
                holder.radioButton.setBackgroundResource(R.drawable.bathroom_radio_color_bg);
            }
        }


        if (!TextUtils.isEmpty(floorsBean.getDeviceNo())){
            holder.textView.setText(floorsBean.getName());
            holder.bathroom_status.setText("");
            holder.radioButton.setBackgroundResource(R.drawable.bathroom_radio_bg);
            holder.radioButton.setChecked(true);
            if (onItemClickListener != null) onItemClickListener.click(position);
        }
    }

    @Override
    public int getItemCount() {
        return  floorsBeans == null ? 0 : floorsBeans.size() ;
    }

    class RoomHodler extends RecyclerView.ViewHolder {
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
