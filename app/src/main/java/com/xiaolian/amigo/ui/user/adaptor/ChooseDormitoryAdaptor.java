package com.xiaolian.amigo.ui.user.adaptor;

import android.content.Context;
import android.support.v4.util.ObjectsCompat;
import android.view.View;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 选择宿舍
 *
 * @author zcd
 * @date 17/10/11
 */

public class ChooseDormitoryAdaptor extends CommonAdapter<EditDormitoryAdaptor.UserResidenceWrapper> {
    private Long residenceId;

    public ChooseDormitoryAdaptor(Context context, int layoutId, List<EditDormitoryAdaptor.UserResidenceWrapper> datas) {
        super(context, layoutId, datas);
    }

    public void setResidenceId(Long residenceId) {
        this.residenceId = residenceId;
    }

    @Override
    protected void convert(ViewHolder holder, EditDormitoryAdaptor.UserResidenceWrapper userResidenceWrapper, int position) {
        if (ObjectsCompat.equals(residenceId, userResidenceWrapper.getResidenceId())) {
            holder.getView(R.id.iv_tick).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.iv_tick).setVisibility(View.GONE);
        }
        holder.setText(R.id.tv_dormitory_name, userResidenceWrapper.getResidenceName());
//        if (userResidenceWrapper.isDefault()) {
////            holder.getView(R.id.tv_default_dormitory).setVisibility(View.VISIBLE);
//        } else {
//            holder.getView(R.id.tv_default_dormitory).setVisibility(View.GONE);
//        }
        // 是否存在设备
        if (userResidenceWrapper.isExist()) {
            holder.getView(R.id.tv_device_exist).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.tv_device_exist).setVisibility(View.VISIBLE);
        }
    }
}
