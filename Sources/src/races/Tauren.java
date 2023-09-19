package races;

public enum Tauren {
	BASEHPBONUS(20),
	BASEMPBONUS(0),
	BASESTRBONUS(12),
	BASEINTBONUS(6),
	BLUNTDEFENSE(1),
	SLASHINGDEFENSE(2),
	MAGICDEFENSE(0);
	
	int bonusAmount;
	
	Tauren(int bonus) {
		this.bonusAmount = bonus;
	}
	
	public int getAmount() { return this.bonusAmount; }
}
