package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.data.network.model.user.SchoolNameListRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

import java.util.List;

public interface IChooseSchoolView  extends IBaseView{
    void referSchoolList(List<SchoolNameListRespDTO.SchoolNameListBean> schoolNameList);

    void setreferComplete();
}
