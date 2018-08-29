package com.xiaolian.amigo.ui.main.data;

import android.support.v4.app.Fragment;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.lostandfound.SocalFragment;
import com.xiaolian.amigo.ui.main.HomeFragment2;
import com.xiaolian.amigo.ui.main.ProfileFragment2;
import com.xiaolian.amigo.ui.user.UserFragment;

/**
 *  @author   wcm
 *  主页底部导航栏的数据，图片资源等
 */
public class DataGenerator {
    public static int[] mTableNor = new int[]{R.drawable.tab_home_nor ,  R.drawable.tab_personal_nor ,  R.drawable.tab_social_nor };

    public static int[] mTableSle  = new int[]{R.drawable.tab_home_sel , R.drawable.tab_personal_sel };


    public static Fragment[] getFragment(){
        Fragment fragments[] = new Fragment[3];
        fragments[0] = new HomeFragment2();
        fragments[1] = new SocalFragment();
        fragments[2] = new ProfileFragment2();
        return fragments;
    }

    /**
     * 获取TableView
     */
    public void getTableView(){

    }


}
