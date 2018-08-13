package com.xiaolian.amigo.ui.device.bathroom;

import android.text.TextUtils;
import android.util.Log;

import com.xiaolian.amigo.data.enumeration.BathTradeType;
import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathFloorDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathGroupDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathPreBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathRoomDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BuildingTrafficDTO;
import com.xiaolian.amigo.data.network.model.bathroom.TryBookingResultRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomView;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;

import static com.xiaolian.amigo.util.Constant.BOOKING_SUCCESS;
import static com.xiaolian.amigo.util.Constant.EMPTY;
import static com.xiaolian.amigo.util.Constant.NONE;
import static com.xiaolian.amigo.util.Constant.PRE_USING;
import static com.xiaolian.amigo.util.Constant.QUEUEING;

/**
 * @author zcd
 * @date 18/7/3
 */
public class ChooseBathroomPresenter<V extends IChooseBathroomView> extends BasePresenter<V>
        implements IChooseBathroomPresenter<V> {
    private static final String TAG = ChooseBathroomPresenter.class.getSimpleName();
    private IBathroomDataManager bathroomDataManager;
    private ISharedPreferencesHelp sharedPreferencesHelp ;

    @Inject
    public ChooseBathroomPresenter(IBathroomDataManager bathroomDataManager) {
        this.bathroomDataManager = bathroomDataManager;
    }

    @Override
    public void getBathroomList(long buildingId) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(buildingId);
        addObserver(bathroomDataManager.tree(reqDTO),
                new NetworkObserver<ApiResult<BathBuildingRespDTO>>() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onReady(ApiResult<BathBuildingRespDTO> result) {
                        if (null == result.getError()) {
                            getMvpView().setTvTitle(result.getData().getBuildingName());
                            getMvpView().refreshBathroom(convertFrom(result.getData()),
                                    result.getData().getMethods(), result.getData().getMissTimes());
                        } else {
                            getMvpView().onError(result.getError().getDisplayMessage());
                        }
                    }

                    @Override
                    public void onCompleted() {
                        getMvpView().hideBathroomDialog();
                    }
                });
    }

    @Override
    public void preBooking(String deviceNo) {
        BathBookingReqDTO reqDTO = new BathBookingReqDTO();
        if (TextUtils.isEmpty(deviceNo)){
            reqDTO.setDeviceNo(0);
            reqDTO.setType(BathTradeType.BOOKING_WITHOUT_DEVICE.getCode());
        }else {
            reqDTO.setDeviceNo(Integer.parseInt(deviceNo));
            reqDTO.setType(BathTradeType.BOOKING_DEVICE.getCode());
        }

        addObserver(bathroomDataManager.preBooking(reqDTO), new NetworkObserver<ApiResult<BathPreBookingRespDTO>>() {

            @Override
            public void onReady(ApiResult<BathPreBookingRespDTO> result) {
                if (null == result.getError()) {

                    getMvpView().gotoBookingView(result.getData().getPrepayInfo().getBalance(),
                            null, null,
                            null, null,
                            result.getData().getLocation(), result.getData().getMaxMissAbleTimes(),
                            result.getData().getPrepayInfo().getMinPrepay(), result.getData().getMissedTimes(),
                            result.getData().getPrepayInfo().getPrepay() ,result.getData().getReservedMinute());
//                    if (result.getData().getBonus() != null) {
//                        getMvpView().gotoBookingView(result.getData().getBalance(),
//                                result.getData().getBonus().getId(), result.getData().getBonus().getDescription(),
//                                result.getData().getBonus().getAmount(), result.getData().getExpiredTime(),
//                                result.getData().getLocation(), result.getData().getMaxMissAbleTimes(),
//                                result.getData().getMinPrepay(), result.getData().getMissedTimes(),
//                                result.getData().getPrepay() , result.getData().getReservedTime());
//                    }else{
//                        getMvpView().gotoBookingView(result.getData().getBalance(),
//                                null, null,
//                                null, result.getData().getExpiredTime(),
//                                result.getData().getLocation(), result.getData().getMaxMissAbleTimes(),
//                                result.getData().getMinPrepay(), result.getData().getMissedTimes(),
//                                result.getData().getPrepay() ,result.getData().getReservedTime());
//                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void precondition() {
        addObserver(bathroomDataManager.getOrderPrecondition(),new NetworkObserver<ApiResult<BathOrderPreconditionRespDTO>>(){
            @Override
            public void onStart() {
                getMvpView().showBathroomDialog();
            }

            @Override
            public void onReady(ApiResult<BathOrderPreconditionRespDTO> result) {
                 if (result.getError() == null){
                     getMvpView().saveBookingInfo(result.getData());
                     if (result.getData().getStatus() == QUEUEING){
                            getMvpView().startQueue(result.getData().getBathQueueId());
                     }else if (result.getData().getStatus() == BOOKING_SUCCESS){
                            getMvpView().startBooking(result.getData().getBathBookingId());
                     }else if (result.getData().getStatus() == PRE_USING){
                           getMvpView().startUsing(result.getData().getBathOrderId());
                     }
                 }
            }

            @Override
            public void onCompleted() {
                getMvpView().hideBathroomDialog();
            }
        });
    }

    @Override
    public boolean getBathroomPassword() {
       return  bathroomDataManager.getBathroomPassword();
    }

    @Override
    public void buildingTraffic(long id) {
        SimpleReqDTO reqDTO = new SimpleReqDTO();
        reqDTO.setId(id);
        addObserver(bathroomDataManager.traffi(reqDTO) ,new NetworkObserver<ApiResult<BuildingTrafficDTO>>(){

            @Override
            public void onReady(ApiResult<BuildingTrafficDTO> result) {
                if (result.getError() == null){
                    getMvpView().trafficInfo(result.getData().getFloors());
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });

    }

    @Override
    public void booking(long deviceNo, long floorId) {
        BathBookingReqDTO reqDTO = new BathBookingReqDTO();
        if (floorId != 0){
            reqDTO.setFloorId(floorId);
            reqDTO.setType(BathTradeType.BOOKING_WITHOUT_DEVICE.getCode());
        }else{
            reqDTO.setDeviceNo(deviceNo);
            reqDTO.setType(BathTradeType.BOOKING_DEVICE.getCode());
        }

        addObserver(bathroomDataManager.booking(reqDTO) , new NetworkObserver<ApiResult<TryBookingResultRespDTO>>(){


            @Override
            public void onReady(ApiResult<TryBookingResultRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getQueueInfo() != null){
                         getMvpView().startQueue(result.getData().getQueueInfo().getId());
                    }else{
                        if (result.getData().getBookingInfo()!= null){
                            getMvpView().startBooking(result.getData().getBookingInfo().getId());
                        }
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
                            NONE));
                }

                for (BathRoomDTO room : group.getBathRooms()) {
                    bathroomWrappers.get(room.getYaxis() * maxX + room.getXaxis())
                            .setId(room.getId());
                    bathroomWrappers.get(room.getYaxis() * maxX + room.getXaxis())
                            .setDeviceNo(room.getDeviceNo());
//                    Log.e(TAG, "convertFrom: " + room.getDeviceNo());
                    bathroomWrappers.get(room.getYaxis() * maxX + room.getXaxis())
                            .setStatus(room.getStatus());
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
