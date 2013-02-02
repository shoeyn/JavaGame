import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Tank1 extends Sprite{

    private final int START_Y = 300; 
    private final int START_X = 50;
    public int SCREEN_WIDTH = 600;
    private int dx;

    private final String tank = "../images/tank.png";

    public void Player(){

        ImageIcon ii = new ImageIcon(this.getClass().getResource(tank));

        SetImage(ii.getImage());
        SetX(START_X);
        SetY(START_Y);
    }

    public void move() {
      x +=dx;
    	if(x<5){
    		x=5;
    	}
    	else if (x>595){
    		x=595;
    	}
    }

    public void keyPressed(KeyEvent e) {
    	int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT)
        {
            dx = -1;
        }

        if (key == KeyEvent.VK_RIGHT)
        {
            dx = 1;
        }

    }

    public void keyReleased(KeyEvent e) {
    	int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT)
        {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT)
        {
            dx = 0;
        }
    }
}
