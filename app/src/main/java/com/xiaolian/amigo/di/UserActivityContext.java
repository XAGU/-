package com.xiaolian.amigo.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * UserActivityContext
 * @author zcd
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UserActivityContext {
}
