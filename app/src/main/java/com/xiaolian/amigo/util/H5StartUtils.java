package com.xiaolian.amigo.util;

import android.content.Context;
import android.content.Intent;

import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.WebActivity;

import javax.inject.Inject;

import static com.xiaolian.amigo.ui.base.WebActivity.INTENT_KEY_URL;

/**
 * @author  wcm
 * 跳转h5页面
 */
public class H5StartUtils {

    private static final String TAG = H5StartUtils.class.getSimpleName();
    @Inject
    ISharedPreferencesHelp sharedPreferencesHelp ;

    private Context context ;

    @Inject
    public H5StartUtils(Context context){
        this.context = context ;
    }

    public  void startH5Service(){
        String url = Constant.H5_SERVICE_CENTER
                + "?accessToken=" + sharedPreferencesHelp.getAccessToken()
                +"&refreshToken=" + sharedPreferencesHelp.getReferToken()
                +"&unreadCount=" + sharedPreferencesHelp.getUnReadWorkMessageCount()
                +"&schoolId=" + sharedPreferencesHelp.getUserInfo().getSchoolId();
        Intent intent = new Intent(context, WebActivity.class);
        android.util.Log.e(TAG, "startServiceH5: " + url );
        intent.putExtra(INTENT_KEY_URL, url);
        context.startActivity(intent);
    }
}
