package com.xiaolian.amigo.tmp.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yik on 2017/9/11.
 */

public class BeanFactory {

    private static Map<String, Object> beanMap = new HashMap<>();

    static {

        //Demo
        

    }

    public static <T> T getBean(Class<T> clazz) {
        return (T) beanMap.get(clazz.getName());
    }

    public static void registerBean(Object instance) {
        synchronized (beanMap) {
            if (beanMap.get(instance.getClass().getName()) != null) {
                beanMap.put(instance.getClass().getName(), instance);
            }
        }
    }
}
