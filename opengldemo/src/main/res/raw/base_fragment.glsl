//设置float采用的数据精度
precision mediump float;

//采样点坐标
varying vec2 aCoord;

//采样器
uniform sampler2D vTexture;

uniform sampler2D texture2;

void main() {
    //gl_FragColor 变量接收像素值
    //texture2D() : 采样器，采集aCoord的像素
    gl_FragColor = texture2D(vTexture,aCoord);
}
