

package com.xuexiang.ppzj.adapter.base.delegate;

import com.alibaba.android.vlayout.LayoutHelper;

import java.util.Collection;

/**
 * 简易DelegateAdapter适配器
 *
 */
public abstract class SimpleDelegateAdapter<T> extends BaseDelegateAdapter<T> {

    private int mLayoutId;

    private LayoutHelper mLayoutHelper;

    public SimpleDelegateAdapter(int layoutId, LayoutHelper layoutHelper) {
        super();
        mLayoutId = layoutId;
        mLayoutHelper = layoutHelper;
    }

    public SimpleDelegateAdapter(int layoutId, LayoutHelper layoutHelper, Collection<T> list) {
        super(list);
        mLayoutId = layoutId;
        mLayoutHelper = layoutHelper;
    }

    public SimpleDelegateAdapter(int layoutId, LayoutHelper layoutHelper, T[] data) {
        super(data);
        mLayoutId = layoutId;
        mLayoutHelper = layoutHelper;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return mLayoutId;
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }
}
