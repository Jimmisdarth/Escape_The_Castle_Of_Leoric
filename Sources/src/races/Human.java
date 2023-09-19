package races;

public enum Human {
	BASEHPBONUS(5),
	BASEMPBONUS(5),
	BASESTRBONUS(9),
	BASEINTBONUS(9),
	BLUNTDEFENSE(1),
	SLASHINGDEFENSE(1),
	MAGICDEFENSE(0);
	
	int bonusAmount;
	
	Human(int bonus) {
		this.bonusAmount = bonus;
	}
	
	public int getAmount() { return this.bonusAmount; }
}
