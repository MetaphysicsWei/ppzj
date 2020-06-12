

package com.xuexiang.ppzj.core.webview;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

import com.just.agentweb.core.web.AgentWebUIControllerImplBase;

import java.lang.ref.WeakReference;

public class UIController extends AgentWebUIControllerImplBase {

    private WeakReference<Activity> mActivity;

    public UIController(Activity activity) {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    public void onShowMessage(String message, String from) {
        super.onShowMessage(message, from);
        Log.i(TAG, "message:" + message);
    }

    @Override
    public void onSelectItemsPrompt(WebView view, String url, String[] items, Handler.Callback callback) {
        // 使用默认的UI
        super.onSelectItemsPrompt(view, url, items, callback);
    }

}
