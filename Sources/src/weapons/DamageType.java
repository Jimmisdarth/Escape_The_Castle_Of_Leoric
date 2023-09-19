package weapons;

public enum DamageType {
	
	SLASHING, BLUNT, MAGICAL;
	
	@Override
	public String toString() {
		switch (this) {
			case SLASHING:
				return "SLASHING";
			case BLUNT:
				return "BLUNT";
			case MAGICAL:
				return "MAGICAL";
			default:
				return "null";
		}
	}
}
