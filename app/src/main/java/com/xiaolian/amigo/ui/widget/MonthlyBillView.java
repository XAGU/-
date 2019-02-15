package com.xiaolian.amigo.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xiaolian.amigo.util.ScreenUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/6/1
 */
public class MonthlyBillView extends View {
    private Context mContext;
    private Paint textPaint;
    private Paint arcPaint;
    private Paint linePaint;

    private WeakReference<Bitmap> bitmapBuffer;
    private Canvas bitmapCanvas;

    private float distance;
    private float radius;

    // 饼图的宽高
    private int barWidth,barHeight;
    private List<ViewData> datas;
    private List<AngleSE> angleSEs;
    private List<RectF> lengedRectes;
    private List<RectF> descRectes;
    private OnSelectedListener mListener;

    private float totalNum;

    private int lengedHeight = 0;
    private boolean isLengedVisible = false;
    private int initAngle = -60;

    private boolean hasData = true;

    public MonthlyBillView(Context context) {
        super(context);
        init(context);
    }

    public MonthlyBillView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MonthlyBillView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        descRectes = new ArrayList<>();
        mContext = context;

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(ScreenUtils.dpToPxInt(context,11));

        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setTextSize(radius);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.DKGRAY);
        linePaint .setTextSize(3);

        distance = ScreenUtils.dpToPxInt(context,16);

        setRadius(ScreenUtils.dpToPxInt(context,68));
    }

    public void setRadius(float rs){
        this.radius = rs;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public void setData(List<ViewData> datas) {
        this.datas = datas;
        this.totalNum = 0;
        for (ViewData data : datas) {
            totalNum += data.getNumber();
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if (!hasData) {
                return super.onTouchEvent(event);
            }
            float actionX = event.getX(); //点击点的坐标
            float actionY = event.getY();
            double distance = Math.sqrt(Math.pow(Math.abs(actionX-barWidth/2),2)+
                    Math.pow(Math.abs(actionY-barHeight/2),2));
//            double angle = Math.atan((actionY-barHeight/2) /(actionX-barWidth/2)) /3.14 * 180 + initAngle;
//            float X = barWidth/2,Y=(barHeight-lengedHeight)/2;
//            if(actionX > X && actionY<Y){
//                angle = 90-angle;
//            } else if (actionX > X && actionY>Y) {
//                angle = 90+angle;
//            }else if (actionX < X && actionY>Y) {
//                angle = 270-angle;
//            }else if (actionX < X && actionY<Y) {
//                angle = 270+angle;
//            }

            double angle = Math.atan((actionY-barHeight/2) /(actionX-barWidth/2)) /3.14 * 180;
            float X = barWidth/2,Y=(barHeight-lengedHeight)/2;
            if(actionX > X && actionY<Y){
                if (angle < initAngle) {
                    angle = angle + 360;
                }
            } else if (actionX > X && actionY>Y) {
                // go nothing
            }else if (actionX < X && actionY>Y) {
                angle = 180 + angle;
            }else if (actionX < X && actionY<Y) {
                angle = 180 + angle;
            }

            if(angleSEs == null || angleSEs.size() == 0 || mListener == null) return false;

            for(int i=0;i<angleSEs.size();i++){
                if(distance <= radius){
                    if(angle > angleSEs.get(i).getStartAngle() && angle<angleSEs.get(i).getSweepAngle()){
                        mListener.onSelected(i); //当点击点在圆内且在扇形上时，触发监听事件
                    }
                }
//                else if(lengedRectes.get(i).contains(actionX,actionY)){
//                    mListener.onSelected(i); //当点击点在描述文字上时，触发监听事件(此处是当饼图部分太小，无法点击时的补充)
//                }
                else if(descRectes != null
                        && !descRectes.isEmpty()
                        && descRectes.get(i).contains(actionX,actionY)){
                    mListener.onSelected(i); //当点击点在描述文字上时，触发监听事件(此处是当饼图部分太小，无法点击时的补充)
                }
            }
            return false;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int Width = onWidthMeasure(widthMeasureSpec); //计算控件的宽度
        int height = onHeightMeasure(heightMeasureSpec);//计算控件的高度
        setMeasuredDimension(Width,height);
    }

    private int onWidthMeasure(int width){
        int mode = MeasureSpec.getMode(width);
        int size = MeasureSpec.getSize(width);

        if(mode == MeasureSpec.EXACTLY){
            barWidth = size;
        }else if(mode == MeasureSpec.AT_MOST){
            barWidth = width - getPaddingLeft() - getPaddingRight();
        }
        return barWidth;
    }


    private int onHeightMeasure(int height){
        int mode1 = MeasureSpec.getMode(height);
        int size1 = MeasureSpec.getSize(height);
        int minSize = (int) ScreenUtils.dpToPxInt(mContext,120);
        if(mode1 == MeasureSpec.EXACTLY){
            barHeight = size1;
        }else { //当控件的高度为wrapContet时计算控件的高度
            if(datas != null && datas.size()>0){
                barHeight = (int) radius*2 + lengedHeight + (int) distance*2 + (int) distance*datas.size() ;
            }else {
                barHeight = minSize - getPaddingTop() - getPaddingBottom();
            }
        }

        return barHeight;
    }

    //当控件的宽度，高度发生变化是调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = getMeasuredWidth();
        int hight = getMeasuredHeight();
        if (bitmapBuffer == null
                || (bitmapBuffer.get().getWidth() != width)
                || (bitmapBuffer.get().getHeight() != hight)) {

            if (width > 0 && hight > 0) {

                bitmapBuffer = new WeakReference<Bitmap>(Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_4444));
                bitmapCanvas = new Canvas(bitmapBuffer.get());
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        bitmapBuffer.get().eraseColor(Color.TRANSPARENT);//绘制时先擦除画板上的内容(用于更新界面)

        if(datas != null && datas.size() > 0 ){
            if(isLengedVisible){
//                drawLenged();
            }

            angleSEs = new ArrayList<>();

            RectF arcRect = new RectF(barWidth/2-radius,(barHeight-lengedHeight)/2 - radius,
                    barWidth/2+radius,(barHeight-lengedHeight)/2 + radius);//圆形所在的RectF，圆心与控件中心重叠
            float startAngle = initAngle,sweepAngle = 0;
            float perAngle = totalNum/360;//每一度的数量
            String desc;
            float lineAngle;

            descRectes.clear();

            Rect rect = new Rect();
            for(int i=0;i<datas.size();i++){
                sweepAngle = (float) (datas.get(i).getNumber()/perAngle); //当前值的度数
                arcPaint.setColor(ContextCompat.getColor(mContext, datas.get(i).getColorRes()));
                bitmapCanvas.drawArc(arcRect,startAngle,sweepAngle,true,arcPaint);//绘制扇形
                angleSEs.add(new AngleSE(startAngle,sweepAngle+startAngle));

                lineAngle = startAngle + sweepAngle/2;//绘制描述文字的指示线，从扇形中间开始
                desc = datas.get(i).getDesc();
                if (hasData) {
                    RectF rectF = drawLineAndText(sweepAngle,lineAngle, "¥" + datas.get(i).getNumber(),
                            desc, rect,i);
                    descRectes.add(rectF);
                }

                startAngle += sweepAngle; //开始角度变为扇形结束的角度，下次绘制时从前一个扇形的结束区绘制*
            }
            bitmapCanvas.save();
            bitmapCanvas.restore();
        }

        Rect displayRect = new Rect(0,0,barWidth,barHeight);
        Rect det = new Rect(0,0,getWidth(),getHeight());

        canvas.drawBitmap(bitmapBuffer.get(),displayRect,det,null);
        canvas.restore();

        canvas.save();
        arcPaint.setColor(Color.parseColor("#ffffff"));
        canvas.drawCircle(barWidth/2, barHeight/2, ScreenUtils.dpToPx(mContext, 39), arcPaint);
        canvas.restore();

    }

    private RectF drawLineAndText(float sweepAngle,float lineAngle,String amount, String desc,Rect rect,int i){
        RectF descRectF = new RectF();
        float lineStartX,lineStartY ,lineEndX,lineEndY ;
        linePaint.setColor(ContextCompat.getColor(mContext, datas.get(i).getColorRes()));
        lineStartX   =   barWidth/2   +   (radius)   *  (float) Math.cos(lineAngle *   3.14   /180 );
        lineStartY   =   (barHeight-lengedHeight)/2   +   (radius)  *   (float) Math.sin(lineAngle   *   3.14/180);

        // 如果弧形小于30度
        if(Math.abs(sweepAngle) <= 30){
            float num = 0.6f;
            lineEndX   =   barWidth/2   +   (radius+ distance*num*1f)   *  (float) Math.cos(lineAngle *   3.14   /180 );
            lineEndY   =   (barHeight-lengedHeight)/2   +   (radius+ distance*num*1f)  *   (float) Math.sin(lineAngle   *   3.14   /180);
        }else {
            lineEndX   =   barWidth/2   +   (radius+ distance)   *  (float) Math.cos(lineAngle *   3.14   /180 );
            lineEndY   =   (barHeight-lengedHeight)/2   +   (radius+ distance)  *   (float) Math.sin(lineAngle   *   3.14   /180);
        }
        textPaint.setTextSize(ScreenUtils.dpToPxInt(mContext,12));
        textPaint.getTextBounds("消", 0, 1, rect);
        if (lineEndX > barWidth / 2) {
            descRectF.top = lineEndY - rect.height();
            descRectF.bottom = lineEndY + rect.height();
            descRectF.left = barWidth - textPaint.measureText(desc);
            descRectF.right = barWidth;
        } else {
            descRectF.top = lineEndY - rect.height();
            descRectF.bottom = lineEndY + rect.height();
            descRectF.left = 0;
            descRectF.right = textPaint.measureText(desc);
        }

        for (RectF rectF : descRectes) {
            if (rectF.intersect(descRectF)) {
                // 两个描述的间距
                float  rectSpace = ScreenUtils.dpToPx(getContext() ,15);
                //  descRectF  目标矩形 ，  rectF  原有矩形
                if (descRectF.top > rectF.top) {
                    //  > 表明在下方
                    float num = 0.6f ;
                    lineEndX   =   barWidth/2   +   (radius+ distance*num*1f+descRectF.width())   *  (float) Math.cos(lineAngle *   3.14   /180 );
//                    lineEndY   =   (barHeight-lengedHeight)/2   + Math.abs  (radius+ distance*num*1f+descRectF.height())  *   (float) Math.sin(lineAngle   *   3.14   /180);
                    lineEndY = rectF.top + rect.height() + rectSpace  ;
                } else {
                    // <  表明在上方
                    float num = 1f ;
                    lineEndX   =   barWidth/2   +   (radius+ distance*num*1f)   *  (float) Math.cos(lineAngle *   3.14   /180 );
//                    lineEndY   =   (barHeight-lengedHeight)/2   + (radius+ distance*num*1f)  *   (float) Math.sin(lineAngle   *   3.14   /180);
                    lineEndY = rectF.top - rect.height() - rectSpace ;
                }
                break;
            }
        }
        bitmapCanvas.drawLine(lineStartX,lineStartY,lineEndX,lineEndY,linePaint);

        textPaint.getTextBounds(desc,0,desc.length(),rect);
        textPaint.setColor(Color.parseColor("#999999"));
        textPaint.setFakeBoldText(false);
        textPaint.setShadowLayer(0,0,0,Color.TRANSPARENT);
        if (lineStartX>barWidth/2) { //当指示线位于饼图右侧时，在右侧绘制第二条指示线及文字
            bitmapCanvas.drawLine(lineEndX,lineEndY,barWidth,lineEndY,linePaint);
//            bitmapCanvas.drawText(desc,lineEndX+distance/2+4,lineEndY+rect.height()/2,textPaint);
            textPaint.setTextSize(ScreenUtils.dpToPxInt(mContext,10));
            textPaint.getTextBounds("消", 0, 1, rect);
//            bitmapCanvas.drawText(desc,barWidth-textPaint.measureText(desc),lineEndY+rect.height()+ScreenUtils.dpToPxInt(mContext, 2),textPaint);
            bitmapCanvas.drawText(desc , barWidth - textPaint.measureText(desc) , getBaseLineY(lineEndY + (rect.height() /2)) + ScreenUtils.dpToPxInt(mContext ,2),textPaint );
            textPaint.setTextSize(ScreenUtils.dpToPxInt(mContext,12));
            textPaint.getTextBounds("消", 0, 1, rect);
//            bitmapCanvas.drawText(amount,barWidth-textPaint.measureText(amount),lineEndY-ScreenUtils.dpToPxInt(mContext, 4),textPaint);
            bitmapCanvas.drawText(amount , barWidth - textPaint.measureText(amount) , getBaseLineY(lineEndY - (rect.height() /2)) - ScreenUtils.dpToPxInt(mContext ,2),textPaint );
            descRectF.top = lineEndY - rect.height();
            descRectF.bottom = lineEndY + rect.height();
            descRectF.left = barWidth - textPaint.measureText(desc);
            descRectF.right = barWidth;
        }else {//当指示线位于饼图左侧时，在左侧绘制第二条指示线及文字
            bitmapCanvas.drawLine(lineEndX,lineEndY,0,lineEndY,linePaint);
            textPaint.setTextSize(ScreenUtils.dpToPxInt(mContext,10));
            textPaint.getTextBounds("消", 0, 1, rect);
//            bitmapCanvas.drawText(desc,0,lineEndY+rect.height()+ScreenUtils.dpToPxInt(mContext, 2),textPaint);
            bitmapCanvas.drawText(desc , 0 , getBaseLineY(lineEndY + (rect.height() /2)) + ScreenUtils.dpToPxInt(mContext ,2),textPaint );
            textPaint.setTextSize(ScreenUtils.dpToPxInt(mContext,12));
            textPaint.getTextBounds("消", 0, 1, rect);
//            bitmapCanvas.drawText(amount,0,lineEndY-ScreenUtils.dpToPxInt(mContext, 4),textPaint);

            bitmapCanvas.drawText(amount , 0 , getBaseLineY(lineEndY - (rect.height() /2)) - ScreenUtils.dpToPxInt(mContext ,2),textPaint );
            descRectF.top = lineEndY - rect.height();
            descRectF.bottom = lineEndY + rect.height();
            descRectF.left = 0;
            descRectF.right = textPaint.measureText(desc);
        }
        return descRectF;
    }
    

    private int getBaseLineY(float  rectCenterY){
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

        int baseLineY = (int) (rectCenterY - top/2 - bottom/2);//基线中间点的y轴计算公式
        return baseLineY ;
    }


    public void setOnSelectedListener(OnSelectedListener l){
        mListener = l;
    }

    public interface OnSelectedListener{ //点击监听接口
        void onSelected(int position);
    }
    public class AngleSE{
        private float startAngle;
        private float sweepAngle;

        public AngleSE(float startAngle, float sweepAngle) {
            this.startAngle = startAngle;
            this.sweepAngle = sweepAngle;
        }

        public float getStartAngle() {
            return startAngle;
        }

        public float getSweepAngle() {
            return sweepAngle;
        }
    }

    @Data
    public static class ViewData {
        private double number;
        private int colorRes;
        private String desc;
        private Integer deviceType;

        public ViewData(double number, int colorRes,
                        String desc, Integer deviceType) {
            this.number = number;
            this.colorRes = colorRes;
            this.desc = desc;
            this.deviceType = deviceType;
        }
    }
}
