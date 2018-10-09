package com.xiaolian.amigo.ui.user.adaptor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author  wcm
 *  18/08/29
 */

public class TableFragmentPagerAdapter  extends FragmentPagerAdapter{

    private Fragment[] fragments ;
    public TableFragmentPagerAdapter(FragmentManager fm ,Fragment[] fragments) {
        super(fm);
        this.fragments = fragments ;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.length;
    }
}
