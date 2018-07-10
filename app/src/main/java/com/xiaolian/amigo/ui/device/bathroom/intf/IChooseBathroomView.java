package com.xiaolian.amigo.ui.device.bathroom.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.device.bathroom.ChooseBathroomOuterAdapter;

import java.util.List;

/**
 * @author zcd
 * @date 18/7/3
 */
public interface IChooseBathroomView extends IBaseView {
    void refreshBathroom(List<ChooseBathroomOuterAdapter.BathGroupWrapper> wrappers,
                         List<Integer> methods, Integer missTimes);
}
