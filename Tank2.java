package project.java.tankwars;

import java.awt.Image;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Tank2{
int x2, dx2, y2;
Image image;

	public Tank2(){
		ImageIcon i = new ImageIcon("res/tank2.png");
		image = i.getImage();
		x2 = 650;
		y2 = 474;
	}
	
	// moves the object
	public void move(){
		x2 += dx2;

	}
	
	public int getX(){
		return this.x2;
	}
	
	public int getY(){
		return this.y2;
	}
	
	public Image getImage(){
		return this.image;
	}
	
	// when a key is pressed, depending on whether tank1 is locked we move either tank1 or tank2. the same goes for keyReleased
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_SPACE)
			if(Tank1.tank1Locked)
				Tank1.tank1Locked = false;
			else
				Tank1.tank1Locked = true;
		if(key == KeyEvent.VK_LEFT) 
			if(x2<0)
				dx2 = 0;
			else
				dx2 = -1;
		if(key == KeyEvent.VK_RIGHT)
			if(x2>730)
				dx2 = 0;
			else
				dx2 = 1;
	}
	
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_LEFT) 
			dx2 = 0;
		if(key == KeyEvent.VK_RIGHT)
			dx2 = 0;
	}

}
