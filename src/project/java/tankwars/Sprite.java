package project.java.tankwars;

import java.awt.Image;

public class Sprite {
  
	protected int x;
	protected int y;
	int power;
	double angle;
	boolean visible;
	boolean dead;
	private Image image;

	public int getX(){
		return this.x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getY(){
		return this.y;
	}

	public void SetY(int y){
		this.y = y;
	}

	public boolean GetDead(){
		return this.dead;
	}

	public void SetDead(boolean dead){
		this.dead = dead;
	}

	public void SetVisible(boolean visible){
		this.visible = visible;
	}

	public boolean IsVisible(){
		return this.visible;
	}

	public void Die(){
		this.visible = false;
	}

	public void SetImage(Image image){
		this.image = image;
	}

	public Image getImage(){
		return this.image;
		
	}

}
