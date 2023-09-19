package enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import entities.Player;
import main.GamePanel;
import weapons.DamageType;
import weapons.DamageTypeAmount;
import weapons.Dice;
import weapons.Weapon;

public class Leoric extends Enemy{
	
	private final int strength = 10;
	private final int intelligence = 10;

	public Leoric(GamePanel gp, int x, int y) {
		super(gp, "Leoric", "The Ultimate Skeleton King", 120, 2, 30000, x, y);	// hp, lockCounter, droppedXP
		
		setupEnemyModifiers(0.5,0.5,0.5);	// Slashing, Blunt, Magical
		setupSound("the rage of a mighty warrior");
		
		LinkedList<DamageType> damageType = new LinkedList<>();
		damageType.add(DamageType.SLASHING);
		damageType.add(DamageType.BLUNT);
		damageType.add(DamageType.MAGICAL);
		
		Dice sdice = new Dice(2, 6, 5); // N, S, Bonus
		Dice bdice = new Dice(1, 4, 2); // N, S, Bonus
		Dice mdice = new Dice(1, 4, 2); // N, S, Bonus
		
		// Slash, Blunt, Magic
		this.weapon = new Weapon("Mace of the Fallen Champion", "Leoric's weapon 2d6+5 BLUNT, 1d4+2 SLASHING, 1d4+2 MAGICAL", damageType, 
				sdice, bdice, mdice);
	}
	
	@Override
	public void attack(Player player) { 
			
		LinkedList<DamageTypeAmount>  finalDamageAmount = weapon.getDamage();
			
		for(DamageTypeAmount damage : finalDamageAmount) {
				
			if (damage.getDamageType() == DamageType.SLASHING) {
				damage.addDamage(strength);
			}
			else if (damage.getDamageType() == DamageType.BLUNT) {
				damage.addDamage(strength);
			}
			else {
				damage.addDamage(intelligence);
			}
		}
		
		player.recieveDamage(finalDamageAmount); 
	}

	@Override
	public void drawInMinimap(Graphics2D g2) {
		
		int tileSize = gp.getTileSize();
		
		g2.setColor(Color.RED);
		
		g2.fillRect(x*tileSize + 12, y*tileSize + 12, 8, 8);
	}
}
