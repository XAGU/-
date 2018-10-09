package com.xiaolian.amigo.ui.device.bathroom.adapter;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/7/5
 */
public class BuyCodeRecordAdapter extends CommonAdapter<BuyCodeRecordAdapter.RecordWrapper> {

    public BuyCodeRecordAdapter(Context context, int layoutId, List<RecordWrapper> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, RecordWrapper recordWrapper, int position) {
        holder.setText(R.id.tv_left_top, recordWrapper.getAmount());
        holder.setText(R.id.tv_left_bottom_first, recordWrapper.getLeftBottomText());
        holder.setText(R.id.tv_left_bottom_second,
                TimeUtils.noticeTimestampFormat(recordWrapper.getCreateTime()));
        holder.setText(R.id.tv_right, recordWrapper.getRightText());
    }

    public static final class RecordWrapper {
        private String amount;
        private String leftBottomText;
        private Long createTime;
        private String rightText;

        public RecordWrapper(String amount, String leftBottomText, Long createTime, String rightText) {
            this.amount = amount;
            this.leftBottomText = leftBottomText;
            this.createTime = createTime;
            this.rightText = rightText;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getLeftBottomText() {
            return leftBottomText;
        }

        public void setLeftBottomText(String leftBottomText) {
            this.leftBottomText = leftBottomText;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public String getRightText() {
            return rightText;
        }

        public void setRightText(String rightText) {
            this.rightText = rightText;
        }
    }
}
