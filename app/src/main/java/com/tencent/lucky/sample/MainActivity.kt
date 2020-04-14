package com.tencent.lucky.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tencent.lucky.sample.fresco.FrescoActivity
import com.tencent.lucky.sample.glide.GlideActivity
import com.tencent.lucky.sample.picasso.PicassoActivity
import com.tencent.lucky.sample.picasso.PicassoAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var glidePreviewGif = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews(){
        glideGif.setOnCheckedChangeListener { _, isChecked ->
            glidePreviewGif = isChecked
        }

        glideButton.setOnClickListener {
            val intent = Intent(this, GlideActivity::class.java).apply {
                putExtra(GlideActivity.IS_GIF, glidePreviewGif)
            }
            startActivity(intent)
        }

        frescoButton.setOnClickListener {
            val intent = Intent(this, FrescoActivity::class.java)
            startActivity(intent)
        }

        picassoButton.setOnClickListener {
            val intent = Intent(this, PicassoActivity::class.java)
            startActivity(intent)
        }
    }
}

val datas = mutableListOf(
    Type.Raw,
    Type.Brightness,
    Type.Grayscale,
    Type.Lut_Moka,
    Type.Lut_Mopian,
    Type.Sharpen,
    Type.Invert,
    Type.BoxBlur,
    Type.GaussianBlur,
    Type.Aero,
    Type.BulgeDistortion,
    Type.GlassSphere,
    Type.Swirl,
    Type.Vignetting,
    Type.Cartoon,
    Type.Sketch,
    Type.MultiTransform_GrayAndBoxBlur
)

val gifUrls = mutableListOf(
    "https://wa.qq.com/hot-res/7899f1fc5c32dceeeee02c1fab61893a-m.gif"
)
