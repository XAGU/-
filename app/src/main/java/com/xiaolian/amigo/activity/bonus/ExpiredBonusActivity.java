package com.xiaolian.amigo.activity.bonus;

import android.os.Bundle;
import android.widget.ListView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.BaseActivity;
import com.xiaolian.amigo.activity.bonus.adaptor.BonusAdaptor;
import com.xiaolian.amigo.activity.bonus.adaptor.ExpiredBonusAdaptor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 过期红包
 * <p>
 * Created by caidong on 2017/9/8.
 */
public class ExpiredBonusActivity extends BaseActivity {

    static List<ExpiredBonusAdaptor.Bonus> bonuses = new ArrayList<ExpiredBonusAdaptor.Bonus>() {
        {
            add(new ExpiredBonusAdaptor.Bonus(1, 1, "xxxx", "yyyy"));
            add(new ExpiredBonusAdaptor.Bonus(1, 1, "xxxx", "yyyy"));
            add(new ExpiredBonusAdaptor.Bonus(1, 1, "xxxx", "yyyy"));
            add(new ExpiredBonusAdaptor.Bonus(1, 1, "xxxx", "yyyy"));
        }
    };

    @BindView(R.id.lv_bonuses)
    ListView lv_bonuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_expired);
        ButterKnife.bind(this);

        ExpiredBonusAdaptor adapter = new ExpiredBonusAdaptor(this, R.layout.item_bonus_expired, bonuses);
        lv_bonuses.setAdapter(adapter);
    }
}
