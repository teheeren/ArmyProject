import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JComponent;


public class BattleField extends JComponent
{
	private static final long serialVersionUID = 1L;

	ArrayList<Shape> shapes = new ArrayList<Shape>();
	Unit unitSelected;
	Point startDrag, endDrag;
	ArrayList<Unit> uA = new ArrayList<Unit>();

	public BattleField(final Game game)
	{
/*
		JTextField typingArea = new JTextField(20);
        typingArea.addKeyListener(new KeyListener()
		  {
		    public void keyTyped(KeyEvent e) 
		    {
		        System.out.println("keyTyped ==========================");
		    	int id = e.getID();
		        if (id == KeyEvent.KEY_TYPED) {
		            char c = e.getKeyChar();
//			    	Game.log("key character = '" + c + "'");
			    	Game.println(2, "key character = '" + c + "'");
		        }
			    repaint();
			}

			public void keyPressed(KeyEvent arg0) {
		        System.out.println("keyPressed ==========================");
			}

			public void keyReleased(KeyEvent arg0) {
				System.out.println("keyReleased ==========================");
            }
		  } );
*/
		
		this.addMouseListener(new MouseAdapter()
		  {
		    public void mousePressed(MouseEvent e)
		    {
		    	Game.log(""+e.getX());
		    	Game.log(""+e.getY());

				for (Army a : game.armies) {
					for (Unit u : a.units) {
					if ( u.checkIntersection(e.getX(), e.getY()))
						{
						    unitSelected = u;
			            	Game.log(unitSelected.name + " selected!");
							break; 
						}
					}
				}

		  	  startDrag = new Point(e.getX(), e.getY());
			  endDrag = startDrag;
			  repaint();
			}

		    public void mouseReleased(MouseEvent e)
			{
/*
   		  	  Shape r = makeRectangle(
			   	          startDrag.x, startDrag.y,
						      e.getX(), e.getY());
				  shapes.add(r);
 */
			  startDrag = null;
			  endDrag = null;
			  repaint();
			}
		  } );

		this.addMouseMotionListener(new MouseMotionAdapter()
		{
		  public void mouseDragged(MouseEvent e)
		  {
		    endDrag = new Point(e.getX(), e.getY());
			repaint();
		  }
		} );
		
	}

	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;

		// turn on antialiasing
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);

		// draw background grid
		g2.setPaint(Color.LIGHT_GRAY);
		for (int i = 0; i < getSize().width; i += 10)
		{
			Shape line = new Line2D.Float(
				i, 0, i, getSize().height);
			g2.draw(line);
		}

		for (int i = 0; i < getSize().height; i += 10)
		{
			Shape line = new Line2D.Float(
				0, i, getSize().width, i);
			g2.draw(line);
		}

		// draw the shapes
		Color[] colors = {Color.RED, Color.BLUE, Color.PINK,
		    Color.YELLOW, Color.MAGENTA, Color.CYAN };
		int colorIndex = 0;

		g2.setStroke(new BasicStroke(2));
		g2.setComposite(AlphaComposite.getInstance(
			AlphaComposite.SRC_OVER, 0.50f));

		for (Shape s : shapes)
		{
			g2.setPaint(Color.BLACK);
			g2.draw(s);
			g2.setPaint(colors[(colorIndex++)%6]);
			g2.fill(s);
		}

		// paint the temporary rectangle
		if (startDrag != null && endDrag != null)
		{
			g2.setPaint(Color.LIGHT_GRAY);
			Shape r = makeRectangle(startDrag.x, startDrag.y,
				endDrag.x, endDrag.y);
			g2.draw(r);
		}
	}

	private Rectangle2D.Float makeRectangle(
		int x1, int y1, int x2, int y2)
	{
		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);
		int width = Math.abs(x1 - x2);
		int height = Math.abs(y1 - y2);

		return new Rectangle2D.Float(
			x, y, width, height);
	}
}
