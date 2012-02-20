import java.awt.Color;
import java.util.ArrayList;

/**
 * @author Theo
 *
 */
public class Army {
//	ArrayList<Unit> units = new ArrayList<Unit>();
	ArrayList<Unit> units = new ArrayList<Unit>();
	String name;
	ArrayList<Character> characters = new ArrayList<Character>();
    Color color;
    int numUnits = 0;

	/**
	 * @param name
	 */
	public Army(String name, Color color ) {
		super();
		this.name = name;
		this.color = color;
	}

	public ArrayList<Unit> getUnits() {
		return units;
	}

	public void addUnit( Unit unit) {
		this.units.add(unit);
		numUnits++;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Character> getCharacters() {
		return characters;
	}

	public void setCharacters(ArrayList<Character> characters) {
		this.characters = characters;
	}
	
    public String toString()
    {
    	System.out.println("Initial size of units :  " + units.size());

        return getClass().getName() + 
        		" name = " + name +
        		"\n units =" + units
        		;
    }

}
