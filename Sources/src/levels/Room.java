package levels;

import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Stream;

import entities.Entity;
import items.Armor;
import items.Item;
import items.Usable;
import weapons.Weapon;

//Exei ena dwmatio pou exei anafores se ola ta diplana dwmatia pou einai prosbasima 
//meso kapoias "portas". An einai to teleutaio dwmatio ths pistas tote isExit = true.
public class Room {
	
	//int y, x;
	public int row, col;
	
	Room n, w, e, s;
	Entity entity;
	LinkedList<Item> armorStack = new LinkedList<>();
	LinkedList<Item> weaponStack = new LinkedList<>();
	LinkedList<Item> usableStack = new LinkedList<>();
	
    boolean Exit;
    boolean visited;	// Gia th dhmiourgia tou map kai gia na deixnoume ta room pou exei paei o paikths sto minimap.
    
    // Otan yparxei toixos tote to reference sto diplano dwmatio einai null.
    public Room(int row, int col) {
    	
    	this.row = row;
    	this.col = col;
    	
    	this.n = null;
    	this.w = null;
    	this.e = null;
    	this.s = null;
    	
    	this.visited = false;
    	
    	this.Exit = false;
    }
    
    public int getX() { return col; }
    public int getY() { return row; }
    
    public Room getRoomAt(Direction d) {
    	
    	switch (d) {
			case NORTH:
				return this.n;
			case WEST:
				return this.w;
			case EAST:
				return this.e;
			case SOUTH:
				return this.s;
			default:
				return null;
    	}
    }
    
    public LinkedList<Room> getNeighbors() {
    	
    	LinkedList<Room> neighbors = new LinkedList<>();
    	
    	Stream.of(Direction.values()).forEach(direction -> {
    		
    		Room neighbor = getRoomAt(direction);
    		if (neighbor != null){
    			neighbors.add(neighbor);
    		}
    	});
    	
    	return neighbors;
    }
    
    public Entity getEntity() { return entity; }
    
    private Item popItem(LinkedList<Item> ls) {
    	
    	if (ls.size() > 0) {
    		return ls.pop();
    	}
    	else{
    		return null;
    	}
    }
    
    public Armor  popArmor()  { return (Armor)  popItem(armorStack);  } 
    public Weapon popWeapon() { return (Weapon) popItem(weaponStack); }
    public Usable popUsable() { return (Usable) popItem(usableStack); }
    
    private Item topItem(LinkedList<Item> ls) {
    	if (ls.size() > 0) {
    		return ls.get(0);
    	}
    	else{
    		return null;
    	}
    }
    
    public Armor  topArmor()  { return (Armor)  topItem(armorStack);  }
    public Weapon topWeapon() { return (Weapon) topItem(weaponStack); }
    public Usable topUsable() { return (Usable) topItem(usableStack); }
    
    public boolean hasEntity() { return entity != null; }
    public boolean isExit() { return this.Exit; }
    public boolean isVisited() { return visited; }
    
    public void setEntity(Entity e) { this.entity = e; }
    public void removeEntity() { this.entity = null; }
    
    private void pushItem(Item i, LinkedList<Item> ls) {
    	i.teleportTo(col, row);
    	ls.push(i); 
    }
    public void pushArmor(Armor a)   { pushItem(a, armorStack);  }  
    public void pushWeapon(Weapon w) { pushItem(w, weaponStack); }
    public void pushUsable(Usable u) { pushItem(u, usableStack); }
    
    public void setVisited(boolean v) { visited = v; }
    
    // Dhmiourgei ena pinaka 5X5 pou antistoixei sto map tou paixnidiou.
    //
    // Epistrefei to prwto stoixeio tou pinaka kathos auto einai to dwmatio 
    // ekkinhshs ths pistas.
    public static Room[][] generateMap(int width, int heigth){
    	
    	Room map[][] = initializeMapValues(width, heigth);
    	
    	var currentRoom = map[1][1];
    	
    	LinkedList<Room> stack = new LinkedList<>();
    	
    	currentRoom.visited = true;
    	stack.push(currentRoom);
    	
    	while(!stack.isEmpty()) {
    		currentRoom = stack.pop();
    		
    		if (currentRoom.hasUnvisitedNeighbors(map)) {
    			
    			stack.push(currentRoom);
    			
    			var neigbhor = currentRoom.getRandomNeigbhor(map);
    			
    			currentRoom.connect(neigbhor);
    			neigbhor.visited = true;
    			
    			stack.push(neigbhor);	
    		}
    	}
    	
    	Room finalMap[][] = new Room[width][heigth];
    	
    	Room.cropMap(map, finalMap, width, heigth);
    	
    	finalMap[width-1][heigth-1].Exit = true;
    	
    	return finalMap;
    }
    
    private static Room[][] initializeMapValues(int width, int heigth){
    	
    	Room map[][] = new Room[width+2][heigth+2];
    	
    	for(int row = 0; row < heigth+2; ++row) {
			for(int col = 0; col < width+2; ++col) {
				map[row][col] = new Room(row, col);
				
				if (col == 0) {
					map[row][col].visited = true;
				}
				else if (row == 0) {
					map[row][col].visited = true;
				}
				else if (col == width+1) {
					map[row][col].visited = true;
				}
				else if (row == heigth+1) {
					map[row][col].visited = true;
				}
			}
    	}
    	
    	return map;
    }
    
    private static void cropMap(Room map1[][], Room map2[][], int width, int heigth) {
    	
    	for(int row = 0; row < heigth; ++row) {
			for(int col = 0; col < width; ++col) {
				map2[row][col] = new Room(row, col);
			}
    	}
    	
    	for(int row = 1; row < heigth+1; ++row) {
			for(int col = 1; col < width+1; ++col) {
				map2[row-1][col-1] = map1[row][col];
				
				map2[row-1][col-1].row = row-1;
				map2[row-1][col-1].col = col-1;
				
				map2[row-1][col-1].visited = false;
			}
    	}
    }
 
    private void connect(Room neigbhor) {
    	
    	int dx = this.col - neigbhor.col;
    	int dy = this.row - neigbhor.row;
    	
    	if (dx == 0 && dy == 1) {	// Neigbhor is at NORTH
    		this.n = neigbhor;
    		neigbhor.s = this;
    	}
    	if (dx == 0 && dy == -1) {	// Neigbhor is at SOUTH
    		this.s = neigbhor;
    		neigbhor.n = this;
    	}
    	if (dx == -1 && dy == 0) {	// Neigbhor is at EAST
    		this.e = neigbhor;
    		neigbhor.w = this;
    	}
    	if (dx == 1 && dy == 0) {	// Neigbhor is at WEST
    		this.w = neigbhor;
    		neigbhor.e = this;
    	}
    }
    
    private Room getRandomNeigbhor(Room map[][]) {
    	
    	LinkedList<Room> neigbhors = this.getUnvisitedNeigbhors(map);
    	
    	Random rand = new Random(); 
        int upperbound = neigbhors.size();
        int randomInt = rand.nextInt(upperbound); 
    	
    	return neigbhors.get(randomInt);
    }
    
    // Epistrefei tous unvisited geitones apo to dwmatio pou kalesthke
    public LinkedList<Room> getUnvisitedNeigbhors(Room map[][]){
		
    	LinkedList<Room> unvisitedNeigbhors = new LinkedList<>();
    	
    	if (map[row+1][col].visited == false) {	// NORTH
    		unvisitedNeigbhors.add(map[row+1][col]);
    	}
    	if (map[row-1][col].visited == false) {	// SOUTH
    		unvisitedNeigbhors.add(map[row-1][col]);
    	}
    	if (map[row][col+1].visited == false) {	// EAST
    		unvisitedNeigbhors.add(map[row][col+1]);
    	}
    	if (map[row][col-1].visited == false) {	// WEST
    		unvisitedNeigbhors.add(map[row][col-1]);
    	}
    	
    	return unvisitedNeigbhors;
    }
    
    private boolean hasUnvisitedNeighbors(Room map[][]) {
    	
    	return !map[row+1][col].visited || !map[row-1][col].visited || !map[row][col+1].visited || !map[row][col-1].visited;
    }
    
    public static void printMapStatus(Room map[][], int width, int heigth) {
    	
    	for(int row = 0; row < heigth+2; ++row) {
    		for(int col = 0; col < width+2; ++col) {
    			if(map[row][col].visited == true) {
    				System.out.print("1 ");
    			}
    			else {
    				System.out.print("0 ");
    			}
    			
    		}
    		System.out.println();
    	}
    	
    	for(int row = 0; row < heigth+2; ++row) {
    		for(int col = 0; col < width+2; ++col) {
    			System.out.print(row + "" + col + " ");
    			
    		}
    		System.out.println();
    	}
    }
}
