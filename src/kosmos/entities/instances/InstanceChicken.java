package kosmos.entities.instances;

import flounder.entities.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.models.*;
import flounder.resources.*;
import flounder.space.*;
import flounder.textures.*;

/// Automatically generated entity source
/// Date generated: 30.3.2017 - 12:8
/// Created by: matthew

public class InstanceChicken extends Entity {
	private static final MyFile COLLADA = new MyFile(FlounderEntities.ENTITIES_FOLDER, "chicken", "collada.dae");
	private static final TextureObject TEXTURE = TextureFactory.newBuilder().setFile(new MyFile(FlounderEntities.ENTITIES_FOLDER, "chicken", "diffuse.png")).setNumberOfRows(1).create();
	
	public InstanceChicken(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
		super(structure, position, rotation);
		new kosmos.entities.components.ComponentAnimation(this, 0.2f, COLLADA, TEXTURE, 1);
		new kosmos.entities.components.ComponentSurface(this, 1.0f, 0.0f, false, false);
	 }
 }

