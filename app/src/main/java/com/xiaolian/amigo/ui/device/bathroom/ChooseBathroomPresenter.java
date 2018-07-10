package com.xiaolian.amigo.ui.device.bathroom;

import com.xiaolian.amigo.data.manager.intf.IBathroomDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathBuildingRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathFloorDTO;
import com.xiaolian.amigo.data.network.model.bathroom.BathGroupDTO;
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
        reqDTO.setId(1L);
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

    private List<ChooseBathroomOuterAdapter.BathGroupWrapper> convertFrom(BathBuildingRespDTO bathBuildingRespDTO) {
        List<ChooseBathroomOuterAdapter.BathGroupWrapper> bathGroupWrappers = new ArrayList<>();
        for (BathFloorDTO floor : bathBuildingRespDTO.getFloors()) {
            for (BathGroupDTO group : floor.getGroups()) {
                int maxX = 0;
                int maxY = 0;
                for (BathRoomDTO room : group.getBathRooms()) {
                    if (room.getXAxis() > maxX) {
                        maxX = room.getXAxis();
                    }
                    if (room.getYAxis() > maxY) {
                        maxY = room.getYAxis();
                    }
                }
                ChooseBathroomOuterAdapter.BathGroupWrapper groupWrapper = new ChooseBathroomOuterAdapter.BathGroupWrapper();
                List<ChooseBathroomAdapter.BathroomWrapper> bathroomWrappers = new ArrayList<>();
                for (int i = 0; i < (maxX + 1) * (maxY + 1); i ++) {
                    bathroomWrappers.add(new ChooseBathroomAdapter.BathroomWrapper("",
                            ChooseBathroomAdapter.BathroomStatus.NONE));
                }

                for (BathRoomDTO room : group.getBathRooms()) {
                    bathroomWrappers.get(room.getYAxis() * maxX + room.getXAxis())
                            .setId(room.getId());
                    bathroomWrappers.get(room.getYAxis() * maxX + room.getXAxis())
                            .setName(room.getDeviceNo());
                    bathroomWrappers.get(room.getYAxis() * maxX + room.getXAxis())
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
