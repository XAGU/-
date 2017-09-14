package com.xiaolian.amigo.tmp.activity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yik on 2017/9/6.
 */

public class MenuListViewAdapter extends BaseAdapter {

    private List<Item> dataList;

    private Context mContext;


    public MenuListViewAdapter(Context context, List<Item> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Item getItem(int position) {

        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_profile_menu, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageLeft.setImageResource(getItem(position).leftImageId);
        holder.textMid.setText(getItem(position).text);

        return convertView;
    }

    final class ViewHolder {
        @BindView(R.id.imageLeft)
        ImageView imageLeft;
        @BindView(R.id.textMid)
        TextView textMid;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static final class Item {
        int leftImageId;
        String text;
        Class<? extends Activity> activityClazz;

        public Item(int leftImageId, String text, Class<? extends Activity> activityClazz) {
            this.leftImageId = leftImageId;
            this.text = text;
            this.activityClazz = activityClazz;
        }
    }

}
