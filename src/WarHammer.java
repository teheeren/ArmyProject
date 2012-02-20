import java.io.IOException;
import java.awt.Color;


public class WarHammer
{	
	public static Game game;
	
    public static void main(String[] args) throws IOException, InterruptedException 
    {
    	Army theo = new Army("Theo", Color.red);
    	Army alex = new Army("Alex", Color.blue);
    	Unit uT, uA;

        // Unit test
        game = new Game("Theo vs.Alex");
        
        game.addArmy(theo);
        game.addArmy(alex);
       //----------------------------------------------------------------------        
        uT = new Unit("Dwarf Warriers #1", 15, 5, "Warrior", theo);
        uT.setLoc(150,120);
//        uT.direction = 180;
        uT.direction = 45;
        uT.armourSave = 6; // dwarfs wear chainmail
        uT.standard = 1;
        uT.musician = 1;
        uT.champion = 1;
//        uT.profile.movement = 6;
        
		Game.table.add(uT);
		Game.table.validate();
		
//		uT.moveTo(200,200,100);
		
		//----------------------------------------------------------------------        
        uA = new Unit("Night Goblins Spearmen #1", 20, 5, "Night Goblins", alex);  
        uA.setLoc(100, 120+100);
//        uA.direction = 0;
        uA.standard = 1;
        uA.musician = 1;
        uA.champion = 1;
        
        Game.table.add(uA);
        Game.table.validate();

        uT.setCombatUnits(uA);
        uA.setCombatUnits(uT);

        //----------------------------------------------------------------------        
        uT = new Unit("Dwarf Warriers #2", 15, 5, "Warrior", theo);  
        uT.setLoc(250, 120+100);
        uT.direction = 180;
        uT.armourSave = 6; // dwarfs wear chainmail
        uT.standard = 1;
        uT.musician = 1;
        uT.champion = 1;
        Game.table.add(uT);
        Game.table.validate();

        uA = new Unit("Night Goblins Spearmen #2", 20, 5, "Night Goblins", alex);  
        uA.setLoc(200, 120);
        uA.standard = 1;
        uA.musician = 1;
        uA.champion = 1;
        Game.table.add(uA);
        Game.table.validate();
        Game.table.repaint();
        
        uT.setCombatUnits(uA);
        uA.setCombatUnits(uT);
        
/*
        System.out.println("==============================================");
        System.out.println(theo);      
        System.out.println("==============================================");
        System.out.println(uT);
        System.out.println("==============================================");
        System.out.println(uT.combatUnits);
        System.out.println("==============================================");
        System.out.println(uT.profile);
*/
        game.play();
        }


}
