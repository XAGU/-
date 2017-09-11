package com.xiaolian.amigo.activity.favorite.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.xiaolian.amigo.R;

import java.util.List;

/**
 * Created by caidong on 2017/9/11.
 */
public class FavoriteAdaptor extends BaseSwipeAdapter {

    Context mContext;
    List<Favorite> favorites;

    public FavoriteAdaptor(Context mContext, List<Favorite> favorites) {
        this.mContext = mContext;
        this.favorites = favorites;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.sl_collection;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_favorite, parent, false);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));

        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, v.findViewById(R.id.ll_operator));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView device = (TextView)convertView.findViewById(R.id.tv_device);
        device.setText("饮水机");

        TextView location = (TextView)convertView.findViewById(R.id.tv_location);
        location.setText("图书馆二楼休闲室");

        final SwipeLayout sl = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
        sl.setShowMode(SwipeLayout.ShowMode.PullOut);

        final TextView delete  = (TextView) convertView.findViewById(R.id.tv_delete);
        delete.setTag(position);
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "删除", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        return favorites.size();
    }

    @Override
    public Object getItem(int position) {
        return favorites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class Favorite {
        // 设备类型
        Integer type;
        // 设备位置
        String location;

        public Favorite(Integer type, String location) {
            this.type = type;
            this.location = location;
        }
    }
}