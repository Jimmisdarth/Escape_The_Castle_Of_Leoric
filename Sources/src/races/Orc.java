package races;

public enum Orc {
	BASEHPBONUS(10),
	BASEMPBONUS(0),
	BASESTRBONUS(10),
	BASEINTBONUS(8),
	BLUNTDEFENSE(1),
	SLASHINGDEFENSE(1),
	MAGICDEFENSE(0);
	
	int bonusAmount;
	
	Orc(int bonus) {
		this.bonusAmount = bonus;
	}
	
	public int getAmount() { return this.bonusAmount; }
}
