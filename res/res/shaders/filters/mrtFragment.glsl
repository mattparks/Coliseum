#version 130

//---------INCLUDES------------
#include "maths.glsl"

//---------CONSTANT------------
const int LIGHTS = 86;

const int SHADOW_PCF = 0;
const float SHADOW_BIAS = 0.001;
const float SHADOW_DARKNESS = 0.6;

//---------IN------------
in vec2 pass_textureCoords;

//---------UNIFORM------------
layout(binding = 0) uniform sampler2D originalAlbedo;
layout(binding = 1) uniform sampler2D originalNormals;
layout(binding = 2) uniform sampler2D originalExtras;
layout(binding = 3) uniform sampler2D originalDepth;
layout(binding = 4) uniform sampler2D shadowMap;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightColour[LIGHTS];
uniform vec3 lightPosition[LIGHTS];
uniform vec3 lightAttenuation[LIGHTS];

uniform mat4 shadowSpaceMatrix;
uniform float shadowDistance;
uniform float shadowTransition;
uniform float shadowMapSize;

uniform vec3 fogColour;
uniform float fogDensity;
uniform float fogGradient;

//---------OUT------------
layout(location = 0) out vec4 out_colour;

//---------CALCULATE LOCATION------------
vec3 decodeLocation() {
    float depth = texture(originalDepth, pass_textureCoords).x;
    vec4 p = inverse(projectionMatrix) * (vec4(pass_textureCoords, depth, 1.0) * 2.0 - 1.0);
    return vec3(inverse(viewMatrix) * vec4(p.xyz / p.w, 1.0));
}

//---------SHADOW------------
float shadow(sampler2D shadowMap, vec4 shadowCoords, float shadowMapSize) {
    float totalTextels = (SHADOW_PCF * 2.0 + 1.0) * (SHADOW_PCF * 2.0 + 1.0);
    float texelSize = 1.0 / shadowMapSize;
    float total = 0.0;

    for (int x = -SHADOW_PCF; x <= SHADOW_PCF; x++) {
        for (int y = -SHADOW_PCF; y <= SHADOW_PCF; y++) {
            float shadowValue = texture(shadowMap, shadowCoords.xy + vec2(x, y) * texelSize).r;

            if (shadowCoords.z > shadowValue + SHADOW_BIAS) {
                total += 1.0;
            }
        }
    }

    total /= totalTextels;
    return 1.0 - (total * SHADOW_DARKNESS * shadowCoords.w);
}

//---------FOG VISIBILITY------------
float visibility(vec4 positionRelativeToCam, float fogDensity, float fogGradient) {
	return clamp(exp(-pow((length(positionRelativeToCam.xyz) * fogDensity), fogGradient)), 0.0, 1.0);
}

//---------MAIN------------
void main(void) {
	vec4 albedo = texture(originalAlbedo, pass_textureCoords);

	// Ignores anything this is not a rendered object, so mostly the cleared colour.
	if (albedo.a == 0.0) {
	    out_colour = vec4(fogColour, 1.0);
	    return;
	}

	vec4 normals = texture(originalNormals, pass_textureCoords);

	vec4 extras = texture(originalExtras, pass_textureCoords);
	float shineDamper = extras.r;
	float reflectivity = extras.g;
	bool ignoreFog = extras.b == (1.0 / 3.0) || extras.b == (3.0 / 3.0);
	bool ignoreShadows = extras.b == (2.0 / 3.0) || extras.b == (3.0 / 3.0);

	vec4 worldPosition = vec4(decodeLocation(), 1.0);

	vec3 toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
    vec4 positionRelativeToCam = viewMatrix * worldPosition;
    vec4 shadowCoords = shadowSpaceMatrix * worldPosition;
    float distanceAway = length(positionRelativeToCam.xyz);
    distanceAway = distanceAway - ((shadowDistance * 2.0) - shadowTransition);
    distanceAway = distanceAway / shadowTransition;
    shadowCoords.w = clamp(1.0 - distanceAway, 0.0, 1.0);

    vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	for (int i = 0; i < LIGHTS; i++) {
		vec3 toLightVector = lightPosition[i] - worldPosition.xyz;
		float distance = length(toLightVector);
		float attinuationFactor = lightAttenuation[i].x + (lightAttenuation[i].y * distance) + (lightAttenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector);

		float brightness = max(dot(normals.xyz, unitLightVector), 0.0);
		vec3 reflectedLightDirection = reflect(-unitLightVector, normals.xyz);
		float specularFactor = max(dot(reflectedLightDirection, normalize(toCameraVector)), 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);

		totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attinuationFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColour[i]) / attinuationFactor;
	}

    out_colour = vec4(albedo.rgb, 1.0);

    if (!ignoreShadows) {
        out_colour = vec4(out_colour.rgb * shadow(shadowMap, shadowCoords, shadowMapSize), 1.0);
        out_colour = (vec4(totalDiffuse, 1.0) * out_colour) + vec4(totalSpecular, 1.0);
    }

    if (!ignoreFog) {
        out_colour = mix(vec4(fogColour, 1.0), out_colour, visibility(positionRelativeToCam, fogDensity, fogGradient));
    }
}
