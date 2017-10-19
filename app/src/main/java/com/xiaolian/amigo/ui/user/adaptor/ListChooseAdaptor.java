package com.xiaolian.amigo.ui.user.adaptor;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.user.Residence;
import com.xiaolian.amigo.data.network.model.user.School;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Data;

/**
 * ListChooseAdapter
 *
 * @author zcd
 */
public class ListChooseAdaptor extends RecyclerView.Adapter<ListChooseAdaptor.ViewHolder> {

    private List<Item> datas;

    private OnItemClickListener mOnItemClickListener;

    private int lastTickPostion = -1;

    private boolean checkDeviceExist = false;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public ListChooseAdaptor(List<Item> datas) {
        this.datas = datas;
    }

    public ListChooseAdaptor(List<Item> datas, boolean checkDeviceExist) {
        this.datas = datas;
        this.checkDeviceExist = checkDeviceExist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_choose, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
                if (lastTickPostion != -1) {
                    datas.get(lastTickPostion).tick = false;
                }
                datas.get(position).tick = true;
                lastTickPostion = position;
                notifyDataSetChanged();
            }
        });
        holder.tv_content.setText(datas.get(position).content);
        if (datas.get(position).tick) {
            holder.iv_tick.setVisibility(View.VISIBLE);
            lastTickPostion = position;
        } else {
            holder.iv_tick.setVisibility(View.GONE);
        }
        if (checkDeviceExist) {
            if (datas.get(position).isDeviceExist()) {
                holder.tv_device_exist.setVisibility(View.GONE);
            } else {
                holder.tv_device_exist.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == datas ? 0 : datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.iv_tick)
        ImageView iv_tick;
        @BindView(R.id.tv_device_exist)
        TextView tv_device_exist;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Data
    public static class Item implements Parcelable {
        String content;
        boolean tick = false;
        Long id;
        String extra;
        int deviceType;
        boolean deviceExist = true;

        public Item(String content, boolean tick, Long id) {
            this(content, tick);
            this.id = id;
        }

        public Item(String content, int deviceType) {
            this(content, false);
            this.deviceType = deviceType;
        }

        public Item(String content, boolean tick) {
            this.content = content;
            this.tick = tick;
        }

        public Item(School school, boolean tick) {
            this.content = school.getSchoolName();
            this.id = school.getId();
            this.tick = tick;
        }

        public Item(Residence residence) {
            this.content = residence.getName();
            this.extra = residence.getFullName();
            this.id = residence.getId();
            this.tick = false;
            this.deviceExist = !TextUtils.isEmpty(residence.getMacAddress());
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.content);
            dest.writeByte(this.tick ? (byte) 1 : (byte) 0);
            dest.writeValue(this.id);
            dest.writeString(this.extra);
            dest.writeByte(this.deviceExist ? (byte) 1 : (byte) 0);
        }

        protected Item(Parcel in) {
            this.content = in.readString();
            this.tick = in.readByte() != 0;
            this.id = (Long) in.readValue(Long.class.getClassLoader());
            this.extra = in.readString();
            this.deviceExist = in.readByte() != 0;
        }

        public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel source) {
                return new Item(source);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };
    }
}
