package com.xiaolian.amigo.tmp.activity.wallet.adaptor;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import java.util.List;

/**
 * Created by caidong on 2017/9/7.
 */

public class PrepayAdaptor extends ArrayAdapter<PrepayAdaptor.Prepay> {

    private int resourceId;
    private Context context;

    public PrepayAdaptor(@NonNull Context context, @LayoutRes int resource, List<Prepay> prepays) {
        super(context, resource, prepays);
        this.context = context;
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Prepay prepay = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);

            viewHolder = new ViewHolder();
            viewHolder.tv_prepay_device = (TextView) view.findViewById(R.id.tv_prepay_device);
            viewHolder.tv_prepay_time = (TextView) view.findViewById(R.id.tv_prepay_time);
            viewHolder.tv_prepay_amount = (TextView) view.findViewById(R.id.tv_prepay_amount);
            viewHolder.tv_prepay_title = (TextView) view.findViewById(R.id.tv_prepay_title);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_prepay_device.setText(prepay.device);
        viewHolder.tv_prepay_amount.setText(prepay.amount);
        viewHolder.tv_prepay_amount.setText(prepay.time);
        return view;
    }

    private class ViewHolder {
        TextView tv_prepay_device;
        TextView tv_prepay_amount;
        TextView tv_prepay_time;
        TextView tv_prepay_title;
    }

    public static class Prepay {
        // 设备名称
        String device;
        // 预付金额
        String amount;
        // 时间
        String time;

        public Prepay(String device, String amount, String time) {
            this.device = device;
            this.amount = amount;
            this.time = time;
        }
    }
}
