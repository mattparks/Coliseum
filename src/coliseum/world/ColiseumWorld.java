package coliseum.world;

import coliseum.chunks.*;
import coliseum.entities.instances.*;
import coliseum.particles.*;
import coliseum.particles.loading.*;
import coliseum.particles.spawns.*;
import flounder.entities.*;
import flounder.framework.*;
import flounder.lights.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.bounding.*;
import flounder.textures.*;

import java.util.*;

public class ColiseumWorld extends IModule {
	private static final ColiseumWorld INSTANCE = new ColiseumWorld();
	public static final String PROFILE_TAB_NAME = "Coliseum World";

	private Fog fog;
	private SkyCycle skyCycle;
	private ChunksManager chunksManager;

	private Entity entitySun;
	private Entity entityMoon;

	private boolean worldGenerated;

	public ColiseumWorld() {
		super(ModuleUpdate.UPDATE_PRE, PROFILE_TAB_NAME, FlounderBounding.class, FlounderTextures.class, FlounderEntities.class, ColiseumParticles.class);
		this.worldGenerated = false;
	}

	@Override
	public void init() {
		this.fog = new Fog(new Colour(), 0.003f, 2.0f, 0.0f, 50.0f);
		this.skyCycle = new SkyCycle();

		this.entityMoon = new InstanceMoon(FlounderEntities.getEntities(), new Vector3f(200.0f, 200.0f, 200.0f), new Vector3f(0.0f, 0.0f, 0.0f));
		this.entitySun = new InstanceSun(FlounderEntities.getEntities(), new Vector3f(-200.0f, -200.0f, -200.0f), new Vector3f(0.0f, 0.0f, 0.0f));

		List<ParticleTemplate> templates = new ArrayList<>();
		templates.add(ColiseumParticles.load("snow"));
		ParticleSystem system = new ParticleSystem(templates, new SpawnCircle(35.0f, new Vector3f(0.0f, 1.0f, 0.0f)), 150, 0.5f, 0.75f);
		system.setSystemCentre(new Vector3f(0.0f, 24.0f, 0.0f));
	}

	@Override
	public void update() {
		if (!worldGenerated) {
			this.chunksManager = new ChunksManager();
			worldGenerated = true;
		}

		skyCycle.update();
		fog.setFogColour(skyCycle.getSkyColour());

		if (chunksManager != null) {
			chunksManager.update();
		}
	}

	@Override
	public void profile() {
	}

	public static Fog getFog() {
		return INSTANCE.fog;
	}

	public static SkyCycle getSkyCycle() {
		return INSTANCE.skyCycle;
	}

	public static Entity getEntitySun() {
		return INSTANCE.entitySun;
	}

	public static Entity getEntityMoon() {
		return INSTANCE.entityMoon;
	}

	@Override
	public IModule getInstance() {
		return INSTANCE;
	}

	@Override
	public void dispose() {
		worldGenerated = false;

		if (chunksManager != null) {
			chunksManager.dispose();
			chunksManager = null;
		}
	}
}
