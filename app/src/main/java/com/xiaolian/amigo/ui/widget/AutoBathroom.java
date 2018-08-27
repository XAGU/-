package com.xiaolian.amigo.ui.widget;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
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
import com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomView;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.RxHelper;
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

    private Context context;

    private static final float MAXZ00M = 1.6f;


    private boolean isCanSelect = false ;

    private List<BathBuildingRespDTO.FloorsBean> floorsBeanList;

    ChooseBathroomPresenter<IChooseBathroomView> presenter;

    public void setData(List<BathBuildingRespDTO.FloorsBean> floorsBeans) {
        if (this.floorsBeanList != null && this.floorsBeanList.size() > 0) {
            this.floorsBeanList.clear();
        }
        if (this.floorsBeanList != null) {
            this.floorsBeanList.addAll(floorsBeans);
        } else {
            this.floorsBeanList = new ArrayList<>();
            this.floorsBeanList.addAll(floorsBeans);
        }
        isAddName = true;
        bathRoomsBean = null;
        if (bathRoomsBeanList != null) {
            bathRoomsBeanList.clear();
        }
        if (context != null) init(context);
        requestLayout();
        Log.e(TAG, "setData: >>>>>>>>>>>>>   invalidate" );
        invalidate();
//        Log.e(TAG, "setData: " + floorsBeanList.size());
    }

    public void setIsSelect(boolean isCanSelect){
        Log.e(TAG, "setIsSelect: " + isCanSelect );
        this.isCanSelect  = isCanSelect ;
    }


    public void setPresenter(ChooseBathroomPresenter<IChooseBathroomView> presenter) {
        this.presenter = presenter;
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
    /**
     * 带有每个浴室x, y 坐标的floorBeans ;
     */
    private List<BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean> updataFloorBeans;


    // check 选择的bathroomList ;
    private List<BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean> bathRoomsBeanList;


    private boolean isAddName = false;

    /**
     * 点击接口
     */
    private BathroomClick bathroomClick;


    float mDelWidth;
    float mDelHeight;


    private static BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean bathRoomsBean;

    public void setBathroomClick(BathroomClick bathroomClick) {
        this.bathroomClick = bathroomClick;
    }

    public AutoBathroom(Context context) {
        super(context);
    }

    public AutoBathroom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        init(context);
    }

    public AutoBathroom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
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


    List<BathBuildingRespDTO.FloorsBean> newFloorBeans = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        if (floorsBeanList == null || floorsBeanList.size() == 0){
            return ;
        }
        if (newFloorBeans != null) {
            newFloorBeans.clear();
        } else {
            newFloorBeans = new ArrayList<>();
        }
        measureBathroom(widthMeasureSpec, heightMeasureSpec);


    }

    float realMaxWidth ;

    private void measureBathroom(int widthMeasureSpec, int heightMeasureSpec) {
        //  缓存未设置bottom的楼层  和  left的组
        List<BathBuildingRespDTO.FloorsBean> cacheListfloorBeans = new ArrayList<>();

        // 缓存未设置最大组的楼层
        List<BathBuildingRespDTO.FloorsBean.GroupsBean> cacheGroupsBeans = new ArrayList<>();

        realHeight = 0;
        realWidth = 0;
        realMaxWidth = 0;

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
        }

    float  Animzoom  ;
    Paint strokePaint  ;
    Paint fillPaint ;
    private void init(Context context) {
        Animzoom = getMatrixScaleX();
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
        updataFloorBeans = new ArrayList<>();

        displayMetrics = new DisplayMetrics();
        bathRoomsBeanList = new ArrayList<>();


        //  颜色
        colorHint = context.getResources().getColor(R.color.colorHintBathroom);
        //  获取屏幕宽度
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screentHeight = displayMetrics.heightPixels;

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(colorTextGroup);
        textPaint.setTextSize(ScreenUtils.sp2px(context ,13));
        textPaint.setTextAlign(Paint.Align.CENTER);


        hintPaint = new TextPaint();
        hintPaint.setAntiAlias(true);
        hintPaint.setColor(Color.RED);
        hintPaint.setTextSize(ScreenUtils.sp2px(context ,13));
        hintPaint.setTextAlign(Paint.Align.CENTER);

        paintCanUse = new Paint();
        paintCanUse.setStyle(Paint.Style.FILL);
        paintCanUse.setAntiAlias(true);
        paintCanUse.setStrokeWidth(paintStroke);
        paintCanUse.setColor(colorCanUse);

        int clolorStroke = context.getResources().getColor(R.color.bathroom_stroke);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(paintStroke * zoom);
        strokePaint.setColor(clolorStroke);

        fillPaint= new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);
        fillPaint.setStrokeWidth(paintStroke * zoom);
        fillPaint.setColor(Color.WHITE);

        paintUsing = new Paint();
        paintUsing.setStyle(Paint.Style.FILL);
        paintUsing.setColor(colorUsing);

        colorBooked = context.getResources().getColor(R.color.colorBlue);
        paintBooked = new Paint();
        paintBooked.setStyle(Paint.Style.FILL);
        paintBooked.setStrokeWidth(paintStroke);
        paintBooked.setColor(colorBooked);

        colorChose = context.getResources().getColor(R.color.colorGreenBathroom);
        paintChose = new Paint();
        paintChose.setStyle(Paint.Style.FILL);
        paintChose.setColor(colorChose);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        presenter.isReferBathroom = false ;
        if (updataFloorBeans != null && updataFloorBeans.size() > 0){
            updataFloorBeans.clear();
            updataFloorBeans = new ArrayList<>();
        }

            mDelWidth = (getWidth() - realWidth ) / 2  ;
            mDelHeight = (getHeight() - realHeight ) / 2 ;

        drawSeat(canvas);
        presenter.isReferBathroom = true ;

        Log.e(TAG, "onDraw >>>>>>>>>>>>>>>: draw " );

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        int x = (int) event.getX();
        super.onTouchEvent(event);
        isScaling = false ;
        presenter.isReferBathroom = false ;
//        scaleGestureDetector.onTouchEvent(event);
        if (isCanSelect) {
            Log.e(TAG, "onTouchEvent: " + isCanSelect );
            gestureDetector.onTouchEvent(event);
        }
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
                if (!isScaling && !isOnClick ) {
                    int dx ;
                    int dy ;
                    int downDX = Math.abs(x - downX);
                    int downDY = Math.abs(y - downY);
                    if ((downDX > 10 || downDY > 10) && !pointer ) {

                        if (realWidth > getWidth()) {
                             dx = x - lastX;
                        }else {
                            dx = 0 ;
                        }

                        if (realHeight > getHeight()){
                              dy = y - lastY;
                        }else{
                            dy = 0 ;
                        }
//                        dx = x - lastX ;
//                        dy = y - lastY ;

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
                    Log.e(TAG, "onTouchEvent:>>>>>>>   move " );
                    autoScroll();
                }
                break;
        }
        isOnClick = false;
        presenter.isReferBathroom = true ;
        lastY = y;
        lastX = x;

        return true;
    }

    float drawTextDel;

    /**
     * 画房间框
     * @param canvas
     * @param left
     * @param top
     * @param
     */
    private void drawRect(Canvas canvas , float left , float top  , String text , BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean room , float scaleX){

        drawTextDel = ScreenUtils.dpToPx(context , 1);
        Path path = new Path();
        float radius = ScreenUtils.dpToPx(context ,3);
        room.setLeft(left);
        room.setTop(top);
        room.setRight((left + BathroomMin ));
        room.setBottom((top + BathroomMin ));
        if ( bathRoomsBean != null && room.getDeviceNo() == bathRoomsBean.getDeviceNo()){
            room.setStatus(bathRoomsBean.getStatus());
        }else{
            if (room.getStatus() == Constant.BATH_CHOSE){
                room.setStatus(AVAILABLE);
            }
        }
        if (updataFloorBeans.indexOf(room) == -1) {
            updataFloorBeans.add(room);
        }else{
            int  position = updataFloorBeans.indexOf(room);
            updataFloorBeans.set(position , room);
        }
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        switch (room.getStatus()){
            case BATH_USING:
                path.addRoundRect(new RectF(left , top ,left + BathroomMin  , top + BathroomMin ) , radius, radius , Path.Direction.CW);
                canvas.drawPath(path ,paintUsing);
//                canvas.drawPath(path ,paint);
                break;
            case AVAILABLE:
                path.addRoundRect(new RectF(left , top ,left + BathroomMin  , top + BathroomMin ) , radius, radius, Path.Direction.CW);
                canvas.drawPath(path ,fillPaint);
                canvas.drawPath(path ,strokePaint);

                break;
            case BATH_BOOKED:
                path.addRoundRect(new RectF(left , top ,left + BathroomMin  , top + BathroomMin ) , radius, radius  , Path.Direction.CW);
                canvas.drawPath(path ,paintBooked);
//                canvas.drawPath(path ,paint);
                break;
            case BATH_CHOSE:
                path.addRoundRect(new RectF(left , top ,left + BathroomMin  , top + BathroomMin ) , radius , radius  , Path.Direction.CW);
                canvas.drawPath(path ,paintChose);
//                canvas.drawPath(path ,paint);
                break;
            }


        if (scaleX > 0.9 ){
            if (room.getStatus() != BATH_BOOKED) {
                Paint.FontMetricsInt fontMetrics = hintPaint.getFontMetricsInt();
                Rect rect = new Rect();
                hintPaint.getTextBounds(text, 0, text.length(), rect);

                if (room.getStatus() == BATH_CHOSE) {
                    hintPaint.setColor(Color.WHITE);
                } else {
                    hintPaint.setColor(colorHint);
                }

                int baseline = (int) ((top - drawTextDel + (BathroomMin * scaleX - rect.height() / 2) * scaleX - ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top)));


                StaticLayout layout = new StaticLayout(text, hintPaint, (int) (BathroomMin * scaleX), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                // 这里的参数300，表示字符串的长度，当满300时，就会换行
                canvas.save();
                canvas.translate(left + BathroomMin / 2 * scaleX, baseline);
                layout.draw(canvas);
                canvas.restore();//别忘了restore
            }
//            canvas.drawText(text,left + BathroomMin / 2 * scaleX ,baseline , hintPaint);
        }

    }

    private float zoom;

    void drawSeat(Canvas canvas) {

        zoom = getMatrixScaleX();
        if (zoom < 1){
            zoom = 1 ;
        }
        int textSize = (int) (13 * zoom);
        if (textSize  < 12){
            textSize = 12 ;
        }else if (textSize  > 15){
            textSize = 15 ;
        }

        int hintSize = (int) (10* zoom);
        hintPaint.setTextSize(ScreenUtils.sp2px(context ,hintSize) );
        float translateX = getTranslateX();
        float translateY = getTranslateY();
        float scaleX = zoom;
        float scaleY = zoom;


        float  floorBathroomBootom  ;   //  每行的底部坐标

        if (floorsBeanList != null && floorsBeanList.size() > 0) {

            for (int i = 0; i <floorsBeanList.size() ; i ++) {

                //  每行
                BathBuildingRespDTO.FloorsBean floorsBean = floorsBeanList.get(i);

                // 底部坐标
                floorBathroomBootom = floorsBean.getBottom() * scaleY + translateY   ;
                //  画列
                for (int j = 0; j < floorsBean.getGroups().size(); j++) {

                    BathBuildingRespDTO.FloorsBean.GroupsBean groupsBean = floorsBean.getGroups().get(j);

                    //  这组的底部坐标 , 即文字的底部坐标
                    float groupBottom  = floorBathroomBootom ;

                    //  这组的左边坐标
                    float groupLeft = groupsBean.getLeft() + translateX   ;

                    // 房间块的底部坐标，  即为组的坐标 -  字体的间距
//                    float groupBathroomBottom = (groupBottom  - borderGroupAndText -getTextWidth(groupsBean).height()) * scaleY  ;
                      float groupBathroomBottom = groupBottom - getTextWidth(groupsBean).height() * scaleY - borderGroupAndText;
                            //每列浴室房间矩形的开头位置，组的宽度和浴室房间矩形宽度的差值。
                    float groupsBathroomLeft = groupLeft+ Math.abs((groupsBean.getWidth() - groupsBean.getRectWidth()) /2 );
//


                    for (int m = 0; m < groupsBean.getBathRooms().size(); m++) {

                        BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean roomsBean = groupsBean.getBathRooms().get(m);
                        if (isAddName) {
                            roomsBean.setRoomName(groupsBean.getDisplayName() + roomsBean.getName() + "号房间");
                        }
//                            drawRect(canvas, groupsBathroomLeft  + ( roomsBean.getXaxis() -1 ) * (BathroomMin + borderBathroom),
//                                    groupBathroomBottom  - ((groupsBean.getMaxY() - roomsBean.getYaxis()  + 1 ) * (BathroomMin) + (groupsBean.getMaxY() - roomsBean.getYaxis()) * borderBathroom )
//                                , roomsBean.getName() + "" , roomsBean , scaleX);

                        drawRect(canvas, groupsBathroomLeft + mDelWidth + ( roomsBean.getXaxis() -1 ) * (BathroomMin + borderBathroom) * scaleX,
                                                                 groupBathroomBottom  + mDelHeight- ((groupsBean.getMaxY() - roomsBean.getYaxis() + 1 ) * (BathroomMin) + (groupsBean.getMaxY() - roomsBean.getYaxis()) * borderBathroom ) * scaleY
                                , roomsBean.getName() + "" , roomsBean , scaleX);


                    }
                    drawGruopsName(canvas, groupLeft + mDelWidth , mDelHeight+groupBottom * scaleX, groupsBean.getDisplayName(), groupsBean.getWidth() );

                }
                //  每行的开头位置
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
            canvas.drawText(text,left + rectWidth / 2    ,baseline , textPaint);
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
        float currentSeatBitmapWidth = realWidth  ;
        float currentSeatBitmapHeight = realHeight  ;
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

            if (getTranslateX() < 0 && currentSeatBitmapWidth /2  + getTranslateX()   < getWidth() /2  + marginTop ) {
                moveXLength = (getWidth() /2   - getTranslateX() - currentSeatBitmapWidth /2 - marginTop );
            } else {
                //往左侧滑动
                if (getTranslateX() + getWidth() /2  > currentSeatBitmapWidth / 2 + marginTop ) {
                    moveXLength = -(getWidth() /2   + getTranslateX() - currentSeatBitmapWidth / 2 - marginTop);
                } else {
                    //右侧滑动
//                    moveXLength = -getTranslateX() ;
                }
            }

        }
          float startYPosition = 0 ;

        //处理上下滑动
        if (currentSeatBitmapHeight < getHeight()) {  //

            if (getTranslateY() < startYPosition) {
                moveYLength = startYPosition - getTranslateY();
            } else {
                moveYLength = -(getTranslateY() - (startYPosition));
            }

        } else {

            if (getTranslateY() < 0 && getTranslateY() + currentSeatBitmapHeight / 2 < getHeight() /2 + marginTop  ) {
                    moveYLength = getHeight() /2 - (currentSeatBitmapHeight /2 + getTranslateY()  + marginTop );
            } else {
                //往上滑动
                if (getTranslateY()    + getHeight() /2  > currentSeatBitmapHeight /2 + marginTop ) {
//                    moveYLength = getHeight() - (getTranslateY() + currentSeatBitmapHeight);
                    moveYLength = currentSeatBitmapHeight /2 - (getTranslateY() + getHeight() /2 ) + marginTop;
                } else {
//                    moveYLength = -(getTranslateY() - (startYPosition));
                }
            }
        }

        Point start = new Point();
        start.x = (int) getTranslateX();
        start.y = (int) getTranslateY();

        Point end = new Point();
        end.x = (int) (start.x + moveXLength);
        end.y = (int) (start.y + moveYLength);

//        moveAnimate(start, end);
        move(end);
    }

    private void autoScale() {

        if (getMatrixScaleX() > 1.6f) {
            zoomAnimate(getMatrixScaleX(), 1.6f);
        } else if (getMatrixScaleX() < 0.98f) {
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
        Log.e(TAG, "move:>>>>>>>>> invalidate " );
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

            matrix.postScale(scaleFactor, scaleFactor, getWidth() /2 , getHeight() / 2 );
            if (bathroomClick != null) bathroomClick.onScale(scaleFactor);
//            requestLayout();
            init(context);
            invalidate();
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

    //  点击时的坐标
    float x = width /2  ;
    float y  = height / 2 ;

    float lastPointX ;
    float lastPointY ;

    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e(TAG, "onSingleTapConfirmed: "  );
            boolean isTouchBathroom  = false ;
            isOnClick = true;
            lastPointX = x ;
            lastPointY = y ;
             x = (int) e.getX();
             y = (int) e.getY();
            if (bathRoomsBeanList != null && bathRoomsBeanList.size() > 0){
                bathRoomsBeanList.clear();
            }
            if (updataFloorBeans != null && updataFloorBeans.size() > 0) {
                for (BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean roomsBean : updataFloorBeans) {
                    if (x > roomsBean.getLeft() && x < roomsBean.getRight() &&
                            y > roomsBean.getTop() && y < roomsBean.getBottom()) {
                        isTouchBathroom = true ;
                        if (roomsBean.getStatus() == AVAILABLE) {
                            bathRoomsBean = roomsBean ;
                            roomsBean.setStatus(BATH_CHOSE);
                            if (bathroomClick != null) bathroomClick.BathroomClick( roomsBean);
                            bathRoomsBeanList.add(roomsBean);
                        } else if (roomsBean.getStatus() == BATH_CHOSE) {
                            bathRoomsBean = roomsBean;
                            roomsBean.setStatus(AVAILABLE);
                            if (bathroomClick != null) bathroomClick.BathroomClick(null);
                        }

                    }
                }
            }

            if (!isTouchBathroom){
                if (bathRoomsBean != null && bathRoomsBean.getStatus() == BATH_CHOSE){
                    bathRoomsBean.setStatus(AVAILABLE);
                }
                if (bathroomClick != null) bathroomClick.BathroomClick(null);
            }

            Log.e(TAG, "onSingleTapConfirmed:>>>>>>>>>>>> invalidate:"  );
            invalidate();

            return super.onSingleTapConfirmed(e);
        }
    });

    /**
     * 设置浴室房间选择为空
     */
    public void setBathroomAvable(){
        if (bathRoomsBean != null && bathRoomsBean.getStatus() == BATH_CHOSE){
            bathRoomsBean.setStatus(AVAILABLE);
        }
        if (bathroomClick != null) bathroomClick.BathroomClick(null);
    }


    public  interface  BathroomClick{
        void BathroomClick(BathBuildingRespDTO.FloorsBean.GroupsBean.BathRoomsBean bathRoomsBean);

        void onScale(float scale) ;
    }
}
