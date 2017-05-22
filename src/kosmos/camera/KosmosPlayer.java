/*
 * Copyright (C) 2017, Equilibrium Games - All Rights Reserved
 *
 * This source file is part of New Kosmos
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package kosmos.camera;

import flounder.camera.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.networking.*;
import kosmos.chunks.*;
import kosmos.network.packets.*;
import kosmos.world.*;

public class KosmosPlayer extends Player {
	public static final float PLAYER_OFFSET_Y = (float) (Math.sqrt(2.0) * 0.25);

	public static final float SPEED = 6.0f;
	public static final float BOOST_MUL = 2.0f;
	public static final float JUMP_POWER = 5.0f;
	public static final float FLY_SPEED = 8.0f;

	private Vector3f position;
	private Vector3f rotation;

	private boolean noclipEnabled;

	private Timer timer;
	private static boolean needSendData;

	public KosmosPlayer() {
		super();
	}

	@Override
	public void init() {
		this.position = new Vector3f();
		this.rotation = new Vector3f();

		this.noclipEnabled = false;

		this.timer = new Timer(1.0 / 14.3); // 14.3 ticks per second.
		KosmosPlayer.needSendData = true;
	}

	@Override
	public void update() {
		if (KosmosWorld.get().getEntityPlayer() == null) {
			return;
		}

		// Gets the current position and deltas.
		Vector3f newPosition = KosmosWorld.get().getEntityPlayer().getPosition();
		Vector3f newRotation = KosmosWorld.get().getEntityPlayer().getRotation();
		float dx = newPosition.x - position.x;
		float dy = newPosition.y - position.y;
		float dz = newPosition.z - position.z;
		float ry = newRotation.y - rotation.y;

		// Try to send data to the server if needed.
		if (dx != 0.0f || dy != 0.0f || dz != 0.0f || ry != 0.0f) {
			if (!needSendData) {
				needSendData = true;
				timer.resetStartTime();
			}
		}

		// Stop spam requests by using a tick rate.
		if (needSendData && timer.isPassedTime()) {
			sendData();
		}

		// Sets the current player position to the current entity.
		this.position.set(newPosition);
		this.rotation.set(newRotation);
	}

	/**
	 * Sends this players data to the server.
	 */
	private void sendData() {
		if (FlounderNetwork.get().getUsername() != null && FlounderNetwork.get().getSocketClient() != null && KosmosChunks.get().getCurrent() != null) {
			new PacketMove(FlounderNetwork.get().getUsername(), position, rotation, KosmosChunks.get().getCurrent().getPosition().x, KosmosChunks.get().getCurrent().getPosition().z).writeData(FlounderNetwork.get().getSocketClient());
			needSendData = false;
			timer.resetStartTime();
		}
	}

	public boolean isNoclipEnabled() {
		return noclipEnabled;
	}

	public void setNoclipEnabled(boolean noclipEnabled) {
		this.noclipEnabled = noclipEnabled;
	}

	public static void askSendData() {
		KosmosPlayer.needSendData = true;
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public Vector3f getRotation() {
		return rotation;
	}

	@Override
	public boolean isActive() {
		return true;
	}
}
