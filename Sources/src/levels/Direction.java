package levels;

//		N
//	  W   E
//	  	E

// Auto tha fygei sto telos
// Exei 4 kateythinseis pou antistoixoun se 4 kateythinseis tis pyjidas (N, W, S, E).
public enum Direction {
    NORTH, SOUTH, EAST, WEST;
	
	// Epistrefei se string tis kateuthinseis pou antistoixoun px.
	public String toString() {
		switch (this) {
			case NORTH:
				return "NORTH";
			case WEST:
				return "WEST";
			case SOUTH:
				return "SOUTH";
			case EAST:
				return "EAST";
			default:
				return "null";
		}
	}
	
	// Ektelei strofh pros ta aristera se sxesh me ekei poy "koitaei"
	// dld an koitas sto North tote sta aristera sou einai h West
	// opote meta koitas sth West.
    public Direction left() {
    	switch (this) {
    		case NORTH:
    			return WEST;
    		case WEST:
    			return SOUTH;
    		case SOUTH:
    			return EAST;
    		case EAST:
    			return NORTH;
    		default:
    			return null;
    	}
    }
    
    // Ektelei strofh pros ta dejia se sxesh me ekei poy "koitaei"
 	// dld an koitas sto North tote sta dejia sou einai h East
 	// opote meta koitas sth East.
    public Direction right() {
    	switch (this) {
			case NORTH:
				return EAST;
			case EAST:
				return SOUTH;
			case SOUTH:
				return WEST;
			case WEST:
				return NORTH;
			default:
				return null;
    	}
    }
}

