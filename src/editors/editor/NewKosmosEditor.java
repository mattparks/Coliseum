/*
 * Copyright (C) 2017, Equilibrium Games - All Rights Reserved
 *
 * This source file is part of New Kosmos
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package editors.editor;

import editors.entities.*;
import flounder.devices.*;
import flounder.framework.*;
import flounder.framework.updater.*;
import flounder.resources.*;
import kosmos.*;
import kosmos.camera.*;
import org.lwjgl.glfw.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.*;

/**
 * The editors entrance and selection class.
 */
public class NewKosmosEditor extends TimerTask {
	private JFrame frame;
	private JRadioButton optionEntities;

	private boolean startEntrance;
	private boolean running;

	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.schedule(new NewKosmosEditor(), 0, 1000);
	}

	private NewKosmosEditor() {
		frame = new JFrame("New Kosmos Editor");
		frame.setSize(300, 420);
		frame.setResizable(false);
		running = true;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				frame.setVisible(false);
				running = false;
			}
		});

		frame.setLayout(new FlowLayout());

		optionEntities = new JRadioButton("Entities");
		JButton buttonSubmit = new JButton("Submit");

		buttonSubmit.addActionListener((ActionEvent actionEvent) -> {
			System.out.println("Starting entrance from button!");
			startEntrance = true;
		});

		ButtonGroup group = new ButtonGroup();
		group.add(optionEntities);

		frame.add(optionEntities);
		frame.add(buttonSubmit);
		frame.pack();

		frame.setSize(frame.getWidth() + 64, frame.getHeight() + 8);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setVisible(true);
	}

	@Override
	public void run() {
		if (startEntrance) {
			System.out.println("Starting editor entrance.");
			startEntrance = false;

			if (optionEntities.isSelected()) {
				Framework entrance = new Framework("Kosmos Editors", new UpdaterDefault(GLFW::glfwGetTime), -1, new ExtensionEntities(), new FrameEntities(), new KosmosRenderer(), new KosmosCamera(), new EditorPlayer(), new EditorGuis());
				FlounderDisplay.setup(1080, 720, "Kosmos Editor Entities", new MyFile[]{new MyFile(MyFile.RES_FOLDER, "icon", "icon.png")}, false, true, 0, false, true);
				frame.setVisible(false);
				entrance.run();
			} else {
				System.err.println("No editor selected!");
			}

			frame.setVisible(true);
			frame.toFront();
		}

		if (!running) {
			System.exit(0);
		}
	}
}
