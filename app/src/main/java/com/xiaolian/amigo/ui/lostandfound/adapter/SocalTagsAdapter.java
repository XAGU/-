package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.SpaceBottomItemDecoration;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * recycleview 适配器
 * Created by RedDargon on 2017/3/27.
 */

public class SocalTagsAdapter extends RecyclerView.Adapter<SocalTagsAdapter.ItemViewHolder> {
    private final RecyclerView recycleview;
    private LayoutInflater inflater;
    private List<String> list;
    private final LinearLayoutManager manager;
    private boolean move;
    private int index;
    private Context context ;

    public SocalTagsAdapter(Context context, List<String> list, RecyclerView recyclerView) {
        this.context = context ;
        inflater = LayoutInflater.from(context);
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
        //可以在这里做分割线处理
        manager = new LinearLayoutManager(context);
        this.recycleview = recyclerView;
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceBottomItemDecoration(ScreenUtils.dpToPxInt(context ,5)));
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //在这里进行第二次滚动
                if (move ){
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = index - manager.findFirstVisibleItemPosition();
                    if ( 0 <= n && n < recyclerView.getChildCount()){
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = recyclerView.getChildAt(n).getLeft();
                        //最后的移动
                        recyclerView.smoothScrollBy(top, top);
                    }
                }
            }
        });
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(inflater.inflate(R.layout.item_social_tag, null , false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String  imgUrl = list.get(position);

        if (position ==0) {
            holder.img.setBackgroundResource(R.drawable.shishi);
        }else if (position < list.size() - 8 ){
            holder.img.setBackgroundResource(R.drawable.xingqu);
        }else{
            holder.img.setBackgroundResource(R.drawable.shishi);
        }
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context ,"" + position ,Toast.LENGTH_SHORT).show();
                scollToPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void scollToPosition(int n) {
        this.index = n ;
        //拿到当前屏幕可见的第一个position跟最后一个postion
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        //区分情况
        if (n <= firstItem ){
            //当要置顶的项在当前显示的第一个项的前面时
            recycleview.smoothScrollToPosition(n);
        }else if ( n <= lastItem ){
            //当要置顶的项已经在屏幕上显示时
            int top = recycleview.getChildAt(n - firstItem).getLeft();
            recycleview.smoothScrollBy(top,top);
        }else{
            //当要置顶的项在当前显示的最后一项的后面时
            recycleview.smoothScrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        ItemViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }
}
