/*
 * Copyright (C) 2017, Equilibrium Games - All Rights Reserved
 *
 * This source file is part of New Kosmos
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package kosmos.entities.components;

import flounder.entities.*;
import flounder.entities.components.*;
import flounder.helpers.*;
import flounder.physics.*;
import flounder.physics.bounding.*;

import javax.swing.*;
import java.awt.event.*;

/**
 * Gives an object a collider for spatial interaction. Note that a collider doesn't necessarily need to be used for collision. A collider component can be used for any spatial interaction.
 * <p>
 * For example, a checkpoint can use a ComponentCollider to detect when the player has reached it.
 */
public class ComponentCollider extends IComponentEntity implements IComponentBounding, IComponentEditor {
	public static final int ID = EntityIDAssigner.getId();

	private AABB aabb;
	private QuickHull hull;
	private boolean renderAABB;

	/**
	 * Creates a new ComponentCollider.
	 *
	 * @param entity The entity this component is attached to.
	 */
	public ComponentCollider(Entity entity) {
		super(entity, ID);
		this.aabb = new AABB();
		this.hull = new QuickHull();
		this.renderAABB = true;
	}

	/**
	 * @return Returns a AABB representing the basic collision range.
	 */
	public AABB getAABB() {
		return aabb;
	}

	/**
	 * Gets the flounder.models convex hull.
	 *
	 * @return The flounder.models convex hull.
	 */
	public QuickHull getHull() {
		return hull;
	}

	/**
	 * Gets if the AABB should be rendered.
	 *
	 * @return If the AABB should be rendered.
	 */
	public boolean renderAABB() {
		return renderAABB;
	}

	/**
	 * Sets if the AABB should be rendered.
	 *
	 * @param renderAABB If the AABB should be rendered.
	 */
	public void setRenderAABB(boolean renderAABB) {
		this.renderAABB = renderAABB;
	}

	@Override
	public void update() {
		if (super.getEntity().hasMoved()) { //  || aabb.getMinExtents().length() == 0.0f || aabb.getMaxExtents().length() == 0.0f
			ComponentModel componentModel = (ComponentModel) getEntity().getComponent(ComponentModel.ID);

			if (componentModel != null && componentModel.getModel() != null && componentModel.getModel().isLoaded() && componentModel.getModel().getAABB() != null) {
				AABB.recalculate(componentModel.getModel().getAABB(), super.getEntity().getPosition(), super.getEntity().getRotation(), componentModel.getScale(), aabb);
				//	QuickHull.recalculate(componentModel.getModel().getMeshData().getHull(), hull, super.getEntity().getPosition(), super.getEntity().getRotation(), componentModel.getScale());
			}

			// TODO: Calculate the AABBs and Hulls from animated models.
			ComponentAnimation componentAnimation = (ComponentAnimation) getEntity().getComponent(ComponentAnimation.ID);

			if (componentAnimation != null && componentAnimation.getModel() != null && componentAnimation.getModel().getAABB() != null) {
				AABB.recalculate(componentAnimation.getModel().getAABB(), super.getEntity().getPosition(), super.getEntity().getRotation(), componentAnimation.getScale(), aabb);
				//	QuickHull.recalculate(componentAnimation.getModel().getHull(), hull, super.getEntity().getPosition(), super.getEntity().getRotation(), componentAnimation.getScale());
			}
		}

		if (renderAABB) {
			FlounderBounding.addShapeRender(aabb);
		}
	}

	@Override
	public IBounding getBounding() {
		return aabb;
	}

	@Override
	public void addToPanel(JPanel panel) {
		JCheckBox renderAABB = new JCheckBox("Render AABB");
		renderAABB.setSelected(FlounderBounding.renders());
		renderAABB.addItemListener((ItemEvent e) -> {
			this.renderAABB = renderAABB.isSelected();
		});
		panel.add(renderAABB);
	}

	@Override
	public void editorUpdate() {
	}

	@Override
	public Pair<String[], String[]> getSaveValues(String entityName) {
		return new Pair<>(
				new String[]{}, // Static variables
				new String[]{} // Class constructor
		);
	}

	@Override
	public void dispose() {
	}
}
