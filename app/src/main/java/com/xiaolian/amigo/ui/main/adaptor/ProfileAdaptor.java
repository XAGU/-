package com.xiaolian.amigo.ui.main.adaptor;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.VersionUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * 个人中心列表
 *
 * @author zcd
 * @date 17/10/10
 */

public class ProfileAdaptor extends CommonAdapter<ProfileAdaptor.Item> {

    private Context context ;


    public ProfileAdaptor(Context context, int layoutId, List<Item> datas) {
        super(context, layoutId, datas);
        this.context = context ;
    }

    @Override
    protected void convert(ViewHolder holder, Item item, int position) {
        if (VersionUtils.isUpLollipop()){
            holder.setBackgroundRes(R.id.ll_profile ,R.drawable.custom_bg);
        }
        holder.setImageResource(R.id.imageLeft, item.getLeftImageId());
        holder.setText(R.id.textMid, item.getText());
        TextView rightTv = holder.getView(R.id.tv_amount);
        rightTv.setTextColor(context.getResources().getColor(R.color.colorFullRed));
        if (item.getLeftImageId() == R.drawable.profile_wallet) {
            holder.getView(R.id.tv_amount).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_amount, "¥" + item.getBalance());
        } else if (item.getLeftImageId() == R.drawable.profile_luck) {
            holder.getView(R.id.tv_amount).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_amount, String.valueOf(item.getBonusAmount()));
        } else if (item.getLeftImageId() == R.drawable.profile_credits) {
            if (item.getBonusAmount() == -1) {
                holder.getView(R.id.tv_amount).setVisibility(View.GONE);
            } else {
                holder.getView(R.id.tv_amount).setVisibility(View.VISIBLE);
                holder.setText(R.id.tv_amount, String.valueOf(item.getBonusAmount()));
            }
        }else if (item.getLeftImageId() == R.drawable.profile_repair){
            if ( item.getUnReadWorkOrderRemarkMessageCount() > 0){
                holder.getView(R.id.tv_amount).setVisibility(View.VISIBLE);
                holder.setText(R.id.tv_amount, String.valueOf(item.getUnReadWorkOrderRemarkMessageCount()));
            }else{
                holder.getView(R.id.tv_amount).setVisibility(View.GONE);
            }
        } else {
            holder.getView(R.id.tv_amount).setVisibility(View.GONE);
        }

        if (item.isShowDot()) {
            holder.getView(R.id.v_dot).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.v_dot).setVisibility(View.GONE);
        }
    }

    @Data
    public static class Item {
        public Item(int leftImageId, String text, Class<? extends Activity> activityClazz) {
            this.leftImageId = leftImageId;
            this.text = text;
            this.activityClazz = activityClazz;
        }

        public Item(int leftImageId, String text, Class<? extends Activity> activityClazz  , String verifiedStatus) {
            this.leftImageId = leftImageId;
            this.text = text;
            this.activityClazz = activityClazz;
            this.verifiedStatus = verifiedStatus ;
        }

        String verifiedStatus ;
        int leftImageId;
        String text;
        Class<? extends Activity> activityClazz;
        String balance = "0";
        int bonusAmount = 0;
        /**
         * 服务中心未读消息数
         */
        int unReadWorkOrderRemarkMessageCount = 0 ;
        boolean isShowDot = false;
    }
}
