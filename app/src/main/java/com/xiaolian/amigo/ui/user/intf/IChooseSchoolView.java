package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.data.network.model.user.School;
import com.xiaolian.amigo.data.network.model.user.SchoolNameListRespDTO;
import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.widget.school.mode.CityBean;

import java.util.List;

public interface IChooseSchoolView  extends IBaseView{
    void referSchoolList(List<SchoolNameListRespDTO.SchoolNameListBean> schoolNameList);

    void setreferComplete();

    void backToProfile();

    void showOnLineSchool(List<School> schools);

    void finishResult(CityBean cityBean);

    void showChangeSchoolDialog();

    void requestChangeShool();
}
