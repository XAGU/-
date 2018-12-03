package com.xiaolian.amigo.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xiaolian.amigo.data.network.model.connecterror.AppTradeStatisticDataReqDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.FileIOUtils;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.RxHelper;
import com.xiaolian.amigo.util.TimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.xiaolian.amigo.ui.device.DeviceBasePresenter.COUNT_NAME;

public class BleCountServer extends Service {

    private static final String TAG  = BleCountServer.class.getSimpleName() ;
    private Subscription timer;

    public CompositeSubscription subscriptions;

    private String fileName  =  Environment.getExternalStorageDirectory().getAbsolutePath() +"/xiaolian/"+COUNT_NAME;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (subscriptions == null)
        subscriptions = new CompositeSubscription();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (subscriptions == null) subscriptions = new CompositeSubscription();

        if (timer == null){
            timer = RxHelper.delay((int) (TimeUtils.getCountTimeStamp() - System.currentTimeMillis() / 1000), new Action1<Long>() {
                @Override
                public void call(Long aLong) {

                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 查询目标目录下的所有文件
     * @param fileName
     * @return
     */
    private List<File> getFile(String fileName){
        Observable.just(fileName)
                .subscribeOn(Schedulers.io())
                .map(name -> getFile(name))
                .map(new Func1<List<File>, AppTradeStatisticDataReqDTO>() {
                    @Override
                    public AppTradeStatisticDataReqDTO call(List<File> files) {
                        if (files ==null || files.size() == 0) return null ;

                        AppTradeStatisticDataReqDTO reqDTO = new AppTradeStatisticDataReqDTO();

                        List<AppTradeStatisticDataReqDTO.ItemsBean> itemsBeans = new ArrayList<>();
                        for (File file : files){
                            String time = file.getName() ;
                            if (time.length() > 10){
                                time = time.substring(0 , 10);
                            }

                           String content =  FileIOUtils.readFile2String(file);
                           // 获取这个文件内的所有记录
                           String[] records =content.split(Constant.TRADE_STATISTIC_ITEM_SEPARATOR);
                           if (records.length == 0)  break;
                           //  遍历所有记录
                           for (String record : records){

                               //   String 格式为 type:SCAN、target:SERVER、result:success、time:120;
                               //   通过、分隔符获取属性数组
                               String[] attributes = record.split(Constant.TRADE_STATISTIC_PARAM_SEPARATOR);

                               if (attributes.length == 0) break;

                               /**
                                * 遍历所有属性值
                                * type:SCAN
                                * target:SERVER
                                * result:SUCCESS
                                * time:120
                                *
                                */
                               for (String attribute : attributes){

                                   String[] result = attribute.split(Constant.TRADE_STATISTIC_CONTENT_SEPARATOR);

                                   if (result.length == 0) break;
                                   // 为每一个item 添加数据
                                   if (itemsBeans.size() == 0){
                                      //  如果没有数据 直接存储
                                       AppTradeStatisticDataReqDTO.ItemsBean itemsBean = new AppTradeStatisticDataReqDTO.ItemsBean();
                                       if (TextUtils.equals(result[0] ,"macAddress")) itemsBean.setMacAddress(result[1]);
                                       if (TextUtils.equals(result[0] , "time")) {
                                           try {
                                               itemsBean.setTime(Long.parseLong(result[1]));
                                           }catch (NumberFormatException e){
                                               android.util.Log.e(TAG, "parseLongTime: " + e.getMessage() );
                                               itemsBean.setTime(0L);
                                           }
                                       }
                                       if (TextUtils.equals(result[0] , "target")) itemsBean.setTarget(result[1]);
                                       if (TextUtils.equals(result[0] , "result")) itemsBean.setResult(result[1]);
                                       if (TextUtils.equals(result[0] , "type")) itemsBean.setType(result[1]);
                                       if (TextUtils.equals(result[0] , "supplierId")) {
                                           try {
                                               itemsBean.setSupplierId(Integer.parseInt(result[1]));
                                           }catch (NumberFormatException e){
                                               android.util.Log.e(TAG, "parseLongSupplierId: " + e.getMessage() );
                                               itemsBean.setTime(0);
                                           }
                                       }

                                       if (TextUtils.equals(result[0] ,"deviceType")) itemsBean.setDeviceType(result[1]);
                                       if (TextUtils.equals(result[0] , "residenceId")){

                                           try {
                                               itemsBean.setResidenceId(Integer.parseInt(result[1]));
                                           }catch (NumberFormatException e){
                                               android.util.Log.e(TAG, "parseLongResidenceId: " + e.getMessage() );
                                               itemsBean.setResidenceId(0L);
                                           }
                                       }
                                       itemsBean.setCount(1);
                                       itemsBeans.add(itemsBean);
                                   }else{

                                       for (int i = 0 ; i < itemsBeans.size() ; i++){
                                           AppTradeStatisticDataReqDTO.ItemsBean itemsBean = itemsBeans.get(i);
                                           //  判断是否有存储macAddress 的数据
                                            if (attribute.contains(itemsBean.getMacAddress())){
                                                // 如果有，再判断是否有此交互行为
                                                if (attribute.contains(itemsBean.getType())){

                                                    String typeResult = attribute
                                                }


                                                break ;
                                            }

                                       }
                                   }

                               }



                           }

                        }
                    }
                })
                );
    }

    /**
     * 获取目录下所有文件
      * @param fileName
     * @return
     */
    private List<File> getFiles(String fileName){
        if (TextUtils.isEmpty(fileName)) return null;
        File file = new File(fileName);
        if (!file.exists() || file.isDirectory()) return null ;
        try{
            File[] files = file.listFiles();
            if (files != null && files.length > 0){
                List<File> fileList = new ArrayList<>(files.length);
                Collections.addAll(fileList , files);
                return fileList ;
            }else{
                return null ;
            }
        }catch (Exception e){
            android.util.Log.e(TAG, "getFiles: " + e.getMessage()  );
        }
        return null ;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.unsubscribe();
        }
        clearObservers();
    }

    // 重置订阅列表集合
    public void resetSubscriptions() {
        clearObservers();
        subscriptions = new CompositeSubscription();
    }

    public void clearObservers() {
        if (null != subscriptions && !subscriptions.isUnsubscribed()) {
            // handler.post(() -> {
            subscriptions.unsubscribe();
            subscriptions.clear();
            // });
        }
    }

    public <P> void addObserver(Observable<P> observable, BasePresenter.NetworkObserver observer, Scheduler scheduler) {
        if (null != subscriptions) {
            this.subscriptions.add(
                    observable
                            .subscribeOn(Schedulers.io())
                            .observeOn(scheduler)
                            .subscribe(observer));
        }
    }
}
