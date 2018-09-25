package com.xiaolian.amigo.ui.widget.photoview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.model.CustomImageSizeModelImp;
import com.xiaolian.amigo.util.ImageHelper;

import java.text.NumberFormat;
import java.util.List;

/**
 * 图片浏览器-滑动页
 * <p>
 * Created by zcd on 9/22/17.
 */

public class AlbumViewPager extends ViewPager {
    public interface OnSingleTapListener {
        void onSingleTap();
    }
    public OnSingleTapListener listener;
    public final static String TAG = "AlbumViewPager";
    public NumberFormat percentFormat;

    public AlbumViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        percentFormat = NumberFormat.getPercentInstance();
    }

    public void setOnSingleTapListener(OnSingleTapListener listener) {
        this.listener = listener;
    }


    /**
     * 删除当前项
     *
     * @return “当前位置/总数量”
     */
    public String deleteCurrentPath() {
        return ((ViewPagerAdapter) getAdapter()).deleteCurrentItem(getCurrentItem());

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        try {
            return super.onInterceptTouchEvent(arg0);
        } catch (Exception ex) {
        }
        return false;
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private List<String> paths;//大图地址 如果为网络图片 则为大图url

        public ViewPagerAdapter(List<String> paths) {
            this.paths = paths;
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int position) {
            //注意，这里不可以加inflate的时候直接添加到viewGroup下，而需要用addView重新添加
            //因为直接加到viewGroup下会导致返回的view为viewGroup
            View imageLayout = inflate(getContext(), R.layout.item_album_pager, null);

            View loading = imageLayout.findViewById(R.id.progressBar);
            viewGroup.addView(imageLayout);
            assert imageLayout != null;
            PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.matrix_imageview);
            imageView.setOnViewTapListener((View view, float x, float y) -> {
                if (listener != null) {
                    listener.onSingleTap();
                }
            });
            final TextView mTvProgress = (TextView) imageLayout.findViewById(R.id.album_textview_progress);
            String path = paths.get(position);
            imageLayout.setTag(path);

            ImageHelper.loadAlbum(getContext(), imageView, path);
            ImageHelper.displayImage(getContext() ,new CustomImageSizeModelImp(path) ,imageView ,loading);
            /*mTvProgress.setText(FileUtils.FormetFileSize(current)+
					"/"+
					FileUtils.FormetFileSize(total)+
					"("+
					percentFormat.format((double)((current*1.0f)/(total*1.0f)))+
					")"
					);*/
            return imageLayout;
        }


        @Override
        public int getItemPosition(Object object) {
            //在notifyDataSetChanged时返回None，重新绘制
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int arg1, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        //自定义获取当前view方法
        public String deleteCurrentItem(int position) {
            String path = paths.get(position);
            if (path != null) {
                //FileOperateUtil.deleteSourceFile(path, getContext());
                paths.remove(path);
                notifyDataSetChanged();
                if (paths.size() > 0)
                    return (getCurrentItem() + 1) + "/" + paths.size();
                else {
                    return "0/0";
                }
            }
            return null;
        }
    }


}