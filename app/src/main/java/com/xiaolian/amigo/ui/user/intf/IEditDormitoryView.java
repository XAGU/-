package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.user.adaptor.EditDormitoryAdaptor;

import java.util.List;

/**
 * 编辑宿舍
 * <p>
 * Created by zcd on 9/19/17.
 */

public interface IEditDormitoryView extends IBaseView {
    void addMore(List<EditDormitoryAdaptor.UserResidenceWrapper> userResidenceWrappers);

    void notifyAdaptor();
}
