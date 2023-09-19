package main;

import java.awt.Color;
import java.awt.Graphics2D;

import enemies.Enemy;
import items.Armor;
import items.Usable;
import levels.Direction;
import levels.MapUpdate;
import levels.Room;
import weapons.Weapon;

public class MiniMap implements MapUpdate{
	
	GamePanel gp;
	Room map[][];
	
	Graphics2D g2;
	
	// MINIMAP SETTING
	final int MAXSHOWDISTANCE = 2;
	boolean showAllEnemies = false;
	
	boolean hideUnvisitedRooms = true;
	
	public MiniMap(GamePanel gp, Room map[][]) {
		this.gp = gp;
		this.map = map;
	}
	
	public boolean getShowAllEnemies() { return showAllEnemies; }
	public void setShowAllEnemies(boolean sh) { this.showAllEnemies = sh; }
	
	public void toggleShowAllEnemies() {
		showAllEnemies = !showAllEnemies;
	}
	
	public void toggleHideUnvisitedRooms() {
		hideUnvisitedRooms = !hideUnvisitedRooms;
	}
	
	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		
		drawMap();		// Zwgrafizoume prwta to xarth kai meta ta entites
		
		drawPlayer();
		drawEnemies();	// Wste na fainontai auta apo panw tou.
	}
	
	public void drawMap() {
		
		for(int row = 0; row < gp.getMaxLabyrinthRow(); ++row) {
			for(int col = 0; col < gp.getMaxLabyrinthCol(); ++col) {
				
				if (map[row][col].isVisited() || !hideUnvisitedRooms) {
					drawRoom(row, col);
				}
				else {
					drawBlackTile(row, col);
				}
				
				drawItem(row, col);
			}
		}
	}
	
	public void drawBlackTile(int row, int col) {
		
		int screenX = col * gp.getTileSize();
		int screenY = row * gp.getTileSize();
		
		g2.setColor(Color.BLACK);
		g2.fillRect(screenX, screenY, gp.getTileSize(), gp.getTileSize());
	}
	
	public void drawRoom(int row, int col) {
		
		int screenX = col * gp.getTileSize();
		int screenY = row * gp.getTileSize();
		
		g2.setColor(Color.gray);
		g2.fillRect(screenX, screenY, gp.getTileSize(), gp.getTileSize());
			
		if (map[row][col].getRoomAt(Direction.NORTH) == null) {	// Panw Toixos
			g2.setColor(Color.orange);
			g2.fillRect(screenX, screenY, gp.getTileSize(), 2);
		}
		if (map[row][col].getRoomAt(Direction.SOUTH) == null) {	// Katw Toixos
			g2.setColor(Color.orange);
			g2.fillRect(screenX, screenY + gp.getTileSize()-2, gp.getTileSize(), 2);
		}
		if (map[row][col].getRoomAt(Direction.EAST) == null) {	// Dejis Toixos
			g2.setColor(Color.orange);
			g2.fillRect(screenX + gp.getTileSize()-2, screenY, 2, gp.getTileSize());
		}
		if (map[row][col].getRoomAt(Direction.WEST) == null) {	// Aristeros Toixos
			g2.setColor(Color.orange);
			g2.fillRect(screenX, screenY, 2, gp.getTileSize());
		}
	}
	
	public void drawItem(int row, int col) {
		
		drawArmor(row, col);
		drawWeapon(row, col);
		drawUsable(row, col);
	}
	
	private void drawArmor(int row, int col) {
		
		Armor item = map[row][col].topArmor();
		if (item != null) {
			item.drawInMinimap(g2);
		}
	}
	
	private void drawWeapon(int row, int col) {
		
		Weapon item = map[row][col].topWeapon();
		if (item != null) {
			item.drawInMinimap(g2);
		}
	}
	
	private void drawUsable(int row, int col) {
		
		Usable item = map[row][col].topUsable();
		if (item != null) {
			item.drawInMinimap(g2);
		}
	}
	
	public void drawPlayer() { gp.getPlayer().drawInMinimap(g2); }
	
	public void drawEnemies() {
		
		if (showAllEnemies) {
			
			for(Enemy enemy : gp.getEnemies()) {
				if (enemy.isAlive()) {
					enemy.drawInMinimap(g2);
				}
			}
		}
		else {
			
			int playerX = gp.getPlayer().getX();
			int playerY = gp.getPlayer().getY();
			
			for(Enemy enemy : gp.getEnemies()) {
				if (enemy.isAlive()) {
					
					int x = enemy.getX();
					int y = enemy.getY();
					
					int absDX = Math.abs(playerX - x) ;
					int absDY = Math.abs(playerY - y) ;
					
					if (absDX <= MAXSHOWDISTANCE && absDY <= MAXSHOWDISTANCE) {
						enemy.drawInMinimap(g2);
					}
				}
			}
		}
		
	}

	@Override
	public void updateMap(Room[][] map) {
		this.map = map;
	}
}
