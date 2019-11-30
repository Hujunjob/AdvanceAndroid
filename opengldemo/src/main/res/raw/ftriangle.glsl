#version 320 es

precision lowp float;

out vec4 FragColor;

in vec3 ourColor; // 从顶点着色器传来的输入变量（名称相同、类型相同）

//在程序里设定该值，从CPU传递给GPU
//uniform vec4 ourColor;

void main() {
    //    FragColor = vec4(1.0f,0.5f,0.2f,1.0f);
    //    FragColor = vertexColor;
    //    FragColor = ourColor;
    FragColor = vec4(ourColor,1.0);
}
