package com.journeyapps.barcodescanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.google.zxing.ResultPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义viewfinderView
 *
 * @author zcd
 * @date 18/1/15
 */

public class CustomViewfinderView extends ViewfinderView {
    public int laserLinePosition = 0;
    public float[] position = new float[]{0f, 0.5f, 1f};
    //    public int[] colors=new int[]{0x00ffffff,0xffffffff,0x00ffffff};
    public int[] colors = new int[]{0x00baacff, 0xffbaacff, 0x00baacff};

    public int[] colorsScan = new int[]{0x00ff5555 , 0xffff5555 , 0x00ff5555} ;


    private int[] linearColor = colors ;
    public LinearGradient linearGradient;
    //    private Rect frame;
    private String scannerTipText;

    public CustomViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setColors(int  type){
        if (type == 1){
            linearColor = colors ;
        }else{
            linearColor = colorsScan ;
        }
        invalidate();
    }
    /**
     * 重写draw方法绘制自己的扫描框
     *
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        refreshSizes();
        if (framingRect == null || previewFramingRect == null) {
            return;
        }

        Rect frame = framingRect;
        Rect previewFrame = previewFramingRect;
//        int inset = 200;
//        if (frame == null) {
//            frame = new Rect(framingRect.left + inset,
//                    framingRect.top + inset,
//                    framingRect.right - inset,
//                    framingRect.bottom - inset);
//        }
//        previewFrame.inset(100, 100);

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        //绘制4个角

        paint.setColor(0xFFFFFFFF);//定义画笔的颜色
        canvas.drawRect(frame.left, frame.top, frame.left + 70, frame.top + 10, paint);
        canvas.drawRect(frame.left, frame.top, frame.left + 10, frame.top + 70, paint);

        canvas.drawRect(frame.right - 70, frame.top, frame.right, frame.top + 10, paint);
        canvas.drawRect(frame.right - 10, frame.top, frame.right, frame.top + 70, paint);

        canvas.drawRect(frame.left, frame.bottom - 10, frame.left + 70, frame.bottom, paint);
        canvas.drawRect(frame.left, frame.bottom - 70, frame.left + 10, frame.bottom, paint);

        canvas.drawRect(frame.right - 70, frame.bottom - 10, frame.right, frame.bottom, paint);
        canvas.drawRect(frame.right - 10, frame.bottom - 70, frame.right, frame.bottom, paint);
        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);
        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, frame, paint);
        } else {
            drawTipText(canvas, frame, canvas.getWidth());
            //  paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            //  scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
            int middle = frame.height() / 2 + frame.top;
            laserLinePosition = laserLinePosition + 5;
            if (laserLinePosition > frame.height()) {
                laserLinePosition = 0;
            }
            if (linearGradient == null) {
                linearGradient = new LinearGradient(frame.left + 1, frame.top + laserLinePosition,
                        frame.right - 1, frame.top + 10 + laserLinePosition, linearColor, position, Shader.TileMode.CLAMP);
            }
            // Draw a red "laser scanner" line through the middle to show decoding is active

            //  paint.setColor(laserColor);
            paint.setShader(linearGradient);
            //绘制扫描线
            canvas.drawRect(frame.left + 1, frame.top + laserLinePosition, frame.right - 1, frame.top + 10 + laserLinePosition, paint);
            paint.setShader(null);
            float scaleX = frame.width() / (float) previewFrame.width();
            float scaleY = frame.height() / (float) previewFrame.height();

            List<ResultPoint> currentPossible = possibleResultPoints;
            List<ResultPoint> currentLast = lastPossibleResultPoints;
            int frameLeft = frame.left;
            int frameTop = frame.top;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new ArrayList<>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(CURRENT_POINT_OPACITY);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            POINT_SIZE, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(CURRENT_POINT_OPACITY / 2);
                paint.setColor(resultPointColor);
                float radius = POINT_SIZE / 2.0f;
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            radius, paint);
                }
            }
            postInvalidateDelayed(16,
                    frame.left,
                    frame.top,
                    frame.right,
                    frame.bottom);
            // postInvalidate();

        }
    }

    private void drawTipText(Canvas canvas, Rect frame, int width) {
        if (TextUtils.isEmpty(scannerTipText)) {
//            scannerTipText = getResources().getString(R.string.zxing_scanner_tip);
              scannerTipText = "请将二维码放入框内，即可自动扫描" ;
        }
        paint.setColor(Color.WHITE);
        paint.setTextSize(DimentionUtils.convertSpToPixels(12, getContext()));
        float textWidth = paint.measureText(scannerTipText);
        float x = (width - textWidth) / 2;
        //根据 drawTextGravityBottom 文字在扫描框上方还是下文，默认下方
        float y = frame.bottom + 100;
        canvas.drawText(scannerTipText, x, y, paint);
    }
}