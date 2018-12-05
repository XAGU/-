package com.xiaolian.amigo.ui.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.xiaolian.amigo.data.base.LogInterceptor;
import com.xiaolian.amigo.data.network.IDeviceConnectErrorApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.connecterror.AppTradeStatisticDataReqDTO;
import com.xiaolian.amigo.data.prefs.SharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.util.AppUtils;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.FileIOUtils;
import com.xiaolian.amigo.util.NetworkUtil;
import com.xiaolian.amigo.util.RxHelper;
import com.xiaolian.amigo.util.TimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.xiaolian.amigo.ui.device.DeviceBasePresenter.COUNT_NAME;
import static com.xiaolian.amigo.util.Log.getContext;

/**
 * @author  wcm
 * @data  18/12/4
 *
 * 用于每5分钟上传统计信息的服务.
 */
public class BleCountService extends Service {

    private static final String TAG  = BleCountService.class.getSimpleName() ;
    private Subscription timer;


    // 已经上传的文件信息，上传完成后，将这些文件删除
    private List<File> uploadFile = new ArrayList<>() ;

    private String fileName  =  Environment.getExternalStorageDirectory().getAbsolutePath() +"/xiaolian/"+COUNT_NAME;

    private SharedPreferencesHelp sharedPreferencesHelp ;

    private IDeviceConnectErrorApi deviceConnectErrorApi ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initRequestVariable();
    }

    /**
     * 初始化网络请求变量
     */
    private void initRequestVariable() {
        //  优化   使用dagger2
        if (sharedPreferencesHelp == null){
            Gson gson = new Gson() ;
            sharedPreferencesHelp = new SharedPreferencesHelp(this ,gson);
        }

        if (deviceConnectErrorApi == null){
            LogInterceptor logInterceptor = new LogInterceptor(sharedPreferencesHelp);
            Retrofit retrofit = provideRetrofit(logInterceptor);
            deviceConnectErrorApi = retrofit.create(IDeviceConnectErrorApi.class);
        }
    }


    /**
     * 获取
     * @param logInterceptor
     * @return
     */
    Retrofit provideRetrofit(LogInterceptor logInterceptor) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(logInterceptor)
                .connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(new HttpLoggingInterceptor())
                .build();
        return new Retrofit.Builder()
                .baseUrl(Constant.SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initRequestVariable();
        Log.e(TAG, "onStartCommand: " );
        if (timer == null){
            Log.e(TAG, "onStartCommand: " );
//            handleUploadTradSatistic(fileName);
            timer = RxHelper.intervel(60, aLong ->
                    handleUploadTradSatistic(fileName)
            );
        }

        /**
         * START_STICKY   服务被Kill掉之后，将保留开始状态；若内存足够的情况下，将会重启Service ， 但intent为空
         *
         * START_NOT_STICKY   服务被kill掉之后，并且没有新的intent传递给它，service将移除开始状态，直到startService
         *
         * START_REDELIVER_INTENT  在运行onStartCommand后service进程被kill后，系统将会再次启动service，并传入最后一个intent给onstartCommand
         * 。直到调用stopSelf(int)才停止传递intent。如果在被kill后还有未处理好的intent
         * ，那被kill后服务还是会自动启动。因此onstartCommand不会接收到任何null的intent。
         */
        flags = START_STICKY ;
        return super.onStartCommand(intent , flags ,startId );
    }

    /**
     * 处理上传设备交易统计信息
     * @param fileName
     * @return
     */
    private void  handleUploadTradSatistic(String fileName){
        Log.e(TAG, "handleUploadTradSatistic: "  );
        Observable.just(fileName)
                .subscribeOn(Schedulers.io())
                .map(name -> getFiles(name))
                .map(files -> {
                    Log.e(TAG, "handleUploadTradSatistic: >>>>> AppTradeStatisticDataReqDTO ");
                    AppTradeStatisticDataReqDTO reqDTO =  fromFilesToReqDto(files);
                    return  reqDTO ;
                }).subscribe(reqDTO -> {
                    if (reqDTO == null) return ;
                    deviceConnectErrorApi.uploadTradSatistic(reqDTO)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(new Subscriber<ApiResult<BooleanRespDTO>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.wtf(TAG ,e.getMessage());
                                }

                                @Override
                                public void onNext(ApiResult<BooleanRespDTO> result) {
                                    if (result.getError() == null) {
                                        //  上传成功删除这些文件
                                        if (uploadFile.size() > 0){
                                            for (File file : uploadFile){
                                                    boolean b = file.delete();
                                                    if (b) {
                                                        uploadFile.remove(file);
                                                        Log.e(TAG, "onDelete: " + file.getName() );
                                                    }
                                            }
                                        }
                                    }
                                }
                            });

                });

    }

    /**
     * 将文件的内容读取出来转为Object类 ;
     * @param files
     * @return
     */
    private AppTradeStatisticDataReqDTO fromFilesToReqDto(List<File> files){
        Log.e(TAG, "fromFilesToReqDto: "  );
            if (files ==null || files.size() == 0) return null ;
            this.uploadFile = files ;
            AppTradeStatisticDataReqDTO reqDTO = new AppTradeStatisticDataReqDTO();

            List<AppTradeStatisticDataReqDTO.ItemsBean> itemsBeans = new ArrayList<>();
            for (File file : files){
                String time = file.getName() ;
                if (time.length() > 10){
                    time = time.substring(0 , 13);
                }

                String content =  FileIOUtils.readFile2String(file);
                // 获取这个文件内的所有记录
                String[] records =content.split(Constant.TRADE_STATISTIC_ITEM_SEPARATOR);
                if (records.length == 0)  break;
                //  遍历所有记录
                for (String record : records){
                    Log.e(TAG, "统计记录: " + record  );
                    //   String 格式为 type:SCAN、target:SERVER、result:success、time:120;
                    //   通过、分隔符获取属性数组
                    String[] attributes = record.split(Constant.TRADE_STATISTIC_PARAM_SEPARATOR);

                    if (attributes.length == 0) break;

                        AppTradeStatisticDataReqDTO.ItemsBean itemsBean = getItemsBean(record , time);
                        if (itemsBean == null) break;
                        // 为每一个item 添加数据
                        if (itemsBeans.size() == 0){
                            itemsBeans.add(itemsBean) ;
                        }else{
                            boolean isSaveRecord = false ;
                            for (int i = 0 ; i < itemsBeans.size() ; i++){
                                AppTradeStatisticDataReqDTO.ItemsBean item = itemsBeans.get(i);
                                //  判断是否有存储macAddress 的数据
                                if (TextUtils.equals(item.getMacAddress() ,itemsBean.getMacAddress())){
                                    // 如果有，再判断是否有此交互行为   比如 SCAN , OEPN
                                    if (TextUtils.equals(item.getType() , itemsBean.getType())){

                                        // 如果交互行为一致，再判断交互对象是否相同 比如 SERVER ,DEVICE
                                        if (TextUtils.equals(item.getTarget() , itemsBean.getTarget())) {

                                            // 如果交互对象相同， 再判断是结果是否一样  比如 SUCCESS ,FAILED

                                            if (TextUtils.equals(item.getResult(), itemsBean.getResult())) {

                                                //  如果 此Type 结果也一样，则Count + 1 ， 判断是否耗时最小 ， 耗时最大， 以及平均时间
                                                isSaveRecord = true ;
                                                int count = item.getCount();
                                                int avgTime = item.getAvgTime();
                                                int minTime = item.getMinTime();
                                                int maxTime = item.getMaxTime();


                                                avgTime = (count * avgTime + itemsBean.getAvgTime()) / (count + 1);
                                                count = count + 1 ;
                                                item.setCount(count);
                                                item.setAvgTime(avgTime);
                                                minTime = Math.min(minTime, itemsBean.getMinTime());
                                                Log.e(TAG, "fromFilesToReqDto:  nowMinTime " + minTime + "   oldMinTime :  "  + item.getMinTime()  );
                                                item.setMinTime(minTime);
                                                maxTime = Math.max(maxTime, itemsBean.getMaxTime());

                                                Log.e(TAG, "fromFilesToReqDto:  nowMaxTime " + maxTime + "  oldMaxTime :  "  + item.getMaxTime()  );
                                                item.setMaxTime(maxTime);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            if (!isSaveRecord) {
                                Log.e(TAG, "fromFilesToReqDto: >>>> addItem "  );
                                itemsBeans.add(itemsBean);
                            }
                        }

                    }
                }
            if (itemsBeans.size() > 0) reqDTO.setItems(itemsBeans);
            reqDTO.setTerminalInfo(getTerminalInfo());
            return  reqDTO ;
        }


    /**
     * 获取日志的通用信息
     * @return
     */
    private AppTradeStatisticDataReqDTO.TerminalInfoBean  getTerminalInfo(){
        AppTradeStatisticDataReqDTO.TerminalInfoBean  terminalInfoBean = new AppTradeStatisticDataReqDTO.TerminalInfoBean();
        String appVersion = AppUtils.getVersionName(this);
        String brand = Build.BRAND;
        String env = "USER" ;
        String ip = NetworkUtil.getIPAddress(this);
        String model = Build.MODEL ;
        String os = "ANDROID" ;
        int systemVersion = Build.VERSION.SDK_INT;
        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        terminalInfoBean.setAppVersion(appVersion);
        terminalInfoBean.setBrand(brand);
        terminalInfoBean.setEnv(env);
        terminalInfoBean.setIp(ip);
        terminalInfoBean.setModel(model);
        terminalInfoBean.setOs(os);
        terminalInfoBean.setSystemVersion(systemVersion +"");
        terminalInfoBean.setUniqueId(androidId);
        return terminalInfoBean ;
    }


    /**
     * 将每条记录的内容转化为Object类
     * @param record
     * @return
     */
    private AppTradeStatisticDataReqDTO.ItemsBean  getItemsBean(String record , String timeStamps){
        if (TextUtils.isEmpty(record))  return null ;
        String[] attributes = record.split(Constant.TRADE_STATISTIC_PARAM_SEPARATOR);
        if (attributes.length == 0) return null ;
        AppTradeStatisticDataReqDTO.ItemsBean itemsBean = new AppTradeStatisticDataReqDTO.ItemsBean();
        for (String attribute : attributes) {

            // 遍历每个属性值，并且设置对应值
            String[] result = attribute.split(Constant.TRADE_STATISTIC_CONTENT_SEPARATOR);
            if (result.length == 0) return null;
            //  如果没有数据 直接存储
            if (TextUtils.equals(result[0], "macAddress")) itemsBean.setMacAddress(result[1]);
            if (TextUtils.equals(result[0], "time")) {
                try {
                    // 时间的Int型
                    int timeInt = Integer.parseInt(result[1]);
                    itemsBean.setAvgTime(timeInt);
                    itemsBean.setMinTime(timeInt);
                    itemsBean.setMaxTime(timeInt);
                } catch (NumberFormatException e) {
                    android.util.Log.e(TAG, "parseLongTime: " + e.getMessage());
                    itemsBean.setAvgTime(0);
                    itemsBean.setMinTime(0);
                    itemsBean.setMaxTime(0);
                }
            }
            if (TextUtils.equals(result[0], "target")) itemsBean.setTarget(result[1]);
            if (TextUtils.equals(result[0], "result")) itemsBean.setResult(result[1]);
            if (TextUtils.equals(result[0], "type")) itemsBean.setType(result[1]);
            if (TextUtils.equals(result[0], "supplierId")) {
                try {
                    itemsBean.setSupplierId(Integer.parseInt(result[1]));
                } catch (NumberFormatException e) {
                    android.util.Log.e(TAG, "parseLongSupplierId: " + e.getMessage());
                    itemsBean.setSupplierId(0);
                }
            }

            if (TextUtils.equals(result[0], "deviceType")) itemsBean.setDeviceType(result[1]);
            if (TextUtils.equals(result[0], "residenceId")) {

                try {
                    itemsBean.setResidenceId(Integer.parseInt(result[1]));
                } catch (NumberFormatException e) {
                    android.util.Log.e(TAG, "parseLongResidenceId: " + e.getMessage());
                    itemsBean.setResidenceId(0L);
                }
            }
            itemsBean.setCount(1);
            try {
                long time = Long.parseLong(timeStamps);
                itemsBean.setTime(time);
            } catch (NumberFormatException e) {
                android.util.Log.e(TAG, "getItemsBean: " + e.getMessage());
                itemsBean.setTime(0);
            }
        }

        return itemsBean ;
    }

    /**
     * 获取目录下所有文件
      * @param fileName
     * @return
     */
    private List<File> getFiles(String fileName){
        if (TextUtils.isEmpty(fileName)) return null;
        File file = new File(fileName);
        if (!file.exists() || !file.isDirectory()) return null ;
        List<File> fileList = new ArrayList<>();
        try{
            File[] files = file.listFiles();
            if (files.length == 0 ) return null ;
            for (int i =0 ; i < files.length ; i ++){
                String mFileName = files[i].getName() ;

                long time = Long.parseLong(mFileName.substring(0 , 13));
                long currentTime = TimeUtils.getCountTimeStamp();

                // 将现在5分钟时间片的文件不上传到服务器。
                if (currentTime != time){
                   fileList.add(files[i]);
                }
            }
            return fileList ;
        }catch (Exception e){
            android.util.Log.e(TAG, "getFiles: " + e.getMessage()  );
            return null ;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.unsubscribe();
        }
    }


}
