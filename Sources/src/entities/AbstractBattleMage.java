package entities;

import java.util.LinkedList;

import items.Armor;
import items.SlotType;
import levels.Room;
import main.GamePanel;
import weapons.DamageType;
import weapons.DamageTypeAmount;

public abstract class AbstractBattleMage extends AbstractPlayer {
	
	int statsPerLevel[][] = {{299, 899, 2699, 6499, 13999, 200000},		// XP
			 				 { 10,  13,   16,   20,    28,     40},		// HP
			 				 { 10,  20,   30,   40,    50,     70},		// MP
			 				 {  5,   7,    9,   11,    13,     15},		// Strength
			 				 {  5,   9,   13,   17,    21,     25}};	// Intelligence

	public AbstractBattleMage(GamePanel gp, String n, String d, int baseHp, int baseMana, int baseStr,
			int baseInt, int x, int y) {
		super(gp, n, d, baseHp, baseMana, baseStr, baseInt, 0, x, y);
		
		updateStats();	// Gia na exei ta swsta stats me to pou jekinhsei to paixnidi.
	}
	
	@Override
	public void castSpell() {
		
		if (manaPoints >= 5) {
			
			manaPoints -= 5;
			Room room = gp.getRoom(x, y).getRoomAt(entityDirection);	// Pairnoume to mprostino dwmatio.
			
			while(room != null && room.getEntity() == null) {	// Oso the xtypame toixo kai de brhskoume exthro.
				room = room.getRoomAt(entityDirection);
			}
			
			if (room != null && room.getEntity() != null) {
				LinkedList<DamageTypeAmount> spellDamage = new LinkedList<>();
				spellDamage.add(new DamageTypeAmount(DamageType.MAGICAL, intelligence));
				
				room.getEntity().recieveDamage(spellDamage);			
			}
		}
		
		//Thelei prosthiki gia ta items kai ta opla pou mporei na exei o paikths.
	}

	@Override
	public int getXPForLevelUp() { return statsPerLevel[0][currentLevel]; }

	@Override
	public void updateStats() {
		
		maxHitPoints    = baseHitPoints    + statsPerLevel[1][currentLevel];
		maxManaPoints   = baseManaPoints   + statsPerLevel[2][currentLevel];
		
		hitPoints    = maxHitPoints;
		manaPoints   = maxManaPoints;
		
		strength     = baseStrength     + statsPerLevel[3][currentLevel];
		intelligence = baseIntelligence + statsPerLevel[4][currentLevel];
		
		// STATS FROM ITEMS
		Armor finger = (Armor) slotMap.get(SlotType.FINGER);
		Armor neck = (Armor) slotMap.get(SlotType.NECK);
		addArmorStats(finger);
		addArmorStats(neck);
	}
}
