package weapons;

import java.util.Random;

public class Dice {
	
	int N;	// number of dices
	int S;	// sides of the dice (1 to S)
	
	int bonus;
	
	Random random = new Random();	// for generating random numbers
	
	public Dice(int N, int S, int b) {
		initialization(N,S,b);
	}
	
	public Dice(int magicNumber) {
		
		if (magicNumber == 3) {
			initialization(1,3,0);
		}
		if (magicNumber == 6) {
			initialization(1,6,1);
		}
		if (magicNumber == 9) {
			initialization(1,6,2);
		}
		if (magicNumber == 12) {
			initialization(1,6,4);
		}
		if (magicNumber == 15) {
			initialization(1,6,6);
		}
		if (magicNumber == 18) {
			initialization(2,6,0);
		}
		if (magicNumber == 21) {
			initialization(2,6,2);
		}
		if (magicNumber == 24) {
			initialization(2,6,4);
		}
		if (magicNumber == 27) {
			initialization(2,6,6);
		}
		if (magicNumber == 30) {
			initialization(4,4,0);
		}
		if (magicNumber == 33) {
			initialization(4,4,2);
		}
		if (magicNumber == 36) {
			initialization(4,4,4);
		}
	}
	
	public int getN() { return N; }
	public int getS() { return S; }
	public int getBonus() { return bonus; }
	
	private void initialization(int N, int S, int b) {
		this.N = N;
		this.S = S;
		this.bonus = b;
	}
	
	// Roll the dice with bonus.
	//
	// Because we have N numbers of dices we use a for loop to emulate the rolling.
	public int roll() {
		
		int result = 0;
		
		for(int i = 0; i < N; ++i) {
			result += random.nextInt(S)+1;
		}
		
		return result + bonus;
	}
	
	@Override
	// Epirstrefei to NdS + bonus px 1d6 + 2.
	public String toString() { 
		
		StringBuilder sBuilder = new StringBuilder();
		
		if (bonus != 0) {
			sBuilder.append(N)
					.append("d")
					.append(S)
					.append(" + ")
					.append(bonus);
			
			return sBuilder.toString();
		}
		else {
			sBuilder.append(N)
			.append("d")
			.append(S);
	
			return sBuilder.toString();
		}
	}
	
	public static Dice zeroDice() {
		Dice zeroDice = new Dice(0,0,0);
		return zeroDice;
	}
}
