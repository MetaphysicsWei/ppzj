

package com.xuexiang.ppzj.fragment;

import com.xuexiang.ppzj.R;
import com.xuexiang.ppzj.core.BaseFragment;
import com.xuexiang.ppzj.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import butterknife.BindView;


@Page(name = "设置")
public class SettingsFragment extends BaseFragment implements SuperTextView.OnSuperTextViewClickListener {

    @BindView(R.id.menu_common)
    SuperTextView menuCommon;
    @BindView(R.id.menu_privacy)
    SuperTextView menuPrivacy;
    @BindView(R.id.menu_push)
    SuperTextView menuPush;
    @BindView(R.id.menu_helper)
    SuperTextView menuHelper;
    @BindView(R.id.menu_change_account)
    SuperTextView menuChangeAccount;
    @BindView(R.id.menu_logout)
    SuperTextView menuLogout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initViews() {
        menuCommon.setOnSuperTextViewClickListener(this);
        menuPrivacy.setOnSuperTextViewClickListener(this);
        menuPush.setOnSuperTextViewClickListener(this);
        menuHelper.setOnSuperTextViewClickListener(this);
        menuChangeAccount.setOnSuperTextViewClickListener(this);
        menuLogout.setOnSuperTextViewClickListener(this);
    }

    @SingleClick
    @Override
    public void onClick(SuperTextView superTextView) {
        switch(superTextView.getId()) {
            case R.id.menu_common:
            case R.id.menu_privacy:
            case R.id.menu_push:
            case R.id.menu_helper:
                XToastUtils.toast(superTextView.getLeftString());
                break;
            case R.id.menu_change_account:
            case R.id.menu_logout:
                XToastUtils.toast(superTextView.getCenterString());
                break;
            default:
                break;
        }
    }
}
