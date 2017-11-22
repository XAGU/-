package com.xiaolian.amigo.ui.user.adaptor;

import android.content.Context;
import android.view.View;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * <p>
 * Created by zcd on 10/11/17.
 */

public class ChooseDormitoryAdaptor extends CommonAdapter<EditDormitoryAdaptor.UserResidenceWrapper> {

    public ChooseDormitoryAdaptor(Context context, int layoutId, List<EditDormitoryAdaptor.UserResidenceWrapper> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, EditDormitoryAdaptor.UserResidenceWrapper userResidenceWrapper, int position) {

        holder.setText(R.id.tv_dormitory_name, userResidenceWrapper.getResidenceName());
        if (userResidenceWrapper.isDefault()) {
            holder.getView(R.id.tv_default_dormitory).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.tv_default_dormitory).setVisibility(View.GONE);
        }
        // 是否存在设备
        if (userResidenceWrapper.isExist()) {
            holder.getView(R.id.tv_device_exist).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.tv_device_exist).setVisibility(View.VISIBLE);
        }
    }
}
