package tankwars;

import java.awt.Image;

public class Sprite {
  
	int x;
	int y;
	int power;
	double angle;
	boolean visible;
	boolean dead;
	Image image;
	
	public int GetX(){
		return this.x;
	}
	
	public void SetX(int x){
		this.x = x;
	}
	
	public int GetY(){
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
	
	public Image GetImage(){
		return this.image;
	}

}
