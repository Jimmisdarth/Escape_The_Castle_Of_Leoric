package items;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import main.GamePanel;
import weapons.DamageType;
import weapons.Dice;
import weapons.Weapon;

public class ItemGenerator {
	
	GamePanel gp;
	
	Random rng = new Random();

	String[] namePrefix = {"laughing", "deadly", "eternal", "amphibious", "improbable",
								  "reckless", "smiting", "boring", "mysterious"};
	
	String[] nameSuffix = {"of Doom", "of Pestilence", "of the Dead", "of Mythos", "of the clan McCloud", "of the Stars"};
	
	String itemStats ;
	
	Map<SlotType, List<String>> slotsMap = 
			Map.of( SlotType.NECK,      List.of("Necklace", "Scarf", "Amulet", "Necktie", "Bowtie"),
					SlotType.FINGER,    List.of("Ring", "Mittens", "Glove"),
					SlotType.MAIN_HAND, List.of("Sword", "Katana", "Staff", "Axe", "Mace", "Halberd", "Cutlass", "Morningstar"),
					SlotType.OFF_HAND,  List.of("Short Sword", "Dagger", "Baton", "Spiked Skull"));
	
	// None, Feeble, Common, Rare, Supreme
	Rarity[] rarityProbabilities = 
									{Rarity.NONE,   Rarity.NONE,   Rarity.NONE,   Rarity.NONE,   Rarity.NONE,   
									 Rarity.FEEBLE, Rarity.FEEBLE, Rarity.FEEBLE, Rarity.FEEBLE, Rarity.FEEBLE, 
									 Rarity.COMMON, Rarity.COMMON, Rarity.COMMON, Rarity.COMMON,
									 Rarity.RARE,   Rarity.RARE,   Rarity.RARE,
									 Rarity.SUPREME
									};
	
//	Dice sDice, bDice, mDice;
	Dice dices[];
	
	public ItemGenerator(GamePanel gp) { this.gp = gp; }
	
	public void newRandomPotion() {
		
		int x = rng.nextInt(gp.getMaxLabyrinthCol());
		int y = rng.nextInt(gp.getMaxLabyrinthRow());
		
		Dice dice = new Dice(1,12,0);
		int outcome = dice.roll();
		
		Usable potion;
		
		if(outcome <= 8) {	// Ta HP potions na einai pio pithana gia na mh ginetai abuse to Mana.
			int amount = 30;
			ItemEffect itemEffect = new ItemEffect(EffectType.HP_REPLENISH, amount);
			potion = new Usable(gp, "Health Potion", "+" + amount + " HP.", 4, 3, false, 2, itemEffect);
		}
		else {
			int amount = 40;
			ItemEffect itemEffect = new ItemEffect(EffectType.MP_REPLENISH, amount);
			potion = new Usable(gp, "Mana Potion", "+" + amount + " MP.", 4, 3, false, 2, itemEffect);
		}
		
		gp.getRoom(x,y).pushUsable(potion);
	}
	
	public String nameGenerator(Rarity rarity, SlotType slotType) {
		
		List<String> slotNames = slotsMap.get(slotType);
		String slotName = slotNames.get(rng.nextInt(slotNames.size()));
		
		String rarityName;

		rarityName = rarity.toString() + " ";
		
		return new StringBuilder(rarityName)
				.append(namePrefix[rng.nextInt(namePrefix.length)])
				.append(" ")
				.append(slotName)
				.append(" ")
				.append(nameSuffix[rng.nextInt(nameSuffix.length)]).toString();
	}
	
	public void newRandomEquippable(Rarity r, int x, int y, boolean setPotition) {
		
		if (setPotition == false) {
			x = rng.nextInt(gp.getMaxLabyrinthCol());
			y = rng.nextInt(gp.getMaxLabyrinthRow());
		}
		
		Rarity rarity;
		
		if (r == null) {
			rarity = rarityProbabilities[rng.nextInt(rarityProbabilities.length)];
		}
		else {
			rarity = r;
		}
		
		SlotType slotType = SlotType.values()[rng.nextInt(SlotType.values().length)];
		
		boolean isWeapon = false;
		
		if(slotType == SlotType.MAIN_HAND || slotType == SlotType.OFF_HAND) {
			isWeapon = true;	
		}
		
		String itemName = nameGenerator(rarity, slotType);
		
		if(isWeapon) {
			createWeapon(itemName, slotType, rarity, x, y);
		} else {
			createArmor(itemName, slotType, rarity, x, y);
		}
	}
	
	private void createWeapon(String weaponName, SlotType slotType, Rarity r, int x, int y) {
		
		LinkedList<DamageType> weaponDamage = createDamageForWeapon(r);
		
		Weapon newWeapon = 
				new Weapon(gp, weaponName, itemStats, SlotType.OFF_HAND, 
						weaponDamage, dices[0], dices[1], dices[2], false, 0, 0);	// Slash, Blunt, Magic
		
		gp.getRoom(x,y).pushWeapon(newWeapon);
	}
	
	private LinkedList<DamageType> createDamageForWeapon(Rarity r) {
		
		// we have to split the bonus points of the item into 1-3 of the possible bonuses
		int totalBonus = r.getTotalBonus();
				
		// SLASHING, BLUNT, MAGICAL
		int bonusDiv[] = {0, 0, 0};
		DamageType bonusItm[] = {DamageType.SLASHING, DamageType.BLUNT, DamageType.MAGICAL};
				
		while(totalBonus > 0) {
			bonusDiv[rng.nextInt(bonusDiv.length)] += 3;
			totalBonus = totalBonus - 1;
		}
		
//		sDice = null; bDice = null; mDice = null;
		
		dices = new Dice[3];
		DamageType damageTypes[] = { DamageType.SLASHING, DamageType.BLUNT, DamageType.MAGICAL };
		
		LinkedList<DamageType> itemDamages = new LinkedList<>();
		
		StringBuilder sBuilder = new StringBuilder();
		
		for (int i = 0; i < damageTypes.length; ++i) {
			if (bonusDiv[i] > 0) {
				
				itemDamages.add(bonusItm[i]);
				
				dices[i] = new Dice(bonusDiv[i]);
				sBuilder.append(dices[i].toString())
					 	.append(" ")
					 	.append(damageTypes[i].toString())
					 	.append(", ");
			}
		}
		
		itemStats = sBuilder.toString();
				
		return itemDamages;
	}
	
	private void createArmor(String armorName, SlotType slotType, Rarity r, int x, int y) {
		
		ArrayList<ItemEffect> itemEffects = createBonusForArmor(r);
		
		Armor newArmor = new Armor(gp, armorName, itemStats, x, y, false, slotType, itemEffects);
		gp.getRoom(x,y).pushArmor(newArmor);
	}
	
	private ArrayList<ItemEffect> createBonusForArmor(Rarity r) {
		
		// we have to split the bonus points of the item into 1-3 of the possible bonuses
		int totalBonus = r.getTotalBonus();
		
		// HP_BONUS, MP_BONUS, STR_BONUS, INT_BONUS
		int bonusDiv[] = {0, 0, 0, 0};
		EffectType bonusItm[] = {EffectType.HP_BONUS, EffectType.MP_BONUS, EffectType.STR_BONUS, EffectType.INT_BONUS};
		
		while(totalBonus > 0) {
			bonusDiv[rng.nextInt(bonusDiv.length)] += 1;
			totalBonus = totalBonus - 1;
		}
		
		StringBuilder sBuilder = new StringBuilder();
		
		ArrayList<ItemEffect> itemBonuses = new ArrayList<>();
		
		for(int i = 0; i < bonusDiv.length; i++) {
			if(bonusDiv[i] > 0) {
				itemBonuses.add(new ItemEffect(bonusItm[i], bonusDiv[i]));
				sBuilder.append(" +")
						.append(bonusDiv[i])
						.append(" ")
						.append(bonusItm[i].toString());
			}
		}
		
		itemStats = sBuilder.toString();
		
		return itemBonuses;
	}
}
