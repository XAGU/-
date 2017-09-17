package com.xiaolian.amigo.ui.widget.pageloader;

import android.support.v7.widget.RecyclerView;

/**
 * 上拉加载控件
 * </p>
 * 现在只支持RecyclerView，并且LayoutManager必须为LinearLayoutManager
 *
 * @author zcd
 */
public abstract class PageLoader {
    public interface Callbacks {

        void onLoadMore();

        /**
         * 是否正在加载
         * @return true表示正在加载
         */
        boolean isLoading();

        /**
         * 是否已经加载所有数据
         * @return true表示已经加载完所有数据
         */
        boolean hasLoadedAll();
    }

    public abstract void setHasDataToLoad(boolean hasDataToLoad);

    public abstract void unbind();


    public static RecyclerPageLoader.Builder with(RecyclerView recyclerView, Callbacks callbacks) {
        return new RecyclerPageLoader.Builder(recyclerView, callbacks);
    }


}
