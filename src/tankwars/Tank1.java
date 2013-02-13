package tankwars;

import java.awt.Image;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Tank1{
	
// make a public boolean that tell us which tank i s currently locked
public static boolean tank1Locked = false;
int x, dx, y;
Image image;

	public Tank1(){
		ImageIcon i = new ImageIcon("res/tank1.png");
		image = i.getImage();
		x = 100;
		y = 474;
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
		
		if(key == KeyEvent.VK_SPACE)
			if(tank1Locked)
				tank1Locked = false;
			else
				tank1Locked = true;
		if(key == KeyEvent.VK_LEFT) 
			if(x<0)
				dx = 0;
			else
				dx = -1;
		if(key == KeyEvent.VK_RIGHT)
			if(x>730)
				dx = 0;
			else
				dx = 1;
	}
	
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_LEFT) 
			dx = 0;
		if(key == KeyEvent.VK_RIGHT)
			dx = 0;
	}

}
