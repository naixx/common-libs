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
