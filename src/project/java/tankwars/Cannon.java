package project.java.tankwars;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Cannon {

int x, y;
public static int dx;
double theta;
double thetadif;
Image imgage;

	public Cannon(String imgPath, int x, int y, double theta){
		ImageIcon i = new ImageIcon(imgPath);
		imgage = i.getImage();
		this.x=x;
		this.y=y;
		this.theta = theta;
	}

	public void move(){
		x += dx;
	}
	
	public void rotate(){
		theta += thetadif;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public double getAngle(){
		return theta;
	}
	
	public Image getImage(){
		return imgage;
	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		if(Board.tank1locked == false){
		if(key == KeyEvent.VK_UP) 
			if(theta > -1.57)
				thetadif= -0.01;
			else
				thetadif = 0;
		if(key == KeyEvent.VK_DOWN)
			if(theta < 0)
				thetadif= +0.01;
			else
				thetadif = 0;
		}
		else{
			if(key == KeyEvent.VK_UP) 
				if(theta < -1.57)
					thetadif= 0.01;
				else
					thetadif = 0;
			if(key == KeyEvent.VK_DOWN)
				if(theta > -3.14)
					thetadif= -0.01;
				else
					thetadif = 0;
		}
	}
	
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_UP) 
			thetadif = 0;
		if(key == KeyEvent.VK_DOWN)
			thetadif = 0;
	}

}
