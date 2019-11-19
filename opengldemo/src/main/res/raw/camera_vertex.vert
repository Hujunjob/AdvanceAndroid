#version 120
//把定点坐标给这个变量，确定要画画的形状
attribute vec4 vPosition;

//接收纹理坐标，接收采样器采样图片的坐标
attribute vec4 vCoord;

//变换矩阵，需要将原本的vCoord(01,11,00,10)与矩阵相乘，才能得到surfacetexture的正确采样坐标
uniform mat4 vMatrix;

//传递给片元着色器，像素点
varying vec2 aCoord;

void main() {
    gl_Position = vec4(vec3(0.0), 1.0);
}
