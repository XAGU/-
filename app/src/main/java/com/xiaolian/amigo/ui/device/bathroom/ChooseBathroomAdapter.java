package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Context;
import android.support.v4.util.ObjectsCompat;
import android.view.View;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

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
        if (itemWrapper.getStatus() == BathroomStatus.NONE) {
            holder.getConvertView().setVisibility(View.GONE);
            return;
        }
        holder.getConvertView().setVisibility(View.VISIBLE);
        if (itemWrapper.isSelected()) {
            holder.getView(R.id.tv_content).setVisibility(View.VISIBLE);
            holder.setTextColorRes(R.id.tv_content, R.color.white);
            holder.setText(R.id.tv_content, itemWrapper.getName());
            holder.setBackgroundRes(R.id.ll_inner, R.drawable.bg_bathroom_item_choosed);
            return;
        }
        holder.setText(R.id.tv_content, itemWrapper.getName());
        switch (itemWrapper.getStatus()) {
            case ERROR:
                holder.setBackgroundRes(R.id.ll_inner, R.drawable.bg_bathroom_item_error);
                holder.getView(R.id.tv_content).setVisibility(View.GONE);
                break;
            case USING:
                holder.setBackgroundRes(R.id.ll_inner, R.drawable.bg_bathroom_item_using);
                holder.getView(R.id.tv_content).setVisibility(View.GONE);
                break;
            case AVAILABLE:
                if (isScaled) {
                    holder.getView(R.id.tv_content).setVisibility(View.VISIBLE);
                    holder.setTextColorRes(R.id.tv_content, R.color.colorFullRed);
                    holder.setText(R.id.tv_content, itemWrapper.getName());
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

    @Data
    public static final class BathroomWrapper {
        private Long id;
        private String name;
        private BathroomStatus status;
        private boolean selected = false;

        public BathroomWrapper(String name, BathroomStatus status) {
            this.name = name;
            this.status = status;
        }
    }

    public enum BathroomStatus {
        NONE(0),
        AVAILABLE(1),
        USING(2),
        ERROR(3);

        BathroomStatus(int code) {
            this.code = code;
        }

        private int code;

        public int getCode() {
            return code;
        }

        public static BathroomStatus getStatus(int code) {
            for (BathroomStatus status : BathroomStatus.values()) {
                if (ObjectsCompat.equals(status.getCode(), code)) {
                    return status;
                }
            }
            return null;
        }
    }

    public interface BathroomSelectListener {
        void onBathroomSelected(int bathroomPosition);
    }
}
