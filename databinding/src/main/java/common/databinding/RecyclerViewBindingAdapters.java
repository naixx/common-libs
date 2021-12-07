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

package common.databinding;

import com.github.naixx.BindableRecyclerAdapter;
import com.github.naixx.ItemCallback;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewBindingAdapters {
    @BindingAdapter({ "items", "listitem" })
    public static <T> void setRecyclerViewItems(RecyclerView recyclerView, List<T> items, int layoutId) {
        setRecyclerViewItems(recyclerView, items, layoutId, null);
    }

    @BindingAdapter(value = { "items", "listitem", "callback" })
    public static <T> void setRecyclerViewItems(RecyclerView recyclerView,
                                                List<T> items,
                                                int layoutId,
                                                ItemCallback<T> callback) {
        if (items != null) {
            BindableRecyclerAdapter<T, ?> adapter = new BindableRecyclerAdapter<T, ViewDataBinding>(layoutId, callback);
            adapter.addItems(items);
            recyclerView.setAdapter(adapter);
        }
    }

    @BindingAdapter({ "items", "adapter" })
    public static <T> void setRecyclerViewItems(RecyclerView recyclerView,
                                                ObservableList<T> items,
                                                final BindableRecyclerAdapter<T, ?> adapter) {
        if (items != null && recyclerView.getAdapter() == null) {
            adapter.addItems(items);
            recyclerView.setAdapter(adapter);
            items.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<T>>() {
                @Override
                public void onChanged(ObservableList<T> ts) {
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onItemRangeChanged(ObservableList<T> ts, int i, int i1) {
                    adapter.notifyItemRangeChanged(i, i1);
                }

                @Override
                public void onItemRangeInserted(ObservableList<T> ts, int i, int i1) {
                    adapter.swapItems(ts);
                }

                @Override
                public void onItemRangeMoved(ObservableList<T> ts, int i, int i1, int i2) {
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onItemRangeRemoved(ObservableList<T> ts, int i, int i1) {
                    adapter.swapItems(ts);
                }
            });
        }
    }

    @BindingAdapter("android:nestedScrollingEnabled")
    public static void setNestedScrollingEnabled(RecyclerView view, boolean enabled) {
        view.setNestedScrollingEnabled(enabled);
    }
}
