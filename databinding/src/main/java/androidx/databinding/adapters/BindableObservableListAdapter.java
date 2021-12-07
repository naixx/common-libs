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

package androidx.databinding.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.naixx.common_databinding.BR;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Created by Rostislav Ch on 02.04.2017.
 */

public class BindableObservableListAdapter<T> extends ObservableListAdapter<T> {
    public BindableObservableListAdapter(Context context, List<T> list, int resourceId, int dropDownResourceId, int textViewResourceId) {
        super(context, list, resourceId, dropDownResourceId, textViewResourceId);
    }

    @Override
    public View getViewForResource(int resourceId, int position, View convertView, ViewGroup parent) {
        ViewDataBinding b;
        if (convertView == null) {
            b = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), resourceId, parent, false);
        }
        else {
            b = DataBindingUtil.bind(convertView);
        }
        b.setVariable(BR.item, getItem(position));
        b.executePendingBindings();

        return b.getRoot();
    }
}
