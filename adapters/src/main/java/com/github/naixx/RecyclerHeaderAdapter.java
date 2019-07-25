package com.github.naixx;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerHeaderAdapter<T, VH extends ItemViewHolderBase<T>> extends RecyclerView.Adapter<ItemViewHolderBase<T>> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM   = 1;

    protected     List<T>           items = new ArrayList<>();
    private final HeaderViewFactory headerViewFactory;

    public class HeaderViewHolder extends ItemViewHolderBase<T> {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(T item, int position) {

        }
    }

    public interface HeaderViewFactory {
        View create(ViewGroup parent);
    }

    public static final class ResHeaderViewFactory implements HeaderViewFactory {
        private final int headerLayoutRes;

        public ResHeaderViewFactory(int headerLayoutRes) {this.headerLayoutRes = headerLayoutRes;}

        @Override
        public View create(ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(headerLayoutRes, parent, false);
        }
    }

    public RecyclerHeaderAdapter(HeaderViewFactory headerViewFactory) {
        this.headerViewFactory = headerViewFactory;
    }

    public RecyclerHeaderAdapter() {
        this.headerViewFactory = null;
    }

    @Override
    final public ItemViewHolderBase<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            if (!hasHeader()) {
                return null;
            }
            return onCreateHeaderViewHolder(parent);
        }
        return onCreateItemViewHolder(parent);
    }

    protected abstract VH onCreateItemViewHolder(ViewGroup parent);

    private ItemViewHolderBase<T> onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(headerViewFactory.create(parent));
    }

    public List<T> getItems() {
        return items;
    }

    public void removeAll() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ItemViewHolderBase<T> holder, int position) {
        if (isHeader(position)) {
            return;
        }
        holder.bind(items.get(getItemPos(position)), position);
    }

    protected int getItemPos(int position) {
        if (!hasHeader()) {
            return position;
        } else {
            return position - 1;
        }
    }

    private boolean hasHeader() {return headerViewFactory != null;}

    public void swapItems(List<T> items) {
        this.items.clear();
        addItems(items);
    }

    public void addItems(List<T> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size() + (hasHeader() ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    public boolean isHeader(int position) {
        return hasHeader() && position == 0;
    }
}
