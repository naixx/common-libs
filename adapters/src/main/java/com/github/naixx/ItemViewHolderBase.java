package com.github.naixx;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ItemViewHolderBase<T> extends RecyclerView.ViewHolder {

    public ItemViewHolderBase(View itemView) {
        super(itemView);
    }

    public abstract void bind(@NonNull T item, int position);
}
