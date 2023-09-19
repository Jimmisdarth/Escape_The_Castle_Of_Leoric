package items;

import main.GamePanel;

public abstract class Equippable extends Item {
	
	final SlotType slotType;

	public Equippable(GamePanel gp, String n, String d, int x, int y, boolean e, SlotType s) {
		super(gp, n, d, x, y, e);
		
		this.slotType = s;
	}
	
	public SlotType getSlotType() { return slotType; }
}
