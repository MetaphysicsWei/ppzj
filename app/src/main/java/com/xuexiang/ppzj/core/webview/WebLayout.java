

package com.xuexiang.ppzj.core.webview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.just.agentweb.widget.IWebLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.ppzj.R;


public class WebLayout implements IWebLayout {

    private final SmartRefreshLayout mSmartRefreshLayout;
    private WebView mWebView;

    public WebLayout(Activity activity) {
        mSmartRefreshLayout = (SmartRefreshLayout) LayoutInflater.from(activity).inflate(R.layout.fragment_pulldown_web, null);
        mWebView = mSmartRefreshLayout.findViewById(R.id.webView);
    }

    @NonNull
    @Override
    public ViewGroup getLayout() {
        return mSmartRefreshLayout;
    }

    @Nullable
    @Override
    public WebView getWebView() {
        return mWebView;
    }


}
