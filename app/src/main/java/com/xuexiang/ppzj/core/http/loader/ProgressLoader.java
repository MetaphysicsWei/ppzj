
package com.xuexiang.ppzj.core.http.loader;

import android.content.Context;

import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;

/**
 * 创建进度加载者
 *
 */
public final class ProgressLoader {

    private ProgressLoader() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static IProgressLoaderFactory sIProgressLoaderFactory = new MiniProgressLoaderFactory();

    public static void setIProgressLoaderFactory(IProgressLoaderFactory sIProgressLoaderFactory) {
        ProgressLoader.sIProgressLoaderFactory = sIProgressLoaderFactory;
    }

    /**
     * 创建进度加载者
     *
     * @param context
     * @return
     */
    public static IProgressLoader create(Context context) {
        return sIProgressLoaderFactory.create(context);
    }

    /**
     * 创建进度加载者
     *
     * @param context
     * @param message 默认提示信息
     * @return
     */
    public static IProgressLoader create(Context context, String message) {
        return sIProgressLoaderFactory.create(context, message);
    }
}
