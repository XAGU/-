package com.xiaolian.amigo.ui.user.adaptor;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.user.UserResidence;
import com.xiaolian.amigo.data.network.model.user.UserResidenceInListDTO;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * 编辑宿舍
 *
 * @author zcd
 * @date 17/9/19
 */

public class EditDormitoryAdaptor extends CommonAdapter<EditDormitoryAdaptor.UserResidenceWrapper> {
    private static final String TAG = EditDormitoryAdaptor.class.getSimpleName();
    private Context context;
    private IEditDormitoryPresenter<IEditDormitoryView> presenter;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public EditDormitoryAdaptor(Context context, int layoutId, List<UserResidenceWrapper> datas) {
        super(context, layoutId, datas);
        setHasStableIds(false);
        this.context = context;
    }

    public void setPresenter(IEditDormitoryPresenter<IEditDormitoryView> presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, UserResidenceWrapper userResidenceWrapper, int position) {
        holder.setText(R.id.tv_edit_dormitory_name, userResidenceWrapper.getResidenceName());
        // 只有一个宿舍时，显示为默认宿舍
        if (position == 0) {
            holder.getView(R.id.iv_tick).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.iv_tick).setVisibility(View.GONE); }
        holder.getView(R.id.tv_delete).setOnClickListener(v ->
                {
                    if (position == 0) {
                        presenter.deleteBathroomRecord(userResidenceWrapper.getId(), position, true);
                    }else presenter.deleteBathroomRecord(userResidenceWrapper.getId(), position, false);
                }
                );

        holder.getView(R.id.ll_dormitory).setOnClickListener(v -> listener.onItemClick(userResidenceWrapper, position));
        holder.getView(R.id.ll_dormitory).setOnLongClickListener(v -> {
            longClickListener.onItemLongClick();
            return true;
        });
    }

    public interface OnItemClickListener {
        /**
         * 列表点击事件
         * @param userResidenceWrapper 宿舍
         * @param position 位置
         */
        void onItemClick(UserResidenceWrapper userResidenceWrapper, int position);
    }

    public interface OnItemLongClickListener {
        /**
         * 列表长按事件
         */
        void onItemLongClick();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }


    @Data
    public static class UserResidenceWrapper {
        private long id;
        private Long residenceId;
        private String residenceName;
        private String macAddress;
        private boolean exist = true;
        private boolean isPubBath = false ;
        private long supplierId ;
        private UserResidenceInListDTO residence ;
        public UserResidenceWrapper(UserResidenceInListDTO residence) {
            if (TextUtils.isEmpty(residence.getMacAddress())) {
                exist = false;
            }
            this.residence = residence ;
            this.id = residence.getId();
            this.residenceId = residence.getResidenceId();
            this.residenceName = residence.getResidenceName();
            this.macAddress = residence.getMacAddress();
            this.isPubBath = residence.isPubBath();
            this.supplierId = residence.getSupplierId() ;
        }
    }

}
