/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.xiaolian.amigo.ui.login.intf;


import com.xiaolian.amigo.di.LoginActivityContext;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 登录页presenter
 *
 * @author caidong
 * @date 17/9/14
 */
@LoginActivityContext
public interface ILoginPresenter<V extends ILoginView> extends IBasePresenter<V> {

    /**
     * 点击登录
     *
     * @param mobile        手机号
     * @param password      密码
     * @param androidId     androidId
     * @param brand         手机品牌
     * @param model         手机型号
     * @param systemVersion 系统版本
     * @param appVersion    app版本
     */
    void onLoginClick(String mobile, String password, String androidId,
                      String brand, String model, String systemVersion,
                      String appVersion);

    /**
     * 注册
     *
     * @param code          验证码
     * @param mobile        手机号
     * @param password      密码
     * @param schoolId      学校id
     * @param androidId     androidId
     * @param brand         手机品牌
     * @param model         手机型号
     * @param systemVersion 系统版本
     * @param appVersion    app版本
     */
    void register(String code, String mobile,
                  String password, Long schoolId,
                  String androidId, String brand,
                  String model, String systemVersion,
                  String appVersion);

    /**
     * 获取验证码
     *
     * @param mobile 手机号
     */
    void getVerification(String mobile);

    /**
     * 校验验证码
     *
     * @param mobile 手机号
     * @param code   验证码
     */
    void checkVerification(String mobile, String code);

    /**
     * 密码重置
     *
     * @param mobile   手机号
     * @param password 密码
     * @param code     验证码
     */
    void resetPassword(String mobile, String password, String code);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 获取手机号
     *
     * @return 手机号
     */
    String getMobile();

    void deletePushToken();
}
