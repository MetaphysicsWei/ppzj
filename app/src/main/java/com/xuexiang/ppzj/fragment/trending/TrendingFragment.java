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

package com.xuexiang.ppzj.fragment.trending;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.ppzj.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.ppzj.adapter.entity.NewInfo;
import com.xuexiang.ppzj.utils.DemoDataProvider;
import com.xuexiang.ppzj.utils.Utils;
import com.xuexiang.ppzj.utils.XToastUtils;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.ppzj.R;
import com.xuexiang.ppzj.core.BaseFragment;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import android.Manifest;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.idst.token.AccessToken;
import com.alibaba.idst.util.NlsClient;
import com.alibaba.idst.util.SpeechTranscriber;
import com.alibaba.idst.util.SpeechTranscriberCallback;
import com.alibaba.idst.util.SpeechTranscriberWithRecorder;
import com.alibaba.idst.util.SpeechTranscriberWithRecorderCallback;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;


@Page(anim = CoreAnim.none)
public class TrendingFragment extends BaseFragment {
    private static final String TAG = "TrendingFragment";

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @BindView(R.id.et_basic)
    EditText mEtBasic;
    @BindView(R.id.btn_mic)
    ImageButton nBtnMic;
    @BindView(R.id.text_test)
    TextView mText;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_trending;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        // 第一步，创建client实例，client只需要创建一次，可以用它多次创建transcriber
        client = new NlsClient();
        //UI在主线程更新
        handler= new MyHandler(this);
        nBtnMic.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    startTranscribe(view);
                    Log.d(TAG, "ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_UP:
                    stopTranscribe(view);
                    Log.d(TAG, "ACTION_UP");
                    lateSearch();
                    break;
            }
            return true;
        });

        new Thread(() -> {
            AccessToken token = new AccessToken("LTAI4GHZNbR4bgAfbqJzF8bM", "Rj4BoBM4xuhKsvUwwTR8RgerwW6Ui0");
            try {
                token.apply();
                accessToken = token.getToken();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext());
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);

        //资讯
        mNewsAdapter = new SimpleDelegateAdapter<NewInfo>(R.layout.adapter_news_card_view_list_item, new LinearLayoutHelper()) {
            @Override
            protected void bindData(@NonNull RecyclerViewHolder holder, int position, NewInfo model) {
                if (model != null) {
                    holder.text(R.id.tv_user_name, "文旅资讯");
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
        delegateAdapter.addAdapter(mNewsAdapter);

        recyclerView.setAdapter(delegateAdapter);

        mEtBasic.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    lateSearch();
                }
                return false;
            }
        });
    }

    @Override
    protected void initListeners() {

        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            // TODO: 2020-02-25 这里只是模拟了网络请求
            refreshLayout.getLayout().postDelayed(() -> {
                mNewsAdapter.refresh(DemoDataProvider.getDemoNewInfos(mEtBasic.getText().toString()));
                refreshLayout.finishRefresh();
            }, 1000);
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            // TODO: 2020-02-25 这里只是模拟了网络请求
            refreshLayout.getLayout().postDelayed(() -> {
                mNewsAdapter.refresh(DemoDataProvider.getDemoNewInfos(mEtBasic.getText().toString()));
                refreshLayout.finishLoadMore();
            }, 1000);
        });
        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }

    private String accessToken = "";
    private static MyHandler handler;
    private NlsClient client;
    private SpeechTranscriberWithRecorder speechTranscriber;


    @Override
    public void onDestroy(){
        if (speechTranscriber != null){
            speechTranscriber.stop();
        }

        // 最终，退出时释放client
        client.release();
        super.onDestroy();
    }

    private boolean recording = false;

    private void lateSearch() {
        XToastUtils.toast("搜索:" + mEtBasic.getText().toString());
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {

                getActivity().runOnUiThread(() -> {
                    refreshLayout.autoRefresh();
                });
                t.cancel();
            }
        }, 800);
    }

    /**
     * 启动录音和识别
     */
    public void startTranscribe(View view){
        if(recording) return;

        mEtBasic.setText("");
        mText.setText("正在录音");
        recording = true;

        // 第二步，新建识别回调类
        SpeechTranscriberWithRecorderCallback callback = new MyCallback();

        // 第三步，创建识别request
        speechTranscriber = client.createTranscriberWithRecorder(callback);


        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                if(recording) {
                    Message m = new Message();
                    m.what = 11;
                    m.obj = this;
                    handler.dispatchMessage(m);
                }
                t.cancel();
            }
        }, 8000);

        // 第四步，设置相关参数
        // Token有有效期，请使用https://help.aliyun.com/document_detail/72153.html 动态生成token
        speechTranscriber.setToken(accessToken);
        // 请使用阿里云语音服务管控台(https://nls-portal.console.aliyun.com/)生成您的appkey
        speechTranscriber.setAppkey("gL7SJIIq18aqnSit");
        // 设置返回中间结果，更多参数请参考官方文档
        // 返回中间结果
        speechTranscriber.enableIntermediateResult(true);
        // 开启标点
        speechTranscriber.enablePunctuationPrediction(true);
        // 开启ITN
        speechTranscriber.enableInverseTextNormalization(true);
        // 设置静音断句长度
        speechTranscriber.setMaxSentenceSilence(500);
        // 设置定制模型和热词
        // speechTranscriber.setCustomizationId("yourCustomizationId");
        // speechTranscriber.setVocabularyId("yourVocabularyId");
        speechTranscriber.start();
    }

    public void stopTranscribe(View view){
        if(recording) {
            recording = false;
            mText.setText("点击录音");
            //button.setText("开始 录音");
            // 第八步，停止本次识别
            speechTranscriber.stop();
        }
    }

    // 语音识别回调类，得到语音识别结果后在这里处理
    //    // 注意不要在回调方法里调用transcriber.stop()方法
    //    // 注意不要在回调方法里执行耗时操作
    private static class MyCallback implements SpeechTranscriberWithRecorderCallback {



        // 识别开始
        @Override
        public void onTranscriptionStarted(String msg, int code)
        {
            Log.d(TAG,"OnTranscriptionStarted " + msg + ": " + String.valueOf(code));
        }

        // 请求失败
        @Override
        public void onTaskFailed(String msg, int code)
        {
            Log.d(TAG,"OnTaskFailed " + msg + ": " + String.valueOf(code));
            handler.sendEmptyMessage(0);
        }

        // 识别返回中间结果，只有开启相关选项时才会回调
        @Override
        public void onTranscriptionResultChanged(final String msg, int code)
        {
            Log.d(TAG,"OnTranscriptionResultChanged " + msg + ": " + String.valueOf(code));
            Message message = new Message();
            message.obj = msg;
            handler.sendMessage(message);
        }

        // 开始识别一个新的句子
        @Override
        public void onSentenceBegin(String msg, int code)
        {
            Log.i(TAG, "Sentence begin");
        }

        // 第七步，当前句子识别结束，得到完整的句子文本
        @Override
        public void onSentenceEnd(final String msg, int code)
        {
            Log.d(TAG,"OnSentenceEnd " + msg + ": " + String.valueOf(code));
            Message message = new Message();
            message.obj = msg;
            handler.sendMessage(message);
        }

        // 识别结束
        @Override
        public void onTranscriptionCompleted(final String msg, int code)
        {
            Log.d(TAG,"OnTranscriptionCompleted " + msg + ": " + String.valueOf(code));
            Message message = new Message();
            message.obj = msg;
            handler.sendMessage(message);
            handler.clearResult();
        }

        // 请求结束，关闭连接
        @Override
        public void onChannelClosed(String msg, int code) {
            Log.d(TAG, "OnChannelClosed " + msg + ": " + String.valueOf(code));
        }

        // 手机采集的语音数据的回调
        @Override
        public void onVoiceData(byte[] bytes, int i) {

        }

        // 手机采集的语音音量大小的回调
        @Override
        public void onVoiceVolume(int i) {

        }
    };

    // 根据识别结果更新界面的代码
    private static class MyHandler extends Handler {
        StringBuilder fullResult = new StringBuilder();
        private final TrendingFragment mFragment;

        public MyHandler(TrendingFragment fragment) {
            mFragment = fragment;
        }

        public void clearResult(){
            this.fullResult = new StringBuilder();
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null) {
                Log.i(TAG, "Empty message received.");
                return;
            }
            if(msg.what == 11) {
                mFragment.stopTranscribe(null);
                return;
            }
            if(msg.what == 12) {

                return;
            }

            String rawResult = (String)msg.obj;
            String result = null;
            String displayResult = null;
            if (rawResult != null && !rawResult.equals("")){
                JSONObject jsonObject = JSONObject.parseObject(rawResult);
                if (jsonObject.containsKey("payload")){
                    result = jsonObject.getJSONObject("payload").getString("result");
                    int time = jsonObject.getJSONObject("payload").getIntValue("time");
                    if (time != -1) {
                        fullResult.append(result);
                        displayResult = fullResult.toString();
                        fullResult.append("\n");
                    } else {
                        displayResult = fullResult.toString() + result;
                    }

                    Log.d(TAG, displayResult);
                    mFragment.mEtBasic.setText(displayResult);
                }
            }
            if(result != null) Log.d(TAG, result);
            //mFragment.mResultEdit.setText(result);
        }
    }



    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private SimpleDelegateAdapter<NewInfo> mNewsAdapter;


}
