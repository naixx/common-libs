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
