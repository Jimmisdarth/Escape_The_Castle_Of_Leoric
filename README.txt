Ακολουθήθηκε η προσέγγιση που προτάθηκε στην εργασία με τις εξής αλλαγές:

Σχεδίαση:
	1.  Τα πάντα είναι Entities. Ο παίκτης, οι εχθροί και τα αντικείμενα είναι entities,
 	    τα entity έχουν θέση (x,y) όνομα, περιγραφή, κατεύθυνση και hit points.

	2.  Τα items κληρονομούν από τη κλαση Entity (an items is an entity) και έχουν μία boolean για το αν 
   	    είναι equipped ή όχι, Item, Equippable, Armor, Weapon υλοπoιήθηκαν ως classes.

	3.  Για τα Equippables, Armor, Weapon ακολοθούθηκε η προσέγγιση της άσκησης με τη διαφορά ότι μόνο το Armor
    	    μπορεί να έχει ItemEffects, δλδ να προσδίδει bonus στα stats του παίκτη. Ενώ το Weapon έχει μια λίστα από 
    	    DamageTypes και (κρυφά) Dices που αντιστοιχούν στα DamageTypes ώστε να υπολογίσουν το τελίκα damage που θα 
            προκληθεί, πχ 1d6 + 2 Magical.

	4.  Στους εχθρούς προστέθηκαν οι μεταβλητές actionLockCounter, lockCount, διότι οι εχθροί θα κινούνται κάθε 
    	    lockCount χρόνους παιχνιδιού, δηλαδή οι εχθροί δε μπορούν να πάνε πιο γρήγορα από το παίκτη και ανάλογα 
    	    με το τύπο του εχθρού να είναι πιο αργοί ή πιο γρήγοροι πχ Slime έχει lockCount = 4.
		
	5.  Οι εχθροί κατά τον θάνατο τους πετάνε ένα  τυχαίο item με συγκεκριμένο rarirty, ανάλογα με τον εχθρό πάντα.

Γραφικά:
	1.  Στο minimap φαίνονται οι εχθροί μέχρι 3 tiles μακρία από το παίκτη ώστε ο παίκτης να έχει μια μερική αίσθηση
		για το που βρίσκονται.
		
	2.  Οι εχθροί έχουν διαφορετικό χρώμα (βλέπε void drawInMinimap(Graphics2D g2) στην Entity).
	
	2.  Τα dropped Items φαίνονται σε όλα το minimap ανεξάρτητα από το αν ένα δωμάτιο είναι visited ή οχι.
	
	3.  Έβαλα κάποιες φλόγες στο sprite το skeleton demon γιατί είχε ίδιο sprite με του skeleton.
	
	4.  Τα items που είναι dropped στοιβάζονται το ένα πάνω στο άλλο και φάινεται μόνο το top item.
	
	5.  Τα dropped items ανάλογα με τη κλάση τους έχουν και διαφορετικό χρώμα και βρίσκονται σε διαφορετικές
		στοίβες. Τα weapons έχουν πορτοκαλί χρώμα, Τα armors έχουν ανοιχτό μπλε χρώμα, Τα Usables (δλδ τα potions) 
		έχουν κόκκινο χρώμα.
	
Χειρισμός:
	1.  Ο παίκτης έχει τη δυνατότητα να κάνει drop ένα equippable με το κουμπί f.
		
	2.  Τα potions τα μαζευεί ο παίκτης με το κουμπί p και τα κάνει drop με το κουμπί l.

	3.  Προστέθηκαν κάποιες εντολές για debug (;,.,0,9) πχ η ; δείχνει όλους τους εχθρούς στο χάρτη ανεξάρτητα της
	    απόστασης τους από τον πάικτη (βλέπε περισσότερα στο void getKeyCodeForAlivePlayer(char userInput) στην
	    GamePanel class).
	
