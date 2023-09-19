package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import enemies.Enemy;
import entities.Player;
import items.Armor;
import items.Equippable;
import items.Item;
import items.SlotType;
import items.Usable;
import levels.Room;
import weapons.Dice;
import weapons.Weapon;

public class GamePanel extends JPanel implements KeyListener{
	
	// SCREEN SETTINGS
	private final int originalTileSize = 16;	// 16x16 tile
	private final int scale = 2;
	
	private final int tileSize = originalTileSize * scale;	// 32x32 tile
		
	private final int maxLabyrinthCol = 8;
	private final int maxLabyrinthRow = 8;
		
	private final int screenWidth  = 700;	// pixels
	private final int screenHeight = 326;	// pixels
	
	private String pickedItem = "";
	private boolean removedState = false;
	private String removedItemName = "";
	
	private String usedPotion = "";
	
	private boolean gameFinished = false;
	
	// PLAYER'S POSITION
	private int x,y;
	
	// GAME LOG
	JTextArea ta = new JTextArea(8,20);
	JScrollPane sp = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	// SYSTEM
	AssetSetter assetSetter = new AssetSetter(this);
	UI ui = new UI(this, ta, screenWidth, screenHeight);
	EnemyManager enemyManager = new EnemyManager(this);
	LevelManager levelManager = new LevelManager(this);
	
	// MAP
	Room map[][] = Room.generateMap(maxLabyrinthRow, maxLabyrinthCol); 	
	
	// PLAYER
	Player player = new Player(this, 0, 0);
	
	// ENEMIES
	LinkedList<Enemy> enemies = new LinkedList<>();
	
	// MAPS AND VIEWS
	MiniMap miniMap = new MiniMap(this, map);
	FirstPersonView firstPersonView = new FirstPersonView(this, map, player.getX(), player.getY());
	
	public GamePanel() {	
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground((Color.white));
		this.setDoubleBuffered(true);	// Better rendering performance
		
		this.addKeyListener(this);
		this.setFocusable(true); 	// GamePanel is "focused" to receive key input
		
		ta.addKeyListener(this);	// Ean clikaroume sto JTextArea na mporei to paixnidi na synexizei na dexetai
		ta.setEditable(false);		// inputs apo to keyboard kai na mhn ginetai edit wste na mh fainontai ta koumpia pou patontai.
	}
	
	public int getTileSize() { return tileSize; }
	
	public int getScreenWidth()  { return screenWidth; }
	public int getScreenHeight() { return screenHeight; }
	
	public int getMaxLabyrinthRow() { return maxLabyrinthRow; }
	public int getMaxLabyrinthCol() { return maxLabyrinthCol; }
	
	public boolean isGameFinished() { return gameFinished; }
	
	public String  getPickedItem()      { return pickedItem;      }
	public boolean getRemovedState()    { return removedState;    }
	public String  getRemovedItemName() { return removedItemName; }
	
	public String usedPotion()      { return usedPotion; }
	
	public AssetSetter getAssetSetter() { return this.assetSetter; }
	
	public UI getUi() { return ui; }
	
	public EnemyManager getEnemyManager() { return enemyManager; }
	public LevelManager getLevelManager() { return levelManager; }
	
	public Room[][] getMap() { return map; }
	public Room getRoom(int x, int y) { return map[y][x]; }
	
	public Player getPlayer()    { return player; }
	public Room getPlayersRoom() { return getRoom(player.getX(), player.getY()); }
	
	public LinkedList<Enemy> getEnemies() { return enemies; }

	public MiniMap getMiniMap() { return miniMap; }
	public FirstPersonView getFirstPersonView() { return firstPersonView; }
	
	public JScrollPane getJScrollPane() { return sp; }
	
	public void setPickedItem(String s)    { this.pickedItem   = s; }
	public void setRemovedState(boolean s) { this.removedState = s; }
	public void setUsedPotion(String s)    { this.usedPotion   = s; }
	
	public void setMap(Room m[][]) { this.map = m; }
	
	public void setupGame() {
		
		assetSetter.setupPlayer();
		assetSetter.setupItems();
		enemyManager.addEnemies();
		
		ui.printLevelEntry();
	}
	
	public void restartGame() {
		
		player = new Player(this,0,0);
		levelManager.restartGameLevel();
		
		setupGame();
		
		ta.setText("");
		ui.printLevelEntry();
	}
	
	public void update(char userInput) {
		
		if (player.isAlive()) {
			
			boolean freeAction = isFreeAction(userInput);
			
			if (!freeAction) {
				enemyManager.update();

				if (userInput == '\n' && player.reachedExit()) {
					if (levelManager.getMapLevel() == levelManager.getFinalLevel() && enemyManager.isLeoricDead()) {
						gameFinished = true;
					}
					else {
						levelManager.generateNewLevel();
						ta.setText("");
					}

				}
			}
		}
		else {
			if (userInput == '\n') {
				restartGame();
			}
		}
	}
	
	// Built in method
	@Override
	public void paintComponent(Graphics g) {	
			
		super.paintComponent(g);
			
		Graphics2D g2 = (Graphics2D)g;
		
		// MINIMAP
		miniMap.draw(g2);
		
		// FIRST PERSON VIEW
		firstPersonView.draw(g2);
		
		// PLAYER STATS 
		ui.paintPlayerStats(g2);
		
		if (player.isDead()) {
			ui.displayDeathStatus(g2);
		}
		else if(gameFinished) {
			ui.displayGameFinishedStatus(g2);
		}
		
		g2.dispose();	// release system resources that are being used
	}
	
	public boolean isFreeAction(char userInput) {  
		return userInput == 'a' || userInput == 'd' || 
		       userInput == ';' || userInput == '.' || userInput == '/' || userInput == '0' || userInput == '9';
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		char userInput = e.getKeyChar();
		
		if (!gameFinished) {
			if (player.isAlive()) {
				getKeyCodeForAlivePlayer(userInput);
			}
			else {
				getKeyCodeForDeadPlayer(userInput);
			}
		}
	}
	
	private void getKeyCodeForDeadPlayer(char userInput) {
		
		switch(userInput) {
		case 'a' : 
			player.turnLeft();
			break;
		case 'd' :
			player.turnRight();
			break;
		case '\n':
			// RESTART THE GAME
			break;
		}
		
		update(userInput);
		repaint();
	}
	
	private void getKeyCodeForAlivePlayer(char userInput) {
		
		x = player.getX();
		y = player.getY();
		
		switch(userInput) {
		case 'a' : 
			player.turnLeft();
			break;
		case 'd' :
			player.turnRight();
			break;
		case 'w' :
			player.moveForward();
			break;
		case 'x' :
			// REGURAL ATTACK
			player.attack();
			break;
		case 'c' :
			// MAGIC ATTACK
			player.castSpell();
			break;
		case 'r' :
			// REST
			player.rest();
			break;
		case 'h' :
			// USE HEALTH POTION
			usedPotion = player.useHealingPotion();
			break;
		case 'm' :
			// USE MANA POTION
			usedPotion = player.useManaPotion();
			break;
		case 'e' :
			// EQUIP ITEM
			equipItem();
			break;
		case 'f' :
			// DROP THE ITEM IN THE CURRENT SLOT.
			dropItem();
			break;
		case 'p' : 
			// PICKUP AN USABLE IN THE FLOOR
			pickupUsable();
			break;
		case 'l' :
			// DROP A RANDOM USABLE FROM THE PLAYERS INVENTORY ON THE FLOOR.
			dropUsable();
			break;
		case '1' :
			// CHOOSE MAIN HAND.
			player.setCurrentSlot(SlotType.MAIN_HAND);
			break;
		case '2' :
			// CHOOSE OFF HAND.
			player.setCurrentSlot(SlotType.OFF_HAND);
			break;
		case '3' :
			// CHOOSE FINGER.
			player.setCurrentSlot(SlotType.FINGER);
			break;
		case '4' :
			// CHOOSE NECK.
			player.setCurrentSlot(SlotType.NECK);
			break;
		case ';' :
			// Toggle showing enemies in minimap
			miniMap.toggleShowAllEnemies();
			break;
		case '.' :
			// Toogle to show or hide unvisited rooms from minimap.
			miniMap.toggleHideUnvisitedRooms();
			break;
		case '/' :
			// PRINT PLAYERS INVENTORY.
			player.printInventory();
			break;
		case '0' :
			// ADD XP
			player.addXP(100);
			break;
		case '9' :
			// DICE DEBUG
			for(int i = 3; i <= 36; i += 3) {
				Dice dice = new Dice(i);
				System.out.println(dice.toString());
			}
		case '\n' :
			// GENERATE NEW MAP IF PLAYER IS AT DOWN RIGHT CORNER OF THE LABYRINTH.
			break;
		}
		
		update(userInput);
		repaint();
		ui.outputGameStatus(userInput);
	}
	
	private void equipItem() {
		
		if (player.getCurrentSlot() == SlotType.MAIN_HAND || player.getCurrentSlot() == SlotType.OFF_HAND) {
			equipWeapon();
		}
		else if (player.getCurrentSlot() == SlotType.FINGER || player.getCurrentSlot() == SlotType.NECK) {
			equipArmor();
		}
	}
	
	private void equipWeapon() {
		
		Equippable item = getRoom(x,y).topWeapon();
		
		if (item != null && player.canEquip((Equippable) item)) {
			
			pickedItem = item.getName();
			
			Item itemRemoved = player.equip((Equippable) getRoom(x,y).popWeapon());
			if (itemRemoved != null) {
				getRoom(x,y).pushWeapon((Weapon) itemRemoved);
				removedState = true;
				removedItemName = itemRemoved.getName();
			}
		}
	}
	
	private void equipArmor() {
		
		Equippable item = getRoom(x,y).topArmor();
		
		if (item != null && player.canEquip((Equippable) item)) {
			
			pickedItem = item.getName();
			
			Item itemRemoved = player.equip((Equippable) getRoom(x,y).popArmor());
			if (itemRemoved != null) {
				getRoom(x,y).pushArmor((Armor) itemRemoved);
				removedState =  true;
				removedItemName = itemRemoved.getName();
			}
		}
	}
	
	private void dropItem() {
		
		Item itemRemoved = player.remove();
		
		if (itemRemoved != null) {
			
			removedState = true;
			removedItemName = itemRemoved.getName();
			
			if (itemRemoved instanceof Armor) {
				getRoom(x,y).pushArmor((Armor) itemRemoved);
			}
			if (itemRemoved instanceof Weapon) {
				getRoom(x,y).pushWeapon((Weapon) itemRemoved);
			}
		}
	}
	
	private void pickupUsable() {
		
		Item usable = getRoom(x,y).topUsable();
		if (usable != null) {
			pickedItem = usable.getName();
			player.pickUp(getRoom(x,y).popUsable());
		}
	}
	
	private void dropUsable() {
		
		Usable droppedUsable = player.drop();
		if (droppedUsable != null) {
			
			removedState = true;
			removedItemName = droppedUsable.getName();
			
			getRoom(x,y).pushUsable(droppedUsable);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
}
