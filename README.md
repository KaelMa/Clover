# Clover

>  [中文文档](README.zh.md)


Clover is an offline GPU runtime framework that can add various GPU filter effects to images, such as Gaussian blur and beauty. Clover is compatible with common image loading frameworks, including Glide, Picasso, and Fresco. Users can add GPU filter effects to images with just one line of code without changing the image loading usage. It also includes other functions based on offline GPU runtime, such as hardware video frame capture.

Features: Easy to Use

- One line of code to achieve the function

- 260kb in size

- filter runtime specifically designed for images

- Pure Kotlin implementation

- Extensible filter interface and filter library based on OpenGL ES3.0


## How to Use

### Step1
**Gradle**
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.KaelMa:Clover:1.0.2'
}
```
### Step2
Clover supports common image loading frameworks, including Glide, Picasso, and Fresco, and also provides an interface for self-developed image loading frameworks. 
Details are as follows:

####  Glide

```kotlin
// One line code with Gaussian Blur Effect
Glide.with(context)
    .load(imageUrl)
    .transform(GaussianBlurGlideTransform())
    .into(holder.image)

// One line code with Frosted Glass Effect
Glide.with(context)
		.load(imageUrl)
		.diskCacheStrategy(DiskCacheStrategy.ALL)
		.transform(AeroGlideTransform(blurSize = 10f))
		.into(holder.image)
```

#### Picasso

```kotlin
// One line code with Gaussian Blur Effect
Picasso.with(mContext)
    .load(R.drawable.demo)
    .transform(GaussianBlurPicassoTransform())
    .into((ImageView)
```

#### Fresco

```kotlin
// One line code with Gaussian Blur Effect
val request = ImageRequestBuilder.newBuilderWithResourceId(imageUrl)
	.setPostprocessor(GrayscaleFrescoTransform())
	.build()
val controller = Fresco.newDraweeControllerBuilder()
	.setImageRequest(request)
	.setOldController(holder.image.controller)
	.build() as PipelineDraweeController
holder.image.controller = controller
```

#### Self-developed Image Loading Framework

```kotlin
// It provides an inBitmap -> filter -> outBitmap interface, which requires the image framework to handle caching logic itself.
val outBitmap = Clover.with()
    .setImage(inBitmap)
    .setFilter(filter)
    .getFilterBitmap()
```



## Demo

### inner filters

![](md/clover-demo.gif)


## Advanced

### Cache
Benefits to the capabilities of the image loading framework, users can set disk caching on demand.

> Sample by Glide

```
Glide.with(context)
    .load(imageUrl)
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .transform(GaussianBlurGlideTransform(5f))
    .into(holder.image)
```
If it is the same image and the radius of Gaussian blur is the same, it will not be generated repeatedly, and the result image in the disk cache will be used directly.

### Combination filters
Support for self-combining filter effects
```
Glide.with(context)
    .load(imageUrl)
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .transform(MultiTransformation(
        GrayscaleGlideTransform(),
        BoxBlurGlideTransform(5f)
        ))
    .into(holder.image)
```
The effect is to apply an grayscale filter and then a Box Blur filter.
If performance optimization is required, it can inherit from GPUImageFilterGroup. However, for image rendering, this is usually not a problem. Download time is usually hundreds of times that of filter processing time (1000ms -> 5ms).

### Filters
All filters must implement the `BaseFilter` interface. 
The runtime environment is an offline Pbuffer environment and is compatible with both GpuImage formats. 
Currently, all built-in filters are ported from GpuImage and reimplemented using OpenGL ES3 and Kotlin. 
The specific built-in filters are as follows:

**[inner filters](md/Filters.zh.md)**

### Framework Diagram

![](md/image-20200429-201923.png)

### Flowchart

![](md/image-20200421143708669.png)


### How To Expand
Implement `BaseFilter`interface，and extends `GlideBaseTransform`

```kotlin
interface BaseFilter {
    /**
     * init filter, must be called from gl thread
     */
    fun init()
    /**
     * on output size changed
     * @param width
     * @param height
     */
    fun onOutputSizeChanged(width: Int, height: Int) {}
    /**
     * draw
     * @param inputTexName input texture name
     */
    fun draw(inputTexName: Int)
    /**
     * destroy gl resource
     */
    fun destroy()
}

// sample by gaussian blur
class GaussianBlurGlideTransform(var blurSize: Float = 2.0f):
    GlideBaseTransform(GPUImageGaussianBlurFilter(blurSize))
{
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((id + blurSize).toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        return o is GaussianBlurGlideTransform && o.blurSize == blurSize
    }

    override fun hashCode(): Int {
        return id.hashCode() + ((blurSize + 1.0f) * 10).toInt()
    }
}
```

## Other Features
**Capture Frame by Hardware**

Based on MediaCodec and an offline GPU context, implemented with OpenGL, it offers 10 times the performance of the system's MediaMetadataRetriever.

```kotlin
object CloverUtil {
    /**
     * Get Frame by MediaCodec + OpenGL
     * @param videoPath video path
     * @param timeUs timestamp
     * @return bitmap 
     */
    @Throws(ExecutionException::class, InterruptedException::class)
    fun getFrameAtTime(videoPath: String, timeUs: Long): Bitmap? {
        return ExtractUtil.getFrameAtTime(videoPath, timeUs)
    }
}
```

## Open Source License

**MIT**