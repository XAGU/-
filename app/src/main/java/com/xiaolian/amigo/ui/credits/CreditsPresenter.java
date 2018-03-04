package com.xiaolian.amigo.ui.credits;

import com.xiaolian.amigo.data.manager.intf.ICreditsDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.credits.CreditsExchangeReqDTO;
import com.xiaolian.amigo.data.network.model.credits.CreditsRuleItemsDTO;
import com.xiaolian.amigo.data.network.model.credits.CreditsRuleRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.credits.intf.ICreditsPresenter;
import com.xiaolian.amigo.ui.credits.intf.ICreditsView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 积分兑换
 * @author zcd
 * @date 18/2/26
 */

public class CreditsPresenter<V extends ICreditsView> extends BasePresenter<V>
    implements ICreditsPresenter<V> {
    private ICreditsDataManager creditsDataManager;
    private int mCredits;

    @Inject
    public CreditsPresenter(ICreditsDataManager creditsDataManager) {
        this.creditsDataManager = creditsDataManager;
    }

    @Override
    public void getRules() {
        addObserver(creditsDataManager.getRules(), new NetworkObserver<ApiResult<CreditsRuleRespDTO>>() {

            @Override
            public void onReady(ApiResult<CreditsRuleRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getItems() != null
                            && !result.getData().getItems().isEmpty()) {
                        List<CreditsAdapter.CreditsItem> items = new ArrayList<>();
                        for (CreditsRuleItemsDTO creditsRuleItemsDTO : result.getData().getItems()) {
                            items.add(new CreditsAdapter.CreditsItem(creditsRuleItemsDTO.getBonusId(),
                                    creditsRuleItemsDTO.getDeviceType(), creditsRuleItemsDTO.getBonusAmount(),
                                    creditsRuleItemsDTO.getCredits()));
                        }
                        getMvpView().renderRules(items);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void exchange(Long bonusId, Integer credits, Integer deviceType) {
        CreditsExchangeReqDTO reqDTO = new CreditsExchangeReqDTO();
        reqDTO.setBonusId(bonusId);
        reqDTO.setCredits(credits);
        reqDTO.setDeviceType(deviceType);
        addObserver(creditsDataManager.exchange(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        mCredits = mCredits - credits;
                        creditsDataManager.setCredits(mCredits);
                        getMvpView().renderCredits(mCredits);
                        getMvpView().onSuccess("兑换成功");
                    } else {
                        getMvpView().onError("兑换失败");
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getCredits() {
        mCredits = creditsDataManager.getCredits();
        getMvpView().renderCredits(mCredits);
    }

    @Override
    public void checkForExchange(Long bonusId, Integer deviceType, String bonusAmount, Integer pointAmount) {
        if (mCredits < pointAmount) {
            getMvpView().onError("积分不足");
        } else {
            getMvpView().showExchangeDialog(bonusId, deviceType, bonusAmount, pointAmount);
        }
    }
}
