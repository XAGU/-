package com.xiaolian.amigo.ui.main.data;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.lostandfound.SocalFragment;
import com.xiaolian.amigo.ui.main.HomeFragment2;
import com.xiaolian.amigo.ui.main.ProfileFragment2;
import com.xiaolian.amigo.ui.main.intf.IMainPresenter;
import com.xiaolian.amigo.ui.main.intf.IMainView;

/**
 *  @author   wcm
 *  主页底部导航栏的数据，图片资源等
 */
public class DataGenerator {
    public static int[] mTableNor = new int[]{R.drawable.tab_home_nor,  R.drawable.tab_social_nor  ,  R.drawable.tab_personal_nor };

    public static int[] mTableSle  = new int[]{R.drawable.tab_home_sel , R.drawable.tab_social_nor, R.drawable.tab_personal_sel  };


    public static Fragment[] getFragment(IMainPresenter<IMainView> presenter , boolean isServerError){
        Fragment fragments[] = new Fragment[3];
        fragments[0] = new HomeFragment2(presenter ,isServerError);
        fragments[1] = new SocalFragment();
        fragments[2] = new ProfileFragment2();
        return fragments;
    }


    /**
     * 获取TableView
     */
    public static View getTableView(Context context , int position , boolean isSelected , boolean isShowRed ) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_table_content, null, false);
        ImageView tab_image = view.findViewById(R.id.table_image);
        ImageView tab_red = view.findViewById(R.id.table_red);
        RelativeLayout tab_rl = view.findViewById(R.id.tab_rl);

        if (position == 0) {
            if (isSelected) {
                tab_image.setImageResource(mTableNor[0]);
            } else {
                tab_image.setImageResource(mTableSle[0]);
            }
        }

        if (position == 1) {
                tab_image.setBackgroundResource(R.drawable.tab_social_nor);
                tab_rl.setBackground(null);
                tab_image.setImageBitmap(null);
                if (isShowRed) {
                    tab_red.setVisibility(View.VISIBLE);
                } else {
                    tab_red.setVisibility(View.VISIBLE);
                }
        }


        if (position == 2) {
            if (isSelected) {
                tab_image.setBackgroundResource(R.drawable.tab_personal_sel);
                tab_red.setVisibility(View.GONE);
            } else {
                tab_image.setBackgroundResource(R.drawable.tab_personal_nor);
                if (isShowRed) {
                    tab_red.setVisibility(View.VISIBLE);
                } else {
                    tab_red.setVisibility(View.GONE);
                }
            }
        }
        return view ;
    }


}
