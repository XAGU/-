package com.xiaolian.amigo.data.enumeration.annotation;

import android.support.annotation.IntDef;

import static com.xiaolian.amigo.data.enumeration.annotation.LostAndFound.FOUND;
import static com.xiaolian.amigo.data.enumeration.annotation.LostAndFound.LOST;

/**
 * @author zcd
 * @date 18/5/13
 */
@IntDef({LOST, FOUND})
public @interface LostAndFound {
    int LOST = 1;
    int FOUND = 2;
}
