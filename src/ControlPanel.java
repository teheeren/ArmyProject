import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.event.*;

public class ControlPanel extends JFrame {
	private static final long serialVersionUID = 1L;
	JFrame frame1;
	JPanel panel1;
	Game game;
	
	public void createAndShowGUI(Game game1)  {

		panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 20,15));
		game = game1;
		
		frame1 = new JFrame("Control Panel");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setSize(400, 400);
		frame1.setLocation(100, 700);
		frame1.getContentPane().add(panel1);

		// continue button
		JButton b1 = new JButton(" continue ");
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("You clicked the continue button");
			}
		});      
		panel1.add(b1);

		// back button
		JButton b2 = new JButton(" NEXT ");
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("You clicked the NEXT button");
				game.latch.countDown(); 

			}
		});      
		panel1.add(b2);

		frame1.pack();
		frame1.setVisible(true);
		frame1.repaint();

	}
}