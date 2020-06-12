
package com.xuexiang.ppzj.utils.sdkinit;

import com.github.anrwatchdog.ANRWatchDog;
import com.xuexiang.xutil.common.logger.Logger;

/**
 * ANR看门狗监听器初始化
 *
 */
public final class ANRWatchDogInit {

    private static final String TAG = "ANRWatchDog";

    private ANRWatchDogInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * ANR看门狗
     */
    private static ANRWatchDog sANRWatchDog;

    /**
     * ANR监听触发的时间
     */
    private static final int ANR_DURATION = 4000;


    /**
     * ANR静默处理【就是不处理，直接记录一下日志】
     */
    private final static ANRWatchDog.ANRListener SILENT_LISTENER = error -> Logger.eTag(TAG, error);

    /**
     * ANR自定义处理【可以是记录日志用于上传】
     */
    private final static ANRWatchDog.ANRListener CUSTOM_LISTENER = error -> {
        Logger.eTag(TAG, "Detected Application Not Responding!", error);
        //这里进行ANR的捕获后的操作

        throw error;
    };

    public static void init() {
        //这里设置监听的间隔为2秒
        sANRWatchDog = new ANRWatchDog(2000);
        sANRWatchDog.setANRInterceptor(duration -> {
            long ret = ANR_DURATION - duration;
            if (ret > 0) {
                Logger.wTag(TAG, "Intercepted ANR that is too short (" + duration + " ms), postponing for " + ret + " ms.");
            }
            //当返回是0或者负数时，就会触发ANR监听回调
            return ret;
        }).setANRListener(CUSTOM_LISTENER).start();
    }

    public static ANRWatchDog getANRWatchDog() {
        return sANRWatchDog;
    }
}
