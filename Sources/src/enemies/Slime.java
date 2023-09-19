package enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import main.GamePanel;
import weapons.DamageType;
import weapons.Dice;
import weapons.Weapon;

public class Slime extends Enemy {

	public Slime(GamePanel gp, int x, int y) {
		super(gp, "Slime", "A Big Slime", 20, 4, 30, x, y);	// hp, lockCounter, droppedXP
		
		setupEnemyModifiers(1,0,0);		// Slashing, Blunt, Magical
		setupSound("bubbling sounds");
		
		LinkedList<DamageType> damageType = new LinkedList<>();
		damageType.add(DamageType.BLUNT);
		
		Dice dice = new Dice(1, 6, 2); // N, S, Bonus
		
		this.weapon = new Weapon("Blob of Slimes", "Slimes Weapon 16d+2 B", damageType, null, dice, null);
	}

	@Override
	public void drawInMinimap(Graphics2D g2) {
		
		int tileSize = gp.getTileSize();
		
		g2.setColor(Color.green);
		
		g2.fillRect(x*tileSize + 12, y*tileSize + 12, 8, 8);
	}
}
