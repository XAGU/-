package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.IUserApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.user.User;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Part;

/**
 * 个人信息模块DataManager
 * @author zcd
 */

public interface IUserDataManager extends IUserApi {
    Observable<ApiResult<String>> uploadFile(@Part("filename=\"image.jpg\"") RequestBody images);


    User getUser();

    void setUser(User user);

}
