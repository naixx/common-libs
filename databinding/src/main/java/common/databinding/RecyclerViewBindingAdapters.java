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
