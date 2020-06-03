/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.ppzj.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.xuexiang.ppzj.R;
import com.xuexiang.ppzj.adapter.entity.NewInfo;
import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示数据
 *
 * @author xuexiang
 * @since 2018/11/23 下午5:52
 */
public class DemoDataProvider {

    public static String[] titles = new String[]{
            "诗画浙江",
            "诗画浙江",
            "玩转浙江田园图",
            "浙江武学",
            "浙江龙舟图",
    };
//    轮播图图片

    public static String[] urls = new String[]{//640*360 360/640=0.5625
            "http://img.tourzj.com/img/topb4.jpg",//诗画浙江
            "http://img.tourzj.com/img/banner-hangzhou.jpg",//诗画浙江
            "http://img.tourzj.com/img/banner-lygl-list-li.png",//玩转浙江田园图"
            "http://cdn.imengyu.top/images/2.jpg",//浙江武学
            "http://cdn.imengyu.top/images/3.jpg",//浙江龙舟图


    };

    @MemoryCache
    public static List<BannerItem> getBannerList() {
        List<BannerItem> list = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            BannerItem item = new BannerItem();
            item.imgUrl = urls[i];
            item.title = titles[i];

            list.add(item);
        }
        return list;
    }

    /**
     * 用于占位的空信息
     *
     * @return
     */
    @MemoryCache
    public static List<NewInfo> getDemoNewInfos() {
        List<NewInfo> list = new ArrayList<>();
        list.add(new NewInfo("资讯", "良渚凭什么申遗成功？一眼5000年的古城，是一生必去的打卡地！")
                .setSummary("浙江杭州良渚古城遗址，被正式列入《世界遗产名录》，中国以55处世界遗产的数量，位列世界遗产总数第一！")
                .setDetailUrl("http://www.tourzj.gov.cn/play/ddetails_zixun.aspx?id=1864")
                .setImageUrl("http://img.tourzj.com/admin/media/eecfd82f-74b4-4311-a023-e805d63f4172.jpg"));
        list.add(new NewInfo("资讯", "别再惦记蓬莱仙境，去浙8个隐世禅修地当一回“无忧人”！")
                .setSummary("终日困在“钢筋森林”中的我们，被现实生活中的各类烦恼所扰。为何不寻一处清净之地，来一场修身养性的禅修之旅！")
                .setDetailUrl("http://www.tourzj.gov.cn/play/ddetails_zixun.aspx?id=1865")
                .setImageUrl("http://img.tourzj.com/admin/media/09064528-1ddd-4987-99e5-4718663f5935.jpg"));

        list.add(new NewInfo("资讯", "连非洲人都热回家的夏天，你能去哪儿？答案在这里……")
                .setSummary("到哪里才能逃离这个闷热的城市，找到一片cool到没边的清凉之地？")
                .setDetailUrl("http://www.tourzj.gov.cn/play/ddetails_zixun.aspx?id=1867")
                .setImageUrl("http://img.tourzj.com/admin/media/6ecb0a21-a457-432f-8468-60f6635cd19f.jpg"));

        list.add(new NewInfo("资讯", "刷遍夏日美景！浙江十大最美自驾公路，周末带上全家去兜风！")
                .setSummary("这条隐藏在新疆深处的“奇迹之路，其瑰伟不亚于川藏线")
                .setDetailUrl("http://www.tourzj.gov.cn/play/ddetails_zixun.aspx?id=1869")
                .setImageUrl("http://img.tourzj.com/admin/media/61d274cc-bc70-4b3d-9fbc-5db8d71503bc.jpg"));

        list.add(new NewInfo("资讯", "高温席卷的夏天，不如去这些堪比童话仙境的小众森林里乘个凉！")
                .setSummary("逶迤起伏的群山，云雾飘渺的天空。奔流而下的溪涧，凉爽清新的山风。")
                .setDetailUrl("http://www.tourzj.gov.cn/play/ddetails_zixun.aspx?id=1870")
                .setImageUrl("http://img.tourzj.com/admin/media/c284e0a2-2ea0-4528-91b5-d713e1fa6f4e.jpg"));

        list.add(new NewInfo("资讯", "洗肺又养眼！浙7条徒步道带你领略溢出屏幕的清新！")
                .setSummary("趁着天气还没有真正热起来，不如到山里运动一下，一起去看山花，挖野菜，轻松溜达，享受山海美景！")
                .setDetailUrl("http://www.tourzj.gov.cn/play/ddetails_zixun.aspx?id=1876")
                .setImageUrl("http://img.tourzj.com/admin/media/e467a122-4734-4ad4-b58a-3eee7ede3efa.jpg"));

        list.add(new NewInfo("资讯", "抢头条了！德云社烧饼、摇滚巨匠汪峰&黄贯中、灰姑娘…你最期待谁？")
                .setSummary("如果你想找笑点，浙里有德云社艺人为你讲相声；如果你想听天籁，浙里有来自巴黎圣母院的男童合唱团；如果你想嗨翻天，浙里有摇滚巨匠汪峰对战黄贯中。")
                .setDetailUrl("http://www.tourzj.gov.cn/play/ddetails_zixun.aspx?id=1877")
                .setImageUrl("http://img.tourzj.com/admin/media/a56dfb64-a7a6-4c1f-84a3-406ace18e28c.jpg"));

        list.add(new NewInfo("资讯", "浙6个深山隐居地，才是夏天最值得一去的旅行地！")
                .setSummary("一年中最清新的夏季时节又将到来 挑个日子，拣个山头，树下闲坐，看风吹起")
                .setDetailUrl("http://www.tourzj.gov.cn/play/ddetails_zixun.aspx?id=1883")
                .setImageUrl("http://img.tourzj.com/admin/media/2c8ea702-d55b-4c46-8182-0493e63f77f5.jpg"));
        return list;


    }

    public static List<AdapterItem> getGridItems(Context context) {
        return getGridItems(context, R.array.grid_titles_entry, R.array.grid_icons_entry);
    }


    private static List<AdapterItem> getGridItems(Context context, int titleArrayId, int iconArrayId) {
        List<AdapterItem> list = new ArrayList<>();
        String[] titles = ResUtils.getStringArray(titleArrayId);
        Drawable[] icons = ResUtils.getDrawableArray(context, iconArrayId);
        for (int i = 0; i < titles.length; i++) {
            list.add(new AdapterItem(titles[i], icons[i]));
        }
        return list;
    }

}
