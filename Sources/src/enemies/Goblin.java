package enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import main.GamePanel;
import weapons.DamageType;
import weapons.Dice;
import weapons.Weapon;

public class Goblin extends Enemy{

	public Goblin(GamePanel gp, int x, int y) {
		super(gp, "Goblin", "A Precious Goblin", 30, 1, 60, x, y);	// hp, lockCounter, droppedXP
		
		setupEnemyModifiers(0.5,0,0);	// Slashing, Blunt, Magical
		setupSound("fast footsteps and coins dropping");
		
		LinkedList<DamageType> damageType = new LinkedList<>();
		damageType.add(DamageType.SLASHING);
		
		Dice dice = new Dice(1, 6, 1); // N, S, Bonus
		
		this.weapon = 
				new Weapon("Crude Sword", "Goblin's Weapon 16d+1 S", damageType, dice, null, null);
	}

	@Override
	public void drawInMinimap(Graphics2D g2) {
		
		int tileSize = gp.getTileSize();
		
		g2.setColor(Color.yellow);
		
		g2.fillRect(x*tileSize + 12, y*tileSize + 12, 8, 8);
	}

}
