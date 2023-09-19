package items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import main.GamePanel;

public class Armor extends Equippable{
	
	ArrayList<ItemEffect> effects = new ArrayList<>();
	
	public Armor(GamePanel gp, String n, String d, int x, int y, boolean e, SlotType s, ArrayList<ItemEffect> effects) {
		super(gp, n, d, x, y, e, s);
		
		addEffects(effects);
	}

	public ArrayList<ItemEffect> getEffects() { return effects; }
	
	private void addEffects(ArrayList<ItemEffect> newEffects) {
		
		newEffects.stream().forEach(itemEffect -> {
			
			this.effects.add(itemEffect);
		});
	}
	
	@Override
	public void drawInMinimap(Graphics2D g2) {
		
		if (!isEquipped()) {
			
			int tileSize = gp.getTileSize();
			
			int x = getX();
			int y = getY();
			
			g2.setColor(Color.CYAN);
			g2.fillRect(x*tileSize + 18, y*tileSize + 4, 6, 6);
		}
	}
}
