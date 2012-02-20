

public class Profile {
    int movement;
    int weaponSkill;
    int balisticSkill;
    int strength;
    int toughness;
    int wounds;
    int initiative;
    int attacks;
    int leadership;
    String name;
    

    public Profile(
    		int movement,
    		int weaponSkill,
    		int balisticSkill,
    		int strength,
    		int toughness,
    		int wounds,
    		int initiative,
    		int attacks,
    		int leadership,
    		String name )
    {
   		this.movement = movement;
		this.weaponSkill = weaponSkill;
		this.balisticSkill = balisticSkill;
		this.strength = strength;
		this.toughness = toughness;
		this.wounds = wounds;
		this.initiative = initiative;
		this.attacks = attacks;
		this.leadership = leadership;
		this.name = name;
    };

    public Profile(  )
    {
    }
    

	public int getMovement() {
		return movement;
	}
	public void setMovement(int m) {
		movement = m;
	}
	public int getWeaponSkill() {
		return weaponSkill;
	}
	public void setWeaponSkill(int wS) {
		weaponSkill = wS;
	}
	public int getBalisticSkill() {
		return balisticSkill;
	}
	public void setBalisticSkill(int bS) {
		balisticSkill = bS;
	}
	public int getStrength() {
		return strength;
	}
	public void setStrength(int s) {
		strength = s;
	}
	public int getToughness() {
		return toughness;
	}
	public void setToughness(int t) {
		toughness = t;
	}
	public int getWounds() {
		return wounds;
	}
	public void setWounds(int w) {
		wounds = w;
	}
	public int getInitiative() {
		return initiative;
	}
	public void setInitiative(int i) {
		initiative = i;
	}
	public int getAttacks() {
		return attacks;
	}
	public void setAttacks(int a) {
		attacks = a;
	}
	public int getLeadership() {
		return leadership;
	}
	public void setLeadership(int ld) {
		leadership = ld;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	 /**
     * Creates a string that describes this Profile.
     * @return a string with the location, direction, and color of this actor
     */
    public String toString()
    {
        return getClass().getName() + 
        		" name=" + name +
        		"\n  Movement=" + movement + 
        		"\n  Weapon Skill=" + weaponSkill + 
        		"\n  Balistic Skill=" + balisticSkill +
        		"\n  Strength=" + strength +
        		"\n  Toughness=" + toughness +
        		"\n  Wounds=" + wounds +
        		"\n  Initiative=" + strength +
        		"\n  Attacks=" + attacks ;
    }

}
