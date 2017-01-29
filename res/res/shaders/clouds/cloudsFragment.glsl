#version 130

//---------CONSTANT------------
const float shadowDarkness = 0.7;

//---------IN------------
in vec4 pass_positionRelativeToCam;
in vec2 pass_textureCoords;
in vec4 pass_shadowCoords;

//---------UNIFORM------------
layout(binding = 0) uniform sampler2D diffuseMap;
layout(binding = 1) uniform sampler2D shadowMap;
uniform float shadowMapSize;
uniform float darkness;
uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

//---------OUT------------
layout(location = 0) out vec4 out_colour;

//---------SHADOW------------
float shadow(void) {
    const float bias = 0.0001;
	float shadowTexelSize = 1.0 / shadowMapSize;
	float shadowHalfw = shadowTexelSize * 0.5;
	float shadowTotal = 0.0;
	float shadowValue = 0.0;
	float shadowShadeFactor;
	shadowValue = texture(shadowMap, pass_shadowCoords.xy + vec2(0 + shadowHalfw, 0 + shadowHalfw)).r + bias;

    if (pass_shadowCoords.x > 0.0 && pass_shadowCoords.x < 1.0 && pass_shadowCoords.y > 0.0 && pass_shadowCoords.y < 1.0 && pass_shadowCoords.z > 0.0 && pass_shadowCoords.z < 1.0) {
        if (shadowValue < pass_shadowCoords.z) {
            shadowTotal += shadowDarkness * pass_shadowCoords.w;
        }

        shadowValue = texture(shadowMap, pass_shadowCoords.xy + vec2(shadowTexelSize + shadowHalfw, 0 + shadowHalfw)).r + bias;

        if (shadowValue < pass_shadowCoords.z) {
            shadowTotal += shadowDarkness * pass_shadowCoords.w;
        }

        shadowValue = texture(shadowMap, pass_shadowCoords.xy + vec2(0 + shadowHalfw, shadowTexelSize + shadowHalfw)).r + bias;

        if (shadowValue < pass_shadowCoords.z) {
            shadowTotal += shadowDarkness * pass_shadowCoords.w;
        }

        shadowValue = texture(shadowMap, pass_shadowCoords.xy + vec2(shadowTexelSize + shadowHalfw, shadowTexelSize + shadowHalfw)).r + bias;

        if (shadowValue < pass_shadowCoords.z) {
            shadowTotal += shadowDarkness * pass_shadowCoords.w;
        }

        shadowShadeFactor = 1.0 - (shadowTotal / 4.0);
    } else {
        shadowShadeFactor = 1.0;
    }

    return shadowShadeFactor;
}

//---------VISIBILITY------------
float visibility(void) {
	return clamp(exp(-pow((length(pass_positionRelativeToCam.xyz) * fogDensity), fogGradient)), 0.0, 1.0);
}

//---------MAIN------------
void main(void) {
	vec4 diffuseColour = texture(diffuseMap, pass_textureCoords);

	// Creates the output image.
	out_colour = vec4(1, 0, 0, 1);
	out_colour = mix(vec4(fogColour, 1.0), diffuseColour * shadow() * (-(darkness - 0.5) + 0.5), visibility());
}