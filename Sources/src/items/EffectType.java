package items;

public enum EffectType {
	
	NONE(false),
	INT_BONUS(false),
	STR_BONUS(false),
	MP_BONUS(false),
	HP_BONUS(false),
	HP_REPLENISH(true),
	MP_REPLENISH(true),
	DEFENSE(false);
	
	private final boolean isEffect; // Ean den einai effect tote einai false kai mporei na mpei
									// se kapoio slot.
	
	private EffectType(boolean isEffect) {
		this.isEffect = isEffect;
	}
	
	public boolean isUsable() { return isEffect; }
	public boolean canBeInASlot() { return !isUsable(); }
	
	@Override
	public String toString() {
		switch (this) {
			case NONE:
				return "NONE";
			case INT_BONUS:
				return "INT_BONUS";
			case STR_BONUS:
				return "STR_BONUS";
			case MP_BONUS:
				return "MP_BONUS";
			case HP_BONUS:
				return "HP_BONUS";
			case HP_REPLENISH:
				return "HP_REPLENISH";
			case MP_REPLENISH:
				return "MP_REPLENISH";
			case DEFENSE:
				 return "DEFENSE";
			default:
				return "null";
		}
	}
}
