package project.java.tankwars;

import java.awt.Image;

public class Shot {

private double x,y,theta;
Image image;
boolean visible = true;
double time = 0;
int direction;

	public Shot(double x, double y, double theta){
		this.x = x;
		this.y = y;
		this.theta = theta;
	}
	
	public void shoot(){
		time += 0.025;
		if(time>5 || y>=503){
			Board.shooting = false;
			System.out.println("DONE");
		}
		else
			x +=    10 * Math.cos(theta) * time;
			y +=  - 10 * Math.sin(theta)*time + 4.5*time*time;
	}

	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public Image getImage(){
		return image;
	}

}
