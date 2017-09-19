package com.xiaolian.amigo.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭工具类
 * <p>
 * Created by zcd on 9/19/17.
 */

public final class CloseUtils {

    private CloseUtils() {
        throw new UnsupportedOperationException("can't instantiate CloseUtils");
    }

    /**
     * 关闭IO
     *
     * @param closeables closeables
     */
    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 安静关闭IO
     *
     * @param closeables closeables
     */
    public static void closeIOQuietly(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
