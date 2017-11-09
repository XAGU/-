package com.xiaolian.amigo.di.componet;

import com.xiaolian.amigo.di.MoreActivityContext;
import com.xiaolian.amigo.di.module.MoreActivityModule;
import com.xiaolian.amigo.ui.more.AboutUsActivity;
import com.xiaolian.amigo.ui.more.MoreActivity;

import dagger.Component;

/**
 * MoreActivityComponent
 * @author zcd
 */
@MoreActivityContext
@Component(dependencies = ApplicationComponent.class, modules = MoreActivityModule.class)
public interface MoreActivityComponent {
    void inject(MoreActivity activity);

    void inject(AboutUsActivity activity);
}
