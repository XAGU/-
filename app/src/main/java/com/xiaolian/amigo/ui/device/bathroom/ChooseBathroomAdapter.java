package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Context;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.scale.ViewHelper;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import static com.xiaolian.amigo.util.Constant.AVAILABLE;
import static com.xiaolian.amigo.util.Constant.BATH_USING;
import static com.xiaolian.amigo.util.Constant.ERROR;

/**
 * @author zcd
 * @date 18/6/27
 */
public class ChooseBathroomAdapter extends CommonAdapter<ChooseBathroomAdapter.BathroomWrapper> {
    private boolean isScaled = false;
    private BathroomSelectListener listener;

    public ChooseBathroomAdapter(Context context, int layoutId, List<BathroomWrapper> datas,
                                 BathroomSelectListener listener) {
        super(context, layoutId, datas);
        this.listener = listener;
    }

    @Override
    protected void convert(ViewHolder holder, BathroomWrapper itemWrapper, int position) {
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onBathroomSelected(position);
                }
            }
        });

        holder.getConvertView().setVisibility(View.VISIBLE);
        if (itemWrapper.isSelected()) {
            holder.getView(R.id.tv_content).setVisibility(View.VISIBLE);
            holder.setTextColorRes(R.id.tv_content, R.color.white);
            holder.setText(R.id.tv_content, itemWrapper.getDeviceNo());
            holder.setBackgroundRes(R.id.ll_inner, R.drawable.bg_bathroom_item_choosed);
            return;
        }
        holder.setText(R.id.tv_content, itemWrapper.getDeviceNo());
        switch (itemWrapper.getStatus()) {
            case ERROR:
                holder.setBackgroundRes(R.id.ll_inner, R.drawable.bg_bathroom_item_error);
                holder.getView(R.id.tv_content).setVisibility(View.GONE);
                break;
            case BATH_USING:
                holder.setBackgroundRes(R.id.ll_inner, R.drawable.bg_bathroom_item_using);
                holder.getView(R.id.tv_content).setVisibility(View.GONE);
                break;
            case AVAILABLE:
                if (isScaled) {
                    holder.getView(R.id.tv_content).setVisibility(View.VISIBLE);
                    holder.setTextColorRes(R.id.tv_content, R.color.colorFullRed);
                    holder.setText(R.id.tv_content, itemWrapper.getDeviceNo());
                    ViewHelper.setScaleX(holder.getView(R.id.tv_content), 2.0f );// x方向上缩小
                    ViewHelper.setScaleY(holder.getView(R.id.tv_content), 2.0f );// y方向上缩小
                } else {
                    holder.getView(R.id.tv_content).setVisibility(View.GONE);
                }
                holder.setBackgroundRes(R.id.ll_inner, R.drawable.bg_bathroom_item_available);
                break;
        }

    }

    public boolean isScaled() {
        return isScaled;
    }

    public void setScaled(boolean scaled) {
        isScaled = scaled;
    }


    public static final class BathroomWrapper {
        private Long id;
        private String deviceNo;
        private int status;
        private boolean selected = false;


        public BathroomWrapper(String name, int status) {
            this.deviceNo = name;
            this.status = status;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }


    public interface BathroomSelectListener {
        void onBathroomSelected(int bathroomPosition);
    }
}
