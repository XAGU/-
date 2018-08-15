package com.xiaolian.amigo.ui.widget;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import static com.xiaolian.amigo.util.Constant.AVAILABLE;
import static com.xiaolian.amigo.util.Constant.BATH_BOOKED;
import static com.xiaolian.amigo.util.Constant.BATH_CHOSE;
import static com.xiaolian.amigo.util.Constant.BATH_USING;

/**
 * Created by baoyunlong on 16/6/16.
 */
public class AutoBathroom extends View {
    private static final String TAG = AutoBathroom.class.getSimpleName();

    private final boolean DBG = false;

    Paint paint = new Paint();

    private Context context ;

    private List<BathBuildingRespDTO.FloorsBean> floorsBeanList ;



    public void setData(List<BathBuildingRespDTO.FloorsBean> floorsBeanList ){
        this.floorsBeanList = floorsBeanList ;
        isAddName = true  ;
        requestLayout();
        invalidate();
//        float moveWidth  ;
//        float moveHeight ;
//        moveWidth = screenWidth > realWidth ? (screenWidth - realWidth) / 2 : (realWidth - screenWidth) / 2 ;
//        moveHeight = screenHeight > realHeight ? (screenHeight - realHeight) / 2 : (realHeight - screenHeight) / 2 ;
//        matrix.postTranslate(moveWidth , moveHeight);
    }


    Matrix matrix = new Matrix();


    int lastX;
    int lastY;

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
    float marginTop  ;


    boolean isOnClick;


    private int downX, downY;
    private boolean pointer;



    /**
     * 边框o
     */
    private float paintStroke = 1 ;
    /**
     * 房间最小宽高
     */
    private float BathroomMin = 129  ;

    /**
     * 房间最大宽高
     */
    private float bathroomMax = 42 ;

    /**
     * bathroom之间的间距
     */
    private float borderBathroom = 9 ;   //  最大为

    /**
     * 与屏幕的间距
     */
    private float borderScreenLeft = 63 ;

    /**
     * 每组之间的间距
     */
    private float borderGroups = 60 ;


    /**
     * 每组行之间的间距
     */
    private float borderGroupsHorziontal = 42 ;
    

    private float borderFloorHorizontal = 19  ;
    /**
     *  字体与上一个组的间距
     */
    private float borderGroupAndText = 42 ;

    /**
     * 每层之间的间距，这个是将浴室矩形和字体统称为一层，这个间距是指字体与下一个组之间的间距
     */
    private float borderfloor = 57 ;

    /**
     * 字体画笔
     */
    private TextPaint textPaint ;

    /**
     * 屏幕宽度
     */
    private float screenWidth ;

    /**
     * 屏幕高度
     */
    private float screentHeight ;

    private WindowManager windowManager ;

    private DisplayMetrics displayMetrics ;

    /**
     * 正在使用
     */
    private int colorUsing = Color.RED ;

    /**
     * 可以选择
     */
    private int colorCanUse =  Color.WHITE ;

    /**
     * 已预约房间
     */
    private int colorBooked = Color.BLACK ;

    /**
     * 选中房间
     */
    private int colorChose = Color.GREEN;

    /**
     * 可选的bathroomPaint
     */
    private Paint paintCanUse  ;

    /**
     * 使用中paint
     */
    private Paint paintUsing ;

    /**
     * 选中Paint
     */
    private Paint paintChose ;

    /**
     * 异常paint
     */
    private Paint paintBooked ;

    /**
     * 真正的浴室场景图的宽高
     */
    float realWidth  ;
    float realHeight ;


    float width  ;
    float height ;
    float scaleFactor = 1.0f ;
    /**
     * 带有每个浴室x, y 坐标的floorBeans ;
     */
    private List<BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean> updataFloorBeans ;


    // check 选择的bathroomList ;
    private List<BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean> bathRoomsBeanList ;


    private boolean isAddName  = false ;

    /**
     * 点击接口
     */
    private BathroomClick bathroomClick ;

    private static BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean  bathRoomsBean ;

    public void setBathroomClick(BathroomClick bathroomClick) {
        this.bathroomClick = bathroomClick;
    }

    public AutoBathroom(Context context) {
        super(context);
    }

    public AutoBathroom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        init(context);
    }

    public AutoBathroom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }



    /**
     * 获取每组宽高 浴室的布置为  /  1,1  1,2  1,3
     *                            2,1  2,2  2,3
     *                            3,1  3,2  3,3 /的排列，所以先计算有多少行和列，计算宽度就为行 *  房间的width，
     *                            计算出的结果再与字体大宽度进行比较，取较大值； 高度为 列 * 房间的高度+ 字体的高度
     *
     * @param groupsBean
     * @return  int[0] 每组的宽度
     *          int[1]  每组的高度
     *          int[2]  每组的浴室房间的矩形的宽度
     */
    private float[] measureHeight(BathBuildingRespDTO.FloorsBean.GroupsBean groupsBean){
        float[] widthAndHeight = new float[3];
        int rows = 0  ;   // 行数
        int lows = 0 ;    // 列数
        for (BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean bathRoomsBean : groupsBean.getBathRooms()){
            rows = Math.max(rows ,bathRoomsBean.getXaxis());
            lows = Math.max(lows ,bathRoomsBean.getYaxis());
        }
        widthAndHeight[0] = (BathroomMin * lows + borderBathroom * (lows -1))    ;    // 宽  即为房间块形的宽+ 块之间间隔的宽  + 组与组之间的宽度
        widthAndHeight[1] = BathroomMin * rows + borderBathroom * (rows -1 ) ;   // 高
        widthAndHeight[2] = widthAndHeight[0];

        //  宽高度为   浴室的宽度与字体的宽度比较，取两者间最大者
        widthAndHeight[0] = Math.max(widthAndHeight[0] , getTextWidth(groupsBean).width());

        //  行高为   浴室的行高+ 字体的高度 + 字体与上一层的高度
        widthAndHeight[1] = widthAndHeight[1] + getTextWidth(groupsBean).height()+ borderGroupAndText ;

        return widthAndHeight ;
    }


    /**
     * 获取字体的矩形，计算字体的宽高
     * @param groupsBean
     * @return
     */
    private Rect getTextWidth (BathBuildingRespDTO.FloorsBean.GroupsBean groupsBean){
        Rect rect = new Rect();
        textPaint.getTextBounds(groupsBean.getDisplayName() ,0 ,groupsBean.getDisplayName().length() ,rect);
        return  rect;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

         width = MeasureSpec.getSize(widthMeasureSpec);
         height = MeasureSpec.getSize(heightMeasureSpec);



          realHeight = 0 ;
          realWidth = 0 ;
        float floorHeight  ;   //  每一行的高度
        float floorWidth  ;   //  每一行的宽度
        if (floorsBeanList == null){
            setMeasuredDimension(widthMeasureSpec , heightMeasureSpec);
            return;
        }
        /**
         * 浴室场景图为 浴室房间， 组， 楼层 的结构， 一组有多个房间， 一层有多个组
         * 计算view 的大小为， 先计算组的宽高不包括字体大高度， 然后再每个组的高度相加再加上间隔就为view的高度
         * view 的宽度为一行每组的宽度，按最大的宽度计算
         */
       for (BathBuildingRespDTO.FloorsBean floorsBean : floorsBeanList){  // 每层

            floorWidth = 0 ;
            floorHeight = 0 ;
            for (BathBuildingRespDTO.FloorsBean.GroupsBean groupsBean : floorsBean.getGroups()){   // 每组
                float[] widthAndHeight = measureHeight(groupsBean);
                //  组宽度相加 , 再加上宽度
                floorWidth += widthAndHeight[0] + borderGroupsHorziontal;

                //  高度取最大者，将字体与浴室作为一个整体取值
                floorHeight = Math.max(floorHeight ,widthAndHeight[1]);

                // 设置浴室组的宽度
                groupsBean.setRectWidth(widthAndHeight[2]);

                // 设置组的真正宽度
                groupsBean.setWidth(widthAndHeight[0]);
            }
            realHeight += floorHeight ;
            //  宽度为行最大值  ,  因为多加了一个行距  ，所以要减一个比较 ， 去所有行宽中的最大值
            realWidth = Math.max(realWidth ,floorWidth);

            //  设置的是没一行的top坐标
            floorsBean.setHeight(floorHeight);


        }


        //  高度为所有组的高度 + 两组之间的间距
        realHeight = realHeight  + (floorsBeanList.size() -1) * borderfloor ;


        //  加上间距
        realWidth  += 2 * marginTop ;
        realHeight += 2 * marginTop ;

//        setMeasuredDimension(realWidth , realHeight);
        Log.e(TAG, "onMeasure: " + scaleFactor  + "   " + getMatrixScaleX()  + "  " + getMatrixScaleX());
//        setMeasuredDimension((int)(realWidth * getMatrixScaleX() ), (int)(realHeight * getMatrixScaleX()));

    }

    private void init(Context context) {
        this.context = context ;
        marginTop = ScreenUtils.dpToPx(context ,10);
        BathroomMin = ScreenUtils.dpToPx(context ,15)  ;


        bathroomMax = ScreenUtils.dpToPx(context , 45) ;

        borderBathroom = ScreenUtils.dpToPx(context , 9) ;   //  最大为

        borderScreenLeft = ScreenUtils.dpToPxInt(context , 21) ;

        borderGroups = ScreenUtils.dpToPxInt(context ,15);

        // 层与层之间的距离
        borderFloorHorizontal = ScreenUtils.dpToPxInt(context ,19);

        borderGroupsHorziontal = ScreenUtils.dpToPxInt(context ,15) ;

        borderGroupAndText = ScreenUtils.dpToPxInt(context ,15) ;

        borderfloor = ScreenUtils.dpToPxInt(context ,18) ;


        updataFloorBeans = new ArrayList<>();

        displayMetrics = new DisplayMetrics();
        bathRoomsBeanList = new ArrayList<>();


        //  获取屏幕宽度
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screentHeight = displayMetrics.heightPixels;

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(ScreenUtils.sp2px(context ,10));
        textPaint.setTextAlign(Paint.Align.CENTER);

//        colorCanUse = context.getResources().getColor(R.color.color_bathroom_can_use);
        paintCanUse = new Paint();
        paintCanUse.setStyle(Paint.Style.FILL_AND_STROKE);
        paintCanUse.setAntiAlias(true);
        paintCanUse.setStrokeWidth(paintStroke);
        paintCanUse.setColor(colorCanUse);

        paintUsing = new Paint();
        paintUsing.setStyle(Paint.Style.FILL);
        paintUsing.setColor(colorUsing);

        colorBooked = context.getResources().getColor(R.color.colorBlue);
        paintBooked = new Paint();
        paintBooked.setStyle(Paint.Style.FILL);
        paintBooked.setStrokeWidth(paintStroke);
        paintBooked.setColor(colorBooked);
        paintChose = new Paint();
        paintChose.setStyle(Paint.Style.FILL);
        paintChose.setColor(colorChose);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (updataFloorBeans != null && updataFloorBeans.size() > 0){
            updataFloorBeans.clear();
            updataFloorBeans = new ArrayList<>();
        }
        drawSeat(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        int x = (int) event.getX();
        super.onTouchEvent(event);

        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        int pointerCount = event.getPointerCount();
        if (pointerCount > 1) {
            pointer = true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointer = false;
                downX = x;
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isScaling && !isOnClick) {
                    int downDX = Math.abs(x - downX);
                    int downDY = Math.abs(y - downY);
                    if ((downDX > 10 || downDY > 10) && !pointer) {
                        int dx = x - lastX;
                        int dy = y - lastY;
                        matrix.postTranslate(dx, dy);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

//                autoScale();
                int downDX = Math.abs(x - downX);
                int downDY = Math.abs(y - downY);
                if ((downDX > 10 || downDY > 10) && !pointer) {
//                    autoScroll();
                }

                break;
        }
        isOnClick = false;
        lastY = y;
        lastX = x;

        return true;
    }



    /**
     * 画房间框
     * @param canvas
     * @param left
     * @param top
     * @param
     */
    private void drawRect(Canvas canvas , float left , float top  , String text , BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean room , float scaleX){
        Path path = new Path();
        float radius = ScreenUtils.dpToPx(context ,3);
        switch (room.getStatus()){
            case BATH_USING:
                path.addRoundRect(new RectF(left , top ,left + BathroomMin * scaleX , top + BathroomMin* scaleX ) , radius, radius , Path.Direction.CW);
                canvas.drawPath(path ,paintUsing);
                break;
            case AVAILABLE:
                path.addRoundRect(new RectF(left , top ,left + BathroomMin * scaleX , top + BathroomMin* scaleX ) , radius, radius, Path.Direction.CW);
                canvas.drawPath(path ,paintCanUse);
                break;
            case BATH_BOOKED:
                path.addRoundRect(new RectF(left , top ,left + BathroomMin * scaleX , top + BathroomMin* scaleX ) , radius, radius  , Path.Direction.CW);
                canvas.drawPath(path ,paintBooked);
                break;
            case BATH_CHOSE:
                path.addRoundRect(new RectF(left , top ,left + BathroomMin * scaleX , top + BathroomMin* scaleX ) , radius , radius  , Path.Direction.CW);
                canvas.drawPath(path ,paintChose);
                break;
            }
        room.setLeft(left);
        room.setTop(top);
        room.setRight((left + BathroomMin * scaleX));
        room.setBottom((top + BathroomMin * scaleX));
        if ( bathRoomsBean != null && room.getDeviceNo() == bathRoomsBean.getDeviceNo()){
            room.setStatus(Constant.BATH_CHOSE);
            Log.e(TAG, "drawRect: "  + bathRoomsBean.getDeviceNo() );
        }
        if (updataFloorBeans.indexOf(room) == -1) {
            updataFloorBeans.add(room);
        }


//        if (isShowRoomId && status == STATUS_CANUSE){
//            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
//            int baseline = (int) ((top + BathroomMin - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
//            canvas.drawText(text,left + BathroomMin / 2 ,baseline , textPaint);
//        }

    }

    private float zoom;

    void drawSeat(Canvas canvas) {
        zoom = getMatrixScaleX();
        if (zoom < 1){
            zoom = 1 ;
        }
        long startTime = System.currentTimeMillis();
        float translateX = getTranslateX();
        float translateY = getTranslateY();
        float scaleX = zoom;
        float scaleY = zoom;


        if (floorsBeanList != null && floorsBeanList.size() > 0) {
            float baseWidth = translateX  + marginTop;
            float baseHeight = translateY + marginTop ;   //  上边没有间距

            for (int i = 0; i <floorsBeanList.size() ; i ++) {

                // 每行浴室的开头位置，为固定值
                float baseBathroomRectWidth = baseWidth ;
                //  每行
                BathBuildingRespDTO.FloorsBean floorsBean = floorsBeanList.get(i);
                //  画列
                for (int j = 0; j < floorsBean.getGroups().size(); j++) {
                    BathBuildingRespDTO.FloorsBean.GroupsBean groupsBean = floorsBean.getGroups().get(j);

                    //每列浴室房间矩形的开头位置，组的宽度和浴室房间矩形宽度的差值。
                    baseBathroomRectWidth = baseWidth +  Math.abs(groupsBean.getWidth() / 2  - groupsBean.getRectWidth() / 2) * scaleX;

                    for (int m = 0; m < groupsBean.getBathRooms().size(); m++) {

                        BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean roomsBean = groupsBean.getBathRooms().get(m);
                        if (isAddName) {
                            roomsBean.setName(groupsBean.getDisplayName() + roomsBean.getName() + "房浴室");
                        }
                            drawRect(canvas, baseBathroomRectWidth + (roomsBean.getXaxis() - 1) * (BathroomMin + borderBathroom) * scaleX,
                                baseHeight + (roomsBean.getYaxis() - 1) * (BathroomMin + borderBathroom) * scaleY
                                , roomsBean.getId() + "" , roomsBean , scaleX);


                    }
                    Log.e(TAG, "drawSeat: " + groupsBean.getWidth() * scaleX );
                    drawGruopsName(canvas, baseWidth , baseHeight + floorsBean.getHeight() * scaleY, groupsBean.getDisplayName(), groupsBean.getWidth() * scaleX);
                    //  每列的开头位置
//                    baseWidth += (groupsBean.getWidth() + borderGroups ) * scaleX ;

                    // 浴室矩形的开头
                    baseWidth +=(( groupsBean.getWidth() + borderGroups ) * scaleX);

                }
                //  每行的开头位置

                if (i != floorsBeanList.size() - 1) {
                    baseHeight += (floorsBean.getHeight() + borderfloor) * scaleY;
                }
                baseWidth = translateX ;
            }

            }

            if (bathRoomsBeanList!= null && bathRoomsBeanList.size() > 0){
                Log.e(TAG, "drawSeat: " + bathRoomsBeanList.size() );
                for (BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean bathRoomsBeen : bathRoomsBeanList) {
                    drawRect(canvas, bathRoomsBeen.getLeft(), bathRoomsBeen.getTop(), bathRoomsBeen.getDeviceNo() + "", bathRoomsBeen, scaleX);
                }
            }
        isAddName = false ;

    }


        /**
         * 画每组的名称
         * @param canvas
         * @param left
         * @param top
         * @param text
         */
        private void drawGruopsName(Canvas canvas , float left , float top , String text , float rectWidth){
            Rect rect = new Rect();
            textPaint.getTextBounds(text ,0 , text.length() , rect);
            int baseline = (int) top - (rect.bottom  - rect.top) /2 ;
            canvas.drawText(text,left + rectWidth / 2   ,baseline , textPaint);
        }




    /**
     * 自动回弹
     * 整个大小不超过控件大小的时候:
     * 往左边滑动,自动回弹到行号右边
     * 往右边滑动,自动回弹到右边
     * 往上,下滑动,自动回弹到顶部
     * <p>
     * 整个大小超过控件大小的时候:
     * 往左侧滑动,回弹到最右边,往右侧滑回弹到最左边
     * 往上滑动,回弹到底部,往下滑动回弹到顶部
     */
    private void autoScroll() {
        float currentSeatBitmapWidth = realWidth * scaleX;
        float currentSeatBitmapHeight = realHeight * scaleY;
        float moveYLength = 0;
        float moveXLength = 0;



        //处理左右滑动的情况
        if (currentSeatBitmapWidth < getWidth()) {
            if (getTranslateX() + currentSeatBitmapWidth < getWidth()) {
                if (getTranslateX() < 0 || getMatrixScaleX() < 0) {
                    //计算要移动的距离
                    if (getTranslateX() < 0) {
                        moveXLength = (-getTranslateX());
                    } else {
                        moveXLength = -getTranslateX();
                    }

                }
            }else{
                moveXLength = -(getTranslateX() + currentSeatBitmapWidth - getWidth());
            }
        } else {

            if (getTranslateX() < 0 && getTranslateX() + currentSeatBitmapWidth > getWidth()) {

            } else {
                //往左侧滑动
                if (getTranslateX() + currentSeatBitmapWidth < getWidth()) {
                    moveXLength = getWidth() - (getTranslateX() + currentSeatBitmapWidth);
                } else {
                    //右侧滑动
                    moveXLength = -getTranslateX() ;
                }
            }

        }
          float startYPosition = marginTop ;

        //处理上下滑动
        if (currentSeatBitmapHeight < getHeight()) {  //

            if (getTranslateY() < startYPosition) {
                moveYLength = startYPosition - getTranslateY();
            } else {
                moveYLength = -(getTranslateY() - (startYPosition));
            }

        } else {

            if (getTranslateY() < 0 && getTranslateY() + currentSeatBitmapHeight > getHeight()) {

            } else {
                //往上滑动
                if (getTranslateY() + currentSeatBitmapHeight < getHeight()) {
                    moveYLength = getHeight() - (getTranslateY() + currentSeatBitmapHeight);
                } else {
                    moveYLength = -(getTranslateY() - (startYPosition));
                }
            }
        }

        Point start = new Point();
        start.x = (int) getTranslateX();
        start.y = (int) getTranslateY();

        Point end = new Point();
        end.x = (int) (start.x + moveXLength);
        end.y = (int) (start.y + moveYLength);

        moveAnimate(start, end);

    }

    private void autoScale() {

        if (getMatrixScaleX() > 2.2) {
            zoomAnimate(getMatrixScaleX(), 2.0f);
        } else if (getMatrixScaleX() < 0.98) {
            zoomAnimate(getMatrixScaleX(), 1.0f);
        }
    }


    float[] m = new float[9];

    private float getTranslateX() {
        matrix.getValues(m);
        return m[2];
    }

    private float getTranslateY() {
        matrix.getValues(m);
        return m[5];
    }

    private float getMatrixScaleY() {
        matrix.getValues(m);
        return m[4];
    }

    private float getMatrixScaleX() {
        matrix.getValues(m);
        return m[Matrix.MSCALE_X];
    }

    private float dip2Px(float value) {
        return getResources().getDisplayMetrics().density * value;
    }


    private void moveAnimate(Point start, Point end) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new MoveEvaluator(), start, end);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        MoveAnimation moveAnimation = new MoveAnimation();
        valueAnimator.addUpdateListener(moveAnimation);
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }

    private void zoomAnimate(float cur, float tar) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(cur, tar);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        ZoomAnimation zoomAnim = new ZoomAnimation();
        valueAnimator.addUpdateListener(zoomAnim);
        valueAnimator.addListener(zoomAnim);
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }


    private void zoom(float zoom) {
        float z = zoom / getMatrixScaleX();
        matrix.postScale(z, z, scaleX, scaleY);
        invalidate();
    }

    private void move(Point p) {
        float x = p.x - getTranslateX();
        float y = p.y - getTranslateY();
        matrix.postTranslate(x, y);
        invalidate();
    }

    class MoveAnimation implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Point p = (Point) animation.getAnimatedValue();

            move(p);
        }
    }

    class MoveEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            int x = (int) (startPoint.x + fraction * (endPoint.x - startPoint.x));
            int y = (int) (startPoint.y + fraction * (endPoint.y - startPoint.y));
            return new Point(x, y);
        }
    }

    class ZoomAnimation implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            zoom = (Float) animation.getAnimatedValue();
            zoom(zoom);

            if (DBG) {
                Log.d("zoomTest", "zoom:" + zoom);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

    }


    ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            isScaling = true;
            scaleFactor = detector.getScaleFactor();
            if (getMatrixScaleY() * scaleFactor > 3) {
                scaleFactor = 3 / getMatrixScaleY();
            }
            if (firstScale) {
                scaleX = detector.getCurrentSpanX();
                scaleY = detector.getCurrentSpanY();
                firstScale = false;
            }

            if (getMatrixScaleY() * scaleFactor < 0.5) {
                scaleFactor = 0.5f / getMatrixScaleY();
            }

            matrix.postScale(scaleFactor, scaleFactor, scaleX, scaleY);
            if (bathroomClick != null) bathroomClick.onScale(scaleFactor);
            invalidate();
            requestLayout();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            isScaling = false;
            firstScale = true;
        }
    });


    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            isOnClick = true;
            int x = (int) e.getX();
            int y = (int) e.getY();
            Log.e(TAG, "onSingleTapConfirmed: " + x + "   " + y );
            if (bathRoomsBeanList != null && bathRoomsBeanList.size() > 0){
                bathRoomsBeanList.clear();
            }
            if (updataFloorBeans != null && updataFloorBeans.size() > 0) {
                for (BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean roomsBean : updataFloorBeans) {
                    if (x > roomsBean.getLeft() && x < roomsBean.getRight() &&
                            y > roomsBean.getTop() && y < roomsBean.getBottom()) {
                        if (roomsBean.getStatus() == AVAILABLE) {
                            bathRoomsBean = roomsBean ;
                            roomsBean.setStatus(BATH_CHOSE);
                            if (bathroomClick != null) bathroomClick.BathroomClick( roomsBean);
                            bathRoomsBeanList.add(roomsBean);
                        } else if (roomsBean.getStatus() == BATH_CHOSE) {
                            bathRoomsBean = null ;
                            roomsBean.setStatus(AVAILABLE);
                        }
                    }
                }
            }

            invalidate();

            return super.onSingleTapConfirmed(e);
        }
    });



    public  interface  BathroomClick{
        void BathroomClick(BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean bathRoomsBean);

        void onScale(float scale) ;
    }
}
