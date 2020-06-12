
package com.xuexiang.ppzj.fragment.news;

import android.os.Message;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.ppzj.R;
import com.xuexiang.ppzj.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.ppzj.adapter.base.delegate.SingleDelegateAdapter;
import com.xuexiang.ppzj.adapter.entity.NewInfo;
import com.xuexiang.ppzj.core.BaseFragment;
import com.xuexiang.ppzj.utils.DemoDataProvider;
import com.xuexiang.ppzj.utils.Utils;
import com.xuexiang.ppzj.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.imageview.ImageLoader;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import java.util.Map;
import android.os.Handler;

import butterknife.BindView;
import redis.clients.jedis.Jedis;
/**
 * 首页动态
 *
 */
@Page(anim = CoreAnim.none)
public class NewsFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private SimpleDelegateAdapter<NewInfo> mNewsAdapter;

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext());
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        //轮播条
        SingleDelegateAdapter bannerAdapter = new SingleDelegateAdapter(R.layout.include_head_view_banner) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                SimpleImageBanner banner = holder.findViewById(R.id.sib_simple_usage);
                banner.setSource(DemoDataProvider.getBannerList())
                        .setOnItemClickListener((view, item, position1) -> XToastUtils.toast("headBanner position--->" + position1)).startScroll();
            }
        };
        //九宫格菜单
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setPadding(0, 16, 0, 0);
        gridLayoutHelper.setVGap(10);
        gridLayoutHelper.setHGap(0);
        SimpleDelegateAdapter<AdapterItem> commonAdapter = new SimpleDelegateAdapter<AdapterItem>(R.layout.adapter_common_grid_item, gridLayoutHelper, DemoDataProvider.getGridItems(getContext())) {
            private Map myset;

            @Override
            protected void bindData(@NonNull RecyclerViewHolder holder, int position, AdapterItem item) {
                //主线程中创建一个Handler对象，用于获取子线程数据
                Handler handler=new Handler() {
                    @Override
                    public void handleMessage(android.os.Message msg){
                        //接收子线程发送的数据
                         myset = (Map) msg.obj;
                    };
                };
                // 创建一个子线程来连接数据库并获取数据库中对应表的数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Jedis jedis = new Jedis("129.226.188.246",6379);//ip和端口号
                        jedis.auth("ppzj123456");
                        Map<String, String> myset = jedis.hgetAll("ppzj");

                        //在子线程中创建一个Message对象，为携带子线程数据给主线程
                        Message message = new Message();
                        message.obj = myset;
                        //使用handler对象将message发送到主线程
                        handler.sendMessage(message);
                        jedis.close();
                    }
                }).start();

                if (item != null) {
                    RadiusImageView imageView = holder.findViewById(R.id.riv_item);
                    imageView.setCircle(true);
                    ImageLoader.get().loadImage(imageView, item.getIcon());
                    // holder.text(R.id.tv_title, item.getTitle().toString().substring(0, 1));  取消显示标题第一个字的功能
                    holder.text(R.id.tv_sub_title, item.getTitle());
                    holder.click(R.id.ll_container, v ->
                            new MaterialDialog.Builder(getContext())
                                    //标题
                                    .title(item.getTitle())
                                    //文本内容
                                    .content((CharSequence) myset.get(item.getTitle()))
                                    //确认按键
                                    .positiveText("了解")
                                    .show());
                }

            }
        };

        //资讯的标题
        SingleDelegateAdapter titleAdapter = new SingleDelegateAdapter(R.layout.adapter_title_item) {
            @Override
            public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
                holder.text(R.id.tv_title, "文旅资讯");
                holder.text(R.id.tv_action, "更多");
                holder.click(R.id.tv_action, v -> XToastUtils.toast("更多"));
            }
        };

        //资讯
        mNewsAdapter = new SimpleDelegateAdapter<NewInfo>(R.layout.adapter_news_card_view_list_item, new LinearLayoutHelper()) {
            @Override
            protected void bindData(@NonNull RecyclerViewHolder holder, int position, NewInfo model) {
                if (model != null) {
                    //holder.text(R.id.tv_user_name, model.getUserName());
                    //holder.text(R.id.tv_tag, model.getTag());
                    holder.text(R.id.tv_title, model.getTitle());
                    holder.text(R.id.tv_summary, model.getSummary());
                    holder.text(R.id.tv_praise, model.getPraise() == 0 ? "点赞" : String.valueOf(model.getPraise()));
                    holder.text(R.id.tv_comment, model.getComment() == 0 ? "评论" : String.valueOf(model.getComment()));
                    holder.text(R.id.tv_read, "阅读量 " + model.getRead());
                    holder.image(R.id.iv_image, model.getImageUrl());

                    holder.click(R.id.card_view, v -> Utils.goWeb(getContext(), model.getDetailUrl()));
                }
            }
        };

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        delegateAdapter.addAdapter(bannerAdapter);
        delegateAdapter.addAdapter(commonAdapter);
        delegateAdapter.addAdapter(titleAdapter);
        delegateAdapter.addAdapter(mNewsAdapter);

        recyclerView.setAdapter(delegateAdapter);
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            // TODO: 2020-02-25 这里只是模拟了网络请求
            refreshLayout.getLayout().postDelayed(() -> {
                mNewsAdapter.refresh(DemoDataProvider.getDemoNewInfos());
                refreshLayout.finishRefresh();
            }, 1000);
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            // TODO: 2020-02-25 这里只是模拟了网络请求
            refreshLayout.getLayout().postDelayed(() -> {
                mNewsAdapter.loadMore(DemoDataProvider.getDemoNewInfos());
                refreshLayout.finishLoadMore();
            }, 1000);
        });
        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }
}
