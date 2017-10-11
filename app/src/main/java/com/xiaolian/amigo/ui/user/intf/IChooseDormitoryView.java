package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseListView;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;

import java.util.List;

/**
 * 宿舍列表
 * <p>
 * Created by zcd on 10/11/17.
 */

public interface IChooseDormitoryView extends IBaseListView {
    void addMore(List<EditDormitoryAdaptor.UserResidenceWrapper> wrappers);

    void backToDevice(String macAddress, String location);
}
