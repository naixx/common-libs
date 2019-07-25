package common.databinding;

import com.google.android.material.tabs.TabLayout;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

/**
 * Created by Rostislav Ch on 07.11.2017.
 */
@InverseBindingMethods({ @InverseBindingMethod(type = TabLayout.class, attribute = "selectedTab", method =
        "getSelectedTabPosition") })
public class TabLayoutBindingAdapter {

    @BindingAdapter("selectedTab")
    public static void setSelectedItemPosition(TabLayout view, int position) {
        if (view.getSelectedTabPosition() != position) {
            view.getTabAt(position).select();
        }
    }

    @BindingAdapter(value = { "onSelectedTabChange", "selectedTabAttrChanged" }, requireAll = false)
    public static void setListeners(TabLayout view,
                                    final TabLayout.OnTabSelectedListener listener,
                                    final InverseBindingListener inverseBindingListener) {

        if (listener == null && inverseBindingListener == null) view.setOnTabSelectedListener(null);

        view.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (listener != null) listener.onTabSelected(tab);
                if (inverseBindingListener != null) inverseBindingListener.onChange();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
