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

@LoginActivityContext
public interface ILoginPresenter<V extends ILoginView> extends IBasePresenter<V> {

    // 点击登录
    void onLoginClick(String mobile, String password, String androidId,
                      String brand, String model, String systemVersion);

    // 注册
    void register(String code, String mobile,
                  String password, Long schoolId,
                  String androidId, String brand,
                  String model, String systemVersion);

    // 获取验证码
    void getVerification(String mobile);

    // 校验验证码
    void checkVerification(String mobile, String code);

    // 密码重置
    void resetPassword(String mobile, String password, String code);

    void logout();

    String getMobile();
}
