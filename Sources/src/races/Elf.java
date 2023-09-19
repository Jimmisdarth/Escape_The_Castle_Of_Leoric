package races;

public enum Elf {
	BASEHPBONUS(5),
	BASEMPBONUS(20),
	BASESTRBONUS(6),
	BASEINTBONUS(12),
	BLUNTDEFENSE(0),
	SLASHINGDEFENSE(1),
	MAGICDEFENSE(2);
	
	int bonusAmount;
	
	Elf(int bonus) {
		this.bonusAmount = bonus;
	}
	
	public int getAmount() { return this.bonusAmount; }
}
