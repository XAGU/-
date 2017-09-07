package com.xiaolian.amigo.activity.wallet;

import android.os.Bundle;
import android.widget.ListView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.activity.BaseActivity;
import com.xiaolian.amigo.activity.wallet.adaptor.RechargeAdaptor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的钱包
 * <p>
 * Created by caidong on 2017/9/7.
 */
public class RechargeActivty extends BaseActivity {

    List<RechargeAdaptor.RechargeGroup> groups = new ArrayList<RechargeAdaptor.RechargeGroup>() {
        {
            add(new RechargeAdaptor.RechargeGroup(new RechargeAdaptor.Recharge[]{
                    new RechargeAdaptor.Recharge(1, "20元", "售价198888"),
                    new RechargeAdaptor.Recharge(2, "20元", "送一个美女"),
                    new RechargeAdaptor.Recharge(3, "100元", "售价100万"),
            }));
            add(new RechargeAdaptor.RechargeGroup(new RechargeAdaptor.Recharge[]{
                    new RechargeAdaptor.Recharge(1, "30元", "售价58"),
                    new RechargeAdaptor.Recharge(2, "15元", "送5000元红包"),
            }));
        }
    };

    @BindView(R.id.lv_recharge_groups)
    ListView rechargeGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_recharge);
        ButterKnife.bind(this);

        RechargeAdaptor adaptor = new RechargeAdaptor(this, R.layout.item_wallet_recharge, groups);
        rechargeGroups.setAdapter(adaptor);
    }
}
