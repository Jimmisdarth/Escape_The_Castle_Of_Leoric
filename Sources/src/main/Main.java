package main;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Main {
	
	public static void main(String[] args) {
		
		JFrame window = new JFrame();	// We create our window
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Escape The Castle of King Leroic");
		
		GamePanel gamePanel = new GamePanel();
		
		window.add(gamePanel);
		window.add(gamePanel.getJScrollPane(), BorderLayout.PAGE_END);

		window.pack();	// window will be sized to fit the preferred size of GamePanel
		
		window.setLocationRelativeTo(null);	// The window will be at the center of the screen
		window.setVisible(true);
		
		gamePanel.setupGame();
	}
}
