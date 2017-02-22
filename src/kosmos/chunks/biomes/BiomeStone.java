/*
 * Copyright (C) 2017, Equilibrium Games - All Rights Reserved
 *
 * This source file is part of New Kosmos
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package kosmos.chunks.biomes;

import flounder.maths.vectors.*;
import kosmos.chunks.*;
import kosmos.chunks.tiles.*;
import kosmos.particles.loading.*;

public class BiomeStone implements IBiome {
	@Override
	public String getBiomeName() {
		return "rock";
	}

	@Override
	public Tile getMainTile() {
		return Tile.TILE_STONE;
	}

	@Override
	public void generateEntity(Chunk chunk, Vector2f worldPos, Vector2f tilePosition, int height) {
		float rotation = KosmosChunks.getNoise().noise1((worldPos.x - worldPos.y) / 66.6f) * 3600.0f;

		switch ((int) (KosmosChunks.getNoise().noise1((worldPos.y - worldPos.x) / 11.0f) * 200.0f)) {
			/*case 1:
				new InstanceBush(chunk.getEntities(),
						new Vector3f(
								chunk.getPosition().x + (float) (tilePosition.x * 0.5),
								(float) ((2.5 * 0.25) + (height * Math.sqrt(2.0)) * 0.5),
								chunk.getPosition().z + (float) (tilePosition.y * 0.5)
						),
						new Vector3f(0.0f, rotation, 0.0f)
				);
				break;
			case 2:
				new InstanceRockGem(chunk.getEntities(),
						new Vector3f(
								chunk.getPosition().x + (float) (tilePosition.x * 0.5),
								(float) ((0.0f * 0.25) + (height * Math.sqrt(2.0)) * 0.5),
								chunk.getPosition().z + (float) (tilePosition.y * 0.5)
						),
						new Vector3f(0.0f, rotation, 0.0f)
				);
				break;*/
			default:
				break;
		}
	}

	@Override
	public ParticleTemplate getWeatherParticle() {
		return null;
	}

	@Override
	public float getTempDay() {
		return 8.2f;
	}

	@Override
	public float getTempNight() {
		return -13.0f;
	}

	@Override
	public float getHumidity() {
		return 10.0f;
	}
}