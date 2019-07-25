package common.databinding;

/**
 * Created by Rostislav Ch on 01.06.2017.
 */

public class ImageBindingAdapters {
//    @BindingAdapter("resource")
//    public static void setSrc(ImageView view, int res) {
//        view.setImageResource(res);
//    }
//
//    @BindingAdapter(value = { "imageUrl", "cornerRadius" }, requireAll = false)
//    public static void setSrc(ImageView view, String url, Float cornerRadius) {
//        if (cornerRadius != null) {
//            Picasso.with(view.getContext())
//                    .load(url)
//                    .fit()
//                    .centerCrop()
//                    .transform(new RoundedTransformation(cornerRadius, 0))
//                    .into(view);
//        } else {
//            if (view.getScaleType() == ImageView.ScaleType.CENTER_INSIDE) {
//                Picasso.with(view.getContext()).load(url).fit().centerInside().into(view);
//            } else if (view.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
//                Picasso.with(view.getContext()).load(url).fit().centerCrop().into(view);
//            } else {
//                Picasso.with(view.getContext()).load(url).into(view);
//            }
//        }
//    }
//
//    @BindingAdapter(value = { "image" })
//    public static void setSrc(ImageView view, Image image) {
//        if(image == null) return;
//        setSrc(view, Uri.parse(image.original.url));
//    }
//
//    @BindingAdapter(value = { "imageUrl" })
//    public static void setSrc(ImageView view, Uri url) {
//        if (view.getScaleType() == ImageView.ScaleType.CENTER_INSIDE) {
//            Picasso.with(view.getContext()).load(url).fit().centerInside().into(view);
//        } else if (view.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
//            Picasso.with(view.getContext()).load(url).fit().centerCrop().into(view);
//        } else {
//            Picasso.with(view.getContext()).load(url).into(view);
//        }
//    }
//
//    @BindingAdapter(value = { "android:src", "placeholder", "errorDrawable" }, requireAll = false)
//    public static void setSrc(ImageView view, Image image, Drawable placeholder, Drawable error) {
//        if (image != null && image.thumbnail != null) {
//            setSrc(view, image.thumbnail, placeholder, error);
//        } else {
//            view.setImageDrawable(placeholder);
//        }
//    }
//
//    @BindingAdapter(value = { "android:src", "placeholder", "errorDrawable" }, requireAll = false)
//    public static void setSrc(ImageView view, ImageDef image, Drawable placeholder, Drawable error) {
//        if (image != null) {
//            RequestCreator requestCreator = Picasso.with(view.getContext()).load(image.url);
//
//            if (placeholder != null) {
//                requestCreator.placeholder(placeholder);
//            }
//            if (error != null) {
//                requestCreator.error(error);
//            }
//
//            if (placeholder instanceof Animatable || (placeholder instanceof LayerDrawable)) {
//                ImageTarget target = new ImageTarget(view);
//                view.setScaleType(ImageView.ScaleType.CENTER); //placeholder shouldn't be scaled, later we will set
//                // original scaletype
//                requestCreator.into(target);
//                view.setTag(R.id.picasso, target);
//            } else {
//                if (view.getScaleType() == ImageView.ScaleType.CENTER_INSIDE) {
//                    requestCreator.fit().centerInside();
//                } else if (view.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
//                    requestCreator.fit().centerCrop();
//                }
//                requestCreator.into(view);
//            }
//        } else {
//            view.setImageDrawable(placeholder);
//        }
//    }
//
//    @BindingAdapter(value = { "android:src", "withLoading", "errorDrawable" }, requireAll = false)
//    public static void setSrc(ImageView view, ImageDef image, Boolean withLoading, Drawable error) {
//        if (withLoading != null && withLoading) {
//            setSrc(view, image, ImageTarget.getProgressBarIndeterminate(view.getContext()), error);
//        } else {
//            setSrc(view, image, (Drawable) null, error);
//        }
//    }
//
//    @BindingAdapter("imageFile")
//    public static void setSrc(ImageView view, File file) {
//        if (view.getScaleType() == ImageView.ScaleType.CENTER_INSIDE) {
//            Picasso.with(view.getContext()).load(file).fit().centerInside().into(view);
//        } else if (view.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
//            Picasso.with(view.getContext()).load(file).fit().centerCrop().into(view);
//        } else {
//            Picasso.with(view.getContext()).load(file).into(view);
//        }
//    }
//
//    @BindingAdapter(value = { "android:src", "placeholder" })
//    public static void setSrc(ImageView view, Uri image, Drawable placeholder) {
//        if (image != null) {
//            RequestCreator requestCreator = Picasso.with(view.getContext()).load(image);
//            requestCreator.placeholder(placeholder);
//            if (placeholder instanceof Animatable || (placeholder instanceof LayerDrawable)) {
//                ImageTarget target = new ImageTarget(view);
//                requestCreator.into(target);
//                view.setTag(R.id.picasso, target);
//            } else {
//                if (view.getScaleType() == ImageView.ScaleType.CENTER_INSIDE) {
//                    requestCreator.fit().centerInside();
//                } else if (view.getScaleType() == ImageView.ScaleType.CENTER_CROP) {
//                    requestCreator.fit().centerCrop();
//                }
//                requestCreator.into(view);
//            }
//        } else {
//            view.setImageDrawable(placeholder);
//        }
//    }
}
