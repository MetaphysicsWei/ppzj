

package com.xuexiang.ppzj.adapter.base.delegate;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.Collection;

/**
 * 通用的DelegateAdapter适配器
 *
 */
public abstract class BaseDelegateAdapter<T> extends XDelegateAdapter<T, RecyclerViewHolder> {

    public BaseDelegateAdapter() {
        super();
    }

    public BaseDelegateAdapter(Collection<T> list) {
        super(list);
    }

    public BaseDelegateAdapter(T[] data) {
        super(data);
    }

    /**
     * 适配的布局
     *
     * @param viewType
     * @return
     */
    protected abstract int getItemLayoutId(int viewType);

    @NonNull
    @Override
    protected RecyclerViewHolder getViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(inflateView(parent, getItemLayoutId(viewType)));
    }
}
