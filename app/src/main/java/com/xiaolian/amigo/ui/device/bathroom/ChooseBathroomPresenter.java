package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Context;
import android.content.Intent;

import com.xiaolian.amigo.data.enumeration.BathTradeType;
import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBookingStatusReqDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderCurrentRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathOrderPreconditionRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BuildingTrafficDTO;
import com.xiaolian.amigo.data.network.model.bathroom.TryBookingResultRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleReqDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomPresenter;
import com.xiaolian.amigo.ui.device.bathroom.intf.IChooseBathroomView;
import com.xiaolian.amigo.util.RxHelper;

import javax.inject.Inject;

import rx.functions.Action1;

import static com.xiaolian.amigo.util.Constant.ACCEPTED;
import static com.xiaolian.amigo.util.Constant.BOOKING_SUCCESS;
import static com.xiaolian.amigo.util.Constant.FAIL;
import static com.xiaolian.amigo.util.Constant.INIT;
import static com.xiaolian.amigo.util.Constant.OPENED;
import static com.xiaolian.amigo.util.Constant.ORDER_SETTLE;
import static com.xiaolian.amigo.util.Constant.ORDER_USING;
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

    private static int time = 0  ;
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
//                            getMvpView().refreshBathroom(convertFrom(result.getData()),
//                                    result.getData().getMethods(), result.getData().getMissTimes());
                            getMvpView().refreshBathroom(result.getData());
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
    public void precondition(boolean isShowDialog) {
        addObserver(bathroomDataManager.getOrderPrecondition(),new NetworkObserver<ApiResult<BathOrderPreconditionRespDTO>>(){
            @Override
            public void onStart() {
                if (isShowDialog) {
                    getMvpView().showBathroomDialog();
                }
            }

            @Override
            public void onReady(ApiResult<BathOrderPreconditionRespDTO> result) {
                 if (result.getError() == null){
                     getMvpView().saveBookingInfo(result.getData());
                     if (result.getData().getStatus() == QUEUEING){
                            getMvpView().startQueue(result.getData().getBathQueueId());
                     }else if (result.getData().getStatus() == BOOKING_SUCCESS){
                         /**
                          * 进入预约流程 ,查询预约状态
                          */
                          queryBooking(result.getData().getBathBookingId());
                     }else if (result.getData().getStatus() == PRE_USING){
                         /**
                          * 进入用水流程，查询用水状态
                          */
                           queryBathorder(result.getData().getBathOrderId());
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
            public void onStart() {
                super.onStart();
                if (deviceNo > 0){
                    getMvpView().showBathroomDialog();
                }
            }

            @Override
            public void onReady(ApiResult<TryBookingResultRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getQueueInfo() != null){
                         clearTime();
                         getMvpView().startQueue(result.getData().getQueueInfo().getId());
                    }else{
                        if (result.getData().getBookingInfo()!= null){
                            if (result.getData().getBookingInfo().getStatus() ==ACCEPTED){
                                getMvpView().startBooking(result.getData().getBookingInfo().getId());
                            }else if (result.getData().getBookingInfo().getStatus() == INIT){
                                queryBooking(result.getData().getBookingInfo().getId());
                            }
                        }
                    }
                } else {
                    getMvpView().hideBathroomDialog();
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    @Override
    public void clearTime() {
        time = 0 ;
    }

    @Override
    public void queryBooking(long bookingId) {
        BathBookingStatusReqDTO simpleReqDTO = new BathBookingStatusReqDTO();
        simpleReqDTO.setId(bookingId +"");
        addObserver(bathroomDataManager.query(simpleReqDTO) , new NetworkObserver<ApiResult<BathBookingRespDTO>>(){


            @Override
            public void onStart() {

            }

            @Override
            public void onReady(ApiResult<BathBookingRespDTO> result) {

                 if (result.getError() == null){
                       if (result.getData().getStatus() == ACCEPTED){
                           getMvpView().hideBathroomDialog();
                           clearTime();
                            getMvpView().startBooking(result.getData().getId());
                       }else if (result.getData().getStatus() == OPENED){
                           getMvpView().hideBathroomDialog();
                           clearTime();
                           getMvpView().startUsing(result.getData().getBathOrderId());
                       }else if (result.getData().getStatus() == INIT){
                           if (time < 5) {
                               delay(3, new Action1<Long>() {
                                   @Override
                                   public void call(Long aLong) {
                                       time++;
                                        queryBooking(bookingId);

                                   }
                               });
                           }else{
                               getMvpView().hideBathroomDialog();
                                   clearTime();
                                   precondition(true);
                           }
                       }else if (result.getData().getStatus() == FAIL){
                           getMvpView().hideBathroomDialog();
                           clearTime();
                           getMvpView().onError("预约失败");
                       }else{
                           getMvpView().hideBathroomDialog();
                           clearTime();
                       }
                 }else{
                     getMvpView().hideBathroomDialog();
                     clearTime();
                     getMvpView().onError(result.getError().getDisplayMessage());
                 }
            }
        });
    }

    @Override
    public void queryBathorder(long bathOrder) {
        SimpleReqDTO simpleReqDTO = new SimpleReqDTO();
        simpleReqDTO.setId(bathOrder);
        addObserver(bathroomDataManager.orderQuery(simpleReqDTO) , new NetworkObserver<ApiResult<BathOrderCurrentRespDTO>>(){

            @Override
            public void onReady(ApiResult<BathOrderCurrentRespDTO> result) {
                if (result.getError() == null){
                    if (result.getData().getStatus() == ORDER_USING){
                        getMvpView().startUsing(result.getData().getBathOrderId());
                    }else if (result.getData().getStatus() == ORDER_SETTLE){
                        getMvpView().startOrderInfo(result.getData());
                    }
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }


//    private List<ChooseBathroomOuterAdapter.BathGroupWrapper> convertFrom(BathBuildingRespDTO bathBuildingRespDTO) {
//        List<ChooseBathroomOuterAdapter.BathGroupWrapper> bathGroupWrappers = new ArrayList<>();
//        for (BathFloorDTO floor : bathBuildingRespDTO.getFloors()) {
//            for (BathGroupDTO group : floor.getGroups()) {
//                int maxX = 0;
//                int maxY = 0;
//                for (BathRoomDTO room : group.getBathRooms()) {
//                    if (room.getXaxis() > maxX) {
//                        maxX = room.getXaxis();
//                    }
//                    if (room.getYaxis() > maxY) {
//                        maxY = room.getYaxis();
//                    }
//                }
//                ChooseBathroomOuterAdapter.BathGroupWrapper groupWrapper = new ChooseBathroomOuterAdapter.BathGroupWrapper();
//                List<ChooseBathroomAdapter.BathroomWrapper> bathroomWrappers = new ArrayList<>();
//                for (int i = 0; i < (maxX + 1) * (maxY + 1); i ++) {
//                    bathroomWrappers.add(new ChooseBathroomAdapter.BathroomWrapper("",
//                            NONE));
//                }
//
//                for (BathRoomDTO room : group.getBathRooms()) {
//                    bathroomWrappers.get(room.getYaxis() * maxX + room.getXaxis())
//                            .setId(room.getId());
//                    bathroomWrappers.get(room.getYaxis() * maxX + room.getXaxis())
//                            .setDeviceNo(room.getDeviceNo());
////                    Log.e(TAG, "convertFrom: " + room.getDeviceNo());
//                    bathroomWrappers.get(room.getYaxis() * maxX + room.getXaxis())
//                            .setStatus(room.getStatus());
//                }
//
//                groupWrapper.setBathGroups(bathroomWrappers);
//                groupWrapper.setName(floor.getFloorName() + group.getDisplayName());
//                groupWrapper.setSpanWidth(maxX + 1);
//
//                bathGroupWrappers.add(groupWrapper);
//            }
//        }
//
//        return bathGroupWrappers;
//    }


    private void delay(int time ,  Action1<Long> action0 ){
        this.subscriptions.add(RxHelper.delay(time , action0));
    }
}
