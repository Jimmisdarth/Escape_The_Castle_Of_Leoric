package main;

import levels.Room;

public class LevelManager {
	
	GamePanel gp;
	
	int mapLevel = 1;
	final int finalLevel = 2;
	
	public LevelManager(GamePanel gp) {
		this.gp = gp;
	}
	
	public int getMapLevel() { return mapLevel; }
	public int getFinalLevel() { return finalLevel; }
	
	public void setMapLevel(int mapLevel) { this.mapLevel = mapLevel; }
	
	public void generateNewMap() {
		Room map[][] = Room.generateMap(gp.getMaxLabyrinthRow(), gp.getMaxLabyrinthCol());
		
		gp.setMap(map);
		
		gp.getPlayer().teleportTo(0, 0);
		gp.getMiniMap().updateMap(map);
		gp.getFirstPersonView().updateMap(map);
	}
	
	public void generateNewLevel() {	
		++mapLevel;
		
		gp.getEnemyManager().removeEnemies();
		
		generateNewMap();
		gp.getAssetSetter().setupItems();
		
		if (mapLevel == finalLevel) {
			gp.getEnemyManager().addBoss();
		}
		
		gp.getEnemyManager().update();
	}
	
	public void restartGameLevel() {	
		mapLevel = 1;
		
		gp.getEnemyManager().removeEnemies();
		generateNewMap();
	}
}
