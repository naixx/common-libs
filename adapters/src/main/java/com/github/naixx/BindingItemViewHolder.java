package com.github.naixx;

import android.view.View;

import com.github.naixx.adapters.BR;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BindingItemViewHolder<T, B extends ViewDataBinding> extends ItemViewHolderBase<T> {

    protected B binding;

    public BindingItemViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    @Override
    public final void bind(T item, int position) {
        binding.setVariable(getVariableId(), item);
        executeBinding(item, position);
        binding.executePendingBindings();
    }

    protected int getVariableId() {
        return BR.item;
    }

    protected void executeBinding(T item, int position) {}
}
