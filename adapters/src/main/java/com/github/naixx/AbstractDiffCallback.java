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

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

/**
 * Created by Rostislav Ch on 17.03.2017.
 */

public abstract class AbstractDiffCallback<T> extends DiffUtil.Callback {
    protected List<T> oldItems;
    protected List<T> newItems;

    public AbstractDiffCallback(List<T> oldItems, List<T> newItems) {
        this.newItems = newItems;
        this.oldItems = oldItems;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public final boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return areItemsTheSame(oldItems.get(oldItemPosition), newItems.get(newItemPosition));
    }

    @Override
    public final boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return areContentsTheSame(oldItems.get(oldItemPosition), newItems.get(newItemPosition));
    }

    protected abstract boolean areItemsTheSame(T oldItem, T newItem);

    protected boolean areContentsTheSame(T oldItem, T newItem) {
        return oldItem.equals(newItem);
    }
}
