
public class Character {
    String name;
    Profile charProf;
    int hasMount;
    Profile mountProf;
    int points;
    int type;
    int isGeneral;
    int magicLevel;

    static Profile charProfiles[] = {
		// M WS BS  S  T  W  I  A  Ld name
		// Dwarfs
		new Profile( 3, 7, 4, 4, 5, 3, 4, 4, 10, "Dwarf Lord"),
		new Profile( 3, 6, 4, 4, 5, 3, 3, 4,  9, "Runelore"),
		new Profile( 3, 5, 3, 4, 4, 1, 2, 1,  9, "Anvil Guard"),
		new Profile( 3, 7, 3, 4, 5, 3, 5, 4, 10, "Daemon Slayer"),

		 new Profile( 3, 6, 4, 4, 5, 2, 3, 3,  9, "Thane"),
		 new Profile( 3, 5, 4, 4, 4, 2, 2, 2,  9, "Runesmith"),
		 new Profile( 3, 4, 5, 4, 4, 2, 2, 2,  9, "Master Engineer"),
		 new Profile( 3, 6, 3, 4, 5, 2, 4, 3, 10, "Dragon Slayer"),

		 new Profile( 3, 4, 3, 3, 4, 1, 2, 1,  9, "Warrior"),
		 new Profile( 3, 4, 3, 3, 4, 1, 2, 2,  9, "Champion Warrior"),
		 new Profile( 3, 5, 3, 4, 4, 1, 2, 1,  9, "Longbeard"),
		 new Profile( 3, 5, 3, 4, 4, 1, 2, 2,  9, "Champion Longbeard"),
		 new Profile( 3, 4, 3, 3, 4, 1, 2, 1,  9, "Quarreller"),
		 new Profile( 3, 4, 3, 3, 4, 1, 2, 2,  9, "Champion Quarreller"),
		 new Profile( 3, 4, 3, 3, 4, 1, 2, 1,  9, "Thunderer"),
		 new Profile( 3, 4, 3, 3, 4, 1, 2, 2,  9, "Champion Thunderer"), 

		 new Profile( 3, 4, 3, 3, 4, 1, 2, 1,  9, "Miner"),
		 new Profile( 3, 4, 3, 3, 4, 1, 2, 2,  9, "Prospector"),
		 new Profile( 3, 5, 3, 4, 4, 1, 2, 1,  9, "HunitStrengthAerer"),

		 new Profile( 3, 5, 3, 4, 4, 1, 2, 2,  9, "Gatekeeper"),
		 new Profile( 3, 5, 3, 4, 4, 1, 2, 1,  9, "Ironbreaker"),
		 new Profile( 3, 5, 3, 4, 4, 1, 2, 2,  9, "Ironbeard"),
		 new Profile( 3, 4, 3, 3, 4, 1, 2, 1, 10, "Troll Slayer"),
		 new Profile( 3, 5, 3, 4, 4, 1, 3, 2, 10, "Giant Slayer"),
		 new Profile( 0, 0, 0, 0, 7, 3, 0, 0,  0, "Cannon"),
		 new Profile( 0, 0, 0, 0, 7, 3, 0, 0,  0, "Bolt Thrower"),
		 new Profile( 0, 0, 0, 0, 7, 3, 0, 0,  0, "Grudge Thrower"),
		 new Profile( 3, 4, 3, 3, 4, 1, 2, 1,  9, "Artillery Crew"),
		 new Profile( 3, 4, 4, 3, 4, 1, 2, 1,  9, "Engineer"),

		 new Profile( 0, 0, 0, 0, 7, 3, 0, 0,  0, "Organ Gun"),
		 new Profile( 0, 0, 0, 0, 7, 3, 0, 0,  0, "Flame Cannon"),
		 new Profile( 0, 4, 0, 4, 5, 3, 2, 2,  9, "Gyrocopter"),
		// Orcs & Goblins
		 new Profile( 4, 2, 3, 3, 3, 1, 3, 1,  5, "Night Goblins"),

		 new Profile( 0, 0, 0, 0, 0, 0, 0, 0,  0, ""),

		};


    /**
     * Constructs a Profile.
     */
    public Character()
    {
    	charProf = new Profile();
        setName("NoName");
    }
    
	public static Profile findProfile(String name) {
		int x;
		
		for (x=0; x<charProfiles.length; x++){
			if (charProfiles[x].name == name)
			{
				return(charProfiles[x]);
			}
		}
		return(charProfiles[0]);
	}

    public Character(String name)
    {
    	charProf = Character.findProfile(name);
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Profile getCharProf() {
		return charProf;
	}

	public void setCharProf(Profile charProf) {
		this.charProf = charProf;
	}

	public int getHasMount() {
		return hasMount;
	}

	public void setHasMount(int hasMount) {
		this.hasMount = hasMount;
	}

	public Profile getMountProf() {
		return mountProf;
	}

	public void setMountProf(Profile mountProf) {
		this.mountProf = mountProf;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIsGeneral() {
		return isGeneral;
	}

	public void setIsGeneral(int isGeneral) {
		this.isGeneral = isGeneral;
	}

	public int getMagicLevel() {
		return magicLevel;
	}

	public void setMagicLevel(int magicLevel) {
		this.magicLevel = magicLevel;
	}
	
    /**
     * Creates a string that describes this Character.
     * @return a string with the location, direction, and color of this actor
     */
    public String toString()
    {
        return getClass().getName() + "[name=" + name + ",hasMount="
                + hasMount + ",points=" + points + "]\n " + charProf;
    }

}
