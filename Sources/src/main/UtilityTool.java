package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UtilityTool {
	
	public BufferedImage scaleImage(BufferedImage original, int width, int height) {
		
		// We do scaling first so we don't do it in the game loop
		BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
		Graphics2D g2 = scaledImage.createGraphics();
		g2.drawImage(original, 0, 0, width, height, null);
		g2.dispose();
		
		return scaledImage;
	}
}
