package com.tencent.lucky.sample.fresco

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeController
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.facebook.imagepipeline.request.Postprocessor
import com.tencent.lucky.sample.R
import com.tencent.lucky.sample.Type
import com.tencent.lucky.transforms.fresco.FrescoMultiTransform
import com.tencent.lucky.transforms.fresco.adjacent_sample.CartoonFrescoTransform
import com.tencent.lucky.transforms.fresco.base.BrightnessFrescoTransform
import com.tencent.lucky.transforms.fresco.base.GrayscaleFrescoTransform
import com.tencent.lucky.transforms.fresco.base.SharpenFrescoTransform
import com.tencent.lucky.transforms.fresco.blend.LutFrescoTransform
import com.tencent.lucky.transforms.fresco.color_enhance.ColorInvertFrescoTransform
import com.tencent.lucky.transforms.fresco.dural_sample.BoxBlurFrescoTransform
import com.tencent.lucky.transforms.fresco.dural_sample.GaussianBlurFrescoTransform
import com.tencent.lucky.transforms.fresco.other.AeroFrescoTransform
import com.tencent.lucky.transforms.fresco.other.SketchFrescoTransform
import com.tencent.lucky.transforms.fresco.transform.BulgeDistortionFrescoTransform
import com.tencent.lucky.transforms.fresco.transform.GlassSphereFrescoTransform
import com.tencent.lucky.transforms.fresco.transform.SwirlFrescoTransform
import com.tencent.lucky.transforms.fresco.transform.VignetteFrescoTransform
import com.tencent.lucky.transforms.util.ASSET_PREFIX

/**
 * adapter for frescoActivity
 */
class FrescoAdapter(
    private val context: Context,
    private val dataSet: MutableList<Type>,
    private val imageUrl: Int
) : RecyclerView.Adapter<FrescoAdapter.ViewHolder>() {

  override fun getItemCount(): Int {
    return dataSet.size
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val v = LayoutInflater.from(context).inflate(R.layout.fresco_list_item, parent, false)
    return ViewHolder(v)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.image.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
      var processor: Postprocessor? = null

      when (dataSet[position]) {
          Type.Raw -> {
              // do nothing
          }

          Type.Brightness -> {
              processor = BrightnessFrescoTransform(0.4f)
          }

          Type.Grayscale -> {
              processor = GrayscaleFrescoTransform()
          }

          Type.Lut_Moka -> {
              processor = LutFrescoTransform(
                  lutPath = "${ASSET_PREFIX}luts/moka_lf.png",
                  context = context
              )
          }

          Type.Lut_Mopian -> {
              processor = LutFrescoTransform(
                  lutPath = "${ASSET_PREFIX}luts/mopian_lf.png",
                  context = context
              )
          }

          Type.Sharpen -> {
              processor = SharpenFrescoTransform()
          }

          Type.Invert -> {
              processor = ColorInvertFrescoTransform()
          }

          Type.BoxBlur -> {
              processor = BoxBlurFrescoTransform()
          }

          Type.GaussianBlur -> {
              processor = GaussianBlurFrescoTransform()
          }

          Type.Aero -> {
              processor = AeroFrescoTransform()
          }

          Type.BulgeDistortion -> {
              processor = BulgeDistortionFrescoTransform()
          }

          Type.GlassSphere -> {
              processor = GlassSphereFrescoTransform()
          }

          Type.Swirl -> {
              processor = SwirlFrescoTransform()
          }

          Type.Vignetting -> {
              processor = VignetteFrescoTransform()
          }

          Type.Cartoon -> {
              processor = CartoonFrescoTransform()
          }

          Type.Sketch -> {
              processor = SketchFrescoTransform()
          }

          // multiple transformations
          Type.MultiTransform_GrayAndBoxBlur -> {
              processor = FrescoMultiTransform(listOf(GrayscaleFrescoTransform(), BoxBlurFrescoTransform()))
          }

      }
      val request = ImageRequestBuilder.newBuilderWithResourceId(imageUrl)
          .setPostprocessor(processor)
          .build()
      val controller = Fresco.newDraweeControllerBuilder()
          .setImageRequest(request)
          .setOldController(holder.image.controller)
          .build() as PipelineDraweeController

      holder.image.controller = controller
      holder.title.text = dataSet[position].name
  }

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var image: SimpleDraweeView = itemView.findViewById(R.id.image)
    var title: TextView = itemView.findViewById(R.id.title)
  }
}
