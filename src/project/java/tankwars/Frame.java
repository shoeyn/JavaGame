package project.java.tankwars;

import javax.swing.JFrame;

public class Frame {


	public static void main(String[] args) {
		JFrame frame = new JFrame("Tank Wars");
		frame.add(new Board());
		frame.setSize(1000, 600);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

}
