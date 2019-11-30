#version 320 es

//在位置0处，位置变量
layout (location = 0) in vec3 aPos;
//在位置1处，颜色变量
layout (location = 1) in vec3 aColor;

//out vec4 vertexColor;
out vec3 ourColor;

void main() {
    gl_Position = vec4(aPos, 1.0);
    //    vertexColor = vec4(0.5,0.0,0.0,1.0);
    ourColor = aColor;
}
