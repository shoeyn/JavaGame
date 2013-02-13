package tankwars;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Board extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
Tank1 tank1;
Tank2 tank2;
Image img;
Timer time;

	public Board(){
		// creating the new objects, defining the timer and starting it
		addKeyListener(new AL());
		tank1 = new Tank1();
		tank2 = new Tank2();
		setFocusable(true);
		ImageIcon i = new ImageIcon("res/background.png");
		img = i.getImage();
		time = new Timer(15, this);
		time.start();
		
	}
	
	//depending on the value of tank1Locked we determine which tank to be moved
	public void actionPerformed(ActionEvent e){
		if(Tank1.tank1Locked == false)
			tank1.move();
		else
			tank2.move();
		repaint();
	}
	
	// drawing the graphics
	public void paint(Graphics g){
		super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(img, 0, 0, null);
			g2d.drawImage(tank1.getImage(), tank1.getX(), tank1.getY(), null);
			g2d.drawImage(tank2.getImage(), tank2.getX(), tank2.getY(), null);
			
	}
	
	private class AL extends KeyAdapter{
		public void keyReleased(KeyEvent e){
			if(Tank1.tank1Locked == false)
				tank1.keyReleased(e);
			else
				tank2.keyReleased(e);
		}
		
		public void keyPressed(KeyEvent e){
			if(Tank1.tank1Locked == false)
				tank1.keyPressed(e);
			else
				tank2.keyPressed(e);
			
			
		}
	}

}
