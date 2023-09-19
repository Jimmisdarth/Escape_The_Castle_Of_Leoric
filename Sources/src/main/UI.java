package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JTextArea;

import enemies.Enemy;
import entities.Player;
import items.Item;
import items.SlotType;

public class UI {
	
	GamePanel gp;
	JTextArea ta;
	
	final int screenWidth;
	final int screenHeigth;
	
	int playerX = 0, playerY = 0;
	int dx, dy;
	
	int playersLevel = 1;
	
	String deadEnemyDescription = null;
	
	public UI(GamePanel gp, JTextArea ta, int sw, int hw) {
		this.gp = gp;
		this.ta = ta;
		this.screenWidth = sw;
		this.screenHeigth = hw;
		
		setupJTextArea();
	}
	
	public void setDeadEnemyDescription(String d) { deadEnemyDescription = d; }
	
	public void setupJTextArea() {
		Font font = new Font("Courier", Font.BOLD, 14);
		ta.setFont(font);
		ta.setForeground(Color.BLACK);
	}
	
	public void printLevelEntry() {
		
		dx = gp.player.getX() - playerX;
		dy = gp.player.getY() - playerY;
		
		playerX = gp.player.getX();
		playerY = gp.player.getY();
		
		printLevel();
		printRoomStats();
	}
	
	public void outputGameStatus(char userInput) {
		
		if (gp.isGameFinished() == false) {
			
			boolean freeAction = gp.isFreeAction(userInput);
			
			if (!freeAction) {
				
				dx = gp.player.getX() - playerX;
				dy = gp.player.getY() - playerY;
				
				playerX = gp.player.getX();
				playerY = gp.player.getY();
				
				if (userInput == '\n') {
					printLevel();
				}
				if (dx != 0 || dy != 0) {
					printRoomStats();
				}
				if (gp.getEnemyManager().getEnemiesSounds().size() != 0) {
					printSounds();
				}
				if (gp.getEnemyManager().enemiesAttacked()) {
					printDamageTaken();
				}
				if (userInput == 'x') {
					printDamageDealt();
				}
				if (userInput == 'c') {
					ta.append("You casted a spell!\n");
				}
				if (userInput == 'r') {
					ta.append("You take a rest ... +5 HP, + 5 MP\n");
				}
				if (gp.getPickedItem() != "") {
					printPickedItem();
				}
				if (gp.usedPotion() != "" && (userInput == 'h' || userInput == 'm')) {
					potionUsed();
				}
				if (deadEnemyDescription != null) {
					printDeadEnemyDescription();
				}
				if (gp.getRemovedState()) {
					printRemovedItem();
				}
				if (userInput == '1' || userInput == '2' || userInput == '3' || userInput == '4') {
					ta.append("You choose slot " + gp.getPlayer().getCurrentSlot().toString() + '\n');
				}
			}
			
			printHpForEnemyInFront();
			printLevelUp();
			ta.setCaretPosition(ta.getDocument().getLength());	// Gia na deixnei panta to teleutaio pragma pou egrapse sto scrollPane.
		}
	}
	
	private void printRoomStats() {
		
		if (gp.getPlayer().reachedExit()){
			ta.append("You reached the exit of the level.\n");
		}
		
		printCoordinates();
		printDroppedItems();
	}
	
	private void printSounds() {
		for(String sound : gp.getEnemyManager().getEnemiesSounds()) {
			ta.append("You hear " + sound + "\n");
		}
	}
	
	private void printLevel() {
		ta.append("You entered level : " + gp.getLevelManager().getMapLevel() + "\n");
	}
	
	private void printCoordinates() {
		ta.append("X: " + playerX + " Y: " + playerY + '\n');
	}
	
	private void printDroppedItems() {
		printDroppedWeapons();
		printDroppedArmors();
		printDroppedUsables();
	}
	
	private void printDroppedWeapons() {
		Item weapon = gp.getRoom(playerX, playerY).topWeapon();
		
		if (weapon != null) {
			ta.append("You see a " + weapon.getName() + ", " + weapon.getDescription() + '\n');
		}
	}
	
	private void printDroppedArmors() {
		Item armor  = gp.getRoom(playerX, playerY).topArmor();
		
		if (armor != null) {
			ta.append("You see a " + armor.getName()+ ", " + armor.getDescription() + '\n');
		}
	}
	
	private void printDroppedUsables() {
		Item usable = gp.getRoom(playerX, playerY).topUsable();
		
		if (usable != null) {
			ta.append("You see a " + usable.getName() + " " + usable.getDescription() + '\n');
		}
	}
	
	private void printDamageTaken() {
		for(String damageTaken : gp.getPlayer().getDamageTaken()) {
			if (damageTaken != null) {
				ta.append(damageTaken + '\n');
			}
		}
	}
	
	private void printDamageDealt() {
		
		for(String damageDealt : gp.getPlayer().getDamageDealt()) {
			if (damageDealt != null) {
				ta.append(damageDealt + '\n');
			}
		}
	}
	
	private void printPickedItem() {
		ta.append("You picked a " + gp.getPickedItem() + '\n');
		gp.setPickedItem("");
	}
	
	private void potionUsed() {
		ta.append(gp.usedPotion() + '\n');
		gp.setUsedPotion("");
	}
	
	private void printDeadEnemyDescription() {
		ta.append("You killed " + deadEnemyDescription + '\n');
		deadEnemyDescription = null;
	}
	
	private void printRemovedItem() {
		ta.append("You removed " + gp.getRemovedItemName());
		gp.setRemovedState(false);
	}
	
	private void printHpForEnemyInFront() {
		
		Enemy enemy = gp.getPlayer().getEnemyInFront();
		if (enemy != null) {
			ta.append(enemy.getName() + " HP: " + enemy.getHP() + '\n');
		}
	}
	
	private void printLevelUp() {
		
		int levelUpCheck = playersLevel - gp.getPlayer().getLevel();
		
		if (levelUpCheck != 0) {
			playersLevel = gp.getPlayer().getLevel();
			ta.append("You leveled up to level " + playersLevel + "!\n");
		}
	}
	
	public void displayGameFinishedStatus(Graphics2D g2) {
		int x = screenWidth/2 - 75;
		int y = screenHeigth/2 - 10;
		
		displayText(g2, "YOU BEATED THE LABYRINTH!!!!", x,y);
	}
	
	public void displayDeathStatus(Graphics2D g2) { 
		
		int x = screenWidth/2 - 10;
		int y = screenHeigth/2 - 50;
		
		displayText(g2, "YOU DIED!!!!", x,y);
		displayText(g2, "PRESS ENTER TO RESTART", x -= 65, y += 74);
		displayText(g2, " THE GAME", x += 60, y += 20);
	}
	
	public void paintPlayerStats(Graphics2D g2) {
		
		int statsStartingPixelX = screenWidth - 188;
		
		int x = statsStartingPixelX + 15;
		int y = 20;
		
		Player player = gp.getPlayer();
		
		displayText(g2, player.getName(), x, y);
		displayText(g2, player.getDescription(), x, y += 20);
		displayText(g2, "XP: " + player.getXP() + "/" + player.getXPForLevelUp(), x, y += 20);
		
		y += 10;
		g2.drawLine(statsStartingPixelX, y, screenWidth, y);
		
		displayText(g2, "HP: "  + player.getHP() + "/" + player.getMaxHP(), x, y += 20);
		displayText(g2, "MP: "  + player.getMP() + "/" + player.getMaxMP(), x, y += 20);
		displayText(g2, "STR: " + player.getStrength(), x, y += 20);
		displayText(g2, "INT: " + player.getIntelligence(), x, y += 20);
		
		y += 10;
		g2.drawLine(statsStartingPixelX, y, screenWidth, y);
		
		displayText(g2, "Healing Potions: " + player.getNumberOfHPPotions(), x, y += 20);
		displayText(g2, "Mana Potions: "    + player.getNumberOfMPPotions(), x, y += 20);
		
		y += 10;
		g2.drawLine(statsStartingPixelX, y, screenWidth, y);
		displayText(g2, "Inventory Items : " + player.getInventorySize(), x, y += 25);
		
		y += 20;
		g2.drawLine(statsStartingPixelX, y, screenWidth, y);
		
		displayText(g2, "Weapon / Armor :", x = 5, y += 20);
		
		displayText(g2, "MAIN_HAND: " + player.getEquippableName(SlotType.MAIN_HAND), x, y += 20);
		displayText(g2, "OFF_HAND: "  + player.getEquippableName(SlotType.OFF_HAND), x = screenWidth/2, y);
		
		displayText(g2, "FINGER: "    + player.getEquippableName(SlotType.FINGER), x = 5, y += 20);
		displayText(g2, "NECK: "      + player.getEquippableName(SlotType.NECK), x = screenWidth/2, y);
	}
	
	public void displayText(Graphics2D g2, String text, int x, int y) {
		g2.setColor(Color.black);
		g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 14));
		
		g2.drawString(text, x, y);
	}
}
