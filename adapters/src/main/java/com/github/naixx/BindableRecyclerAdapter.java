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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class BindableRecyclerAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected B binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        protected void bind(final T item, int position) {
            if (onItemView(item, binding)) return;
            binding.setVariable(getVariableId(), item);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) callback.onItem(item);
                }
            });
            binding.executePendingBindings();
        }
    }

    protected     List<T>                  items = new ArrayList<>();
    private final int                      layoutResId;
    private final ItemCallback<T>          callback;
    private final DiffUtil.ItemCallback<T> diffCallback;

    public BindableRecyclerAdapter(@LayoutRes int layoutResId,
                                   ItemCallback<T> callback,
                                   boolean hasStableIds,
                                   DiffUtil.ItemCallback<T> diffCallback) {
        this.layoutResId = layoutResId;
        this.callback = callback;
        if (diffCallback != null) this.diffCallback = diffCallback;
        else this.diffCallback = null;
        setHasStableIds(hasStableIds);
    }

    public BindableRecyclerAdapter(@LayoutRes int layoutResId, ItemCallback<T> callback, boolean hasStableIds) {
        this(layoutResId, callback, hasStableIds, null);
    }

    public BindableRecyclerAdapter(@LayoutRes int layoutResId, ItemCallback<T> callback) {
        this(layoutResId, callback, false, null);
    }

    public BindableRecyclerAdapter(@LayoutRes int layoutResId) {
        this(layoutResId, null);
    }

    protected int getVariableId() {
        return BR.item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bind(items.get(position), position);
    }

    public List<T> getItems() {
        return items;
    }

    public void removeAll() {
        items.clear();
        notifyDataSetChanged();
    }

    public void swapItems(List<T> items) {
        this.items.clear();
        addItems(items);
    }

    public void submitList(final List<T> list) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return items.size();
            }

            @Override
            public int getNewListSize() {
                return list.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return diffCallback.areItemsTheSame(items.get(oldItemPosition), list.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return diffCallback.areContentsTheSame(items.get(oldItemPosition), list.get(newItemPosition));
            }
        });
        items.clear();
        items.addAll(list);
        diffResult.dispatchUpdatesTo(this);
    }

    public void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    protected boolean onItemView(T item, B binding) {
        return false;
    }

    public void addItems(List<? extends T> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void removeItem(T item) {
        int i = items.indexOf(item);
        items.remove(item);
        notifyItemRemoved(i);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
