import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Game {
	static int width = 900;
	static int height = 600;
	
	// toHitChart [Attackers Weapon Skill] [ Opponents Weapon Skill]
	public static final int[][] toHitChart = {
	//1 2 3 4 5 6 7 8 9 10
	 {4,4,5,5,5,5,5,5,5,5},  // 1
	 {3,4,4,4,5,5,5,5,5,5},  // 2
	 {3,3,4,4,4,4,5,5,5,5},  // 3
	 {3,3,3,4,4,4,4,4,5,5},  // 4
	 {3,3,3,3,4,4,4,4,4,4},  // 5
	 {3,3,3,3,3,4,4,4,4,4},  // 6
	 {3,3,3,3,3,3,4,4,4,4},  // 7
	 {3,3,3,3,3,3,3,4,4,4},  // 8
	 {3,3,3,3,3,3,3,3,4,4},  // 9
	 {3,3,3,3,3,3,3,3,3,4}   // 10
	};

	// toWoundChart [Attackers Strength] [ Opponents Toughness]
	public static final int[][] toWoundChart = {
	//1 2 3 4 5 6 7 8 9 10
	 {4,5,6,6,0,0,0,0,0,0},  // 1
	 {3,4,5,6,6,0,0,0,0,0},  // 2
	 {2,3,4,5,6,6,0,0,0,0},  // 3
	 {2,2,3,4,5,6,6,0,0,0},  // 4
	 {2,2,2,3,4,5,6,6,0,0},  // 5
	 {2,2,2,2,3,4,5,6,6,0},  // 6
	 {2,2,2,2,2,3,4,5,6,6},  // 7
	 {2,2,2,2,2,2,3,4,5,6},  // 8
	 {2,2,2,2,2,2,2,3,4,5},  // 9
	 {2,2,2,2,2,2,2,2,3,4}   // 10
	};

	String name;
	ArrayList<Army> armies = new ArrayList<Army>();
	static int debugLevel = 5;
	public static JFrame table;
	public static JFrame txtFrame;
	public static JTextArea textArea;
	public static JScrollPane scrollPane;
	public static BattleField bf;
	public static ControlPanel cp = new ControlPanel();
	public static CountDownLatch latch = new CountDownLatch(1); 


	public Game(String name) {
		super();
		this.name = name;
		
		cp.createAndShowGUI(this);
		// game table
		table = new JFrame(name);
		table.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
		
        
        // action log
        txtFrame = new JFrame(name);
		txtFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        textArea = new JTextArea(5, 20);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        textArea.setEditable(false);
        textArea.append("this is a test\n");
        textArea.append("this is only a test\n");
        
        scrollPane = new JScrollPane(textArea);
        txtFrame.add(scrollPane);
        
//        txtFrame.add(textArea);
        txtFrame.setSize(450, height);
        txtFrame.setVisible(true);
        txtFrame.setLocation(width, 100);
        txtFrame.repaint();
		
        // set up battlefield 
        bf = new BattleField(this);
        table.add(bf);

        
        table.setSize(width, height);
        table.setVisible(true);
        table.setLocation(0, 10);
        table.repaint();
	}

	public ArrayList<Army> getArmies() {
		return armies;
	}

	public void addArmy(Army army) {
		this.armies.add(army);
	}

	public void play() throws IOException {
		int i = 0;
		Army a1 = armies.get(0);
		Army a2 = armies.get(1);

        try {
        	// TODO save game
			Game.write(a1.units, "xxx");
		} catch (Exception e) {
			e.printStackTrace();
		}

		while(( a1.numUnits != 0) && ( a2.numUnits != 0))
		{
			i++;
	        println( 2, "a1.numUnits = " + a1.numUnits + ", a2.numUnits = " + a2.numUnits + "");
	        println( 2, "\n############################## Turn " + i + " ##############################\n");
			input();
	        theTurn(a1, a2);
	        theTurn(a2, a1);

			table.invalidate();
			table.validate();
			table.repaint();
		}
		if ( a2.numUnits == 0 ) {
	        println( 2, "\n############# " + a1.name + " WINS! #############\n");
		} else {
	        println( 2, "\n############# " + a2.name + " WINS! #############\n");	
		}
		input();
		System.exit(0);
	}

	/*********************************************************
	 * The Turn
	 * startPhase()    - 1. Start of the turn phase
	 * movementPhase() - 2. Movement phase
	 * magicPhase()    - 3. Magic phase
	 * shootingPhase() - 4. Shooting phase
	 * combatPhase()   - 5. Close Combat phase
	 *********************************************************/
	public void theTurn(Army armyA, Army armyD) {
		
		println( 2, armyA.name + " vs " + armyD.name + "\n");
        startPhase();
        movementPhase();
        magicPhase();
        shootingPhase();
        combatPhase(armyA, armyD);
	}

	/*********************************************************
	 * startPhase() - 1. Start of the turn phase
	 *********************************************************/
	public void startPhase() {
        println( 2, "  1. Start of the turn phase");
	}

	/*********************************************************
	 * movementPhase() - 2. Movement phase
	 *  1. Declare Charges
	 *  2. Rally Fleeing Troops
	 *  3. Compuslsory Moves
	 *  4. Move Chargers
	 *  5. Remaining moves
	 *********************************************************/
	public void movementPhase() {
		println( 2, "  2. Movement phase");

		/*
		 *  1. Declare Charges
		 */
		println( 2, "  2.1 Declare Charges");

		/*
		 *  2. Rally Fleeing Troops
		 */
		println( 2, "  2.2 Rally Fleeing Troops");

		/*
		 *  3. Compuslsory Moves
		 */
		println( 2, "  2.3 Compuslsory Moves");

		/*
		 *  4. Move Chargers
		 */
		println( 2, "  2.4 Move Chargers");

		/*
		 *  5. Remaining moves
		 */
		println( 2, "  2.5 Remaining");
	}

	/*********************************************************
	 * magicPhase()    - 3. Magic phase
	 *********************************************************/
	public void magicPhase() {
		println( 2, "  3. Magic phase");
	}

	/*********************************************************
	 * shootingPhase() - 4. Shooting phase
	 *********************************************************/
	public void shootingPhase() {
        println( 2, "  4. Shooting phase");
	}

	/*********************************************************
	 * combatPhase() - 5.Combat Phase
	 *********************************************************/
	public void combatPhase(Army armyA, Army armyD) {
		ArrayList<Unit> uA = armyA.units;
		ArrayList<Unit> cuA;
		
		println( 2, "  5. Combat phase");

		// search all units for combat phase
		for (Unit u : uA) {
			cuA = u.combatUnits;
			for (Unit cu : cuA) {
				if(!u.destroyed && !cu.destroyed){
				    closeCombatPhase(u, cu);
				}
			}
		}
	}


	/*********************************************************
	 * combatPhase() - 5. Close Combat phase 
	 *  1. Fight Combat
	 *  2. Combat Result
	 *  3. BreakTest
	 *  4. Flee and Pursueprofile_t *A
	 *  5. Redress Ranks
	 * 
	 * After all of the other phases are completed you go into combat phase in
	 * which the player who charges strikes first (only on the first round of
	 * combat, after that it is based on initiative). to hit you compare the WS
	 * of the fighters. if they are the same the attacker hits on a 4+, if the
	 * attackers is 1 or more higher then he/she/it (lol) hits on a 3+. But if
	 * the defenders WS is more than twice that of the attackers then the
	 * attacker will hit on a 5+.
	 * 
	 * After you calculate the hits you go into the damage calculation
	 * calculation part. in hear you compare the attackers S to the defenders
	 * toughness. If they are even (say S3 and T3) the attacker will wound the
	 * defender on a 4+. If the S is 1 higher then the T then the attacker will
	 * wound on a 3+, 2 higher will wound on a 2+. if the S is lower then the T
	 * then the attacker will wound on a 5 or 6 depending on the difference.
	 * 
	 * and finally for the fun part. Now you need to calculate the Armor save.
	 * using dwarfs as an example a typical warrior will get a 3+ save, this is
	 * because they have heavy armour, shield, and a hand weapon (which only
	 * counts for close combat). after you find out what the Armour save is you
	 * will actually raise the number depending on the strength of the attacker
	 * a S4 enemy will change an armour save from a 3+ to a 4+.S5 will put you
	 * at a 5+. S6 6+. S7 or higher you get no Amour save (ha! ha!).
	 * 
	 * And last but not least combat resolution. please just read the book 4
	 * that one.
	 *********************************************************/
	public void closeCombatPhase(Unit au, Unit du) 
	{
	    int casualtiesA = 0;
	    int casualtiesD = 0;
		int scoreA = 0;
	    int scoreD = 0;
		int unitStrengthA = 0;
		int unitStrengthD = 0;
	    Unit pLoser = null;
	    Unit pWinner = null;
	    int distanceLoser = 0;
	    int distanceWinner = 0;
	    int dice;
	    int Ld;

//	    println( 2, "  5. Close Combat phase\n");
	    println( 2, "\n  ======= close combat " + au.name + " vs. " + du.name + " =======");

        //  5.1 Fight Combat - compare attacker and defender WS
		casualtiesD = fightCombat(au, du);
		casualtiesA = fightCombat(du, au);

	 //  5.2 Combat Result (38)
	    println( 4, "  5.2 Combat Result\n");
	    println( 2, "      "+au.name+"   "+du.name+"");
	    // Casualties  (pa.37)
		println( 2, "   Opponent Casualties  +"+casualtiesD+"        +"+casualtiesA);
		au.numFighters -= casualtiesA;
		du.numFighters -= casualtiesD;
		scoreA += casualtiesD;
		scoreD += casualtiesA;

	    // Extra Rank  (pa.8,38,71)
	    println( 2, "   Ranks                +"+au.rank+"        +"+du.rank);
		scoreA += au.rank;
		scoreD += du.rank;

	    // Outnumber Enemy / Unit Strength  (pa.8,38,71)
		if (au.numFighters > du.numFighters) unitStrengthA = 1;
		if (au.numFighters < du.numFighters) unitStrengthD = 1;
	    println( 2, "   Unit Strength        +"+unitStrengthA+" ("+au.numFighters+")   +"+unitStrengthD+" ("+du.numFighters+")");
		scoreA += unitStrengthA;
		scoreD += unitStrengthD;

	    // Standard  (pa.38)
	    println( 2, "   Standard             +"+au.standard+"        +"+du.standard);
		scoreA += au.standard;
		scoreD += du.standard;

	    // Battle Standard  (pa.38)
	    println( 2, "   Battle Standard      +"+au.battleStandard+"        +"+du.battleStandard);
		scoreA += au.battleStandard;
		scoreD += du.battleStandard;

	    // High Ground  (pa.38)
	    // Flank Attack  (pa.38)
	    // Rear Attack  (pa.38)
	    // OverKill  (pa.38)

	    // Totals
		println( 2, "                     =======    =======");
		println( 2, "        Totals          +"+scoreA+"        +"+scoreD);
		if(scoreA > scoreD)
		{
			println( 2, "   +"+au.army.name+" wins!");
	        pLoser = du;
	        pWinner = au;
		}
		else if (scoreD > scoreA)
		{
			println( 2, "   +"+du.army.name+" wins!");
	        pLoser = au;
	        pWinner = du;
		}
		else
		{
	        println( 2, "   Tie!   Musician      +"+au.musician+"        +"+du.musician);
	        if(au.musician > du.musician)
	        {
	            println( 2, "   "+au.army.name+" wins!");
	            pLoser = du;
	            pWinner = au;
	        }
	        else if (du.musician > au.musician)
	        {
	            println( 2, "   "+du.army.name+" wins!");
	            pLoser = au;
	            pWinner = du;
	        }
	        else
	        {
	            println( 2, "   Tie!");
	        }
		}
		println( 2, "" );

	    // if it's a tie, get out now
	    if (pLoser == null)
	        return;

	 //  5.3 Break Test pa.39
	    println( 4, "  5.3 Break Test");
	    dice = rollDice();
	    dice += rollDice();
	    
	    // +1 Ld for musician
	    Ld = pLoser.profile.leadership;
	    if (pLoser.musician != 0)
	    {
	    	println( 2, "   "+pLoser.name+" has a musician, +1 Ld!");
	        Ld++;
	    }
	    println( 2, "   2D6 = "+dice+", score diff = "+Math.abs(scoreA-scoreD)+", Ld = "+Ld);

		dice += Math.abs(scoreA-scoreD);
	    // snake eyes always hold
	    if ((dice > Ld) && (dice != 2))
	    {
	        println( 2, "   "+pLoser.name+" Breaks! ("+dice+">"+Ld+")");
	    }
	    else
	    {
	    	println( 2, "   "+pLoser.name+" Holds!");
	        return;
	    }

	 //  5.4 Flee and Pursue
	    println( 4, "  5.4 Flee and Pursue\n");

	    distanceLoser = rollDice();
	    distanceLoser += rollDice();
	    if (pLoser.profile.movement > 6) 
	        distanceLoser += rollDice();
	    pLoser.rotate(180);
	    pLoser.moveDist(distanceLoser);
	    pLoser.rotate(180);

	    distanceWinner = rollDice();
	    distanceWinner += rollDice();
	    if (pWinner.profile.movement > 6) 
	        distanceWinner += rollDice();
	    pWinner.moveDist(distanceWinner);

	    
	    println( 2, "   "+pLoser.name+" flees "+distanceLoser+", "+pWinner.name+" pursues "+distanceWinner+"!");
	    if (distanceWinner >= distanceLoser)
	    {
	    	println( 2, "   "+pLoser.name+" is destroyed!");
//	        unitRemoveCombatUnit(pWinner, pLoser);
//	        unitRemoveCombatUnit(pLoser, pWinner);
	        pLoser.destroyed = true;
	        pLoser.numFighters = 0;
	        pLoser.army.numUnits--;
	        table.repaint();
	    }
	    println( 2, "" );

	 //  5.5 Redress Ranks
	    println( 4, "  5.5 Redress Ranks");
	    au.redress();
	    du.redress();
	}

	/*********************************************************
	 * fightCombat() - 5.1 Fight Combat
	 *********************************************************/
	public int fightCombat(Unit au, Unit du) {
		int numDice;
		int i;
		int dice;
		int hit = 0;
	    int wound = 0;
	    int casualties = 0;

		print( 4,"  5.1 Fight Combat:  ");
		println( 4, au.name + " against " + du.name);
		/*
		 * 5.1.1 Fight Combat - compare attacker and defender WS if they are the
		 * same the attacker hits on a 4+ if the attackers is 1 or more higher
		 * then he hits on a 3+ if the defenders WS is more than twice that of
		 * the attackers then the attacker will hit on a 5+
		 */
		// Hit test
		print( 4,"  5.1.1 Hit test - ");
		println( 4, "Attacker's Weapon Skill "
						+ au.profile.weaponSkill
						+ ", Opponent's Weapon Skill "
						+ du.profile.weaponSkill
						+ ", must roll "
						+ toHitChart[au.profile.weaponSkill - 1][du.profile.weaponSkill - 1]
						+ "+");
		numDice = au.file;
		print( 4,"        Throwing " + numDice + " dice: ");
		for (i = 0; i < numDice; i++) {
			dice = rollDice();
			print( 4," " + dice);
			if (dice >= toHitChart[au.profile.weaponSkill - 1][du.profile.weaponSkill - 1]) {
				hit++;
				print( 4," - HIT!,");
			} else {
				print( 4," - no hit,");
			}
		}
		println( 4, "\n        Total = " + hit + " hits");

	    /*
	     *  2. Wound Test
	     */
		print( 4,"  5.1.2 Wound Test - ");
		println( 4, "Attacker's Strength = " 
				+ au.profile.toughness
				+ ", Opponent's Toughness = "
				+ du.profile.toughness
				+ ", must roll "
				+ toWoundChart[au.profile.strength - 1][du.profile.toughness - 1]
				+ "+");
		print( 4,"        Throwing " + hit + " dice:");
		for (i = 0; i < hit; i++) {
			dice = rollDice();
			print( 4," " + dice);
			if (dice >= toWoundChart[au.profile.strength - 1][du.profile.toughness - 1]) {
				wound++;
				print( 4," - WOUND!,");
			} else {
				print( 4," - no wound,");
			}
		}
		println( 4, "\n        Total = " + wound + " wound");
		
	    /*
	     *  3. Saving throws
	     */
		print( 4,"  5.1.3 Saving throws - ");
		println( 4, "Defender has " 
				+ du.armourSave
				+ " armor saves");
		print( 4,"        Throwing " + wound + " dice:");
		for (i = 0; i < wound; i++) {
			dice = rollDice();
			print( 4," " + dice);
			if (dice >= du.armourSave) {
				print( 4," - Save!,");
			} else {
				print( 4," - no Save,");
				casualties++;
			}
		}
		println( 4, "\n        Total = " + casualties + " casualties");

		return(casualties);
	}
	
	/*********************************************************
	 * unitRemoveCombatUnit() - removes a unit from combat
	 *********************************************************/
	int
	unitRemoveCombatUnit(Unit p, Unit pCombatUnit)
	{
		if (p.combatUnits.contains(pCombatUnit))
		{
			p.combatUnits.remove(pCombatUnit);
			p.numCombatUnits--;
			return(1);
		}
		println( 2, "ERROR - unit "+pCombatUnit.name+" not in combat against "+p.name);
		return(0);
	}
	
	/*********************************************************
	 * unitDelete() - deletes a unit
	 *********************************************************/
	void unitDelete(Unit p)
	{
	    Army army = p.army;

	    println( 2, p.name+" deleted.");
	    army.units.remove(p);
	}

	/*********************************************************
	 * RollDice() - returns 1-6
	 *********************************************************/
	       public int rollDice()
	{
	    return ((int)(Math.random() * 6) + 1);
	}

	
	static public char getChar() throws IOException
	{
		char ch = (char) System.in.read();
		input(); 
		return ch;
	}
	
	static public void input() throws IOException
	{
//		while ( (char) System.in.read() != '\n' ); 
		try
		{
			latch.await();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		latch = new CountDownLatch(1);
	}

	public static void scroll(JComponent c)
	{
	    Rectangle visible = c.getVisibleRect();
	    Rectangle bounds = c.getBounds();
	      
	    visible.y = bounds.height - visible.height;
        visible.x = 0;
	    c.scrollRectToVisible(visible);
	}

	static public void println(int level, String str)
	{
		if (level <= debugLevel ) {
			textArea.append( str+"\n" ); 
			scroll(textArea);
	        txtFrame.repaint();
		}
	}
	
	static public void print(int level, String str)
	{
		if (level <= debugLevel ) {
			textArea.append( str ); 
			scroll(textArea);
	        txtFrame.repaint();

		}
	}
	
	// http://www.rgagnon.com/javadetails/java-0470.html
    public static void write(Object f, String filename) throws Exception{
        XMLEncoder encoder =
           new XMLEncoder(
              new BufferedOutputStream(
                new FileOutputStream(filename)));
        encoder.writeObject(f);
        encoder.close();
    }
    
    public static Object read(String filename) throws Exception {
        XMLDecoder decoder =
            new XMLDecoder(new BufferedInputStream(
                new FileInputStream(filename)));
        Object o = (Object)decoder.readObject();
        decoder.close();
        return o;
    }

		      
    public final static boolean DEBUG = true;

    public static void log(String message)
    {
        if (DEBUG)
        {
            String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();            
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

            System.out.println(className + "." + methodName + "():" + lineNumber + " "+ message);
        }
    }

	public String toString()
	{
		return getClass().getName() + 
				"\n  name = " + name + 
				"\n" + armies.toString()
				;
	}

}
