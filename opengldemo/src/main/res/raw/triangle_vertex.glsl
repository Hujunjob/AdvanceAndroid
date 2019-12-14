#version 300 es

layout (location=0)in vec3 aCoord;

layout (location=1)in vec3 aColor;

out vec3 ourColor;

void main() {
    gl_Position = vec4(aCoord,1.0);

    ourColor = aColor;
}
