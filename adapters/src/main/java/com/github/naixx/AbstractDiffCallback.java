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
