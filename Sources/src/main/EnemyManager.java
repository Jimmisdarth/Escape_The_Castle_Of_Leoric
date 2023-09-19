package main;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import enemies.Enemy;
import enemies.Ghost;
import enemies.Goblin;
import enemies.Leoric;
import enemies.SkDemon;
import enemies.SkLord;
import enemies.Skeleton;
import enemies.Slime;
import items.ItemGenerator;
import items.Rarity;
import items.SlotType;
import levels.Direction;
import levels.Room;
import weapons.DamageType;
import weapons.Dice;
import weapons.Weapon;

public class EnemyManager {
	
	GamePanel gp;
	ItemGenerator itemGen;
	
	// MAP SETTINGS
	private final int MAX_NUMBER_OF_ENEMIES = 3;
	private final int MAXS_HEARING_DISTANCE = 2;
	
	// NEARBY ENEMY SOUNDS
	LinkedList<String> sounds;
	
	// LEORIC STATUS
	boolean leoricSpawned = false;
	boolean leoricIsDead = true;
	
	// GAMESTATUS SETTINGS
	private boolean enemiesAttacked = false;
	
	public EnemyManager(GamePanel gp) {
		this.gp = gp;
		this.itemGen = new ItemGenerator(gp);
	}
	
	public LinkedList<String> getEnemiesSounds() { return sounds; }
	
	public boolean isLeoricDead()    { return leoricIsDead; }
	public boolean enemiesAttacked() { return enemiesAttacked; }
	
	public int generateRandomX() {
		
		Random rand = new Random();
		int upperBoundX = gp.getMaxLabyrinthCol();
		
		return rand.nextInt(upperBoundX);
	}
	
	public int generateRandomY() {
		
		Random rand = new Random();
		int upperBoundY = gp.getMaxLabyrinthRow();
		
		return rand.nextInt(upperBoundY);
	}
	
	public void moveEnemies() {
		
		int playerX = gp.getPlayer().getX();
		int playerY = gp.getPlayer().getY();
		
		sounds = new LinkedList<>();
		
		for(Enemy enemy : gp.getEnemies()) {
			enemy.move();
			if (enemy.hasMoved()) {
				
				int absDX = Math.abs(playerX - enemy.getX()) ;
				int absDY = Math.abs(playerY - enemy.getY()) ;
				
				if (absDX <= MAXS_HEARING_DISTANCE && absDY <= MAXS_HEARING_DISTANCE) {
					sounds.add(enemy.makeSound());
				}
			}
		}
	}
	
	private Enemy spawnLevelOneEnemy(int x, int y)   { return new Slime(gp, x, y);  }
	private Enemy spawnLevelTwoEnemy(int x, int y)   { return new Goblin(gp, x, y); }
	
	private Enemy spawnLevelThreeEnemy(int x, int y) { 
		
		Dice dice = new Dice(1, 100, 0); // N, S, bonus
		
		int outcome = dice.roll();
		
		if (outcome <= 50) {
			return new Goblin(gp, x, y);
		}
		else {
			return new Ghost(gp, x, y);
		}
	}
	
	private Enemy spawnLevelFourEnemy(int x, int y)  { 
		
		Dice dice = new Dice(1, 120, 0); // N, S, bonus
		
		int outcome = dice.roll();
		
		if (outcome <= 40) {
			return new Goblin(gp, x, y);
		}
		else if (outcome <= 80){
			return new Ghost(gp, x, y);
		}
		else {
			return new Skeleton(gp, x, y);
		}
	}
	private Enemy spawnLevelFiveEnemy(int x, int y)  { 
		
		Dice dice = new Dice(1, 120, 0); // N, S, bonus
		
		int outcome = dice.roll();
		
		if (outcome <= 40) {
			return new Ghost(gp, x, y);
		}
		else if (outcome <= 80){
			return new Skeleton(gp, x, y);
		}
		else {
			return new SkDemon(gp, x, y);
		}
	}
	
	private Enemy spawnLevelSixEnemy(int x, int y) {
		
		Dice dice = new Dice(1, 120, 0); // N, S, bonus
		
		int outcome = dice.roll();
		
		if (outcome <= 40) {
			return new Skeleton(gp, x, y);
		}
		else if (outcome <= 80){
			return new SkDemon(gp, x, y);
		}
		else {
			return new SkLord(gp, x, y);
		}
	}
	
	// Dhmiourgei enan exthro kai ton prosthetei sti lista me ta enemies.
	//
	// Brhskei mia kenh thesh sto labyrintho kai bazei ton exthro ekei.
	//
	// Analoga me to epipedo toy paikth spawnroun kai oi antisstoixoi exthroi.
	private Enemy addEnemy() {
		int x = generateRandomX();
		int y = generateRandomY();
		
		int playersCurrentLevel = gp.getPlayer().getLevel();
		
		Enemy newEnemy;
			
		if (playersCurrentLevel == 1) {
			newEnemy = spawnLevelOneEnemy(x,y);
		}
		else if (playersCurrentLevel == 2){
			newEnemy = spawnLevelTwoEnemy(x,y);
		}
		else if (playersCurrentLevel == 3){
			newEnemy = spawnLevelThreeEnemy(x,y);
		}
		else if (playersCurrentLevel == 4){
			newEnemy = spawnLevelFourEnemy(x,y);
		}
		else if (playersCurrentLevel == 5){
			newEnemy = spawnLevelFiveEnemy(x,y);
		}
		else{
			newEnemy = spawnLevelSixEnemy(x,y);
		}
		
		gp.getEnemies().add(newEnemy);
		
		return newEnemy;
	}
	
	public Enemy addBoss() {
		
		int x = generateRandomX();
		int y = generateRandomY();
		
		Enemy leoric = new Leoric(gp, x, y);
		
		gp.getEnemies().add(leoric);
		leoricSpawned = true;
		leoricIsDead = false;
		
		return leoric;
	}
	
	// Analoga me to posoi exthroi exoun pethanei prosthetei exthrous mexri to MAX_NUMBER_OF_ENEMIES.
	//
	// An exei spawnarei o leoric then prostitithetai extrhos mexri na pethanei.
	public void addEnemies() {
		
		if (leoricIsDead) {
			
			int numberOfAliveEnemies = gp.getEnemies().size();
			
			for(int i = numberOfAliveEnemies; i < MAX_NUMBER_OF_ENEMIES; ++i) {
				addEnemy();
			}
		}
	}
	
	// Kanei remove olous tous exthrous
	public void removeEnemies() {
		
		Iterator<Enemy> enemyIterator = gp.getEnemies().iterator();

		while (enemyIterator.hasNext()){
			Enemy enemy = enemyIterator.next();
			
			gp.getRoom(enemy.getX(), enemy.getY()).removeEntity();

			enemyIterator.remove();    // You can do the modification here.
		 }
	}
	
	private void removeDeadEnemies() {
		
		Iterator<Enemy> enemyIterator = gp.getEnemies().iterator();

		while (enemyIterator.hasNext()){
			Enemy enemy = enemyIterator.next();

			if (enemy.isDead()){
				
				gp.getUi().setDeadEnemyDescription(enemy.getDescription());
				
				dropItem(enemy);

				int droppedXp = enemy.getDroppedXp();
				gp.getPlayer().addXP(droppedXp);
				
				gp.getRoom(enemy.getX(), enemy.getY()).removeEntity();
				enemyIterator.remove();    // You can do the modification here.
		    }
		 }
	}
	
	// H dropItem kaleitai otan pethainei enas exthros gi auto kai xeirizetai ton thanato tou leoric
	// wste na mhn elgxoume kai sthn removeDeadEnemies kai sthn dropItem dyo fores gia to onoma tou leoric
	// wste na paei na allajei to leoricIsDead se true, gia na spawnroun kanonika oi exthroi.
	private void dropItem(Enemy enemy) {
		
		int x = enemy.getX();
		int y = enemy.getY();
		
		String enemyName = enemy.getName();
		
		if (enemyName.equals("Goblin")) {
			itemGen.newRandomEquippable(Rarity.FEEBLE, x, y, true);
		}
		else if (enemyName.equals("Ghost")) {
			itemGen.newRandomEquippable(Rarity.COMMON, x, y, true);
		}
		else if (enemyName.equals("Skeleton")) {
			itemGen.newRandomEquippable(Rarity.RARE, x, y, true);
		}
		else if (enemyName.equals("Sk.Demon")) {
			itemGen.newRandomEquippable(Rarity.RARE, x, y, true);
		}
		else if (enemyName.equals("Sk.Lord")) {
			itemGen.newRandomEquippable(Rarity.SUPREME, x, y, true);
		}
		else if (enemyName.equals("Leoric")) {
			leoricIsDead = true;
			dropLeoricWeapon(x, y);
		}
	}
	
	private void dropLeoricWeapon(int x, int y) {
		
		LinkedList<DamageType> damageTypes = new LinkedList<>();
		damageTypes.add(DamageType.SLASHING);
		damageTypes.add(DamageType.BLUNT);
		damageTypes.add(DamageType.MAGICAL);
		
		Dice sDice = new Dice(1,4,2);
		Dice bDice = new Dice(2,6,5);
		Dice mDice = new Dice(1,4,2); // N, S, Bonus
		
		Weapon LeoricWeapon = 
				new Weapon(gp, "Mace of the Fallen Champion", "2d6+5 B, 1d4+2 S, 1d4+2 M", SlotType.MAIN_HAND, 
						damageTypes, sDice, bDice, mDice, false, 0, 0);	// Slash, Blunt, Magic
		
		gp.getRoom(x,y).pushWeapon(LeoricWeapon);
	}
	
	private void attackPlayer() {
		
		enemiesAttacked = false;
		
		int x = gp.getPlayer().getX();
		int y = gp.getPlayer().getY();
		
		Room currentRoom = gp.getRoom(x,y);
		
		attackFromRoom(currentRoom.getRoomAt(Direction.NORTH));
		attackFromRoom(currentRoom.getRoomAt(Direction.WEST));
		attackFromRoom(currentRoom.getRoomAt(Direction.SOUTH));
		attackFromRoom(currentRoom.getRoomAt(Direction.EAST));
	}
	
	private void attackFromRoom(Room r) {
		
		if (r != null && r.getEntity() != null) {
			
			enemiesAttacked = true;
			
			Enemy enemy = (Enemy)r.getEntity();
			enemy.attack(gp.getPlayer());
		}
	}
	
	public void update() {
		removeDeadEnemies();	// Afairoime tous exthours pou pethanan
		moveEnemies();
		attackPlayer();
		addEnemies();			// Prosthetoume wste o synolikos arithmos twn entities na einai 3.
	}
}
