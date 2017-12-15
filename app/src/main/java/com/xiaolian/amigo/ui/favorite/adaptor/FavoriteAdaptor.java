package com.xiaolian.amigo.ui.favorite.adaptor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.device.ScanDeviceGroup;
import com.xiaolian.amigo.data.network.model.device.WaterInListDTO;
import com.xiaolian.amigo.ui.favorite.intf.IFavoritePresenter;
import com.xiaolian.amigo.ui.favorite.intf.IFavoriteView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Data;

/**
 *
 * Created by caidong on 2017/9/12.
 */
public class FavoriteAdaptor extends RecyclerView.Adapter<FavoriteAdaptor.ViewHolder> {

    private List<FavoriteWrapper> favorites;
    private IFavoritePresenter<IFavoriteView> presenter;
    private OnItemLongClickListener longClickListener;
    private OnItemClickListener clickListener;
    private OnDeleteListener deleteListener;

    public FavoriteAdaptor(List<FavoriteWrapper> favorites, IFavoritePresenter<IFavoriteView> presenter) {
        this.favorites = favorites;
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view, presenter);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FavoriteWrapper wrapper = favorites.get(position);
        if (null != wrapper) {
            holder.tv_device.setText(Device.DISPENSER.getDesc());
            holder.tv_location.setText(wrapper.getLocation());
            holder.deviceId = wrapper.getId();
            holder.index = holder.getAdapterPosition();
        }
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onItemLongClick();
            return true;
        });
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(holder.getAdapterPosition());
            }
        });
        holder.tv_delete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == favorites ? 0 : favorites.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_device)
        TextView tv_device;
        @BindView(R.id.tv_location)
        TextView tv_location;
        @BindView(R.id.tv_delete)
        TextView tv_delete;

        IFavoritePresenter<IFavoriteView> presenter;
        // 设备id
        Long deviceId;
        // 设备在设备列表中的索引位置
        Integer index;

        public ViewHolder(View itemView, IFavoritePresenter<IFavoriteView> presenter) {
            super(itemView);
            this.presenter = presenter;
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.deleteListener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick();
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnDeleteListener {
        void onDelete(int position);
    }


    @Data
    public static class FavoriteWrapper {
        // 设备id
        Long id;
        // residenceId
        Long residenceId;
        // 设备类型
        Integer type;
        // 设备
        String location;

        public FavoriteWrapper(ScanDeviceGroup device) {
            this.id = device.getWater().get(0).getId();
            this.residenceId = device.getResidenceId();
            this.location = device.getLocation();
        }

        public FavoriteWrapper(WaterInListDTO device) {
            this.id = device.getWater().get(0).getId();
            this.residenceId = device.getResidenceId();
            this.location = device.getLocation();
        }
    }
}
