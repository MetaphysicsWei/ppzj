package com.xuexiang.ppzj.core.webview;

import android.view.KeyEvent;

import com.just.agentweb.core.AgentWeb;
import com.xuexiang.ppzj.core.BaseFragment;

public abstract class BaseWebViewFragment extends BaseFragment {

    protected AgentWeb mAgentWeb;

    //===================生命周期管理===========================//
    @Override
    public void onResume() {
        if (mAgentWeb != null) {
            //恢复
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mAgentWeb != null) {
            //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mAgentWeb.getWebLifeCycle().onResume(); 恢复。
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mAgentWeb != null && mAgentWeb.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        if (mAgentWeb != null) {
            mAgentWeb.destroy();
        }
        super.onDestroyView();
    }
}
