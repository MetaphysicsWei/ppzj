

package com.xuexiang.ppzj.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.KeyEvent;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.xuexiang.ppzj.R;
import com.xuexiang.ppzj.utils.MMKVUtils;
import com.xuexiang.ppzj.utils.Utils;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.activity.BaseSplashActivity;
import com.xuexiang.xutil.app.ActivityUtils;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 启动页【无需适配屏幕大小】
 *
 */
public class SplashActivity extends BaseSplashActivity implements CancelAdapt {

    @Override
    protected long getSplashDurationMillis() {
        return 500;
    }

    /**
     * activity启动后的初始化
     */
    @Override
    protected void onCreateActivity() {
        initSplashView(R.drawable.xui_config_bg_splash);
        startSplash(false);
        requestPermissions();
    }

    // 要申请的权限
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private void requestPermissions() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, permissions, 321);
            }
        }
    }

    /**
     * 启动页结束后的动作
     */
    @Override
    protected void onSplashFinished() {
        boolean isAgree = MMKVUtils.getBoolean("key_agree_privacy", false);
        if (isAgree) {
            ActivityUtils.startActivity(MainActivity.class);
            finish();
        } else {
            Utils.showPrivacyDialog(this, (dialog, which) -> {
                dialog.dismiss();
                MMKVUtils.put("key_agree_privacy", true);
                ActivityUtils.startActivity(MainActivity.class);
                finish();
            });
        }
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
    }
}
