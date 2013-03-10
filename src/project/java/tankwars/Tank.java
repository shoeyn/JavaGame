package project.java.tankwars;

import java.awt.Image;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Tank{
	
// make a public boolean that tell us which tank i s currently locked
//boolean locked = false;
public static boolean still = true;
int x, dx, y;
Image image;

	public Tank(String imgPath, int x, int y){
		ImageIcon i1 = new ImageIcon(imgPath);
		image = i1.getImage();
		this.x = x;
		this.y = y;
	}
	
	public void move(){
		x += dx;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public Image getImage(){
		return this.image;
	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_LEFT) 
			if(x<0)
				{dx = 0;
				Cannon.dx = 0;}
			else
				{dx = -1;
				Cannon.dx = -1;}
		if(key == KeyEvent.VK_RIGHT)
			if(x>950)
				{dx = 0;
				Cannon.dx = 0;}
			else
				{dx = 1;
				Cannon.dx = 1;}
	}
	
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_LEFT) 
			{dx = 0;
			Cannon.dx = 0;}
		if(key == KeyEvent.VK_RIGHT)
			{dx = 0;
			Cannon.dx = 0;}
	}

}
