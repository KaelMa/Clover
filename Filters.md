# 内置滤镜

## 基础滤镜

提供有关输入数据的基本色彩属性操作的滤镜。

| 滤镜                          | 用途     |
| ----------------------------- | -------- |
| BrightnessGlideTransform      | 亮度     |
| ContrastGlideTransform        | 对比度   |
| ExposureGlideTransform        | 曝光度   |
| GammaGlideTransform           | Gamma    |
| GrayscaleGlideTransform       | 灰度     |
| HighlightShadowGlideTransform | 高光阴影 |
| HueGlideTransform             | 色调     |
| OpacityGlideTransform         | 不透明度 |
| RGBGlideTransform             | RGB更改  |
| SaturationGlideTransform      | 饱和度   |
| SharpenGlideTransform         | 锐化     |

## 混合滤镜

用来平滑两个输入源的图层资源为一个单一输出。

| 滤镜              | 用途          |
| ----------------- | ------------- |
| LutGlideTransform | lut颜色映射表 |

## 增强滤镜

用于加强输入流单一色调或多色调的变化、增强、减弱等处理。

| 滤镜                        | 用途             |
| --------------------------- | ---------------- |
| BilateralGlideTransform     | 双边滤波         |
| CGAColorspaceGlideTransform | CGA色域着色      |
| ColorBalanceGlideTransform  | 色泽平衡         |
| ColorInvertGlideTransform   | 反色             |
| ColorMatrixGlideTransform   | 应用任意颜色矩阵 |
| WhiteBalanceGlideTransform  | 白平衡           |

## 模糊滤镜

多采样模糊效果。

| 滤镜                       | 用途         |
| -------------------------- | ------------ |
| BoxBlurGlideTransform      | 均衡模糊效果 |
| GaussianBlurGlideTransform | 高斯模糊效果 |

## 形变滤镜

提供图形变化效果。

| 滤镜                           | 用途         |
| ------------------------------ | ------------ |
| BulgeDistortionGlideTransform  | 鱼眼效果     |
| CrosshatchGlideTransform       | 交叉阴影效果 |
| GlassSphereGlideTransform      | 水晶球效果   |
| SphereRefractionGlideTransform | 球面反射效果 |
| SwirlGlideTransform            | 漩涡效果     |
| VignetteGlideTransform         | 暗角         |

## 其他

其他

| 滤镜                 | 用途                            |
| -------------------- | ------------------------------- |
| SketchGlideTransform | 素描效果                        |
| AeroGlideTransform   | 毛玻璃效果(白色蒙层 + 高斯模糊) |
