package com.xiaolian.amigo.ui.widget.viewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;

import java.util.ArrayList;
import java.util.List;

public class AutoBathroomGroup extends ViewGroup {

    private List<BathBuildingRespDTO.FloorsBean> floorsBeanList;



    public AutoBathroomGroup(Context context) {
        super(context);
    }

    public AutoBathroomGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoBathroomGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 设置数据
    public void setData(List<BathBuildingRespDTO.FloorsBean> floorsBeans){
        if (this.floorsBeanList != null) {
            this.floorsBeanList.addAll(floorsBeans);
        } else {
            this.floorsBeanList = new ArrayList<>();
            this.floorsBeanList.addAll(floorsBeans);
        }
    }



    /**
     *   测量整个viewGroup 大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (floorsBeanList== null && floorsBeanList.size() ==0){
            super.onMeasure(widthMeasureSpec , heightMeasureSpec);
            return  ;
        }



    }



    /**
     * 获取每组宽高 浴室的布置为  /  1,1  1,2  1,3
     * 2,1  2,2  2,3
     * 3,1  3,2  3,3 /的排列，所以先计算有多少行和列，计算宽度就为行 *  房间的width，
     * 计算出的结果再与字体大宽度进行比较，取较大值； 高度为 列 * 房间的高度+ 字体的高度
     *
     * @param groupsBean
     * @return int[0] 每组的宽度
     * int[1]  每组的高度
     * int[2]  每组的浴室房间的矩形的宽度
     */
    private float[] measureHeight(BathBuildingRespDTO.FloorsBean.GroupsBean groupsBean) {
        float[] widthAndHeight = new float[3];
        int rows = 0;   // 行数
        int lows = 0;    // 列数
        for (BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean bathRoomsBean : groupsBean.getBathRooms()) {
            rows = Math.max(rows, bathRoomsBean.getXaxis());
            lows = Math.max(lows, bathRoomsBean.getYaxis());
        }
        groupsBean.setMaxX(rows);
        groupsBean.setMaxY(lows);
        widthAndHeight[0] = (BathroomMin * rows + borderBathroom * (rows - 1));    // 宽  即为房间块形的宽+ 块之间间隔的宽 , 即组中房间的宽度
        widthAndHeight[1] = BathroomMin * lows + borderBathroom * (lows - 1);   // 高
        widthAndHeight[2] = widthAndHeight[0];


        //  宽度为   浴室的宽度与字体的宽度比较，取两者间最大者
        widthAndHeight[0] = Math.max(widthAndHeight[0], getTextWidth(groupsBean).width());

        //  行高为   浴室的行高+ 字体的高度 + 字体与上一层的高度
        widthAndHeight[1] = widthAndHeight[1] + getTextWidth(groupsBean).height() + borderGroupAndText;
        return widthAndHeight;
    }

    /**
     * layout 进行定位
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    // 指定margin   可以自定义margin
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext()  ,  attrs);
    }
}
