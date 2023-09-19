package enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import main.GamePanel;
import weapons.DamageType;
import weapons.Dice;
import weapons.Weapon;

public class SkLord extends Enemy{

	public SkLord(GamePanel gp, int x, int y) {
		super(gp, "Sk.Lord", "A Skeleton Lord", 55, 2, 400, x, y);	// hp, lockCounter, droppedXP
		
		setupEnemyModifiers(0.5,0,0);	// Slashing, Blunt, Magical
		setupSound("an evil laugh");
		
		LinkedList<DamageType> damageType = new LinkedList<>();
		damageType.add(DamageType.BLUNT);
		damageType.add(DamageType.SLASHING);
		
		Dice bdice = new Dice(2, 6, 2); // N, S, Bonus
		Dice sdice = new Dice(1, 6, 2); // N, S, Bonus
		
		// Slash, Blunt, Magic
		this.weapon = new Weapon("Flaming Skull", "Sk.Demon's weapon 2d6+2 B, 1d6+1 M", damageType, sdice, bdice, null);
	}

	@Override
	public void drawInMinimap(Graphics2D g2) {
		
		int tileSize = gp.getTileSize();
		
		g2.setColor(Color.MAGENTA);
		
		g2.fillRect(x*tileSize + 12, y*tileSize + 12, 8, 8);
	}
}
