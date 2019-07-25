package com.github.naixx;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.naixx.adapters.BR;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager.widget.PagerAdapter;

public class BindablePagerAdapter<T, B extends ViewDataBinding> extends PagerAdapter {

    private final List<T>         items = new ArrayList<>();
    private final int             layoutResId;
    private final ItemCallback<T> callback;

    public BindablePagerAdapter(@LayoutRes int layoutResId, ItemCallback<T> callback) {
        this.layoutResId = layoutResId;
        this.callback = callback;
    }

    public BindablePagerAdapter(@LayoutRes int layoutResId) {
        this(layoutResId, null);
    }

    @NonNull
    @Override
    public View instantiateItem(@NonNull ViewGroup parent, int position) {
        T item = items.get(position);
        B binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId, parent, true);
        View view = binding.getRoot();
        view.setTag(item);
        bind(binding, item);
        return view;
    }

    private void bind(B binding, final T item) {
        binding.setVariable(getVariableId(), item);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {if (callback != null) callback.onItem(item);}
        });
        binding.executePendingBindings();
    }

    protected int getVariableId() {
        return BR.item;
    }

    public void swapData(List<T> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemPosition(@NonNull Object object) {
        T item = (T) ((View) object).getTag();
        int position = items.indexOf(item);
        if (position >= 0) {
            // The current data matches the data in this active fragment, so let it be as it is.
            return position;
        } else {
            // Returning POSITION_NONE means the current data does not matches the data this fragment is showing
            // right now.
            // Returning POSITION_NONE constant will force the fragment to redraw its view layout all over again and
            // show new data.
            return POSITION_NONE;
        }
    }
}
