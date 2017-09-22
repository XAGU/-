package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.ILostAndFoundApi;
import com.xiaolian.amigo.data.network.model.user.User;

/**
 * 失物招领
 * <p>
 * Created by zcd on 9/18/17.
 */

public interface ILostAndFoundDataManager extends ILostAndFoundApi {

    User getUserInfo();

}
