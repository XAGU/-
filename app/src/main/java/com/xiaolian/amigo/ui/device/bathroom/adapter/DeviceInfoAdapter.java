package com.xiaolian.amigo.ui.device.bathroom.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/7/2
 */
public class DeviceInfoAdapter extends CommonAdapter<DeviceInfoAdapter.DeviceInfoWrapper> {
    private Context context;

    public DeviceInfoAdapter(Context context, int layoutId, List<DeviceInfoWrapper> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, DeviceInfoWrapper deviceInfoWrapper, int position) {
        TextView tvLeft = holder.getView(R.id.tv_left);
        tvLeft.setText(deviceInfoWrapper.getLeftText());
        tvLeft.setTextColor(ContextCompat.getColor(context,
                deviceInfoWrapper.getLeftTextColor()));
        TextView tvRight = holder.getView(R.id.tv_right);
        tvRight.setText(deviceInfoWrapper.getRightText());
        tvRight.setTextColor(ContextCompat.getColor(context,
                deviceInfoWrapper.getRightTextColor()));
        tvRight.setTextSize(deviceInfoWrapper.getRightTextSizeSp());
        tvRight.setTypeface(tvRight.getTypeface(), deviceInfoWrapper.getRightTextStyle());
        ImageView ivArrow = holder.getView(R.id.iv_arrow);
        ivArrow.setVisibility(deviceInfoWrapper.isHasArrow() ? View.VISIBLE : View.GONE);
    }

    @Data
    public static final class DeviceInfoWrapper {
        private String leftText;
        private int leftTextColor = R.color.colorDark6;
        private String rightText;
        private int rightTextColor = R.color.colorDark2;
        private int rightTextSizeSp;
        private int rightTextStyle;
        private boolean hasArrow = false;

        public DeviceInfoWrapper(String leftText, String rightText,
                                 int rightTextColor, int rightTextSizeSp, int rightTextStyle,
                                 boolean hasArrow) {
            this.leftText = leftText;
            this.rightText = rightText;
            this.rightTextColor = rightTextColor;
            this.rightTextSizeSp = rightTextSizeSp;
            this.rightTextStyle = rightTextStyle;
            this.hasArrow = hasArrow;
        }
    }
}
