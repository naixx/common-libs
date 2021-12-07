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

import android.widget.AbsSpinner;
import android.widget.SpinnerAdapter;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.adapters.AbsSpinnerBindingAdapter;
import androidx.databinding.adapters.AdapterViewBindingAdapter;
import androidx.databinding.adapters.BindableObservableListAdapter;
import androidx.databinding.adapters.PublicObservableListAdapter;

/**
 * Created by Rostislav Ch on 24.04.2017.
 */

@BindingMethods({ //
        @BindingMethod(type = AbsSpinner.class, attribute = "android:enabled", method = "setEnabled"),//
})
@SuppressWarnings("unchecked")
public class SpinnerBindingAdapters {
    @BindingAdapter(value = { "android:entries", "nothingLayout", "entry", "entryAttrChanged" }, requireAll = false)
    public static <T> void setEntries(final AbsSpinner view,
                                      List entries,
                                      @LayoutRes int nothingLayout,
                                      T entry,
                                      InverseBindingListener onAttrChanged) {
        if (nothingLayout == 0) {
            AbsSpinnerBindingAdapter.setEntries(view, entries);
            view.setOnItemSelectedListener(new AdapterViewBindingAdapter.OnItemSelectedComponentListener(null,
                    null,
                    onAttrChanged));
            if (entry != null) {
                final int i = entries.indexOf(entry);
                if (i != -1) {
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(i);
                        }
                    });
                }
            }
        } else if (entries != null) {

            SpinnerAdapter adapter1 = view.getAdapter();
            if (adapter1 instanceof NothingSelectedSpinnerAdapter) {
                ((PublicObservableListAdapter) ((NothingSelectedSpinnerAdapter) adapter1).getAdapter()).setList(entries);
            } else {
                PublicObservableListAdapter<T> adapter = new PublicObservableListAdapter<>(view.getContext(),
                        entries,
                        android.R.layout.simple_spinner_item,
                        android.R.layout.simple_spinner_dropdown_item,
                        0);
                view.setAdapter(new NothingSelectedSpinnerAdapter(adapter, nothingLayout, view.getContext()));
            }
            view.setOnItemSelectedListener(new AdapterViewBindingAdapter.OnItemSelectedComponentListener(null,
                    null,
                    onAttrChanged));
            if (entry != null) {
                final int i = entries.indexOf(entry);
                if (i != -1) {
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(i + 1);
                        }
                    });
                }
            }
        } else {
            view.setAdapter(null);
        }
    }

    @BindingAdapter(value = { "android:entries", "nothingSelectedText", "entry", "entryAttrChanged" }, requireAll =
            false)
    public static <T> void setEntriesWithNothingText(final AbsSpinner view,
                                                     List entries,
                                                     String nothingSelectedText,
                                                     T entry,
                                                     InverseBindingListener onAttrChanged) {
        if (entries != null) {
            SpinnerAdapter adapter1 = view.getAdapter();
            if (adapter1 instanceof NothingSelectedSpinnerAdapter) {
                ((PublicObservableListAdapter) ((NothingSelectedSpinnerAdapter) adapter1).getAdapter()).setList(entries);
            } else {
                PublicObservableListAdapter<T> adapter = new PublicObservableListAdapter<>(view.getContext(),
                        entries,
                        android.R.layout.simple_spinner_item,
                        android.R.layout.simple_spinner_dropdown_item,
                        0);
                view.setAdapter(new NothingSelectedSpinnerAdapter(adapter, nothingSelectedText, view.getContext()));
            }
            view.setOnItemSelectedListener(new AdapterViewBindingAdapter.OnItemSelectedComponentListener(null,
                    null,
                    onAttrChanged));
            if (entry != null) {
                final int i = entries.indexOf(entry);
                if (i != -1) {
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(i + 1);
                        }
                    });
                }
            }
        } else {
            view.setAdapter(null);
        }
    }

    @BindingAdapter(value = { "android:entries", "nothingLayout", "listitem", "listitemDropdown", "entry",
            "entryAttrChanged" })
    public static <T> void setEntriesWithLayoutAndDropdown(final AbsSpinner view,
                                                           List entries,
                                                           @LayoutRes int nothingLayoutRes,
                                                           @LayoutRes int listitemRes,
                                                           @LayoutRes int listitemDropdownRes,
                                                           T entry,
                                                           InverseBindingListener onAttrChanged) {
        if (nothingLayoutRes == 0) {
//           TODO combine all methods into one with different overloads
            if (entries != null) {
                SpinnerAdapter oldAdapter = view.getAdapter();
                if (oldAdapter instanceof BindableObservableListAdapter) {
                    //noinspection unchecked
                    ((BindableObservableListAdapter) oldAdapter).setList(entries);
                } else {
                    view.setAdapter(new BindableObservableListAdapter<>(view.getContext(),
                            entries,
                            listitemRes,
                            listitemDropdownRes,
                            0));
                }
            } else {
                view.setAdapter(null);
            }
            view.setOnItemSelectedListener(new AdapterViewBindingAdapter.OnItemSelectedComponentListener(null,
                    null,
                    onAttrChanged));
            if (entry != null) {
                int i = entries.indexOf(entry);
                if (i != -1) {
                    view.setSelection(i, false);
                }
            }
        } else if (entries != null) {
            SpinnerAdapter adapter1 = view.getAdapter();
            if (adapter1 instanceof NothingSelectedSpinnerAdapter) {
                ((BindableObservableListAdapter) ((NothingSelectedSpinnerAdapter) adapter1).getAdapter()).setList(
                        entries);
            } else {
                BindableObservableListAdapter<T> adapter = new BindableObservableListAdapter<>(view.getContext(),
                        entries,
                        listitemRes,
                        listitemDropdownRes,
                        0);
                view.setAdapter(new NothingSelectedSpinnerAdapter(adapter, nothingLayoutRes, view.getContext()));
            }
            view.setOnItemSelectedListener(new AdapterViewBindingAdapter.OnItemSelectedComponentListener(null,
                    null,
                    onAttrChanged));
            if (entry != null) {
                final int i = entries.indexOf(entry);
                if (i != -1) {
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(i + 1);
                        }
                    });
                }
            }
        } else {
            view.setAdapter(null);
        }
    }

    @BindingAdapter(value = { "android:entries", "nothingLayout", "listitem", "entry", "entryAttrChanged" })
    public static <T> void setEntriesWithLayout(AbsSpinner view,
                                                List entries,
                                                @LayoutRes int nothingLayoutRes,
                                                @LayoutRes int listitemRes,
                                                T entry,
                                                InverseBindingListener onAttrChanged) {
        setEntriesWithLayoutAndDropdown(view,
                entries,
                nothingLayoutRes,
                listitemRes,
                listitemRes,
                entry,
                onAttrChanged);
    }

    @InverseBindingAdapter(attribute = "entry", event = "entryAttrChanged")
    public static <T> T getSelectedEntry(AbsSpinner spinner) {
        return (T) spinner.getSelectedItem();
    }
}
