package common.databinding;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.naixx.common_databinding.R;

import androidx.databinding.BindingAdapter;
import common.InputFilterMinMax;

import static android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;

/**
 * Created by Rostislav Ch on 5/15/2018.
 */
public class TextBindingAdapters {
    @BindingAdapter(value = { "minValue", "maxValue" }, requireAll = false)
    public static void setMinMax(EditText view, Long min, Long max) {
        if (min == null) min = Long.MIN_VALUE;
        if (max == null) max = Long.MAX_VALUE;
        view.setFilters(new InputFilter[]{ new InputFilterMinMax(min.doubleValue(), max.doubleValue()) });
    }

    @BindingAdapter("android:editable")
    public static void setEditable(EditText view, boolean editable) {
        if (editable) {
            KeyListener tag = (KeyListener) view.getTag(R.id.keyListener);
            if (tag != null) view.setKeyListener(tag);
        } else {
            KeyListener keyListener = view.getKeyListener();
            if (keyListener != null) view.setTag(R.id.keyListener, keyListener);
            view.setKeyListener(null);
        }
        view.setFocusable(editable);
        view.setFocusableInTouchMode(editable);
        view.setClickable(editable);
        view.setLongClickable(editable);
    }

    @BindingAdapter("disableTextSelection")
    public static void disableTextSelection(TextView view, boolean disable) {
        if (!disable) return;
        view.setInputType(TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        view.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
                return false;
            }
            public void onDestroyActionMode(ActionMode actionMode) { }
        });
        view.setLongClickable(false);
        view.setTextIsSelectable(false);
    }

    @BindingAdapter("switchFocusWhenFull")
    @TargetApi(21)
    public static void switchFocusWhenFull(final EditText v, boolean unused) {

        if (Build.VERSION.SDK_INT < 21) return;

        InputFilter[] filters = v.getFilters();
        InputFilter.LengthFilter filter = null;
        for (InputFilter f : filters) {
            if (f instanceof InputFilter.LengthFilter) {
                filter = (InputFilter.LengthFilter) f;
                break;
            }
        }

        if (filter != null) {

            final InputFilter.LengthFilter finalFilter = filter;
            v.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == finalFilter.getMax()) {
                        View view = v.focusSearch(View.FOCUS_FORWARD);
                        if (view != null) view.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
}
