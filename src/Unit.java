import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


public class Unit extends Component {
    /**
	 * 
	 */
	transient private static final long serialVersionUID = 1L;
	String name;
    int numFighters;
    int rank;
    int file;
    int armourSave;
    int standard;
    int musician;
    int champion;
    int battleStandard;
    Profile profile;
    transient Army army;
    int numCombatUnits;
    ArrayList<Unit> combatUnits = new ArrayList<Unit>();
    boolean fleeing;
    boolean destroyed = false;
    Stroke drawingStroke = new BasicStroke(3);
    Float x, y;
    public int x_speed, y_speed;
    Point loc = new Point();
	Rectangle2D rect = new Rectangle2D.Double(60, 70, 120, 80);
	float direction = 0;
    boolean selected = false;
    boolean rotating = false;
    boolean attacking = false;
    boolean moving = false;
    boolean wheeling = false;

     
	public Unit(String name, int numFighters, int file, String profileName, Army army) {
		super();
		this.name = name;
		this.numFighters = numFighters;
		this.file = file;
		rank = ((numFighters-1) / file) +1;
		this.profile =  Character.findProfile(profileName);
		this.setArmy(army);
		this.armourSave = 3;
//        repaint();
		// test

	}
	
	public void redress() {
		rank = ((numFighters-1) / file) +1;
	}
	public void setLoc(int x, int y) {
		this.loc.x = x;
		this.loc.y = y;
		this.x = (float) x;
		this.y = (float) y;
	}
	
	public void setArmy(Army army) {
		this.army = army;
		army.addUnit(this);
	}
	public int getNumCombatUnits() {
		return numCombatUnits;
	}
	public void setCombatUnits(Unit combatUnits) {
		numCombatUnits++;
		this.combatUnits.add(combatUnits);
	}

	public void pause(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void moveDist(float dist)
	{
		float x2;
		float y2;

		x2 = (float) ((Math.cos( Math.toRadians(direction-90)) * dist) + x);
		y2 = (float) ((Math.sin( Math.toRadians(direction-90)) * dist) + y);
		Game.log(" ");

		try {
			moveTo(x2, y2, 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void moveDist(int dist)
	{
		float x2;
		float y2;

		x2 = (int)(Math.cos( Math.toRadians(direction-90)) * dist * 10) + x;
		y2 = (int)(Math.sin( Math.toRadians(direction-90)) * dist * 10) + y;
		Game.log(" ");

		try {
			moveTo(x2, y2, 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void wheelRight(float a)
	{
		double h;
        float m = (file * 10)/2;          // middle

		Game.log("m = "+m);
		Game.log("x = "+loc.x+", y = "+loc.y);

        h = m * Math.sin(Math.toRadians(a)) / (Math.sin ((180 - Math.toRadians(a))/2) );
        h /= 10;
		Game.log("distance = "+h);

	    direction = (direction + a/2) % 360;
	    moveDist((int)h);
	    direction = (direction + a/2) % 360;	    moveDist(1);
        repaint();

		Game.log("x = "+x+", y = "+y+" , direction"+direction);

//	    loc.x += Math.asin(h);
//	    loc.y += Math.acos(h);
	}

	public void rotate(int angle)
	{
	    direction = (direction + angle) % 360;
	    moveDist(rank);
	}

	public void moveTo(float x2, float y2, int speed) throws InterruptedException
	{
		float dx = x2-x;
		float dy = y2-y;
		//		float dy = loc.y-y2;
		
		if (dx == 0)
			dx = (float) .0001;
		if (dy == 0)
			dy = (float) .0001;
		float slope = dy/dx;
//		float x, y;
		float b =(y2-slope*x2);

		if (slope == 0)
			slope = (float).00001;
		
		if (Math.abs(slope) < 1)
		{
			// we're changing by x
			if (x < x2)
			{

				// we're incrementing from x to x2
			    for (; x < x2; x++)
			    {
			    	y = (x * slope) + b;
			    	Game.table.validate();
			    	Game.table.repaint();
			    	pause(speed);
			    }
			}
			else
			{
				// we're decrementing from x to x2
			    for (; x > x2; x--)
			    {
			    	y = ((float)x * slope) + b;
			    	Game.table.validate();
			    	Game.table.repaint();
			    	pause(speed);
			    }
			}
		}
		else
		{
			// we're changing by y
			if (y < y2)
			{
				// we're incrementing y to y2
			    for (; y < y2; y++)
			    {
			    	x = ((float)y - b) / slope;
			    	Game.table.validate();
			    	Game.table.repaint();
			    	pause(speed);
			    }
			}
			else
			{
				// we're decrementing y to y2
			    for (; y > y2; y--)
			    {
			    	x = ((float)y - b) / slope;
			    	Game.table.validate();
			    	Game.table.repaint();
			    	pause(speed);
			    }
			}
		}

/*
		// detect collision with other units
		for (Unit u : units)
		{
			if (b != this &&
			    b.intersects(r))
			{
				// on collision, the balls swap speeds
				int tempx = x_speed;
				int tempy = y_speed;
				x_speed = b.x_speed;
				y_speed = b.y_speed;
				b.x_speed = tempx;
				b.y_speed = tempy;
				break;
			}
		}
		*/
		/*
		if (super.x < 0)
		{
			super.x = 0;
			x_speed = Math.abs(x_speed);
		}
		else if (super.x > width - d)
		{
			super.x = width - d;
			x_speed = -Math.abs(x_speed);
		}
		if (super.y < 0)
		{
			super.y = 0;
			y_speed = Math.abs(y_speed);
		}
		else if (super.y > height - d)
		{
			super.y = height - d;
			y_speed = -Math.abs(y_speed);
		}
		super.x += x_speed;
		super.y += y_speed;
		*/
		
		//  check for off the table
		if ((x <= 0) || (x >= Game.width)) {
			Game.log(name+" off the table "+x+ " Destroyed ");
			destroyed = true;
		}
		if ((y <= 0) || (y >= Game.height)) {
			Game.log(name+" off the table "+y+ " Destroyed ");
			destroyed = true;
		}
	}
	
	public void paint(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D)g;
		Rectangle2D rec = new Rectangle2D.Double();
		
        loc.x = Math.round(x);
        loc.y = Math.round(y);
		if (destroyed)
			return;
				
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(drawingStroke);
		
		g2.translate(loc.x, loc.y);
		g2.rotate(Math.toRadians(direction));
		g2.translate(-loc.x, -loc.y);
		
		
		int r = file * 10;    // radius
        int m = r/2;          // middle
		int pm = profile.movement * 10;
		
		if (attacking)
		{
		    pm *= 2;
		}

        if (wheeling)
        {
        	g2.draw(new Arc2D.Double((double) loc.x - r - m,
        			(double) loc.y - r,
        			(double) r * 2,
        			(double) r * 2,
        			(double) 0,
        			(double) (pm * 180) / (Math.PI * r),
        			Arc2D.OPEN));


        	g2.draw(new Arc2D.Double((double) loc.x - m,
        			(double) loc.y - r,
        			(double) r * 2,
        			(double) r * 2,
        			(double) 180,
        			(double) -((pm * 180) / (Math.PI * r)),
        			Arc2D.OPEN));
        }
        
        if (moving)
        {
		    paintArrow(g2, loc.x, loc.y, loc.x, loc.y-pm );
        }
        else
        {
		    paintArrow(g2, loc.x, loc.y, loc.x, loc.y-10 );
        }
		
		rect.setRect(loc.x-m, loc.y , (numFighters / rank)*10, (numFighters / file)*10);

		for( int i = 0; i<numFighters; i++) {
			int f = (i % file);
			int rk =  (i / file);
			rec.setRect(loc.x-m + f*10, loc.y + rk*10, 10, 10);
			g2.setPaint(Color.black);
			g2.draw(rec);
			g2.setPaint(army.color);
			g2.fill(rec);
		}
	}
	
	private void paintArrow(Graphics2D g, int x0, int y0, int x1,int y1)
	{
		int deltaX = x1 - x0;
		int deltaY = y1 - y0;
		double frac = 0.1;

		g.drawLine(x0,y0,x1,y1);
		g.drawLine(x0 + (int)((1-frac)*deltaX + frac*deltaY),
				y0 + (int)((1-frac)*deltaY - frac*deltaX),
				x1, y1);
		g.drawLine(x0 + (int)((1-frac)*deltaX - frac*deltaY),
				y0 + (int)((1-frac)*deltaY + frac*deltaX),
				x1, y1);
	}
	
	Boolean checkIntersection( int x, int y )
	{
        AffineTransform af1 = new AffineTransform();
        
        af1.translate(loc.x, loc.y);
		af1.rotate(Math.toRadians(direction) );
		af1.translate(-loc.x, -loc.y);
        Rectangle2D rectRotated = af1.createTransformedShape(rect).getBounds2D();
		return ( rectRotated.contains(x, y));
	}
    
	public String toString()
	{
		return getClass().getName() + 
				" - " + name +
				"\n--------------------------------------" +
				"\n  numFighters = " + numFighters +
        		"\n  rank = " + rank +
        		"\n  file = " + file +
        		"\n  armourSave = " + armourSave +
        		"\n  standard = " + standard +
        		"\n  musician = " + musician +
        		"\n  champion = " + champion +
        		"\n  battleStandard = " + battleStandard +
        		"\n  army = " + army.name +
        		"\n  numCombatUnits = " + numCombatUnits +
//        		"\n  combatUnits = " + combatUnits +
        		"\n  fleeing = " + fleeing +
		        "\n  profile = " + profile
		        ;
    }

}
