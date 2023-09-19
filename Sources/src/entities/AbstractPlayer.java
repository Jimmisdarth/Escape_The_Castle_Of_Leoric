package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import enemies.Enemy;
import items.Armor;
import items.EffectType;
import items.Equippable;
import items.ItemEffect;
import items.SlotType;
import items.Usable;
import levels.Room;
import main.GamePanel;
import weapons.DamageType;
import weapons.DamageTypeAmount;
import weapons.Dice;
import weapons.Weapon;

public abstract class AbstractPlayer extends Entity {
	
	// BASE VALUES
	final int baseHitPoints;
	final int baseManaPoints;
	final int baseStrength;
	final int baseIntelligence;
	
	// MAX VALUES
	int maxHitPoints;
	int maxManaPoints;
	
	// CURRENT VALUES
	int manaPoints;
	int strength;
	int intelligence;
	int defense;
	
	// XP
	int experiencePoints = 0;
	
	// Levels go up by 1. To reach level 1 you must have more than 299 experiencePoints.
	// For level 2 you must have more than 899 experiencePoints etc.
	int currentLevel = 0;
	
	// SLOTS
	SlotType currentSlot;
	Map<SlotType,Equippable> slotMap = new HashMap<>();
		
	// INVENTORY
	int numberOfHPPotions = 0;
	int numberOfMPPotions = 0;
	
	int inventoryMaxSize = 10;
	LinkedList<Usable> inventory = new LinkedList<>();
	
	// STRING FOR DAMAGE TAKEN AND DAMAGE DEALT
	String damageDealt[] = new String[6];
	String damageTaken[] = new String[3];

	public AbstractPlayer(GamePanel gp, String n, String d, 
			int baseHp, int baseMana, int baseStr, int baseInt, int defense, int x, int y) {
		super(gp, n, d, baseHp, x, y);
		
		this.baseHitPoints    = baseHp;
		this.baseManaPoints   = baseMana;
		this.baseStrength     = baseStr;
		this.baseIntelligence = baseInt;
		
		this.maxHitPoints    = baseHp;
		this.maxManaPoints   = baseMana;
		
		this.manaPoints   = baseMana;
		this.strength     = baseStr;
		this.intelligence = baseInt;
		this.defense = defense;
		
		setupSlots();
		setupSpawnRoom(x,y);
	}
	
	private void setupSlots() {
		slotMap.put(SlotType.MAIN_HAND, null);
		slotMap.put(SlotType.FINGER, null);
		slotMap.put(SlotType.NECK, null);
		slotMap.put(SlotType.OFF_HAND, null);
	}
	
	private void setupSpawnRoom(int x, int y) {
		gp.getRoom(x,y).setEntity(this);
		gp.getRoom(x,y).setVisited(true);
	}
	
	public int getBaseHP()  { return baseHitPoints; }
	public int getBaseMP()  { return baseManaPoints; }
	public int getBaseStr() { return baseStrength; }
	public int getBaseInt() { return baseIntelligence; }

	public int getMaxHP()  { return maxHitPoints; }
	public int getMaxMP()  { return maxManaPoints; }
	
	public int getMP()           { return manaPoints; }
	public int getStrength()     { return strength; } 
	public int getIntelligence() { return intelligence; }
	public int getDefense() { return defense; }
	
	public int getXP()    { return experiencePoints; }
	public int getLevel() { return currentLevel+1; }
	
	public abstract int getXPForLevelUp();
	
	public SlotType getCurrentSlot() { return currentSlot; }
	public Equippable getEquippable(SlotType s) { return slotMap.get(s); }
	public String getEquippableName(SlotType s) { 
		
		Equippable e = getEquippable(s);
		
		if (e != null) {
			return getEquippable(s).getName();
		}
		else {
			return "Empty";
		}
	}
	
	public int getNumberOfHPPotions() { return numberOfHPPotions; }
	public int getNumberOfMPPotions() { return numberOfMPPotions; }
	
	public int getInventorySize() { return inventory.size(); }
	public void printInventory() { inventory.stream().forEach(effectType -> { System.out.println(effectType.getName()); }); }
	
	public String[] getDamageTaken() { return damageTaken; }
	public String[] getDamageDealt() { return damageDealt; }
	
	public Enemy getEnemyInFront() {
		
		Room currentRoom = gp.getRoom(x, y);
		Room frontRoom = currentRoom.getRoomAt(entityDirection);
		
		if (frontRoom != null) {
			return (Enemy)frontRoom.getEntity();
		}
		
		return null;
	}
	
	public boolean reachedExit() { return gp.getRoom(x, y).isExit(); }
	
	public void setCurrentSlot(SlotType s) { currentSlot = s; }
	
	public void setDamageDealt(String s[]) {
		
		for(int i = 0; i < 3; ++i) {
			damageDealt[i] = s[i];
		}
	}
	
	
	public boolean canPickUpItem() { return inventoryMaxSize != inventory.size(); }
	
	// Shkwnei to antikeimeno pou brisketai sto patwma tou paikth.
	public void pickUp(Usable u) {
		
		if (inventory.size() < inventoryMaxSize) {
			
			// To inventory tha einai organwmeno se dyo merh. Ena meros to mprosta tha exei mono Health Potions
			// kai to to ypoloipo to pisw meros tha exei mono Mana Potions.
			if (u.getEffect().getEffectType() == EffectType.HP_REPLENISH) {		//	   INVENTORY:
				inventory.push(u);												// 1:  HP POTION 
				++numberOfHPPotions;											//		   .
			}																	// 		   .
			else {																//		   .
				inventory.add(u);												// 4:  HP POTION
				++numberOfMPPotions;											// 5:  MP POTION
			}																	//		   .
			u.setEquipped(true);												//	 	   .
		}																		// 10: MP POTION
	}																			
	
	// Petaie ena antikeimeno apo to inventory sth tyxh.
	public Usable drop() {
		
		int i = inventory.size();
		if (i > 0) {
			Dice dice = new Dice(1,i,0);
			
			int outcome = dice.roll();
			Usable usableToBeRemoved = inventory.remove(outcome-1);
			usableToBeRemoved.setEquipped(false);
			
			if (usableToBeRemoved.getEffect().getEffectType() == EffectType.HP_REPLENISH) {
				--numberOfHPPotions;
			}
			else {
				--numberOfMPPotions;
			}
			
			return usableToBeRemoved;
		}
		else {
			return null;
		}
	}
	
	public boolean canEquip(Equippable e) { return e.getSlotType() == currentSlot; }
	
	// Equip and remove previous.
	//
	// Ean pame na baloume kati sto off_hand h ena equippable pou the mpainei sto
	// antostoixo slot tote epistrefoume null kai de kanoume tipota.
	private Equippable equip(SlotType s, Equippable e) {
		
		if (e != null && s == e.getSlotType()) {
			Equippable itemToBeRemoved = remove(s);
				
			slotMap.replace(s, e);
			e.setEquipped(true);
			
			if (e.getSlotType() == SlotType.FINGER || e.getSlotType() == SlotType.NECK) {
				
				Armor armor = (Armor) e;
				addArmorStats(armor);
			}
				
			return itemToBeRemoved;
		}
		else {
			return null;
		}
	}
	
	public Equippable equip(Equippable e) { return equip(currentSlot, e); }
	
	private Equippable remove(SlotType s) {
		
		Equippable itemToBeRemoved = slotMap.replace(s, null);
			
		if (itemToBeRemoved != null) {
			
			itemToBeRemoved.setEquipped(false);
			
			if (itemToBeRemoved.getSlotType() == SlotType.FINGER || itemToBeRemoved.getSlotType() == SlotType.NECK) {
				Armor armor = (Armor) itemToBeRemoved;
				removeArmorEffects(armor);
			}
		}

		return itemToBeRemoved;
	}
	
	public Equippable remove() { return remove(currentSlot); }
	
	public String useHealingPotion() {
		
		if (inventory.size() != 0 && inventory.getFirst().getEffect().getEffectType() == EffectType.HP_REPLENISH) {
			 
			ItemEffect effect = inventory.getFirst().use();
			hitPoints += effect.getAmount();
			checkHP();
			
			Usable usable = inventory.getFirst();
			
			if (inventory.getFirst().usesLeft() == 0) {
				inventory.removeFirst();
				--numberOfHPPotions;
			}
			
			return usable.getName() + " ... " + usable.getDescription() + " Uses left: " + usable.usesLeft();
		}
		else {
			return "No Healing Potion Available";
		}
	}
	
	public String useManaPotion() {
		
		if (inventory.size() != 0 && inventory.getLast().getEffect().getEffectType() == EffectType.MP_REPLENISH) {
			
			ItemEffect effect = inventory.getLast().use();
			manaPoints += effect.getAmount();
			checkMP();
			
			Usable usable = inventory.getLast();
			
			if (inventory.getLast().usesLeft() == 0) {
				inventory.removeLast();
				--numberOfMPPotions;
			}
			
			return usable.getName() + " ... " + usable.getDescription() + " Uses left: " + usable.usesLeft();
		}
		else {
			return "No Mana Potion Available";
		}
	}
	
	protected void addArmorStats(Armor a) {
		if (a != null) {
			ArrayList<ItemEffect> newEffects = a.getEffects();
			addEffects(newEffects);
		}
	}
	
	protected void addEffects(ArrayList<ItemEffect> newEffects) {
		
		for(ItemEffect effect : newEffects) {	// Prosthetoume ta bonus
			
			int bonus = effect.getAmount();
			
			switch(effect.getEffectType()) {
			case INT_BONUS : intelligence  += bonus; break;
			case STR_BONUS : strength      += bonus; break;
			case MP_BONUS  : maxManaPoints += bonus; break;
			case HP_BONUS  : maxHitPoints  += bonus; break;
			case DEFENSE   : defense       += bonus; break;
			default        : break;
			}
		}
	}
	
	protected void removeArmorEffects(Armor a) {
		if (a != null) {
			ArrayList<ItemEffect> newEffects = a.getEffects();
			removeEffects(newEffects);
		}
	}
	
	protected void removeEffects(ArrayList<ItemEffect> effects) {
		
		for(ItemEffect effect : effects) {	// Afairoume ta bonus pou pithanon yphrxan
			
			int bonus = effect.getAmount();
			
			switch(effect.getEffectType()) {
			case INT_BONUS : intelligence  -= bonus; break;
			case STR_BONUS : strength      -= bonus; break;
			case MP_BONUS  : maxManaPoints -= bonus; checkMP(); break;
			case HP_BONUS  : maxHitPoints  -= bonus; checkHP(); break;
			case DEFENSE   : defense       -= bonus; break;
			default        : break;
			}
		}
	}
	
	@Override
	public void teleportTo(int x, int y) {
		super.teleportTo(x, y);
		
		gp.getRoom(x, y).setVisited(true);
	}
	
	@Override
	public void moveForward() {
		super.moveForward();
		
		Room currentRoom = gp.getRoom(x, y);	// Gia na fainontai sto minimap ta levels pou exoume episkefthei.
		currentRoom.setVisited(true);
	}
	
	// Ean yparxei mprosta tou exthros kai exoume oplo se toulaxiston ena xeri kanei epithesh.
	public void attack() {
		
		damageDealt = new String[3];
		Enemy enemy = this.getEnemyInFront();
		
		if (enemy != null) {
			
			LinkedList<DamageTypeAmount>  finalDamageAmount = calculateTotalDamage();
			enemy.recieveDamage(finalDamageAmount);
		}
	}
	
	private LinkedList<DamageTypeAmount> calculateTotalDamage() {
		
		LinkedList<DamageTypeAmount> mainHandDamage = getDamageFromHand(SlotType.MAIN_HAND);
		LinkedList<DamageTypeAmount> offHandDamage = getDamageFromHand(SlotType.OFF_HAND);
		
		if (mainHandDamage != null && offHandDamage != null) {
			
			LinkedList<DamageTypeAmount> finalDamage = DamageTypeAmount.addDamageTypes(mainHandDamage, offHandDamage);
			return finalDamage;
		}
		else if (mainHandDamage != null) {
			return mainHandDamage;
		}
		else if (offHandDamage != null) {
			return offHandDamage;
		}
		else {
			return DamageTypeAmount.zeroDamage();
		}
	}
	
	// Sto MAIN_HAND prostithete kai to stength kai to intelligence san bonus.
	//
	// // Sto OFF_HAND prostithetai mono to intellignce san bonus.
	private LinkedList<DamageTypeAmount> getDamageFromHand(SlotType s) {
		
		Weapon weapon = (Weapon)slotMap.get(s);
		
		if (weapon != null) {
			
			LinkedList<DamageTypeAmount> finalDamageAmount = weapon.getDamage();
			
			// ADD PLAYERS EFFECTS
			for(DamageTypeAmount damage : finalDamageAmount) {
				
				if (damage.getDamageType() == DamageType.SLASHING && s == SlotType.MAIN_HAND) {
					damage.addDamage(strength);
				}
				else if (damage.getDamageType() == DamageType.BLUNT && s == SlotType.MAIN_HAND) {
					damage.addDamage(strength);
				}
				else if (damage.getDamageType() == DamageType.MAGICAL){
					damage.addDamage(intelligence);
				}	
			}
			
			// SORT
			Collections.sort(finalDamageAmount, new Comparator<DamageTypeAmount>() {

				@Override
				public int compare(DamageTypeAmount d1, DamageTypeAmount d2) {
					int result = d1.getDamageType().compareTo(d2.getDamageType());
					return result;
				}		
			});
			
			return finalDamageAmount;
		}
		else {
			return null;
		}
	}
	
	public abstract void castSpell();
	
	public void recieveDamage(LinkedList<DamageTypeAmount> dTA) {
		
		damageTaken = new String[3];	// Wste na mh deixnei to prohgoumeno damage se periptwsh pou den xtyphsoume exthro.
		int index; 
		
		int incomingDamage;
		
		for(DamageTypeAmount damage : dTA) {
			
			incomingDamage = damage.getDamage() - defense;
			if (incomingDamage < 0) {
				incomingDamage = 0;		// Wste na mhn anebei to hp toy paikth se periptwsh pou to damage tou enemy einai mikrotero
			}							// apo to defense toy paikth.
			
			hitPoints -= incomingDamage;
			
			if (damage.getDamageType() == DamageType.SLASHING) {
				index = 0;
			}
			else if (damage.getDamageType() == DamageType.BLUNT) {
				index = 1;
			}
			else {
				index = 2;
			}
			
			damageTaken[index] = "Damage Taken : " + damage.getDamageType() + " " + incomingDamage;
		}
	}
	
	public void rest() {
		hitPoints  += 5;
		manaPoints += 5;
		
		checkHP();
		checkMP();
	}
	
	// Tsekarei an ta hp exoun jeperasei ta max hp kai thetei th swsth timh.
	private void checkHP() {
		if (hitPoints > maxHitPoints) {
			hitPoints = maxHitPoints;
		}
	}
	
	// Tsekarei an ta mp exoun jeperasei ta max mp kai thetei th swsth timh.
	private void checkMP() {
		if (manaPoints > maxManaPoints) {
			manaPoints = maxManaPoints;
		}
	}
	
	// Methods for levels
	public void addXP(int xp) {
		experiencePoints += xp;
		
		updateLevel();
	}
	
	public void removeXP(int xp) {
		experiencePoints -= xp;
		
		if (experiencePoints < 0) {
			experiencePoints = 0;
		}
		
		updateLevel();
	}
	
	// Tsekarei an exoume arketa xp gia na pame sto epomeno level.
	// An yparxoun tote kanoume levelup.
	private void updateLevel() {
		
		int newLevel = currentLevel;
		
		if (experiencePoints >= 14000) {
			newLevel = 5;
		}
		else if (experiencePoints >= 6500 && experiencePoints <= 13999) {
			newLevel = 4;
		}
		else if (experiencePoints >= 2700 && experiencePoints <= 6499) {
			newLevel = 3;
		}
		else if (experiencePoints >= 900 && experiencePoints <= 2699) {
			newLevel = 2;
		}
		else if (experiencePoints >= 300 && experiencePoints <= 899) {
			newLevel = 1;
		}
		else if (experiencePoints >= 0 && experiencePoints <= 299) {
			newLevel = 0;
		}
		else {
			newLevel = 0;
		}
		
		// Ean egine level up.
		if (newLevel != currentLevel) {
			currentLevel = newLevel;
			updateStats();
		}
	}
	
	protected abstract void updateStats();
	
	@Override
	// Analoga me th kaythisnh pou koitaei o paikths xrwmatizoume to orthogwnio toy wste na deixnei
	// th thesh tou kai th kateythinsh tou.
	public void drawInMinimap(Graphics2D g2) {
		
		int tileSize = gp.getTileSize();
		
		g2.setColor(Color.white);
		
		switch (this.getDirection()) {
		case NORTH:
			g2.fillRect(x*tileSize + 12, y*tileSize + 6, 6, 12);
			break;
		case WEST:
			g2.fillRect(x*tileSize + 6, y*tileSize + 12, 12, 6);
			break;
		case SOUTH:
			g2.fillRect(x*tileSize + 12, y*tileSize + 12, 6, 12);
			break;
		case EAST:
			g2.fillRect(x*tileSize + 12, y*tileSize + 12, 12, 6);
			break;
		}
	}
}
