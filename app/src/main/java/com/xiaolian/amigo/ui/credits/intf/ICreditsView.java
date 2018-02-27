package com.xiaolian.amigo.ui.credits.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.credits.CreditsAdapter;

import java.util.List;

/**
 * 我的积分
 * @author zcd
 * @date 18/2/23
 */

public interface ICreditsView extends IBaseView {
    void renderRules(List<CreditsAdapter.CreditsItem> items);

    void renderCredits(int credits);

    void showExchangeDialog(Long bonusId, Integer deviceType, String bonusAmount, Integer pointAmount);
}
