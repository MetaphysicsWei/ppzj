/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.ppzj.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.xuexiang.ppzj.R;
import com.xuexiang.ppzj.core.BaseActivity;
import com.xuexiang.ppzj.core.BaseFragment;
import com.xuexiang.ppzj.fragment.AboutFragment;
import com.xuexiang.ppzj.fragment.SettingsFragment;
import com.xuexiang.ppzj.fragment.news.NewsFragment;
import com.xuexiang.ppzj.fragment.profile.ProfileFragment;
import com.xuexiang.ppzj.fragment.trending.TrendingFragment;
import com.xuexiang.ppzj.utils.Utils;
import com.xuexiang.ppzj.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.common.CollectionUtils;
import com.xuexiang.xutil.display.Colors;

import butterknife.BindView;
import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

import static android.content.ContentValues.TAG;

/**
 * 程序主页面
 *
 * @author xuexiang
 * @since 2019-07-07 23:53
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener, ClickUtils.OnClick2ExitListener, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    /**
     * 底部导航栏
     */
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    /**
     * 侧边栏
     */
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    private String[] mTitles;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();

        initListeners();

        Weather(); //初始化天气数据


    }

    private void Weather(){
      //  setContentView(R.layout.include_navigation_header);

        TextView weather_city = findViewById(R.id.weather_city);
        TextView weather_tmp = findViewById(R.id.weather_tmp);


        HeConfig.init("HE2006061649011733", "b4a4b372228543ee86870b6620d23c47");
        HeConfig.switchToFreeServerNode();

        HeWeather.getWeatherNow(MainActivity.this, "温州", Lang.CHINESE_SIMPLIFIED , Unit.METRIC , new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "Weather Now onError: ", e);
            }

            @Override
            public void onSuccess(Now dataObject) {
                Log.i(TAG, " Weather Now onSuccess: " + new Gson().toJson(dataObject));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if ( Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus()) ){
                    //此时返回数据
                    NowBase now = dataObject.getNow();

                    Basic base = dataObject.getBasic();
                    Log.i(TAG,"当前地区："+base.getCnty()+"——"+base.getAdmin_area()+"——"+base.getParent_city()+"——"+base.getLocation());
                    Log.i(TAG, " 当前温度为：: " + now.getTmp());

//                    weather_city.setText("地区：" + base.getLocation());
//                    weather_tmp.setText("温度:" +"   " +  now.getTmp() + "℃");


                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });
    }

    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    private void initViews() {
        mTitles = ResUtils.getStringArray(R.array.home_titles);
        toolbar.setTitle(mTitles[0]);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(this);

        initHeader();

        //主页内容填充
        BaseFragment[] fragments = new BaseFragment[]{
                new NewsFragment(),
                new TrendingFragment(),
                new ProfileFragment()
        };
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getSupportFragmentManager(), fragments);
        viewPager.setOffscreenPageLimit(mTitles.length - 1);
        viewPager.setAdapter(adapter);
    }

    private void initHeader() {
        navView.setItemIconTintList(null);
        View headerView = navView.getHeaderView(0);
        LinearLayout navHeader = headerView.findViewById(R.id.nav_header);
        RadiusImageView ivAvatar = headerView.findViewById(R.id.iv_avatar);
        TextView tvAvatar = headerView.findViewById(R.id.tv_avatar);
        TextView tvSign = headerView.findViewById(R.id.tv_sign);

        if (Utils.isColorDark(ThemeUtils.resolveColor(this, R.attr.colorAccent))) {
            tvAvatar.setTextColor(Colors.WHITE);
            tvSign.setTextColor(Colors.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar.setImageTintList(ResUtils.getColors(R.color.xui_config_color_white));
            }
        } else {
            tvAvatar.setTextColor(ThemeUtils.resolveColor(this, R.attr.xui_config_color_title_text));
            tvSign.setTextColor(ThemeUtils.resolveColor(this, R.attr.xui_config_color_explain_text));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar.setImageTintList(ResUtils.getColors(R.color.xui_config_color_gray_3));
            }
        }

        // TODO: 2019-10-09 初始化数据
        ivAvatar.setImageResource(R.drawable.ic_default_head);
        tvAvatar.setText(R.string.app_name);
        tvSign.setText("软件18-1");
        navHeader.setOnClickListener(this);
    }

    protected void initListeners() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //侧边栏点击事件
        navView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.isCheckable()) {
                drawerLayout.closeDrawers();
                return handleNavigationItemSelected(menuItem);
            } else {
                switch (menuItem.getItemId()) {
                    case R.id.nav_settings:
                        openNewPage(SettingsFragment.class);
                        break;
                    case R.id.nav_search:
                        openNewPage(TrendingFragment.class);
                        break;
                    case R.id.nav_about:
                        openNewPage(AboutFragment.class);
                        break;
                    default:
                        XToastUtils.toast("点击了:" + menuItem.getTitle());
                        break;
                }
            }
            return true;
        });

        //主页事件监听
        viewPager.addOnPageChangeListener(this);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
    }



    /**
     * 处理侧边栏点击事件
     *
     * @param menuItem
     * @return
     */
    private boolean handleNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(mTitles, menuItem.getTitle());
        if (index != -1) {
            toolbar.setTitle(menuItem.getTitle());
            viewPager.setCurrentItem(index, false);
            return true;
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_privacy:
                Utils.showPrivacyDialog(this, null);
                break;
            default:
                break;
        }
        return false;
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header:
                XToastUtils.toast("点击头部！");
                break;
            default:
                break;
        }
    }

    //=============ViewPager===================//

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        MenuItem item = bottomNavigation.getMenu().getItem(position);
        toolbar.setTitle(item.getTitle());
        item.setChecked(true);

        updateSideNavStatus(item);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    //================Navigation================//

    /**
     * 底部导航栏点击事件
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(mTitles, menuItem.getTitle());
        if (index != -1) {
            toolbar.setTitle(menuItem.getTitle());
            viewPager.setCurrentItem(index, false);

            updateSideNavStatus(menuItem);
            return true;
        }
        return false;
    }

    /**
     * 更新侧边栏菜单选中状态
     *
     * @param menuItem
     */
    private void updateSideNavStatus(MenuItem menuItem) {
        MenuItem side = navView.getMenu().findItem(menuItem.getItemId());
        if (side != null) {
            side.setChecked(true);
        }
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click(2000, this);
        }
        return true;
    }

    @Override
    public void onRetry() {
        XToastUtils.toast("再按一次退出程序");
    }

    @Override
    public void onExit() {
        XUtil.get().exitApp();
    }


}
