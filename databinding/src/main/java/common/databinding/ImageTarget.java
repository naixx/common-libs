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

/**
 * Created by Rostislav Ch on 01.06.2017.
 */

//class ImageTarget implements Target {
//
//    public static Drawable getProgressBarIndeterminate(Context context) {
//        final int[] attrs = { android.R.attr.indeterminateDrawable };
//        final int attrs_indeterminateDrawable_index = 0;
//
//        TypedArray a = context.obtainStyledAttributes(R.style.Widget_AppCompat_ProgressBar, attrs);
//        try {
//            return a.getDrawable(attrs_indeterminateDrawable_index);
//        } finally {
//            a.recycle();
//        }
//    }
//
//    private final ImageView           imageView;
//    private final ImageView.ScaleType scaleType;
//
//    public ImageTarget(ImageView imageView) {
//        this.imageView = imageView;
//        scaleType = imageView.getScaleType();
//    }
//
//    @Override
//    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//        imageView.setImageDrawable(null);
//        imageView.setScaleType(scaleType);
//        PicassoDrawableHack.setBitmap(imageView, imageView.getContext(), bitmap, from, false, false);
//    }
//
//    @Override
//    public void onBitmapFailed(Drawable errorDrawable) {
//        imageView.setImageDrawable(errorDrawable);
//    }
//
//    @Override
//    public void onPrepareLoad(Drawable placeholder) {
//        imageView.setImageDrawable(placeholder);
//
//        if (placeholder instanceof Animatable) { //21+ api
//            ((Animatable) placeholder).start();
//        } else if (placeholder instanceof LayerDrawable) { //that's usually kitkat
//            LayerDrawable ld = (LayerDrawable) placeholder;
//            for (int i = 0; i < ld.getNumberOfLayers(); i++) {
//                Drawable drawable = ld.getDrawable(i);
//                if (drawable instanceof RotateDrawable) {
//                    ObjectAnimator animator = ObjectAnimator.ofInt(drawable, "level", 0, 10000).setDuration(2000);
//                    animator.setRepeatMode(ValueAnimator.REVERSE);
//                    animator.setRepeatCount(ValueAnimator.INFINITE);
//                    animator.start();
//                }
//            }
//        }
//    }
//}
