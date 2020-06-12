
package com.xuexiang.ppzj.core.webview;

import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebView;

import com.just.agentweb.core.client.MiddlewareWebChromeBase;


public class MiddlewareChromeClient extends MiddlewareWebChromeBase {

    public MiddlewareChromeClient() {

    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Log.i("Info", "onJsAlert:" + url);
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        Log.i("Info", "onProgressChanged:");
    }
}
