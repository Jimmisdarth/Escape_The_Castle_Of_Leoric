package entities;

import main.GamePanel;
import races.Tauren;

public class Player extends AbstractBattleMage {
	
	static int hpBonus  = Tauren.BASEHPBONUS.getAmount();
	static int mpBonus  = Tauren.BASEMPBONUS.getAmount();
	static int strBonus = Tauren.BASESTRBONUS.getAmount();
	static int intBonus = Tauren.BASEINTBONUS.getAmount();

	public Player(GamePanel gp, int x, int y) {
		super(gp, "Player", "BATTLEMAGE/ TAUREN", 100 + hpBonus, mpBonus, strBonus, intBonus, x, y);	//HP, MP, STR, INT	
	}
}
