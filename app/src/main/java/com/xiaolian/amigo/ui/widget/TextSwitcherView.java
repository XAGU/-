package com.xiaolian.amigo.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.marqueeview.WaterTextView;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.RxHelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static android.text.TextUtils.TruncateAt.MARQUEE;

public class TextSwitcherView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private static final String TAG = TextSwitcherView.class.getSimpleName();
    private static final int UPDATE_TEXTSWITCHER = 1 ;

    private ArrayList<String> info = new ArrayList<>();
    private int resIndex = 0 ;

    private Context mContext ;

    private int dataFlag ;  // 是否是一行

    boolean waterTextStatus ;  // 是否大于一行

    int startAgainCount ;

    int waterloopTime ;

    private List<View> views = new ArrayList<>();
    public TextSwitcherView(Context context) {
        this(context , null);
    }

    public TextSwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context){
        this.mContext  = context ;
        this.setFactory(this::makeView);
        this.setInAnimation(getContext() , R.anim.anim_come_in);
        this.setOutAnimation(getContext() , R.anim.anim_get_out);
        Timer timer = new Timer();
        timer.schedule(timerTask ,1 ,3000);

    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if(!waterTextStatus) {
                Message message = Message.obtain();
                message.what = UPDATE_TEXTSWITCHER;
                handler.sendMessage(message);
            }else{ // 多行
                startAgainCount++ ;
                if (startAgainCount > waterloopTime){
                    startAgainCount = 0 ;
                    waterTextStatus = false ;
                }
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_TEXTSWITCHER:
                    updateTextSwitcher();
//                    updateText();
                    break;
                    default:
                        break;

            }
        }
    };
    
    public void updateTextSwitcher() {
        if (this.info != null && this.info.size()>0) {
            this.setText(this.info.get(resIndex++));
            RxHelper.delay(500 , TimeUnit.MILLISECONDS)
                    .subscribe(integer -> {
                        ((MarqueeText)getCurrentView()).startFor0();
                    });
            if (resIndex > this.info.size()-1) {
                resIndex = 0;
            }
        }

    }

    public void getResoure(ArrayList<String> info){
        this.info = info ;
        this.dataFlag = 1 ;
        views.clear();
    }
    String string ;

    public void setDefaultData(String string){
        this.string = string ;
        views.clear();
    }

    int byteCount ;
    int stringCount ;
    int getRealCount ;
    List<String> subArrayList = new ArrayList<>() ;
    public  void subStr(String str, int subSLength)
            throws UnsupportedEncodingException {

        char[] array = str.toCharArray();  //获取字节

        Log.d(TAG, "strbyte/arraybyte: " + str.toString().getBytes("GBK").length + "  " + array.length);
        if (str.toString().getBytes("GBK").length > subSLength) {
            int shi = str.toString().getBytes("GBK").length / subSLength;
            int ge = str.toString().getBytes("GBK").length % subSLength;
            if (shi > 0 && ge != 0) {  //不小于一行，分开显示
                for (int i = 0; i < array.length; i++) {
                    if ((char) (byte) array[i] != array[i]) {
                        byteCount += 2;  //如果是中文，则自加2
                        stringCount += 1;
                    } else {
                        byteCount += 1; //如果不是中文，则自加1
                        stringCount += 1;
                    }
                    if (byteCount >= subSLength) {
                        getRealCount = stringCount;
                        byteCount = 0;
                        stringCount = 0;
                    }
                }
                Log.d(TAG, "shi/ge: "+shi+"  "+ge);
                if (ge>0 && ge<=7) {
                    waterloopTime = 3*shi;
                }else if (ge>7 && ge<=16) {
                    waterloopTime = 3*shi+1;
                }else if(ge>16 && ge<=25){
                    waterloopTime = 4*shi+1;
                }else if(ge>25 && ge<=35){
                    waterloopTime = 5*shi+1;
                }else {
                    waterloopTime = 6*shi+2;
                }
                Log.d(TAG, "waterloopTime: "+waterloopTime);
                for (int i = 0; i <= shi; i++) {
                    if (i == shi) {
                        subArrayList.add(str.substring(getRealCount*i,str.length()));
                    }else {
                        subArrayList.add(str.substring(getRealCount*i,getRealCount*(i+1)));
                    }
                }
            } else {
                subArrayList.add(str); //小于一行则正常显示
                Log.d(TAG, "cannot read?");
            }

        }

    }
    private void updateText(){

        if (this.dataFlag ==1 ){

            if (this.info != null && this.info.size() > 0){

                 try{
                     subStr(info.get(resIndex) , 14);
                     if (subArrayList!=null && subArrayList.size() > 0){
                            if (subArrayList.size() == 1){ // 单行
                                waterTextStatus = false ;
                                this.setText(this.info.get(resIndex));
                                subArrayList.clear();
                                android.util.Log.e(TAG, "subStr:  >>> one" );
                            }else {  // 多行
                                waterTextStatus = true ;
                                this.setText(this.info.get(resIndex));
                                subArrayList.clear();
                                android.util.Log.e(TAG, "subStr: >>> more"  );

                            }

                            resIndex++ ;
                            if (resIndex >= info.size() -1){
                                resIndex = 0 ;
                            }
                       }
                 }catch (UnsupportedEncodingException e){
                     android.util.Log.e(TAG, "subStr: "+ e.getMessage());
                 }
                 }
            }else{
            this.setText(this.string);
        }

    }

    @Override
    public View makeView() {
        MarqueeText textView = new MarqueeText(getContext());
        textView.setTextSize(12);
        textView.setTextColor(getContext().getResources().getColor(R.color.colorFullRed));
        textView.setSingleLine();
//        textView.setEllipsize(MARQUEE);
//        textView.setMarqueeRepeatLimit(1);
        android.util.Log.e(TAG, "makeView: " +  textView.isShown());
        return textView;
    }
}
