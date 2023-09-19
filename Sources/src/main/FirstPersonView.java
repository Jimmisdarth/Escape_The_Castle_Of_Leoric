package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entities.Entity;
import entities.Player;
import levels.Direction;
import levels.MapUpdate;
import levels.Room;

public class FirstPersonView implements MapUpdate{
	
	final int screenWidth = 256;
	final int screenHeight = 256;
	
	GamePanel gp;
	Room map[][];
	
	Graphics2D g2;
	
	int playersX, playersY;
	
	BufferedImage bottomFar, bottomNear, frontFar, frontNear, leftDoorFar, leftDoorNear, leftWallFar, leftWallNear, 
		rightDoorFar, rightDoorNear, rightWallFar, rightWallNear, topFar, topNear, voidFar;
	
	BufferedImage slime, goblin, ghost, skeleton, skeletonDemon, skeletonLord, leoric;
		
	
	public FirstPersonView(GamePanel gp, Room map[][], int x, int y) {
		this.gp = gp;
		this.map = map;
		
		this.playersX = x;
		this.playersY = y;
		
		getSprites();
	}
	
	public void getSprites() {
		
		try {
			
			// Map Spites
			bottomFar  = ImageIO.read(getClass().getResourceAsStream("/sprites/bottom_far128x32.png"));
			bottomNear = ImageIO.read(getClass().getResourceAsStream("/sprites/bottom_near256x64.png"));
			
			frontFar  = ImageIO.read(getClass().getResourceAsStream("/sprites/front_far64x64.png"));
			frontNear = ImageIO.read(getClass().getResourceAsStream("/sprites/front_near256x256.png"));
			
			leftDoorFar  = ImageIO.read(getClass().getResourceAsStream("/sprites/left_door_far32x128.png"));
			leftDoorNear = ImageIO.read(getClass().getResourceAsStream("/sprites/left_door_near64x256.png"));
			
			leftWallFar  = ImageIO.read(getClass().getResourceAsStream("/sprites/left_wall_far32x128.png"));
			leftWallNear = ImageIO.read(getClass().getResourceAsStream("/sprites/left_wall_near64x256.png"));
			
			rightDoorFar  = ImageIO.read(getClass().getResourceAsStream("/sprites/right_door_far32x128.png"));
			rightDoorNear = ImageIO.read(getClass().getResourceAsStream("/sprites/right_door_near64x256.png"));
			
			rightWallFar  = ImageIO.read(getClass().getResourceAsStream("/sprites/right_wall_far32x128.png"));
			rightWallNear = ImageIO.read(getClass().getResourceAsStream("/sprites/right_wall_near64x256.png"));
			
			topFar  = ImageIO.read(getClass().getResourceAsStream("/sprites/top_far128x32.png"));
			topNear = ImageIO.read(getClass().getResourceAsStream("/sprites/top_near256x64.png"));
			
			voidFar = ImageIO.read(getClass().getResourceAsStream("/sprites/void_far64x64.png"));
			
			// Enemy Sprites
			slime    	  = ImageIO.read(getClass().getResourceAsStream("/sprites/slime.png"));
			goblin   	  = ImageIO.read(getClass().getResourceAsStream("/sprites/goblin.png"));
			ghost    	  = ImageIO.read(getClass().getResourceAsStream("/sprites/ghost.png"));
			skeleton 	  = ImageIO.read(getClass().getResourceAsStream("/sprites/skeleton.png"));
			skeletonDemon = ImageIO.read(getClass().getResourceAsStream("/sprites/skeleton-demon.png"));
			skeletonLord  = ImageIO.read(getClass().getResourceAsStream("/sprites/skeleton-lord0.png"));
			leoric        = ImageIO.read(getClass().getResourceAsStream("/sprites/leoric.png"));
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		
		drawMap();
		drawEnemy();
	}
	
	public void drawMap() {
		
		int offset = screenWidth;
		
		int x = gp.getPlayer().getX();
		int y = gp.getPlayer().getY();
		
		Direction playersDirection = gp.getPlayer().getDirection();
		
		Room currentRoom = gp.getRoom(x, y);
		Room frontRoom = currentRoom.getRoomAt(playersDirection);
		
		// Analoga me to poy koitaei o paikths kanoume draw ta antistoixa dwmatia kai oti vlepei.
		g2.fillRect(offset, 0, screenWidth, screenHeight);
		
		// ARISTERA
		if (currentRoom.getRoomAt(playersDirection.left()) == null) {	// Ean yparxei toixos sta aristera
			g2.drawImage(leftWallNear, offset, 0, null);
		}
		else {
			g2.drawImage(leftDoorNear, offset, 0, null);
		}
		
		// DEJIA
		if (currentRoom.getRoomAt(playersDirection.right()) == null) {	// Ean yparxei toixos sta dejia
			g2.drawImage(rightWallNear, offset + 192, 0, null);
		}
		else {
			g2.drawImage(rightDoorNear, offset + 192, 0, null);
		}
		
		// MPOSTA
		if (frontRoom == null) {	// Ean blepei toixo
			g2.drawImage(frontNear, offset + 64, + 64, null);
		}
		else {	// Ean mprosta tou yparxei allo dwmatio
			
			// Ean to mporstino dwmatio exei toixo
			if (frontRoom.getRoomAt(playersDirection) == null) {
				g2.drawImage(frontFar, offset + 96, 96, null);	// toixos
			}
			else {
				g2.drawImage(voidFar, offset + 96, 96, null);	
			}
			
			// DEJIA PLEURA MPROSTA
			if (frontRoom.getRoomAt(playersDirection.right()) == null) {
				g2.drawImage(rightWallFar, offset + 160, 64, null);
			}
			else {
				g2.drawImage(rightDoorFar, offset + 160, 64, null);
			}
			
			// ARISTERA PLEURA MPROSTA
			if (frontRoom.getRoomAt(playersDirection.left()) == null) {
				g2.drawImage(leftWallFar, offset + 64, 64, null);	// toixos
			}
			else {
				g2.drawImage(leftDoorFar, offset + 64, 64, null);
			}
			
			// PATWMA
			g2.drawImage(bottomFar, offset + 64, 160, null);
			
			// TABANI
			g2.drawImage(topFar, offset + 64, 64, null);
		}
		
		// PATWMA
		g2.drawImage(bottomNear, offset, 192, null);
		
		// TABANI
		g2.drawImage(topNear, offset, 0, null);
	}
	
	// Thelei kapoies allages auto edw.
	public void drawEnemy() {
		
		int offset = screenWidth;
		
		Player player = gp.getPlayer();
		
		Entity entityInFront = player.getEnemyInFront();
		
		if (entityInFront != null) {
			
			String entityName = entityInFront.getName();
			
			if (entityName.equals("Slime")) {
				g2.drawImage(slime, offset + 96, 128, null);
			}
			if (entityName.equals("Goblin")) {
				g2.drawImage(goblin, offset + 96, 128, null);
			}
			if (entityName.equals("Ghost")) {
				g2.drawImage(ghost, offset + 96, 128, null);
			}
			if (entityName.equals("Skeleton")) {
				g2.drawImage(skeleton, offset + 96, 128, null);
			}
			if (entityName.equals("Sk.Demon")) {
				g2.drawImage(skeletonDemon, offset + 96, 128, null);
			}
			if (entityName.equals("Sk.Lord")) {
				g2.drawImage(skeletonLord, offset + 96, 128, null);
			}
			if (entityName.equals("Leoric")) {
				g2.drawImage(leoric, offset + 96, 128, null);
			}
		}
	}

	@Override
	public void updateMap(Room[][] map) { this.map = map; }
}
