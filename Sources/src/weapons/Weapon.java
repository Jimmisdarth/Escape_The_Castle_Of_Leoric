package weapons;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import items.Equippable;
import items.SlotType;
import main.GamePanel;

public class Weapon extends Equippable{
	
	LinkedList<DamageType> damageTypesList = new LinkedList<>();
	
	Dice slashingDice, bluntDice, magicDice;
	
	// Gia to paikth.
	public Weapon(GamePanel gp, String n, String d, SlotType s, LinkedList<DamageType> damageTypes, 
			Dice sDice, Dice bDice, Dice mDice, boolean e, int x, int y) {
		super(gp, n, d, x, y, e, s);
		
		addDamage(damageTypes, sDice, bDice, mDice);
	}
	
	// Gia tous enemies.
	public Weapon(String n, String d, LinkedList<DamageType> damageTypes, 
			Dice sDice, Dice bDice, Dice mDice) {
		super(null, n, d, 0, 0, true, SlotType.OFF_HAND);
		
		addDamage(damageTypes, sDice, bDice, mDice);
	}
	
	public int getSlashingBonus() { return slashingDice.getBonus(); }
	public int getBluntBonus() { return bluntDice.getBonus(); }
	public int getMagicBonus() { return magicDice.getBonus(); }
	
	public LinkedList<DamageTypeAmount> getDamage() {
		
		LinkedList<DamageTypeAmount> damageTypeAmounts = new LinkedList<>();
		
		for(DamageType damageType : damageTypesList) {
			
			int damage;
			
			if (damageType == DamageType.BLUNT) {
				damage = bluntDice.roll();
				damageTypeAmounts.add(new DamageTypeAmount(DamageType.BLUNT, damage));
			}
			else if (damageType == DamageType.MAGICAL) {
				damage = magicDice.roll();
				damageTypeAmounts.add(new DamageTypeAmount(DamageType.MAGICAL, damage));
			}
			else {
				damage = slashingDice.roll();
				damageTypeAmounts.add(new DamageTypeAmount(DamageType.SLASHING, damage));
			}
		}
		
		return damageTypeAmounts;
	}
	
	private void addDamage(LinkedList<DamageType> damageTypes, Dice sDice, Dice bDice, Dice mDice) {
		
		for(DamageType dType : damageTypes) {
			
			this.damageTypesList.add(dType);
			
			if (dType == DamageType.SLASHING) {
				this.slashingDice = sDice;
			}
			if (dType == DamageType.BLUNT) {
				this.bluntDice = bDice;
			}
			if (dType == DamageType.MAGICAL) {
				this.magicDice = mDice;
			}
		}
	}

	@Override
	public void drawInMinimap(Graphics2D g2) {
		
		int tileSize = gp.getTileSize();
		
		int x = getX();
		int y = getY();
		
		g2.setColor(Color.ORANGE);
		g2.fillRect(x*tileSize + 6, y*tileSize + 19, 6, 6);
	}
}
