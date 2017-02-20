/*
 * Copyright (C) 2017, Equilibrium Games - All Rights Reserved
 *
 * This source file is part of New Kosmos
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package kosmos.entities.instances;

import flounder.animation.*;
import flounder.collada.*;
import flounder.collada.animation.*;
import flounder.entities.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.space.*;
import flounder.textures.*;
import kosmos.entities.components.*;

public class InstanceCowboy extends Entity {
	private static final MyFile colladaFile = new MyFile(FlounderEntities.ENTITIES_FOLDER, "cowboy", "cowboy.dae");
	private static final TextureObject texture = TextureFactory.newBuilder().setFile(new MyFile(FlounderEntities.ENTITIES_FOLDER, "cowboy", "cowboy.png")).create();

	public InstanceCowboy(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
		super(structure, position, rotation);

		ModelAnimated modelAnimated = FlounderCollada.loadCollada(colladaFile);

		AnimationData animationData = FlounderCollada.loadAnimation(colladaFile);
		Animation animation = FlounderAnimation.loadAnimation(animationData);

		ComponentAnimation componentAnimation = new ComponentAnimation(this, modelAnimated, 0.2f, texture, 1);
		componentAnimation.doAnimation(animation);
		ComponentPlayer componentPlayer = new ComponentPlayer(this);
		ComponentCollider componentCollider = new ComponentCollider(this);
		ComponentCollision componentCollision = new ComponentCollision(this);
	}
}
