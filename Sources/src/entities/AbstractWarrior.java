package entities;

import items.Armor;
import items.SlotType;
import main.GamePanel;

public abstract class AbstractWarrior extends AbstractPlayer {
	
	int statsPerLevel[][] = {{299, 899, 2699, 6499, 13999, 200000},		// XP
							 { 10,  15,   20,   25,    30,     40},		// HP
							 {  0,   0,    0,    0,     0,      0},		// MP
							 {  2,   4,    6,    8,    10,     12},		// Strength
							 {  0,   0,    0,    0,     0,      0}};	// Intelligence

	public AbstractWarrior(GamePanel gp, String n, String d, int baseHp, int baseMana, int baseStr,
			int baseInt, int x, int y) {
		super(gp, n, d, baseHp, baseMana, baseStr, baseInt, 0, x, y);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	// O Warrior de mporei na kanei spell.
	public void castSpell() {
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
