package items;

public enum SlotType {
	MAIN_HAND,
	OFF_HAND,
	FINGER,
	NECK;
	
	@Override
	public String toString() {
		switch (this) {
			case MAIN_HAND:
				return "MAIN_HAND";
			case OFF_HAND:
				return "OFF_HAND";
			case FINGER:
				return "FINGER";
			case NECK:
				return "NECK";
			default:
				return "null";
		}
	}
}
