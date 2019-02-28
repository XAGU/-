package com.xiaolian.amigo.ui.lostandfound.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.xiaolian.amigo.ui.base.BaseFragment;
import com.xiaolian.amigo.ui.lostandfound.BlogFragment;

import java.util.List;

public class BlogAdapter extends FragmentPagerAdapter {

    private List<BlogFragment> baseFragments;

    private FragmentManager fm;

    public BlogAdapter(FragmentManager fm, List<BlogFragment> baseFragments) {
        super(fm);
        this.fm = fm;
        this.baseFragments = baseFragments;
    }


    @Override
    public Fragment getItem(int position) {
        return baseFragments.get(position);
    }

    @Override
    public int getCount() {
        return baseFragments == null ? 0 : baseFragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BlogFragment baseFragment = (BlogFragment) super.instantiateItem(container, position);
        this.fm.beginTransaction().show(baseFragment).commit();
        return baseFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (baseFragments == null || position == -1 ||
                baseFragments.get(position) == null && baseFragments.size() == position) return;
        BaseFragment fragment = baseFragments.get(position);
        this.fm.beginTransaction().hide(fragment).commit();

    }
}
