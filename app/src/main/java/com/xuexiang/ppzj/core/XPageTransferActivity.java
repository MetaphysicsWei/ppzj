
package com.xuexiang.ppzj.core;

import android.os.Bundle;

import com.xuexiang.ppzj.utils.XToastUtils;
import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xrouter.annotation.Router;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xutil.common.StringUtils;

/**
 * https://xuexiangjys.club/xpage/transfer?pageName=xxxxx&....
 * applink的中转
 *
 */
@Router(path = "/xpage/transfer")
public class XPageTransferActivity extends BaseActivity {

    @AutoWired(name = "pageName")
    String pageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XRouter.getInstance().inject(this);

        if (!StringUtils.isEmpty(pageName)) {
            if (openPage(pageName, getIntent().getExtras()) == null) {
                XToastUtils.error("页面未找到！");
                finish();
            }
        } else {
            XToastUtils.error("页面未找到！");
            finish();
        }
    }
}
