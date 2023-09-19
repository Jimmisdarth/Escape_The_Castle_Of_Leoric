package weapons;

import java.util.Iterator;
import java.util.LinkedList;

public class DamageTypeAmount {
	
	DamageType dType;
	int damage;
	
	public DamageTypeAmount(DamageType dType, int d) {
		this.dType = dType;
		this.damage = d;
	}
	
	public DamageType getDamageType() { return dType; }
	public int getDamage() { return damage; }
	
	public int addDamage(int d) { 
		damage += d; 
		return damage;
	}
	
	public int removeDamage(int d) { 
		damage -= d; 
		
		if (damage < 0) {
			damage = 0;
		}
		
		return damage;
	}

	@Override
	public String toString() {
		return "DamageTypeAmount : " + dType + ", " + damage;
	}
	
	public static LinkedList<DamageTypeAmount> addDamageTypes(LinkedList<DamageTypeAmount> d1, LinkedList<DamageTypeAmount> d2) {
		
		LinkedList<DamageTypeAmount> finalDamage = zeroDamage();
		
		mergeLists(finalDamage, d1);
		mergeLists(finalDamage, d2);		
		
		return finalDamage;
	}
	
	private static void mergeLists(LinkedList<DamageTypeAmount> d1, LinkedList<DamageTypeAmount> d2) {
		
		Iterator<DamageTypeAmount> d2Iterator = d2.iterator();

		while (d2Iterator.hasNext()){
			DamageTypeAmount damage = d2Iterator.next();
			
			if (damage.getDamageType() == DamageType.SLASHING) {
				d1.get(0).addDamage(damage.getDamage());
			}
			else if (damage.getDamageType() == DamageType.BLUNT) {
				d1.get(1).addDamage(damage.getDamage());
			}
			else{
				d1.get(2).addDamage(damage.getDamage());
			}
		 }
	}
	
	public static LinkedList<DamageTypeAmount> zeroDamage() {
		
		LinkedList<DamageTypeAmount> zeroDamage = new LinkedList<>();
		zeroDamage.add(new DamageTypeAmount(DamageType.SLASHING, 0));
		zeroDamage.add(new DamageTypeAmount(DamageType.BLUNT, 0));
		zeroDamage.add(new DamageTypeAmount(DamageType.MAGICAL, 0));
		
		return zeroDamage;
	}
}
