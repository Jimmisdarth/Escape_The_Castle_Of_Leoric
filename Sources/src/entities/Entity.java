package entities;

import java.awt.Graphics2D;
import java.util.LinkedList;

import levels.Direction;
import levels.Room;
import main.GamePanel;
import weapons.DamageTypeAmount;

// Kathe entity ws thesh exei mia syntetagmenh (x,y) pou antistoixei se ena dwmatio tou labyrinthou.
public abstract class Entity {
	
	protected GamePanel gp;
	
	private String name;
	private String description;

	protected int hitPoints;
	
	protected int x, y;

	protected Direction entityDirection;
	
	public Entity(GamePanel gp, String n, String d,  int hp, int x, int y) {
		this.gp = gp;
		
		this.name = n;
		this.description = d;
		
		this.hitPoints = hp;
		
		this.entityDirection = Direction.EAST;
		this.x = x;
		this.y = y;
	}
	
	public String getName() { return name; }
	public String getDescription() { return description; }
	
	public int getHP() { return hitPoints; }
	
	public int getX() { return x; }
	public int getY() { return y;}
	
	public Direction getDirection() { return entityDirection; }
	
	public boolean isAlive() { return hitPoints > 0; }
	public boolean isDead() { return !isAlive(); }
	
	public void teleportTo(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void turnRight() { entityDirection = entityDirection.right(); }
	public void turnLeft()  { entityDirection = entityDirection.left(); }
	
	public void moveForward() {	
		Room currentRoom = gp.getRoom(x, y);
		
		Room newRoom = currentRoom.getRoomAt(entityDirection);
		
		if (newRoom != null && newRoom.getEntity() == null) {
			
			switch(entityDirection) {	// Kanoume ena bhma mprosta.
			case NORTH: y -= 1; break;
			case WEST:  x -= 1; break;
			case SOUTH: y += 1; break;
			case EAST:  x += 1; break;
			}
			
			currentRoom.removeEntity();
			newRoom.setEntity(this);
		}
	}
	
	public abstract void recieveDamage(LinkedList<DamageTypeAmount> dTA);
	
	public abstract void drawInMinimap(Graphics2D g2);
}
