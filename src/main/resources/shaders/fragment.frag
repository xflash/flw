#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec3 colour;
uniform int useColour;

void main() {
    fragColor = (useColour == 1)
        ? vec4(colour, 1)
        : texture(texture_sampler, outTexCoord);
}
