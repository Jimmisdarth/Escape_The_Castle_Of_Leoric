package enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import main.GamePanel;
import weapons.DamageType;
import weapons.Dice;
import weapons.Weapon;

public class SkDemon extends Enemy{

	public SkDemon(GamePanel gp, int x, int y) {
		super(gp, "Sk.Demon", "A Flamming Skeleton DEMON", 50, 2, 200, x, y);	// hp, lockCounter, droppedXP
		
		setupEnemyModifiers(0.5,0,0);	// Slashing, Blunt, Magical
		setupSound("flames crackling");
		
		LinkedList<DamageType> damageType = new LinkedList<>();
		damageType.add(DamageType.BLUNT);
		damageType.add(DamageType.MAGICAL);
		
		Dice bdice = new Dice(2, 6, 2); // N, S, Bonus
		Dice mdice = new Dice(1, 6, 1); // N, S, Bonus
		
		// Slash, Blunt, Magic
		this.weapon = new Weapon("Flaming Skull", "Sk.Demon's weapon 2d6+2 B, 1d6+1 M", damageType, null, bdice, mdice);
	}

	@Override
	public void drawInMinimap(Graphics2D g2) {
		
		int tileSize = gp.getTileSize();
		
		g2.setColor(Color.orange);
		
		g2.fillRect(x*tileSize + 12, y*tileSize + 12, 8, 8);
	}
}
