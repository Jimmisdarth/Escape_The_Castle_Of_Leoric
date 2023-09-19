package items;

import java.util.LinkedList;

import entities.Entity;
import main.GamePanel;
import weapons.DamageTypeAmount;

public abstract class Item extends Entity {
	
	boolean equipped;

	public Item(GamePanel gp, String n, String d, int x, int y, boolean e) {
		super(gp, n, d, 0, x, y);
		
		this.equipped = e;
	}
	
	public boolean isEquipped() { return equipped; }
	
	public void setEquipped(boolean e) { this.equipped = e; }

	@Override
	public void recieveDamage(LinkedList<DamageTypeAmount> dTA) {}
}
