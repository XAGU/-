package com.xiaolian.amigo.ui.widget.dialog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaolian.amigo.R;

import java.util.HashMap;

public class ChooseRoomAdapter extends RecyclerView.Adapter<ChooseRoomAdapter.RoomHodler> {

    public HashMap<Integer, Boolean> states = new HashMap<Integer, Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个
    private Context context ;

    public ChooseRoomAdapter(Context context){
        this.context =  context ;
    }
    @Override
    public RoomHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        RoomHodler roomHodler = new RoomHodler(LayoutInflater.from(context).inflate(R.layout.item_choosebathroom , parent , false));
        return roomHodler ;
    }

    @Override
    public void onBindViewHolder(RoomHodler holder, int position) {
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context ,"选择" + position  , Toast.LENGTH_SHORT).show();
                for (int i = 0 ; i < getItemCount() ; i++){
                    states.put(i , false);
                }

                states.put(position , true);
                notifyDataSetChanged();
            }
        });

        if (states.get((Integer) position) == null || states.get((Integer) position) == false) {  //true说明没有被选中
            holder.radioButton.setChecked(false);
        } else {
            holder.radioButton.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class RoomHodler extends RecyclerView.ViewHolder {
        private TextView textView ;
        private RadioButton radioButton ;
        public RoomHodler(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            radioButton = itemView.findViewById(R.id.button);
        }
    }
}
