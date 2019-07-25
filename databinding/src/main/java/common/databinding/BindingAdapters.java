package common.databinding;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.OnRebindCallback;
import androidx.databinding.ViewDataBinding;
import androidx.transition.TransitionManager;
import common.UI;

/**
 * Created by Rostislav Ch on 3/7/2016.
 */
@SuppressWarnings("WeakerAccess")
@BindingMethods({ //
        @BindingMethod(type = TextInputEditText.class, attribute = "error", method = "setError"),//
        @BindingMethod(type = EditText.class, attribute = "error", method = "setError"),//
        @BindingMethod(type = Toolbar.class, attribute = "onNavigationClick",
                method = "setNavigationOnClickListener"), //
})
public class BindingAdapters {

    private static final String BOUNDS = "W";

    public interface DrawableClickListener {
        void onDrawableClick(Context context);
    }

    @BindingAdapter("html")
    public static void setHtml(TextView view, String res) {
        view.setText(Html.fromHtml(res));
    }

    @BindingAdapter("html")
    public static void setHtml(TextView view, int res) {
        view.setText(Html.fromHtml(view.getContext().getString(res)));
    }

    @BindingAdapter("linkMovementMethod")
    public static void setHtml(TextView view, boolean tru) {
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @BindingAdapter("strikethrough")
    public static void setStrikethrough(TextView view, boolean tru) {
        if (tru) view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else view.setPaintFlags(view.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
    }

    /**
     * Html doesn't work after formatting strings
     */
    @BindingAdapter("underline")
    public static void setUnderline(TextView view, boolean underline) {
        if (underline) {
            view.setPaintFlags(view.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            view.setPaintFlags(view.getPaintFlags() ^ Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    @BindingAdapter("progressColor")
    public static void setSeekbarProgressColor(ProgressBar seekBar, int color) {
        LayerDrawable ld = (LayerDrawable) seekBar.getProgressDrawable();
        ClipDrawable d1 = (ClipDrawable) ld.findDrawableByLayerId(android.R.id.progress);
        d1.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @BindingAdapter(value = { "android:drawableRight", "onDrawableClick", "drawableTint" })
    public static void setDrawableRight(TextView view, Drawable drawable, DrawableClickListener listener, int color) {
        setDrawableRight(view, drawable, listener);
        setDrawableTint(view, color);
    }

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter(value = { "android:drawableRight", "onDrawableClick" })
    public static void setDrawableRight(final TextView view, final Drawable drawable, final DrawableClickListener listener) {
        Drawable[] drawables = view.getCompoundDrawables();
        view.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawable, drawables[3]);
        if (listener != null && drawable != null) {
            final int padding = view.getCompoundDrawablePadding() + view.getPaddingRight();
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int x = (int) event.getX();
                    int iconXRect = v.getRight() - drawable.getBounds().width() - padding;

                    if (event.getAction() == MotionEvent.ACTION_UP && x >= iconXRect) {
                        listener.onDrawableClick(view.getContext());
                        return true;
                    }
                    return false;
                }
            });
        } else {
            view.setOnTouchListener(null);
        }
    }

    @BindingAdapter(value = { "android:drawableLeft", "drawableSize" })
    public static void setDrawable(TextView view, Drawable drawable, int drawableSize) {
        if (drawable != null) {
            int px = UI.dpToPx(drawableSize);
            drawable.setBounds(0, 0, px, px);
        }
        view.setCompoundDrawables(drawable, null, null, null);
    }

    @BindingAdapter(value = { "android:drawableTop", "drawableSize" })
    public static void setDrawableTop(TextView view, Drawable drawable, int drawableSize) {
        int px = UI.dpToPx(drawableSize);
        drawable.setBounds(0, 0, px, px);
        view.setCompoundDrawables(null, drawable, null, null);
    }

    @BindingAdapter(value = { "android:drawableRight", "drawableSize" })
    public static void setDrawableRight(TextView view, @Nullable Drawable drawable, int drawableSize) {
        int px = UI.dpToPx(drawableSize);
        if (drawable != null) {
            drawable.setBounds(0, 0, px, px);
        }
        view.setCompoundDrawables(null, null, drawable, null);
    }

//    @BindingAdapter(value = { "android:drawableLeft", "drawableSize" })
//    public static void setDrawable(TextView view, String url, int drawableSize) {
//        int px = UI.dpToPx(drawableSize);
//        Target target = new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                view.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(view.getResources(), bitmap),
//                        null,
//                        null,
//                        null);
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {}
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {}
//        };
//        //https://github.com/square/picasso/issues/352
//        view.setTag(R.id.picasso, target);
//        Picasso.with(view.getContext()).load(url).resize(px, px).centerInside().into(target);
//    }

    @BindingAdapter(value = { "android:drawableLeft", "drawableSizeWidth", "drawableSizeHeight" })
    public static void setDrawable(TextView view, Drawable drawable, int width, int height) {
        if (drawable != null) {
            drawable.setBounds(0, 0, UI.dpToPx(width), UI.dpToPx(height));
        }
        view.setCompoundDrawables(drawable, null, null, null);
    }
//
//    @BindingAdapter({ "decimalFormat", "currency" })
//    public static void decimalFormat(TextView view, double value, Currency currency) {
//        decimalFormat(view, value, currency, true);
//    }
//
//    @BindingAdapter({ "decimalFormat", "currency", "currencyImageShown" })
//    public static void decimalFormat(TextView view, double value, Currency currency, boolean showCurrencyImage) {
//
//        String formatted = CurrencyFormat.format(currency, value);
//        view.setText(formatted);
//
//        if (currency == null || currency.image == null || !showCurrencyImage) {
//            return;
//        }
//        Rect bounds = new Rect();
//        view.getPaint().getTextBounds(BOUNDS, 0, BOUNDS.length(), bounds); //we don't need separators during
// measurement
//
//        int lineHeight = bounds.height();
//        Target target = new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                ImageSpan imageSpan = new ImageSpan(view.getContext(), bitmap, ImageSpan.ALIGN_BASELINE);
//                String replacer = "<<IMAGE>>";
//                String text = replacer + " " + formatted;
//                SpannableString ss = new SpannableString(text);
//                ss.setSpan(imageSpan,
//                        text.indexOf(replacer),
//                        text.indexOf(replacer) + replacer.length(),
//                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                view.setText(ss);
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//        };
//        //https://github.com/square/picasso/issues/352
//        view.setTag(R.id.picasso, target);
//        Picasso.with(view.getContext())
//                .load(currency.image.original.url)
//                .resize(lineHeight, lineHeight)
//                .centerInside()
//                .into(target);
//    }

    @BindingAdapter("android:backgroundTint")
    public static void setBackgroundTint(View view, ColorStateList value) {
        ViewCompat.setBackgroundTintList(view, value);
    }

    @BindingAdapter("android:drawableTint")
    public static void setDrawableTint(TextView textView, int color) {
        Drawable[] result = new Drawable[4];
        Drawable[] drawables = textView.getCompoundDrawables();
        for (int i = 0; i < 4; i++) {
            if (drawables[i] == null) {
                continue;
            }
            Drawable wrap = DrawableCompat.wrap(drawables[i]);
            DrawableCompat.setTint(wrap, color);
            result[i] = wrap;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(result[0], result[1], result[2], result[3]);
    }

    @BindingAdapter("drawableTintList")
    public static void setDrawableTintList(TextView textView, ColorStateList colorList) {
        Drawable[] result = new Drawable[4];
        Drawable[] drawables = textView.getCompoundDrawables();

        for (int i = 0; i < 4; i++) {
            if (drawables[i] == null) {
                continue;
            }
            Drawable wrap = DrawableCompat.wrap(drawables[i]);
            DrawableCompat.setTintList(wrap, colorList);
            result[i] = wrap;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(result[0], result[1], result[2], result[3]);
    }

    @BindingAdapter("backgroundTint")
    public static void setBackgroundTint(View view, int color) {
        Drawable wrap = DrawableCompat.wrap(view.getBackground());
        DrawableCompat.setTint(wrap, color);
    }

    @BindingAdapter("srcCompat")
    public static void setSrcCompat(ImageView view, int res) {
        view.setImageResource(res);
    }

    @BindingAdapter(value = { "textViewLayout", "android:text", "android:textColor" }, requireAll = false)
    public static void setTextSwitcherLayout(final TextSwitcher view, final int layoutId, String text, int color) {
        if (layoutId != 0 && view.getChildCount() == 0) {
            view.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    return LayoutInflater.from(view.getContext()).inflate(layoutId, null);
                }
            });
        }
        TextView nextView = (TextView) view.getNextView();
        nextView.setTextColor(color);
        view.setText(text);
    }

//    @BindingAdapter(value = { "textViewLayout", "decimalFormat", "currency" }, requireAll = false)
//    public static void setTextSwitcherLayout(TextSwitcher view, int layoutId, Double value, Currency currency) {
//        if (layoutId != 0 && view.getChildCount() == 0) {
//            view.setFactory(() -> LayoutInflater.from(view.getContext()).inflate(layoutId, null));
//        }
//        if (value == null || currency == null) {
//            return;
//        }
//        TextView nextView = (TextView) view.getNextView();
//        decimalFormat(nextView, value, currency);
//        nextView.setTextColor(currency.color_code.code);
//        view.showNext();
//    }

    @BindingConversion
    public static int convertBooleanToVisibility(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

    @BindingAdapter("visible")
    public static void setVisible(View v, boolean visible) {
        v.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
    public static Double getDoubleValue(TextView view) {
        try {
            return Double.parseDouble(view.getText().toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

//    @BindingConversion
//    public static int convertColorCodeToInt(ColorCode colorCode) {
//        return colorCode != null ? colorCode.code : Color.LTGRAY;
//    }

    @BindingAdapter("android:onClick")
    public static void onClick(View view, final Runnable res) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res.run();
            }
        });
    }

    @BindingAdapter({ "android:textSize" })
    public static void setTextSize(TextView view, int dimen) {
        int pixelSize = view.getContext().getResources().getDimensionPixelSize(dimen);
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixelSize);
    }

    @BindingAdapter("startFlipping")
    public static void setAutoStart(ViewFlipper view, boolean start) {
        if (start) {
            view.startFlipping();
        } else {
            view.stopFlipping();
        }
    }

    @BindingAdapter("animate")
    public static void animate(View view, ObjectAnimator animator) {
        if (animator != null) {
            animator.setTarget(view);
            animator.start();
        }
    }

    @BindingAdapter("animateRootTransitions")
    public static void animateTransitions(View view, boolean ignored) {
        ViewDataBinding binding = DataBindingUtil.findBinding(view);
        if (binding != null) {
            binding.addOnRebindCallback(new OnRebindCallback() {
                @Override
                public boolean onPreBind(ViewDataBinding binding) {
                    TransitionManager.beginDelayedTransition(((ViewGroup) binding.getRoot()), null);
                    return super.onPreBind(binding);
                }
            });
        }
    }

    @BindingAdapter("android:selected")
    public static void setSelected(View view, boolean selected) {
        view.setSelected(selected);
    }

    @BindingAdapter("android:layout_marginLeft")
    public static void setMarginLeft(View view, int margin) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                marginLayoutParams.leftMargin = margin;
                view.setLayoutParams(marginLayoutParams);
            }
        }
    }

    @BindingAdapter("android:layout_marginRight")
    public static void setMarginRight(View view, int margin) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                marginLayoutParams.rightMargin = margin;
                view.setLayoutParams(marginLayoutParams);
            }
        }
    }

    @BindingAdapter("android:layout_marginTop")
    public static void setMarginTop(View view, int margin) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                marginLayoutParams.topMargin = margin;
                view.setLayoutParams(marginLayoutParams);
            }
        }
    }

    @BindingAdapter("android:layout_marginBottom")
    public static void setMarginBottom(View view, int margin) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                marginLayoutParams.bottomMargin = margin;
                view.setLayoutParams(marginLayoutParams);
            }
        }
    }

    @BindingAdapter("android:layout_marginLeft")
    public static void setMarginLeft(View view, float margin) {
        setMarginLeft(view, pixelsToDimensionPixelSize(margin));
    }

    @BindingAdapter("android:layout_marginRight")
    public static void setMarginRight(View view, float margin) {
        setMarginRight(view, pixelsToDimensionPixelSize(margin));
    }

    @BindingAdapter("android:layout_marginTop")
    public static void setMarginTop(View view, float margin) {
        setMarginTop(view, pixelsToDimensionPixelSize(margin));
    }

    @BindingAdapter("android:layout_marginBottom")
    public static void setMarginBottom(View view, float margin) {
        setMarginBottom(view, pixelsToDimensionPixelSize(margin));
    }

    // Follows the same conversion mechanism as in TypedValue.complexToDimensionPixelSize as used
    // when setting padding. It rounds off the float value unless the value is < 1.
    // When a value is between 0 and 1, it is set to 1. A value less than 0 is set to -1.
    private static int pixelsToDimensionPixelSize(float pixels) {
        final int result = (int) (pixels + 0.5f);
        if (result != 0) {
            return result;
        } else if (pixels == 0) {
            return 0;
        } else if (pixels > 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
