#version 300 es

precision lowp float;

out vec4 FragColor;

in vec2 TexCoord;

//默认为第一个纹理
uniform sampler2D ourTexture;

void main() {
    FragColor = texture(ourTexture,TexCoord);
}
