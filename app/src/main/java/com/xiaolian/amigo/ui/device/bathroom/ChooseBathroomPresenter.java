package com.xiaolian.amigo.ui.device.bathroom;

import com.xiaolian.amigo.data.enumeration.BathTradeType;
import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathFloorDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathGroupDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathRoomDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author zcd
 * @date 18/7/3
 */
public class ChooseBathroomPresenter<V extends IChooseBathroomView> extends BasePresenter<V>
        implements IChooseBathroomPresenter<V> {
    private IBathroomDataManager bathroomDataManager;

    @Inject
    public ChooseBathroomPresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }

    @Override
    public void getBathroomList() {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(2659L);
        addObserver(bathroomDataManager.list(reqDTO),
                new NetworkObserver<ApiResult<BathBuildingRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<BathBuildingRespDTO> result) {
                        if (null == result.getError()) {
                            getMvpView().refreshBathroom(convertFrom(result.getData()),
                                    result.getData().getMethods(), result.getData().getMissTimes());
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }
                });
    }

    @Override
    public void preBooking() {
        BathBookingReqDTO reqDTO = new BathBookingReqDTO();
        reqDTO.setDeviceNo("111111");
        reqDTO.setType(BathTradeType.BOOKING.getCode());
        addObserver(bathroomDataManager.preBooking(reqDTO), new NetworkObserver<ApiResult<BathPreBookingRespDTO>>() {

            @Override
            public void onReady(ApiResult<BathPreBookingRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getBonus() != null) {
                        getMvpView().gotoBookingView(result.getData().getBalance(),
                                result.getData().getBonus().getId(), result.getData().getBonus().getDescription(),
                                result.getData().getBonus().getAmount(), result.getData().getExpiredTime(),
                                result.getData().getLocation(), result.getData().getMaxMissAbleTimes(),
                                result.getData().getMinPrepay(), result.getData().getMissedTimes(),
                                result.getData().getPrepay() , result.getData().getReservedTime());
                    }else{
                        getMvpView().gotoBookingView(result.getData().getBalance(),
                                null, null,
                                null, result.getData().getExpiredTime(),
                                result.getData().getLocation(), result.getData().getMaxMissAbleTimes(),
                                result.getData().getMinPrepay(), result.getData().getMissedTimes(),
                                result.getData().getPrepay() ,result.getData().getReservedTime());
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    private List<ChooseBathroomOuterAdapter.BathGroupWrapper> convertFrom(BathBuildingRespDTO bathBuildingRespDTO) {
        List<ChooseBathroomOuterAdapter.BathGroupWrapper> bathGroupWrappers = new ArrayList<>();
        for (BathFloorDTO floor : bathBuildingRespDTO.getFloors()) {
            for (BathGroupDTO group : floor.getGroups()) {
                int maxX = 0;
                int maxY = 0;
                for (BathRoomDTO room : group.getBathRooms()) {
                    if (room.getXaxis() > maxX) {
                        maxX = room.getXaxis();
                    }
                    if (room.getYaxis() > maxY) {
                        maxY = room.getYaxis();
                    }
                }
                ChooseBathroomOuterAdapter.BathGroupWrapper groupWrapper = new ChooseBathroomOuterAdapter.BathGroupWrapper();
                List<ChooseBathroomAdapter.BathroomWrapper> bathroomWrappers = new ArrayList<>();
                for (int i = 0; i < (maxX + 1) * (maxY + 1); i ++) {
                    bathroomWrappers.add(new ChooseBathroomAdapter.BathroomWrapper("",
                            ChooseBathroomAdapter.BathroomStatus.NONE));
                }

                for (BathRoomDTO room : group.getBathRooms()) {
                    bathroomWrappers.get(room.getYaxis() * maxX + room.getXaxis())
                            .setId(room.getId());
                    bathroomWrappers.get(room.getYaxis() * maxX + room.getXaxis())
                            .setName(room.getDeviceNo());
                    bathroomWrappers.get(room.getYaxis() * maxX + room.getXaxis())
                            .setStatus(ChooseBathroomAdapter.BathroomStatus.getStatus(room.getStatus()));
                }

                groupWrapper.setBathGroups(bathroomWrappers);
                groupWrapper.setName(floor.getFloorName() + group.getDisplayName());
                groupWrapper.setSpanWidth(maxX + 1);

                bathGroupWrappers.add(groupWrapper);
            }
        }

        return bathGroupWrappers;
    }
}
