package com.xiaolian.amigo.tmp.activity.wallet.adaptor;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caidong on 2017/9/7.
 */

public class RechargeAdaptor extends ArrayAdapter<RechargeAdaptor.RechargeGroup> {

    private int resourceId;
    private Context context;

    public RechargeAdaptor(@NonNull Context context, @LayoutRes int resource, List<RechargeGroup> groups) {
        super(context, resource, groups);
        this.context = context;
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RechargeGroup rechargeGroup = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        Recharge[] recharges = rechargeGroup.recharges;
        for (int i = 0; i < recharges.length; i++) {
            ViewGroup rl = null;
            ImageView iv = null;
            TextView tv_main = null;
            TextView tv_sub = null;

            if (i == 0) {
                rl = (ViewGroup) view.findViewById(R.id.iv_recharge_0);
                iv = (ImageView) view.findViewById(R.id.iv_recharge_0_img);
                tv_main = (TextView) view.findViewById(R.id.tv_recharge_0_main);
                tv_sub = (TextView) view.findViewById(R.id.tv_recharge_0_sub);
            } else if (i == 1) {
                rl = (ViewGroup) view.findViewById(R.id.iv_recharge_1);
                iv = (ImageView) view.findViewById(R.id.iv_recharge_1_img);
                tv_main = (TextView) view.findViewById(R.id.tv_recharge_1_main);
                tv_sub = (TextView) view.findViewById(R.id.tv_recharge_1_sub);
            } else { // i == 2
                rl = (ViewGroup) view.findViewById(R.id.iv_recharge_2);
                iv = (ImageView) view.findViewById(R.id.iv_recharge_2_img);
                tv_main = (TextView) view.findViewById(R.id.tv_recharge_2_main);
                tv_sub = (TextView) view.findViewById(R.id.tv_recharge_2_sub);
            }

            rl.setVisibility(View.VISIBLE);

            Recharge recharge = recharges[i];
            // 打折
            if (recharge.type == 1) {
                iv.setVisibility(View.VISIBLE);
                tv_sub.setVisibility(View.VISIBLE);
                iv.setImageResource(R.drawable.wallet_discount);
                tv_sub.setText(recharge.sub);
            }
            // 优惠券
            else if (recharge.type == 2) {
                iv.setVisibility(View.VISIBLE);
                tv_sub.setVisibility(View.VISIBLE);
                iv.setImageResource(R.drawable.wallet_ticket);
                tv_sub.setText(recharge.sub);
            }
            tv_main.setText(recharge.main);
        }
        return view;
    }

    public static class RechargeGroup {
        Recharge[] recharges;

        public RechargeGroup(Recharge[] recharges) {
            this.recharges = recharges;
        }
    }

    public static class Recharge {
        // 充值类型
        Integer type;
        // 主信息
        String main;
        // 附信息
        String sub;

        public Recharge(Integer type, String main, String sub) {
            this.type = type;
            this.main = main;
            this.sub = sub;
        }
    }
}
