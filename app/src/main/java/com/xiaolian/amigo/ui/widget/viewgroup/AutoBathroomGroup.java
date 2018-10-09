package com.xiaolian.amigo.ui.widget.viewgroup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class AutoBathroomGroup extends ViewGroup {

    private List<BathBuildingRespDTO.FloorsBean> floorsBeanList;


    /**
     * 荧幕最小宽度
     */
    int defaultScreenWidth;

    /**
     * 标识是否正在缩放
     */
    boolean isScaling;
    float scaleX, scaleY;

    /**
     * 是否是第一次缩放
     */
    boolean firstScale = true;


    /**
     * 上下加一个margin
     */
    float marginTop;


    boolean isOnClick;


    private int downX, downY;
    private boolean pointer;


    /**
     * 描边
     */
    private float paintStroke = 1;
    /**
     * 房间最小宽高
     */
    private float BathroomMin = 129;

    /**
     * 房间最大宽高
     */
    private float bathroomMax = 42;

    /**
     * bathroom之间的间距
     */
    private float borderBathroom = 9;   //  最大为

    /**
     * 与屏幕的间距
     */
    private float borderScreenLeft = 63;

    /**
     * 每组之间的间距
     */
    private float borderGroups = 60;


    /**
     * 每组行之间的间距
     */
    private float borderGroupsHorziontal = 42;


    private float borderFloorHorizontal = 19;
    /**
     * 字体与上一个组的间距
     */
    private float borderGroupAndText = 42;

    /**
     * 每层之间的间距，这个是将浴室矩形和字体统称为一层，这个间距是指字体与下一个组之间的间距
     */
    private float borderfloor = 57;

    /**
     * 字体画笔
     */
    private TextPaint textPaint;


    private TextPaint hintPaint;

    /**
     * 屏幕宽度
     */
    private float screenWidth;

    /**
     * 屏幕高度
     */
    private float screentHeight;

    private WindowManager windowManager;

    private DisplayMetrics displayMetrics;

    /**
     * 正在使用
     */
    private int colorUsing = Color.RED;

    /**
     * 可以选择
     */
    private int colorCanUse = Color.WHITE;

    /**
     * 已预约房间
     */
    private int colorBooked = Color.BLACK;

    /**
     * 选中房间
     */
    private int colorChose = Color.GREEN;


    private int colorTextGroup = Color.BLACK;


    private int colorHint = Color.RED;

    /**
     * 可选的bathroomPaint
     */
    private Paint paintCanUse;

    /**
     * 使用中paint
     */
    private Paint paintUsing;

    /**
     * 选中Paint
     */
    private Paint paintChose;

    /**
     * 异常paint
     */
    private Paint paintBooked;

    /**
     * 真正的浴室场景图的宽高
     */
    float realWidth;
    float realHeight;


    float width;
    float height;
    float scaleFactor = 1.0f;
    Context context ;

    public AutoBathroomGroup(Context context) {
        this(context , null);
    }

    public AutoBathroomGroup(Context context, AttributeSet attrs) {
        this(context , attrs ,0);
    }

    public AutoBathroomGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.context = context ;
        marginTop = ScreenUtils.dpToPx(context ,21);
        BathroomMin = ScreenUtils.dpToPx(context ,30)  ;

        bathroomMax = ScreenUtils.dpToPx(context , 45) ;

        borderBathroom = ScreenUtils.dpToPx(context , 5) ;   //

        borderScreenLeft = ScreenUtils.dpToPxInt(context , 21) ;

        borderGroups = ScreenUtils.dpToPxInt(context ,30);

        // 层与层之间的距离
        borderFloorHorizontal = ScreenUtils.dpToPxInt(context ,40);

        borderGroupsHorziontal = ScreenUtils.dpToPxInt(context ,30) ;

        borderGroupAndText = ScreenUtils.dpToPxInt(context ,30) ;

        borderfloor = ScreenUtils.dpToPxInt(context ,40) ;

        paintStroke = 1;

        displayMetrics = new DisplayMetrics();



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

        realHeight = 0;
        realWidth = 0;

        float floorHeight;   //  每一行的高度
        float floorWidth;   //  每一行的宽度

        float cacheHeight = 0;  //  缓存的高度
        float cacheWidth = 0;  //  缓存的宽度

        // 是否是要加层与层之间的间距 ,如果是就表明换行了，要加上间距；否则不加
        boolean isWrap = true;
        if (floorsBeanList == null) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
            return;
        }


        //  每行的左边坐标
        float floorGroupLeft = 0;
        float groupLeft = 0;

        float maxGroupY = 0;  //  组的最高列

        float reallyFloorWidth = 0  ;  // 实际的显示一行的宽度
        for (int i = 0; i < floorsBeanList.size(); i++) {  // 每层
            BathBuildingRespDTO.FloorsBean floorsBean = floorsBeanList.get(i);
            if (floorsBean != null && floorsBean.getGroups().size() > 0) {
                // 如果不在同一行， 高度相加 ， 否则就为0
                if (isWrap) {
                    floorGroupLeft = 0;
                    reallyFloorWidth = 0;

                } else {
                    if (i == 0) {
                        floorGroupLeft = 0;
                    } else {
                        floorGroupLeft += (realWidth + (borderFloorHorizontal));
                    }
                }
//                floorsBean.setLeft(floorGroupLeft);
                floorWidth = 0;   //  这行的行宽
                floorHeight = 0;   //  这行的行高


                groupLeft = 0 ;
                for (int groupIndex = 0; groupIndex < floorsBean.getGroups().size(); groupIndex++) {// 每组
                    BathBuildingRespDTO.FloorsBean.GroupsBean groupsBean = floorsBean.getGroups().get(groupIndex);
                    groupsBean.setLeft(groupLeft);
                    float[] widthAndHeight = measureHeight(groupsBean);
                    // 每循环一个组 ，层的宽度 +  组的宽度， 若不是最后一个组，组的宽度就要再加上组的间距
                    if (groupIndex < (floorsBean.getGroups().size() - 1)) {
                        floorWidth += (widthAndHeight[0] + borderGroupsHorziontal);
                        groupLeft += (widthAndHeight[0] + borderGroupsHorziontal);
                    } else {
                        floorWidth += widthAndHeight[0];
                        groupLeft += widthAndHeight[0];
                    }

                    //  高度取最大者，将字体与浴室作为一个整体取值 ，  这层楼的最大值
                    floorHeight = Math.max(floorHeight, widthAndHeight[1]);

                    // 设置房间块的宽度
                    groupsBean.setRectWidth(widthAndHeight[2]);
                    maxGroupY = Math.max(maxGroupY, groupsBean.getMaxY());
                    // 设置组的真正宽度
                    groupsBean.setWidth(widthAndHeight[0]);

                    floorsBean.getGroups().set(groupIndex, groupsBean);

                }
                realWidth = Math.max(realWidth, floorWidth);
                realHeight += floorHeight ;
                floorsBean.setBottom(realHeight);
                if (i < floorsBeanList.size() - 1) {
                    realHeight += borderfloor;
                }
            }
        }

        setMeasuredDimension((int)realWidth , (int)realHeight);

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
     * 获取字体的矩形，计算字体的宽高
     *
     * @param groupsBean
     * @return
     */
    private Rect getTextWidth(BathBuildingRespDTO.FloorsBean.GroupsBean groupsBean) {
        Rect rect = new Rect();
        textPaint.getTextBounds(groupsBean.getDisplayName(), 0, groupsBean.getDisplayName().length(), rect);
        return rect;
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
            if (floorsBeanList == null && floorsBeanList.size() == 0){
                return ;
            }


    }

    // 指定margin   可以自定义margin
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext()  ,  attrs);
    }
}
