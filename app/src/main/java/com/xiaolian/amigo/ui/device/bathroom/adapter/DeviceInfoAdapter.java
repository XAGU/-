package com.xiaolian.amigo.ui.device.bathroom.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.Log;
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
    private static final String TAG = DeviceInfoWrapper.class.getSimpleName();

    private Context context;
    private CountTimeText countTimeText ;
    public DeviceInfoAdapter(Context context, int layoutId, List<DeviceInfoWrapper> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    public void setCountTime(CountTimeText countTimeText){
        this.countTimeText = countTimeText ;
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
        if (deviceInfoWrapper.getRightTextStyle() == Typeface.BOLD){
            TextPaint tp = tvRight.getPaint();
            tp.setFakeBoldText(true);
        }else{
            TextPaint tp = tvRight.getPaint();
            tp.setFakeBoldText(false);
        }
        ImageView ivArrow = holder.getView(R.id.iv_arrow);
        ivArrow.setVisibility(deviceInfoWrapper.isHasArrow() ? View.VISIBLE : View.GONE);
        if ("剩余时间：".equals(deviceInfoWrapper.getRightText())){
            if (countTimeText != null) countTimeText.countTime(tvRight);
        }
    }


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

        public String getLeftText() {
            return leftText;
        }

        public void setLeftText(String leftText) {
            this.leftText = leftText;
        }

        public int getLeftTextColor() {
            return leftTextColor;
        }

        public void setLeftTextColor(int leftTextColor) {
            this.leftTextColor = leftTextColor;
        }

        public String getRightText() {
            return rightText;
        }

        public void setRightText(String rightText) {
            this.rightText = rightText;
        }

        public int getRightTextColor() {
            return rightTextColor;
        }

        public void setRightTextColor(int rightTextColor) {
            this.rightTextColor = rightTextColor;
        }

        public int getRightTextSizeSp() {
            return rightTextSizeSp;
        }

        public void setRightTextSizeSp(int rightTextSizeSp) {
            this.rightTextSizeSp = rightTextSizeSp;
        }

        public int getRightTextStyle() {
            return rightTextStyle;
        }

        public void setRightTextStyle(int rightTextStyle) {
            this.rightTextStyle = rightTextStyle;
        }

        public boolean isHasArrow() {
            return hasArrow;
        }

        public void setHasArrow(boolean hasArrow) {
            this.hasArrow = hasArrow;
        }
    }

    public interface CountTimeText{
        void countTime(TextView textView);
    }
}
