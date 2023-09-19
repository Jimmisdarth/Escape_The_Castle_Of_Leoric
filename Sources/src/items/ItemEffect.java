package items;

public class ItemEffect {
	
	EffectType effectType;
	int amount;	// H posothta tou effect.
	
	public ItemEffect(EffectType e, int a) {
		this.effectType = e;
		this.amount = a;
	}
	
	public EffectType getEffectType() { return effectType; }
	public int getAmount() { return amount; }
}