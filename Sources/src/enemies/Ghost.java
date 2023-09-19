package enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import main.GamePanel;
import weapons.DamageType;
import weapons.Dice;
import weapons.Weapon;

public class Ghost extends Enemy{

	public Ghost(GamePanel gp, int x, int y) {
		super(gp, "Ghost", "A Scary Ghost", 40, 2, 100, x, y);	// hp, lockCounter, droppedXP
		
		setupEnemyModifiers(0,1,0);	// Slashing, Blunt, Magical
		setupSound("a brisk cold wind");
		
		LinkedList<DamageType> damageType = new LinkedList<>();
		damageType.add(DamageType.SLASHING);
		damageType.add(DamageType.MAGICAL);
		
		Dice dice = new Dice(1, 6, 2); // N, S, Bonus
		
		// Slash, Blunt, Magic
		this.weapon = 
				new Weapon("Ghost claws", "Ghost's weapon 16d+2 M, 1d6 +2 S", damageType, dice, null, dice);
	}

	@Override
	public void drawInMinimap(Graphics2D g2) {
		
		int tileSize = gp.getTileSize();
		
		g2.setColor(Color.DARK_GRAY);
		
		g2.fillRect(x*tileSize + 12, y*tileSize + 12, 8, 8);
	}
}
