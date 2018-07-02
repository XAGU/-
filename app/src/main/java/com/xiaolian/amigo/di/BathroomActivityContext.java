package com.xiaolian.amigo.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
import javax.inject.Scope;

/**
 * @author zcd
 * @date 18/6/29
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface BathroomActivityContext {
}
