/*
 * Copyright 2021 Rostislav Chekan
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

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
