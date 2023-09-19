package enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import main.GamePanel;
import weapons.DamageType;
import weapons.Dice;
import weapons.Weapon;

public class Skeleton extends Enemy{

	public Skeleton(GamePanel gp, int x, int y) {
		super(gp, "Skeleton", "A Spooky Skeleton", 45, 2, 120, x, y);	// hp, lockCounter, droppedXP
		
		setupEnemyModifiers(0.5,0,0);	// Slashing, Blunt, Magical
		setupSound("bones cracking");
		
		LinkedList<DamageType> damageType = new LinkedList<>();
		damageType.add(DamageType.BLUNT);
		
		Dice dice = new Dice(2, 6, 4); // N, S, Bonus
		
		// Slash, Blunt, Magic
		this.weapon = new Weapon("Femur Bone", "Skeleton's weapon 2d6+4 B", damageType, null, dice, null);
	}

	@Override
	public void drawInMinimap(Graphics2D g2) {
		
		int tileSize = gp.getTileSize();
		
		g2.setColor(Color.LIGHT_GRAY);
		
		g2.fillRect(x*tileSize + 12, y*tileSize + 12, 8, 8);
	}
}
