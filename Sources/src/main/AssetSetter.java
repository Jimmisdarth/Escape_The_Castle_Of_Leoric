package main;

import java.util.LinkedList;

import entities.Player;
import items.ItemGenerator;
import items.SlotType;
import weapons.DamageType;
import weapons.Dice;
import weapons.Weapon;

public class AssetSetter {

	GamePanel gp;
	ItemGenerator randomItemGenerator;
	
	private final int MAX_NUMBER_OF_SPAWNED_EQUIPPABLES_PER_LEVEL = 3;
	private final int MAX_NUMBER_OF_SPAWNED_POTIONS_PER_LEVEL = 3;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
		this.randomItemGenerator = new ItemGenerator(gp);
	}
	
	public void setupItems() {
		
		for(int i = 0; i < MAX_NUMBER_OF_SPAWNED_EQUIPPABLES_PER_LEVEL; ++i) {
			randomItemGenerator.newRandomEquippable(null,-1,-1, false);
		}
		for(int i = 0; i < MAX_NUMBER_OF_SPAWNED_POTIONS_PER_LEVEL; ++i) {
			randomItemGenerator.newRandomPotion();
		}
	}
	
	public void setupPlayer() {
		Player player = gp.getPlayer();
		setupPlayerMainHand(player);
		setupPlayerOffHand(player);
	}
	
	private void setupPlayerMainHand(Player player) {
		
		LinkedList<DamageType> damageTypes = new LinkedList<>();
		damageTypes.add(DamageType.SLASHING);
		damageTypes.add(DamageType.BLUNT);
		
		Dice sdice = new Dice(1, 6, 0); // N, S, Bonus
		Dice bdice = new Dice(1, 4, 0); // N, S, Bonus
		
		
		player.setCurrentSlot(SlotType.MAIN_HAND);
		Weapon playersWeapon = 
				new Weapon(gp, "Greatsword", sdice.toString() + "SLASHING, " + bdice.toString() + " BLUNT", SlotType.MAIN_HAND, 
						damageTypes, sdice, bdice, null, true, 0, 0);	// Slash, Blunt, Magic
		player.equip(playersWeapon);
	}
	
	private void setupPlayerOffHand(Player player) {
		
		LinkedList<DamageType> damageTypes = new LinkedList<>();
		damageTypes.add(DamageType.MAGICAL);
		
		Dice mDice = new Dice(1, 2, 0); // N, S, Bonus
		
		
		player.setCurrentSlot(SlotType.OFF_HAND);
		Weapon playersWeapon = 
				new Weapon(gp, "Great Wand", mDice.toString() + " Magical", SlotType.OFF_HAND, 
						damageTypes, null, null, mDice, true, 0, 0);	// Slash, Blunt, Magic
		player.equip(playersWeapon);
	}
}
