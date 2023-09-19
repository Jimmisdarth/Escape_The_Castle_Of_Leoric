package items;

public enum Rarity {

	NONE(0),
	FEEBLE(1),
	COMMON(4),
	RARE(8),
	SUPREME(12);
	
	private final int totalBonus;
	
	private Rarity(int t) { this.totalBonus = t; }
	
	public int getTotalBonus() { return totalBonus; }
	
	@Override
	public String toString() {
		switch (this) {
			case NONE:
				return "";
			case FEEBLE:
				return "feeble";
			case COMMON:
				return "common";
			case RARE:
				return "rare";
			case SUPREME:
				return "supreme";
			default:
				return "null";
		}
	}
}
