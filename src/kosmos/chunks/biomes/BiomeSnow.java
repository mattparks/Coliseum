/*
 * Copyright (C) 2017, Equilibrium Games - All Rights Reserved
 *
 * This source file is part of New Kosmos
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package kosmos.chunks.biomes;

import flounder.entities.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.textures.*;
import kosmos.chunks.*;
import kosmos.entities.instances.objects.*;
import kosmos.entities.instances.plants.*;
import kosmos.entities.instances.trees.*;
import kosmos.particles.*;
import kosmos.world.*;

public class BiomeSnow implements IBiome {
	private static final TextureObject texture = TextureFactory.newBuilder().setFile(new MyFile(KosmosChunks.TERRAINS_FOLDER, "snow.png")).clampEdges().create();
	private static final ParticleType particle = new ParticleType("snow", TextureFactory.newBuilder().setFile(new MyFile(KosmosParticles.PARTICLES_FOLDER, "snowParticle.png")).setNumberOfRows(4).create(), 3.5f, 0.20f);

	@Override
	public String getBiomeName() {
		return "snow";
	}

	@Override
	public TextureObject getTexture() {
		return texture;
	}

	@Override
	public Entity generateEntity(Chunk chunk, Vector3f tilePosition) {
		if (Math.abs(KosmosWorld.getNoise().noise2(tilePosition.z * (float) Math.sin(tilePosition.x), tilePosition.x * (float) Math.sin(tilePosition.z))) <= 0.3f) {
			return null;
		}

		float spawn = KosmosWorld.getNoise().noise1((tilePosition.z - tilePosition.x) * (float) Math.sin(tilePosition.x + tilePosition.z)) * 100.0f;
		float rotation = KosmosWorld.getNoise().noise1(tilePosition.x - tilePosition.z) * 3600.0f;

		if (tilePosition.y < 0.0f) {
			if (spawn == 1) {
				return new InstanceLilipad(FlounderEntities.getEntities(), new Vector3f(tilePosition.x, 0.025f, tilePosition.z), new Vector3f(0.0f, rotation, 0.0f));
			}

			return null;
		}

		switch ((int) spawn) {
			case 1:
				return new InstanceTreePine(FlounderEntities.getEntities(),
						new Vector3f(
								tilePosition.x,
								0.375f + tilePosition.y * 0.5f,
								tilePosition.z
						),
						new Vector3f(0.0f, rotation, 0.0f)
				);
			case 2:
				return new InstanceTreeMaple(FlounderEntities.getEntities(),
						new Vector3f(
								tilePosition.x,
								0.53f + tilePosition.y * 0.5f,
								tilePosition.z
						),
						new Vector3f(0.0f, rotation, 0.0f)
				);
			case 3:
				return new InstanceTreeYellow(FlounderEntities.getEntities(),
						new Vector3f(
								tilePosition.x,
								0.5f + tilePosition.y * 0.5f,
								tilePosition.z
						),
						new Vector3f(0.0f, rotation, 0.0f)
				);
			case 4:
				if (spawn - 4 < 0.2f) {
					return new InstanceBottleMaple(FlounderEntities.getEntities(),
							new Vector3f(
									tilePosition.x,
									-0.35f + tilePosition.y * 0.5f,
									tilePosition.z
							),
							new Vector3f(0.0f, rotation, 0.0f)
					);
				}

				return null;
			case 5:
				if (spawn - 5 < 0.2f) {
					return new InstanceTreeSnow(FlounderEntities.getEntities(),
							new Vector3f(
									tilePosition.x,
									0.375f + tilePosition.y * 0.5f,
									tilePosition.z
							),
							new Vector3f(0.0f, rotation, 0.0f)
					);
				}

				return null;
			default:
				return null;
		}
	}

	@Override
	public ParticleType getWeatherParticle() {
		return particle;
	}

	@Override
	public float getTempDay() {
		return -0.5f;
	}

	@Override
	public float getTempNight() {
		return -3.0f;
	}

	@Override
	public float getHumidity() {
		return 23.0f;
	}

	@Override
	public float getWindSpeed() {
		return 0.37f;
	}
}
