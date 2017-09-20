package com.xiaolian.amigo.ui.user.adaptor;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public ListChooseAdaptor(List<Item> datas) {
        this.datas = datas;
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Data
    public static class Item implements Parcelable {
        String content;
        boolean tick;
        Integer id;
        String extra;

        public Item(String content, boolean tick, Integer id) {
            this(content, tick);
            this.id = id;
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
        }

        protected Item(Parcel in) {
            this.content = in.readString();
            this.tick = in.readByte() != 0;
            this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        }

        public static final Creator<Item> CREATOR = new Creator<Item>() {
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
