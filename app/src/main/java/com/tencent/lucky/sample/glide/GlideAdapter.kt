package com.tencent.lucky.sample.glide

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tencent.lucky.sample.R
import com.tencent.lucky.sample.Type
import com.tencent.lucky.transforms.glide.adjacent_sample.CartoonGlideTransform
import com.tencent.lucky.transforms.glide.base.BrightnessGlideTransform
import com.tencent.lucky.transforms.glide.base.GrayscaleGlideTransform
import com.tencent.lucky.transforms.glide.base.SharpenGlideTransform
import com.tencent.lucky.transforms.glide.blend.LutGlideTransform
import com.tencent.lucky.transforms.glide.color_enhance.ColorInvertGlideTransform
import com.tencent.lucky.transforms.glide.dural_sample.BoxBlurGlideTransform
import com.tencent.lucky.transforms.glide.dural_sample.GaussianBlurGlideTransform
import com.tencent.lucky.transforms.glide.other.AeroGlideTransform
import com.tencent.lucky.transforms.glide.other.SketchGlideTransform
import com.tencent.lucky.transforms.glide.transform.BulgeDistortionGlideTransform
import com.tencent.lucky.transforms.glide.transform.GlassSphereGlideTransform
import com.tencent.lucky.transforms.glide.transform.SwirlGlideTransform
import com.tencent.lucky.transforms.glide.transform.VignetteGlideTransform
import com.tencent.lucky.transforms.util.ASSET_PREFIX

/**
 * adapter
 */
class GlideAdapter<T>(
    private val context: Context,
    private val dataSet: MutableList<Type>,
    private val imageUrl: T
) : RecyclerView.Adapter<GlideAdapter.ViewHolder>() {

  override fun getItemCount(): Int {
    return dataSet.size
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val v = LayoutInflater.from(context).inflate(R.layout.layout_list_item, parent, false)
    return ViewHolder(v)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    when (dataSet[position]) {

        Type.Raw -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image)
        }

        Type.Brightness -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(BrightnessGlideTransform(0.4f))
                .into(holder.image)
        }

        Type.Grayscale -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(GrayscaleGlideTransform())
                .into(holder.image)
        }

        Type.Lut_Moka -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(LutGlideTransform(
                    lutPath = "${ASSET_PREFIX}luts/moka_lf.png",
                    context = context
                ))
                .into(holder.image)
        }

        Type.Lut_Mopian -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(LutGlideTransform(
                    lutPath = "${ASSET_PREFIX}luts/mopian_lf.png",
                    context = context
                ))
                .into(holder.image)
        }

        Type.Sharpen -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(SharpenGlideTransform())
                .into(holder.image)
        }

        Type.Invert -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(ColorInvertGlideTransform())
                .into(holder.image)
        }

        Type.BoxBlur -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(BoxBlurGlideTransform(5f))
                .into(holder.image)
        }

        Type.GaussianBlur -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(GaussianBlurGlideTransform(5f))
                .into(holder.image)
        }

        Type.Aero -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(AeroGlideTransform(blurSize = 10f))
                .into(holder.image)
        }

        Type.BulgeDistortion -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(BulgeDistortionGlideTransform())
                .into(holder.image)
        }

        Type.GlassSphere -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(GlassSphereGlideTransform())
                .into(holder.image)
        }

        Type.Swirl -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(SwirlGlideTransform())
                .into(holder.image)
        }

        Type.Vignetting -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(VignetteGlideTransform())
                .into(holder.image)
        }

        Type.Cartoon -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(CartoonGlideTransform())
                .into(holder.image)
        }

        Type.Sketch -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(SketchGlideTransform())
                .into(holder.image)
        }

        // multiple transformations
        Type.MultiTransform_GrayAndBoxBlur -> {
            Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(MultiTransformation(
                    GrayscaleGlideTransform(),
                    BoxBlurGlideTransform(5f)
                ))
                .into(holder.image)
        }
    }
    holder.title.text = dataSet[position].name
  }

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var image: ImageView = itemView.findViewById(R.id.image)
    var title: TextView = itemView.findViewById(R.id.title)
  }
}
