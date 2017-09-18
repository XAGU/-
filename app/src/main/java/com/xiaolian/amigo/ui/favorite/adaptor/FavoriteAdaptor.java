package com.xiaolian.amigo.ui.favorite.adaptor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.device.Device;
import com.xiaolian.amigo.ui.favorite.intf.IFavoritePresenter;
import com.xiaolian.amigo.ui.favorite.intf.IFavoriteView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Data;

/**
 * Created by caidong on 2017/9/12.
 */
public class FavoriteAdaptor extends RecyclerView.Adapter<FavoriteAdaptor.ViewHolder> {

    private List<FavoriteWrapper> favorites;
    private IFavoritePresenter<IFavoriteView> presenter;

    private FavoriteAdaptor(List<FavoriteWrapper> favorites) {
        this.favorites = favorites;
    }

    public FavoriteAdaptor(List<FavoriteWrapper> favorites, IFavoritePresenter<IFavoriteView> presenter) {
        this(favorites);
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
            holder.tv_device.setText(com.xiaolian.amigo.data.enumeration.Device.getDevice(wrapper.getType()).getDesc());
            holder.tv_location.setText(wrapper.getLocation());
            holder.deviceId = wrapper.id;
        }
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

        IFavoritePresenter<IFavoriteView> presenter;
        Long deviceId;

        public ViewHolder(View itemView, IFavoritePresenter<IFavoriteView> presenter) {
            super(itemView);
            this.presenter = presenter;
            ButterKnife.bind(this, itemView);
        }

        // 点击删除按钮删除设备
        @OnClick(R.id.tv_delete)
        void delete() {
            presenter.onDelete(deviceId);
        }
    }

    @Data
    public static class FavoriteWrapper {
        // 设备id
        Long id;
        // 设备类型
        Integer type;
        // 设备
        String location;

        public FavoriteWrapper(Device device) {
            this.id = device.getId();
            this.type = device.getType();
            this.location = device.getLocation();
        }

        public FavoriteWrapper(Integer type, String location) {
            this.type = type;
            this.location = location;
        }
    }
}
