package items;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;

// Ta usable einai potions pou mporei na einai eite gia hp eite gia mana.
//
// Epomenws tha exoun mono ena ItemEffect.
public class Usable extends Item {
	
	ItemEffect effect;
	int uses;
	
	public Usable(GamePanel gp, String n, String d, int x, int y, boolean e, int u, ItemEffect effect) {
		super(gp, n, d, x, y, e);
		
		this.effect = effect;
		this.uses = u;
	}
	
	public ItemEffect getEffect() { return effect; }
	
	public int usesLeft() { return uses; }
	
	public ItemEffect use(){
		
		if (uses > 0) {
			--uses;
			return effect;
		}
		else {
			return null;
		}
	}
	
	@Override
	public void drawInMinimap(Graphics2D g2) {
		
		int tileSize = gp.getTileSize();
		
		int x = getX();
		int y = getY();
		
		g2.setColor(Color.red);
		g2.fillRect(x*tileSize + 20, y*tileSize + 20, 6, 6);
	}
}
