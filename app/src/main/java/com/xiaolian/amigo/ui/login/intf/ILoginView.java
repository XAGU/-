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


import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 登录页面
 * @author caidong
 * @date 17/9/14
 */
public interface ILoginView extends IBaseView {

    /**
     * 跳转到登录页面
     */
    void gotoLoginView();

    /**
     * 跳转到注册第一步页面
     */
    void gotoRegisterStep1View();

    /**
     * 跳转到注册第二步页面
     */
    void gotoRegisterStep2View();

    /**
     * 启动计时器
     */
    void startTimer();

    /**
     * 跳转到主页
     */
    void gotoMainView();

    void alipayLogin(String authCode);

    void showTipDialog(String title,String content);

}
