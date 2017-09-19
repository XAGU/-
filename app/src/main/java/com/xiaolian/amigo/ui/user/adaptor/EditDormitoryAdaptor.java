package com.xiaolian.amigo.ui.user.adaptor;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.user.UserResidence;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditDormitoryView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * 编辑宿舍
 * <p>
 * Created by zcd on 9/19/17.
 */

public class EditDormitoryAdaptor extends CommonAdapter<EditDormitoryAdaptor.UserResidenceWrapper> {

    private Context context;
    private IEditDormitoryPresenter<IEditDormitoryView> presenter;

    public EditDormitoryAdaptor(Context context, int layoutId, List<UserResidenceWrapper> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    public EditDormitoryAdaptor(Context context, int layoutId, List<UserResidenceWrapper> datas,
                                IEditDormitoryPresenter<IEditDormitoryView> presenter) {
        super(context, layoutId, datas);
        this.context = context;
        this.presenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, UserResidenceWrapper userResidenceWrapper, int position) {
        holder.setText(R.id.tv_edit_dormitory_name, userResidenceWrapper.getResidenceName());
        if (userResidenceWrapper.isDefault()) {
            ((ImageView)holder.getView(R.id.iv_choose)).setImageResource(R.drawable.dot_red);
            ((TextView)holder.getView(R.id.tv_choose)).setText("默认宿舍");
            ((TextView) holder.getView(R.id.tv_choose)).setTextColor(ContextCompat.getColor(context, R.color.colorFullRed));
        } else {
            ((ImageView)holder.getView(R.id.iv_choose)).setImageResource(R.drawable.dot_gray);
            ((TextView)holder.getView(R.id.tv_choose)).setText("设为默认");
            ((TextView) holder.getView(R.id.tv_choose)).setTextColor(ContextCompat.getColor(context, R.color.colorDark9));
        }
        holder.getView(R.id.tv_delete).setOnClickListener(v -> presenter.deleteDormitory(userResidenceWrapper.getResidenceId()));
    }

    @Data
    public static class UserResidenceWrapper {
        private int id;
        private int residenceId;
        private String residenceName;
        private boolean isDefault = false;

        public UserResidenceWrapper(UserResidence residence, boolean isDefault) {
            this.isDefault = isDefault;
            this.id = residence.getId();
            this.residenceId = residence.getResidenceId();
            this.residenceName = residence.getResidenceName();
        }
    }

}
