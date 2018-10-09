package com.xiaolian.amigo.ui.lostandfound.intf;

import android.content.Intent;

import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFoundDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.QueryLostAndFoundListRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

import java.util.List;

public interface IBlogView  extends IBaseView{

    void setReferComplete();

    void loadMore(QueryLostAndFoundListRespDTO data);

    void reducePage();

    void onErrorView();

    void onEmpty();

    void notifyAdapter(int position, boolean b);

    /**
     * 最新联子为空
     */
    void postEmpty();

    /**
     * 热门联子为空
     */
    void hostPostsEmpty();

    /**
     * 刷新最新联子
     * @param posts
     */
    void referPost(List<LostAndFoundDTO> posts);

    /**
     * 刷新热门联子
     * @param hotPosts
     */
    void referHotPost(List<LostAndFoundDTO> hotPosts);

    /**
     * 删除指定热门联子
     * @param currentHotPosition
     */
    void removeHotAdapter(int currentHotPosition);

    /**
     * 指定刷新热门联子
     * @param data
     * @param currentHotPosition
     */
    void notifyHotAdapter(Intent data ,int currentHotPosition) ;

    void removePotItem(int currentChoosePosition);

    void notifyPotAdapter(Intent data, int currentChoosePosition);
}
