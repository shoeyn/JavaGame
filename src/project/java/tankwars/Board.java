package project.java.tankwars;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Board extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
public static Tank tank1;
public static Tank tank2;
Cannon cannon1;
Cannon cannon2;
Shot shot;
//Shot shot2;
Image img;
Image test;
public static Timer time;
public static boolean tank1locked=false;
public static boolean shooting = false;

	public Board(){
		// creating the new objects, defining the timer and starting it
		
		addKeyListener(new AL());
		setFocusable(true);
		
		tank1 = new Tank("res/tank11.png", 100, 474);

		tank2 = new Tank("res/tank22.png", 650, 474);
		
		cannon1 = new Cannon("res/cannon.png", 126, 483, 0);
		
		cannon2 = new Cannon("res/cannon.png", 675, 488, -3.14);
		
		ImageIcon i7 = new ImageIcon("res/background.png");
		img = i7.getImage();
		
		time = new Timer(25, this);
		time.start();
		
	}
	
	//depending on the value of tank1Locked we determine which tank to be moved
	public void actionPerformed(ActionEvent e){
		if(shooting)
			shot.shoot();
		else 
			if(tank1locked == false)
				{cannon1.rotate();
				tank1.move();
				cannon1.move();}
			else
				{cannon2.rotate();
				tank2.move();
				cannon2.move();}
		repaint();
	}
	
	// drawing the graphics
	public void paint(Graphics g){
		super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			
			g2d.drawImage(img, 0, 0, null);
			g2d.rotate(cannon1.getAngle(), cannon1.getX(), cannon1.getY());
			g2d.drawImage(cannon1.getImage(), cannon1.getX(), cannon1.getY(), null);	
			g2d.rotate(-(cannon1.getAngle()), cannon1.getX(), cannon1.getY());
			g2d.rotate(cannon2.getAngle(), cannon2.getX(), cannon2.getY());
			g2d.drawImage(cannon2.getImage(), cannon2.getX(), cannon2.getY(), null);
			g2d.rotate(-(cannon2.getAngle()), cannon2.getX(), cannon2.getY());
			g2d.drawImage(tank1.getImage(), tank1.getX(), tank1.getY(), null);
			g2d.drawImage(tank2.getImage(), tank2.getX(), tank2.getY(), null);
			
			if(shooting){
			g2d.drawImage(shot.getImage(), (int)(shot.getX()), (int)(shot.getY()), null);
			}

	}
	
	
	//detecting events like pressing a button
	private class AL extends KeyAdapter{
		public void keyReleased(KeyEvent e){
			if(tank1locked == false)
				{tank1.keyReleased(e);
				cannon1.keyReleased(e);}
			else
				{tank2.keyReleased(e);
				cannon2.keyReleased(e);}
			
		}
		
		public void keyPressed(KeyEvent e){
			if(shooting){}
			else{
			int key = e.getKeyCode();			
			if(key == KeyEvent.VK_SPACE)
				if(tank1locked)
					{tank1locked = false;}
				else
					{tank1locked = true;}
			
			else if (key == KeyEvent.VK_CONTROL){
					shooting = true;
					if(tank1locked){
						shot = new Shot(cannon2.getX()-22*Math.cos(Math.PI-cannon2.getAngle()), cannon2.getY()+22*Math.sin(Math.PI-cannon2.getAngle())-4, (-cannon2.getAngle()));
					}
					else{
						shot = new Shot(cannon1.getX()+22*Math.cos(-cannon1.getAngle()), cannon1.getY()-22*Math.sin(-cannon1.getAngle()), (-cannon1.getAngle()));	
					}
					ImageIcon i5 = new ImageIcon("res/bullet.png");
					shot.image = i5.getImage();
					}
			
			else if(tank1locked == false)
				{tank1.keyPressed(e);
				cannon1.keyPressed(e);}
			else
				{tank2.keyPressed(e);
				cannon2.keyPressed(e);}
			}
			
		}
	}

}
