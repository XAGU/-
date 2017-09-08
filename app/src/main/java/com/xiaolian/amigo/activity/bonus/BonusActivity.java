package com.xiaolian.amigo.activity.bonus;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.ScrollView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.BaseActivity;
import com.xiaolian.amigo.activity.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.activity.wallet.adaptor.PrepayAdaptor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的红包
 * <p>
 * Created by caidong on 2017/9/8.
 */
public class BonusActivity extends BaseActivity {

    static List<BonusAdaptor.Bonus> bonuses = new ArrayList<BonusAdaptor.Bonus>() {
        {
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
            add(new BonusAdaptor.Bonus(1, 1, "xxxx", "yyyy", 3));
        }
    };

    @BindView(R.id.lv_bonuses)
    ListView lv_bonuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        ButterKnife.bind(this);

        BonusAdaptor adapter = new BonusAdaptor(this, R.layout.item_bonus, bonuses);
        lv_bonuses.setAdapter(adapter);
    }
}
