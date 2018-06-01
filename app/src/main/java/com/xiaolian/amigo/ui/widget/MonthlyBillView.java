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
    private int barWidth,barHeight;
    private List<ViewData> datas;
    private List<AngleSE> angleSEs;
    private List<RectF> lengedRectes;
    private OnSelectedListener mListener;

    private float totalNum;

    private int lengedHeight = 0;
    private boolean isLengedVisible = false;

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

        setRadius(ScreenUtils.dpToPxInt(context,80));
    }

    public void setRadius(float rs){
        this.radius = rs;
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
            float startAngle = -90,sweepAngle = 0;
            float perAngle = totalNum/360;//每一度的数量
            String desc;
            float lineAngle;

            Rect rect = new Rect();
            for(int i=0;i<datas.size();i++){
                sweepAngle = datas.get(i).getNumber()/perAngle; //当前值的度数
                arcPaint.setColor(ContextCompat.getColor(mContext, datas.get(i).getColorRes()));
                bitmapCanvas.drawArc(arcRect,startAngle,sweepAngle,true,arcPaint);//绘制扇形
                angleSEs.add(new AngleSE(startAngle,sweepAngle+startAngle));

                lineAngle = startAngle+ sweepAngle/2;//绘制描述文字的指示线，从扇形中间开始
                desc = datas.get(i).getDesc()+","+datas.get(i).getNumber();
                drwaLineAndText(sweepAngle,lineAngle,desc,rect,i);

                startAngle += sweepAngle; //开始角度变为扇形结束的角度，下次绘制时从前一个扇形的结束区绘制*
            }
            bitmapCanvas.save();
            bitmapCanvas.restore();
        }

        Rect displayRect = new Rect(0,0,barWidth,barHeight);
        Rect det = new Rect(0,0,getWidth(),getHeight());

        canvas.drawBitmap(bitmapBuffer.get(),displayRect,det,null);
        canvas.restore();
    }

    private void drwaLineAndText(float sweepAngle,float lineAngle,String desc,Rect rect,int i){
        float lineStartX,lineStartY ,lineEndX,lineEndY ;

        lineStartX   =   barWidth/2   +   (radius- distance)   *  (float) Math.cos(lineAngle *   3.14   /180 );
        lineStartY   =   (barHeight-lengedHeight)/2   +   (radius- distance)  *   (float) Math.sin(lineAngle   *   3.14/180);
        if(Math.abs(sweepAngle) <= 30){ //当偏转角度小于30°时，增加指示线的长度，避免描述文字重叠
            float num = (datas.size() - i)%3;
            lineEndX   =   barWidth/2   +   (radius+ distance*num*1f)   *  (float) Math.cos(lineAngle *   3.14   /180 );
            lineEndY   =   (barHeight-lengedHeight)/2   +   (radius+ distance*num*1f)  *   (float) Math.sin(lineAngle   *   3.14   /180);
        }else {
            lineEndX   =   barWidth/2   +   (radius+ distance)   *  (float) Math.cos(lineAngle *   3.14   /180 );
            lineEndY   =   (barHeight-lengedHeight)/2   +   (radius+ distance)  *   (float) Math.sin(lineAngle   *   3.14   /180);
        }
        bitmapCanvas.drawLine(lineStartX,lineStartY,lineEndX,lineEndY,linePaint);

        textPaint.getTextBounds(desc,0,desc.length(),rect);
        textPaint.setTextSize(ScreenUtils.dpToPxInt(mContext,11));
        textPaint.setFakeBoldText(false);
        textPaint.setShadowLayer(0,0,0,Color.TRANSPARENT);
        if (lineStartX>barWidth/2) { //当指示线位于饼图右侧时，在右侧绘制第二条指示线及文字
            bitmapCanvas.drawLine(lineEndX,lineEndY,lineEndX+distance/2,lineEndY,linePaint);
            bitmapCanvas.drawText(desc,lineEndX+distance/2+4,lineEndY+rect.height()/2,textPaint);
        }else {//当指示线位于饼图左侧时，在左侧绘制第二条指示线及文字

            bitmapCanvas.drawLine(lineEndX,lineEndY,lineEndX-distance/2,lineEndY,linePaint);
            bitmapCanvas.drawText(desc,lineEndX-distance/2-4-rect.width(),lineEndY+rect.height()/2,textPaint);
        }
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
        private int number;
        private int colorRes;
        private String desc;

        public ViewData(int number, int colorRes, String desc) {
            this.number = number;
            this.colorRes = colorRes;
            this.desc = desc;
        }
    }
}
