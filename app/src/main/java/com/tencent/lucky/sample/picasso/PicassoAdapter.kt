package com.tencent.lucky.sample.picasso

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tencent.lucky.sample.R
import com.tencent.lucky.sample.Type
import com.tencent.lucky.transforms.glide.dural_sample.GaussianBlurGlideTransform
import com.tencent.lucky.transforms.glide.other.SketchGlideTransform
import com.tencent.lucky.transforms.picasso.adjacent_sample.CartoonPicassoTransform
import com.tencent.lucky.transforms.picasso.base.BrightnessPicassoTransform
import com.tencent.lucky.transforms.picasso.base.GrayscalePicassoTransform
import com.tencent.lucky.transforms.picasso.base.SharpenPicassoTransform
import com.tencent.lucky.transforms.picasso.blend.LutPicassoTransform
import com.tencent.lucky.transforms.picasso.color_enhance.ColorInvertPicassoTransform
import com.tencent.lucky.transforms.picasso.dural_sample.BoxBlurPicassoTransform
import com.tencent.lucky.transforms.picasso.dural_sample.GaussianBlurPicassoTransform
import com.tencent.lucky.transforms.picasso.other.AeroPicassoTransform
import com.tencent.lucky.transforms.picasso.other.SketchPicassoTransform
import com.tencent.lucky.transforms.picasso.transform.BulgeDistortionPicassoTransform
import com.tencent.lucky.transforms.picasso.transform.GlassSpherePicassoTransform
import com.tencent.lucky.transforms.picasso.transform.SwirlPicassoTransform
import com.tencent.lucky.transforms.picasso.transform.VignettePicassoTransform
import com.tencent.lucky.transforms.util.ASSET_PREFIX

/**
 * adapter
 */
class PicassoAdapter(
    private val context: Context,
    private val dataSet: MutableList<Type>,
    private val imageUrl: Int
) : RecyclerView.Adapter<PicassoAdapter.ViewHolder>() {

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
            Picasso.get()
                .load(imageUrl)
                .into(holder.image)
        }

        Type.Brightness -> {
            Picasso.get()
                .load(imageUrl)
                .transform(BrightnessPicassoTransform(0.4f))
                .into(holder.image)
        }

        Type.Grayscale -> {
            Picasso.get()
                .load(imageUrl)
                .transform(GrayscalePicassoTransform())
                .into(holder.image)
        }

        Type.Lut_Moka -> {
            Picasso.get()
                .load(imageUrl)
                .transform(LutPicassoTransform(
                    lutPath = "${ASSET_PREFIX}luts/moka_lf.png",
                    context = context
                ))
                .into(holder.image)
        }

        Type.Lut_Mopian -> {
            Picasso.get()
                .load(imageUrl)
                .transform(LutPicassoTransform(
                    lutPath = "${ASSET_PREFIX}luts/mopian_lf.png",
                    context = context
                ))
                .into(holder.image)
        }

        Type.Sharpen -> {
            Picasso.get()
                .load(imageUrl)
                .transform(SharpenPicassoTransform())
                .into(holder.image)
        }

        Type.Invert -> {
            Picasso.get()
                .load(imageUrl)
                .transform(ColorInvertPicassoTransform())
                .into(holder.image)
        }

        Type.BoxBlur -> {
            Picasso.get()
                .load(imageUrl)
                .transform(BoxBlurPicassoTransform())
                .into(holder.image)
        }

        Type.GaussianBlur -> {
            Picasso.get()
                .load(imageUrl)
                .transform(GaussianBlurPicassoTransform())
                .into(holder.image)
        }

        Type.Aero -> {
            Picasso.get()
                .load(imageUrl)
                .transform(AeroPicassoTransform())
                .into(holder.image)
        }

        Type.BulgeDistortion -> {
            Picasso.get()
                .load(imageUrl)
                .transform(BulgeDistortionPicassoTransform())
                .into(holder.image)
        }

        Type.GlassSphere -> {
            Picasso.get()
                .load(imageUrl)
                .transform(GlassSpherePicassoTransform())
                .into(holder.image)
        }

        Type.Swirl -> {
            Picasso.get()
                .load(imageUrl)
                .transform(SwirlPicassoTransform())
                .into(holder.image)
        }

        Type.Vignetting -> {
            Picasso.get()
                .load(imageUrl)
                .transform(VignettePicassoTransform())
                .into(holder.image)
        }

        Type.Cartoon -> {
            Picasso.get()
                .load(imageUrl)
                .transform(CartoonPicassoTransform())
                .into(holder.image)
        }

        Type.Sketch -> {
            Picasso.get()
                .load(imageUrl)
                .transform(SketchPicassoTransform())
                .into(holder.image)
        }

        // multiple transformations
        Type.MultiTransform_GrayAndBoxBlur -> {
            Picasso.get()
                .load(imageUrl)
                .transform(GrayscalePicassoTransform())
                .transform(BoxBlurPicassoTransform())
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
