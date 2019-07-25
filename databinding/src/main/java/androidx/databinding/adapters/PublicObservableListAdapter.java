package androidx.databinding.adapters;

import android.content.Context;

import java.util.List;

/**
 * Created by Rostislav Ch on 02.04.2017.
 */

public class PublicObservableListAdapter<T> extends ObservableListAdapter<T> {
    public PublicObservableListAdapter(Context context,
                                       List<T> list,
                                       int resourceId,
                                       int dropDownResourceId,
                                       int textViewResourceId) {
        super(context, list, resourceId, dropDownResourceId, textViewResourceId);
    }
}
