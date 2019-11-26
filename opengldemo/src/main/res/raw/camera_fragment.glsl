//预览相机的着色器，顶点着色器不变，需要修改片元着色器
//不再用sampler2D采样，需要用samplerExternalOES纹理采样器
//需要在头部增加使用扩展纹理的声明#extension GL_OES_EGL_image_external : require
#extension GL_OES_EGL_image_external : require

//设置float采用的数据精度
precision mediump float;

//采样点坐标
varying vec2 aCoord;

//采样器
uniform samplerExternalOES vTexture;

void main() {
    //gl_FragColor 变量接收像素值
    //texture2D() : 采样器，采集aCoord的像素
    gl_FragColor = texture2D(vTexture,aCoord);

    //添加灰度滤镜
    //305911公式，简单的灰度滤镜
//    vec4 rgba = texture2D(vTexture,aCoord);
//    float gray = 0.30 * rgba.r + 0.59*rgba.g+0.11*rgba.b;
//    gl_FragColor = vec4(gray,gray,gray,1.0);
}
