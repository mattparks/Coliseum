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

public class InstanceSun extends Entity {
	private static final ModelObject MODEL = ModelFactory.newBuilder().setFile(new MyFile(FlounderEntities.ENTITIES_FOLDER, "sun", "model.obj")).create();
	private static final TextureObject TEXTURE = TextureFactory.newBuilder().setFile(new MyFile(FlounderEntities.ENTITIES_FOLDER, "sun", "diffuse.png")).setNumberOfRows(1).create();
	
	public InstanceSun(ISpatialStructure<Entity> structure, Vector3f position, Vector3f rotation) {
		super(structure, position, rotation);
		new kosmos.entities.components.ComponentCelestial(this, true);
		new kosmos.entities.components.ComponentModel(this, 16.0f, MODEL, TEXTURE, 1);
		new kosmos.entities.components.ComponentLight(this, new Vector3f(0.0f, 0.0f, 0.0f), new Colour(0.56650835f, 0.47796404f, 0.42216897f), new Attenuation(1.0f, 0.0f, 0.0f));
		new kosmos.entities.components.ComponentSurface(this, 1.0f, 0.0f, true, true);
	 }
 }

