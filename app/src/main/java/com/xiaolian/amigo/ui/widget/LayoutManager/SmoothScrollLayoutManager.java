package com.xiaolian.amigo.ui.widget.LayoutManager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

public class SmoothScrollLayoutManager extends LinearLayoutManager {
    public SmoothScrollLayoutManager(Context context) {
        super(context);
    }

    public SmoothScrollLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public SmoothScrollLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    @Override
//    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
//        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
//
//            @Override
//            public PointF computeScrollVectorForPosition(int targetPosition) {
//                return  SmoothScrollLayoutManager.this.computeScrollVectorForPosition(targetPosition);
//            }
//
//            //该方法控制速度。
//            //if returned value is 2 ms, it means scrolling 1000 pixels with LinearInterpolation should take 2 seconds.
//            @Override
//            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
//                /*
//                     控制单位速度,  毫秒/像素, 滑动1像素需要多少毫秒.
//
//                     默认为 (25F/densityDpi) 毫秒/像素
//
//                     mdpi上, 1英寸有160个像素点, 25/160,
//                     xxhdpi,1英寸有480个像素点, 25/480,
//                  */
//
//                //return 10F / displayMetrics.densityDpi;//可以减少时间，默认25F
//                return super.calculateSpeedPerPixel(displayMetrics);
//            }
//
//            //该方法计算滑动所需时间。在此处间接控制速度。
//            //Calculates the time it should take to scroll the given distance (in pixels)
//            @Override
//            protected int calculateTimeForScrolling(int dx) {
//               /*
//                   控制距离, 然后根据上面那个方(calculateSpeedPerPixel())提供的速度算出时间,
//
//                   默认一次 滚动 TARGET_SEEK_SCROLL_DISTANCE_PX = 10000个像素,
//
//                   在此处可以减少该值来达到减少滚动时间的目的.
//                */
//
//                //间接计算时提高速度，也可以直接在calculateSpeedPerPixel提高
//                if (dx > 3000) {
//                    dx = 3000;
//                }
//
//                int time = super.calculateTimeForScrolling(dx);
//
//
//                return time;
//            }
//        };
//
//        linearSmoothScroller.setTargetPosition(position);
//        startSmoothScroll(linearSmoothScroller);
//    }


}