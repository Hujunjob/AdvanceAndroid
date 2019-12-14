#version 300 es

//在位置0处，位置变量
layout (location = 0) in vec3 aPos;

//在位置1处，颜色变量
layout (location = 1) in vec3 aColor;

//位置2处，设置纹理的顶点坐标
layout (location = 2) in vec2 aTexCood;

out vec2 TexCoord;

//外面传入的旋转矩阵
//uniform mat4 transform;

//Model 矩阵
uniform mat4 model;

//View 矩阵
uniform mat4 view;

//投影矩阵
uniform mat4 projection;

void main() {
    //    gl_Position = vec4(aPos, 1.0);
    //传递变换矩阵进来

    //    gl_Position = transform * vec4(aPos, 1.0);

    gl_Position =  projection * view * model * vec4(aPos,1.0);

    //    vertexColor = vec4(0.5,0.0,0.0,1.0);
    //    ourColor = aColor;
    TexCoord = aTexCood;
}
