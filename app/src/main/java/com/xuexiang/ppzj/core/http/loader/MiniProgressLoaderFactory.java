
package com.xuexiang.ppzj.core.http.loader;

import android.content.Context;

import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader;

/**
 * 迷你加载框创建工厂
 */
public class MiniProgressLoaderFactory implements IProgressLoaderFactory {

    @Override
    public IProgressLoader create(Context context) {
        return new MiniLoadingDialogLoader(context);
    }

    @Override
    public IProgressLoader create(Context context, String message) {
        return new MiniLoadingDialogLoader(context, message);
    }
}
