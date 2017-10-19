package com.xiaolian.amigo.ui.user.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.user.UserResidence;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
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
    private OnItemClickListener listener;

    public EditDormitoryAdaptor(Context context, int layoutId, List<UserResidenceWrapper> datas) {
        super(context, layoutId, datas);
        setHasStableIds(true);
        this.context = context;
    }

    public EditDormitoryAdaptor(Context context, int layoutId, List<UserResidenceWrapper> datas,
                                IEditDormitoryPresenter<IEditDormitoryView> presenter) {
        super(context, layoutId, datas);
        this.context = context;
        this.presenter = presenter;
    }

    public void setPresenter(IEditDormitoryPresenter<IEditDormitoryView> presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, UserResidenceWrapper userResidenceWrapper, int position) {
        holder.setText(R.id.tv_edit_dormitory_name, userResidenceWrapper.getResidenceName());
        // 只有一个宿舍时，显示为默认宿舍
        if (userResidenceWrapper.isDefault() || getDatas().size() == 1) {
            presenter.saveDefaultResidenceId(userResidenceWrapper.getResidenceId());
            ((ImageView)holder.getView(R.id.iv_choose)).setImageResource(R.drawable.dot_red);
            ((TextView)holder.getView(R.id.tv_choose)).setText("默认宿舍");
            ((TextView) holder.getView(R.id.tv_choose)).setTextColor(ContextCompat.getColor(context, R.color.colorFullRed));
        } else {
            ((ImageView)holder.getView(R.id.iv_choose)).setImageResource(R.drawable.dot_gray);
            ((TextView)holder.getView(R.id.tv_choose)).setText("设为默认");
            ((TextView) holder.getView(R.id.tv_choose)).setTextColor(ContextCompat.getColor(context, R.color.colorDark9));
        }
        holder.getView(R.id.tv_delete).setOnClickListener(v -> presenter.deleteDormitory(userResidenceWrapper.getId()));
        holder.getView(R.id.tv_edit).setOnClickListener(v -> {
            Intent intent = new Intent(context, ListChooseActivity.class);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_IS_EDIT, true);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION,
                    ListChooseActivity.ACTION_LIST_BUILDING);
            intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_RESIDENCE_BIND_ID, userResidenceWrapper.getId());
            context.startActivity(intent);
        });
        holder.getView(R.id.ll_dormitory).setOnClickListener(v -> listener.onItemClick(userResidenceWrapper, position));
        // 是否存在设备
        if (userResidenceWrapper.isExist()) {
            holder.getView(R.id.tv_device_exist).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.tv_device_exist).setVisibility(View.VISIBLE);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(UserResidenceWrapper userResidenceWrapper, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Data
    public static class UserResidenceWrapper {
        private Long id;
        private Long residenceId;
        private String residenceName;
        private String macAddress;
        private boolean isDefault = false;
        private boolean exist = true;

        public UserResidenceWrapper(UserResidence residence, boolean isDefault) {
            if (TextUtils.isEmpty(residence.getMacAddress())) {
                exist = false;
            }
            this.isDefault = isDefault;
            this.id = residence.getId();
            this.residenceId = residence.getResidenceId();
            this.residenceName = residence.getResidenceName();
            this.macAddress = residence.getMacAddress();
        }
    }

}
