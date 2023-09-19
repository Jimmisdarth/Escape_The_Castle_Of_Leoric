package enemies;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import entities.Entity;
import entities.Player;
import levels.Direction;
import levels.Room;
import main.GamePanel;
import weapons.DamageType;
import weapons.DamageTypeAmount;
import weapons.Weapon;

public abstract class Enemy extends Entity {
	
	Weapon weapon;
	
	int actionLockCounter = 0;
	int lockCount;
	int droppedXp;
	
	private LinkedList<Room> pathToPlayer = new LinkedList<>();
	
	String sound = "";
	boolean hasMoved = false;
	
	// Gia 1 exoume immunity gia 0 den yparxei kamia allagh.
	// Gia endimese times exoume final Damage = Damage(1 - damageModifier).
	double damageModifierSlashing, damageModifierBlunt, damageModifierMagical;

	public Enemy(GamePanel gp, String n, String d, int hp, int lc, int dpXP, int x, int y) {
		super(gp, n, d, hp, x, y);
		// TODO Auto-generated constructor stub
		
		this.lockCount = lc;
		this.droppedXp = dpXP;
		
		while(this.isColliding()) {
			
			x = gp.getEnemyManager().generateRandomX();
			y = gp.getEnemyManager().generateRandomY();
			
			this.teleportTo(x, y);
		}
		
		gp.getRoom(x, y).setEntity(this);
	}
	
	public Weapon getWeapon() { return weapon; }
	public int getDroppedXp() { return droppedXp; }
	
	public String makeSound() { return sound; }
	public boolean hasMoved() { return hasMoved; }
	
	public void setWeapon(Weapon w) { this.weapon = w;	}
	
	private boolean isColliding() { 
		
		Room newRoom = gp.getRoom(x, y);
		
		if (newRoom.hasEntity()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	protected void setupEnemyModifiers(double s, double b, double m) {
		this.damageModifierSlashing = s;
		this.damageModifierBlunt = b;
		this.damageModifierMagical = m;
	}
	
	protected void setupSound(String s) { this.sound = s; }
	
	public void attack(Player player) { player.recieveDamage(getWeapon().getDamage()); }
	
	@Override
	public void recieveDamage(LinkedList<DamageTypeAmount> dTA) {
		
		String damageDealt[] = new String[3];
		
		if (dTA != null) {
			
			dTA.stream().forEach(damage -> {
				
				int index;
				
				if (damage.getDamageType() == DamageType.SLASHING) {
					index = 0;
					hitPoints -= damage.removeDamage( (int)(damage.getDamage() * damageModifierSlashing));
				}
				else if (damage.getDamageType() == DamageType.BLUNT) {
					index = 1;
					hitPoints -= damage.removeDamage( (int)(damage.getDamage() * damageModifierBlunt));
				}
				else {
					index = 2;
					hitPoints -= damage.removeDamage( (int)(damage.getDamage() * damageModifierMagical));
				}
				
				damageDealt[index] = "Damage Dealt : " + damage.getDamageType() + " " + damage.getDamage();
			});
		}
		
		gp.getPlayer().setDamageDealt(damageDealt);
	}
	
	// BFS
	public void move() {
		
		hasMoved = false;
		
		++actionLockCounter;
		if (actionLockCounter == lockCount) {	// Metakinoume to entity actionLockCounter xronous paixndidiou.
			
			Room enemyRoom = gp.getRoom(x,y);
			Room playersRoom = gp.getPlayersRoom();
			pathToPlayer = tracePathFrom(enemyRoom, playersRoom);
			
			entityDirection = getDirectionFrom(enemyRoom, pathToPlayer.get(1));
			moveForward();
			
			hasMoved = true;
			actionLockCounter = 0;
		}
	}
	
	// Breadth first search from start to end
	private LinkedList<Room> tracePathFrom(Room start, Room end) {
		
		// Inner class to hold the path
		class RoomPath {
			public final RoomPath parent;
			public final Room current;
			
			public RoomPath(Room current, RoomPath parent) {
				this.current = current;
				this.parent  = parent;
			}
		}
		
		LinkedList<RoomPath> scanRooms = new LinkedList<>();
		Set<Room> visited = new HashSet<>();
		RoomPath currentRoom = new RoomPath(start, null);
		
		scanRooms.addFirst(currentRoom);
		visited.add(start);
		
		while(currentRoom.current != end) {
			currentRoom = scanRooms.removeLast();
			for(Room n: currentRoom.current.getNeighbors()) {
				if(! visited.contains(n)) {
					visited.add(n);
					scanRooms.addFirst(new RoomPath(n, currentRoom));
				}
			}
		}
		
		LinkedList<Room> path = new LinkedList<>();
		
		while(currentRoom != null) {
			path.addFirst(currentRoom.current);
			currentRoom = currentRoom.parent;
		}
		
		return path;
	}
	
	// Kineitai se mia tyxaia kateythinsi
	public void RandomStep() {
		
		++actionLockCounter;
		
		if (actionLockCounter == lockCount) {	// Metakinoume to entity actionLockCounter xronous paixndidiou.
			
			entityDirection = getRandomDirection();
			moveForward();
			
			actionLockCounter = 0;
		}
	}
	
	private Direction getRandomDirection() {
		
		Random random = new Random();
		int i = random.nextInt(100)+1;	// Random number from 1 to 100
		
		if (i <= 25) {
			return Direction.NORTH;
		}
		else if (i > 25 && i <= 50) {
			return Direction.WEST;
		}
		else if (i > 50 && i <= 75) {
			return Direction.SOUTH;
		}
		else{
			return Direction.EAST;
		}
	}
	
	private Direction getDirectionFrom(Room start, Room end) {
		
		int x = start.getX();
		int y = start.getY();
		
		int dx = end.getX() - x;
		int dy = end.getY() - y;
		
    	if (dx == 0 && dy == -1) {	// Adjacent is at NORTH
    		return Direction.NORTH;
    	}
    	else if (dx == 0 && dy == +1) {	// Adjacent is at SOUTH
    		return Direction.SOUTH;
    	}
    	else if (dx == +1 && dy == 0) {	// Adjacent is at EAST
    		return Direction.EAST;
    	}
    	else if (dx == -1 && dy == 0) {	// Adjacent is at WEST
    		return Direction.WEST;
    	}
    	else {
        	return getRandomDirection();
    	}

	}
}
