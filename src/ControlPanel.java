import javax.swing.*;
import java.awt.event.*;

public class ControlPanel extends JFrame {
	private static final long serialVersionUID = 1L;
	JFrame frame1;
	
	public void createAndShowGUI()  {

		frame1 = new JFrame("JAVA");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setSize(100, 200);
		frame1.setVisible(true);
		frame1.setLocation(100, 700);
		frame1.repaint();

		// continue button
		JButton b1 = new JButton(" continue ");
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("You clicked the continue button");
			}
		});      
		frame1.getContentPane().add(b1);

		// back button
		JButton b2 = new JButton(" back ");
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("You clicked the back button");
			}
		});      
		frame1.getContentPane().add(b2);

		frame1.pack();
		frame1.setVisible(true);
	}
}