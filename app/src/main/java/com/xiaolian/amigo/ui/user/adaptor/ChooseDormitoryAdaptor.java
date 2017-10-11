package com.xiaolian.amigo.ui.user.adaptor;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    }
}
