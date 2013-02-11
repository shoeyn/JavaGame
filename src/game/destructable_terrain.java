package game;

import processing.core.*; 

import java.util.*; 

@SuppressWarnings("serial")
public class destructable_terrain extends PApplet {

/* global variables */

// the terrain contains the bitmap for all the static pixels
Terrain terrain;
// the background image is drawn behind the terrain
PImage bg;

// physics and rendering engines
Physics physics; // has a list of all physics objects, and uses their velocity to move them
Renderer renderer; // has a list of all renderable objects, and calls their draw() method

Player player;

// translation, used to keep track of where the camera is
int translateX;
int translateY;

// setup(), called before any looping is done
public void setup() {
  size(600,450, JAVA2D);
  
  if (frame != null)
  {
      frame.setTitle("Tank Wars");
  }
  
  // load our images for terrain and background
  bg = loadImage("bg/sky-blurry.png");
  
  terrain = new Terrain(loadImage("maps/more-trees.png"), 2); // new Terrain(image, destructionRes)
  
  // initialize the physics and rendering engines
  physics = new Physics();
  renderer = new Renderer();
 
  // create the player
  player = new Player(100,100);
  physics.add(player);
  renderer.add(player);
}
// Draw loop
public void draw() {
  // update physics
  physics.update();
  
  // load changes into the terrain
  terrain.update();


  /* Rendering */
  // first move our perspective to where the player is
  translateX = (int)constrain(width/2 - player.x, width - terrain.width(), 0);
  translateY = (int)constrain(height/2 - player.y, height - terrain.height(), 0);
  translate(translateX,
            translateY);

  // render the background
  background(255);
  image(bg,translateX * -0.8f,
           translateY * -0.8f);

  // draw the terrain
  terrain.draw(0,0);

  // show terrain normals
  //showNormals();
    
  // draw everything else
  renderer.draw();
}
/* Bullet */
// Acts similarly to PhysicsPixel
class Bullet implements PhysicsObj, RenderObj {
  float x,y; // position
  float lastX, lastY; // last position
  float velX, velY; // velocity
  
  // Constructor
  Bullet(float x, float y, float vX, float vY) {
    this.x = x; this.y = y;
    lastX = x; lastY = y;
    velX = vX; velY = vY;
  }
  
  // methods implemented as a PhysicsObj
  public float getX() { return x; }
  public float getY() { return y; }
  public float getVX() { return velX; }
  public float getVY() { return velY; }
  public void setX(float pX) { x = pX; }
  public void setY(float pY) { y = pY; }
  public void setVX(float vX) { velX = vX; }
  public void setVY(float vY) { velY = vY; }

  public void checkConstraints() {
    int[] collision = rayCast((int)lastX, (int)lastY, (int)x, (int)y);
    if (collision.length > 0) {
      renderer.remove(this);
      physics.remove(this);
      explode(collision[2], collision[3], 60);
    }
    lastX = x;
    lastY = y;
  }
  
  public void draw() {
    fill(0);
    rect(x,y,3,3);
  }
}
/* Controls */
public void keyPressed() { 
  if (key == 'w' || key == 'W')
    player.jump();
  if (key == 'a' || key == 'A')
    player.moveLeft();
  if (key == 'd' || key == 'D')
    player.moveRight();
  println(key);
}
public void keyReleased() {
  if (key == 'a' || key == 'A')
    player.stopLeft();
  if (key == 'd' || key == 'D')
    player.stopRight();
}
public void mousePressed() {
  if (mouseButton == LEFT)
    player.shoot(); 
  else if (mouseButton == RIGHT)
    player.shootAlt();
}
public void mouseReleased() {
  if (mouseButton == LEFT)
    player.stopShooting();
  else if (mouseButton == RIGHT)
    player.stopShootingAlt();  
}
public float getMouseX() {
  return mouseX - translateX;
}
public float getMouseY() {
  return mouseY - translateY;
}

/* Explode */
// Creates an "explosion" by finding all pixels near a point and launching them away
public void explode(int xPos, int yPos, float radius) {
  float radiusSq = radius * radius;
  
  // loop through every x from xPos-radius to xPos+radius
  for (int x = xPos - (int)radius; x < xPos + (int)radius; x += terrain.destructionRes) {
    
    // first make sure that the x is within terrain's boundaries
    if (x > 0 && x < terrain.width()) { 
      
      // next loop through every y pos in this x column
      for (int y = yPos - (int)radius; y < yPos + (int)radius; y += terrain.destructionRes) {
        
        if (y > 0 && y < terrain.height()) { // boundary check
        
          // first determine if this pixel (or if any contained within its square area) is solid
          int solidX = 0,solidY = 0;
          boolean solid = false;
          // loop through every pixel from (xPos,yPos) to (xPos + destructionRes, yPos + destructionRes)
          // to find whether this area is solid or not
          for (int i = 0; i < terrain.destructionRes && !solid; i++) {
            for (int j = 0; j < terrain.destructionRes && !solid; j++) {
              if (terrain.isPixelSolid(x+i,y+j)) {
                solid = true;
                solidX = x+i;
                solidY = y+j;
              }
            }
          }
          if (solid) { // we know this pixel is solid, now we need to find out if it's close enough
            float xDiff = x - xPos;
            float yDiff = y - yPos;
            float distSq = xDiff * xDiff + yDiff * yDiff;
            // if the distance squared is less than radius squared, then it's within the explosion radius
            if (distSq < radiusSq) {
              // finally calculate the distance
              float distance = sqrt(distSq);
              
              // the speed will be based on how far the pixel is from the center
              float speed = 800 * (1 - distance/radius);
              
              if (distance == 0)
                distance = 0.001f; // prevent divide by zero in next two statements
                
              // velocity
              float velX = speed * (xDiff + random(-10,10)) / distance; 
              float velY = speed * (yDiff + random(-10,10)) / distance;
              
              // create the dynamic pixel
              DynamicPixel pixel = new DynamicPixel(terrain.getColor(solidX, solidY), x,y, velX, velY, terrain.destructionRes);
              pixel.stickiness = 800;
              physics.add(pixel);
              renderer.add(pixel);
              
              // remove the static pixels
              for (int i = 0; i < terrain.destructionRes; i++) {
                for (int j = 0; j < terrain.destructionRes; j++) {
                  terrain.removePixel(x+i,y+j);
                }
              }
            }
          }
        }
      }  
    }
  }
}
/* PhysicsObj */
// Any object that will need motion integrated will implement this
// these methods allows the Physics class to forward the object's position using its velocity
interface PhysicsObj {
  public float getX();
  public float getY();
  public float getVX();
  public float getVY();
  public void setX(float pX);
  public void setY(float pY);
  public void setVX(float vX);
  public void setVY(float vY);
  public void checkConstraints();
}
/* Physics */
// Apply motion to objects
class Physics {
  long previousTime;
  long currentTime;
  final int fixedDeltaTime = 16;
  float fixedDeltaTimeSeconds = (float)fixedDeltaTime / 1000.0f;
  int leftOverDeltaTime = 0;

  ArrayList<PhysicsObj> objects;
  
  // Constructor
  Physics() {
    objects = new ArrayList<PhysicsObj>();
  }
  
  public void add(PhysicsObj obj) {
    objects.add((int)random(objects.size()),obj);  
  }
  public void remove(PhysicsObj obj) {
    objects.remove(obj);  
  }
  
  // integrate motion
  public void update() {
    // This game uses fixed-sized timesteps.
    // The amount of time elapsed since last update is split up into units of 16 ms
    // any left over is pushed over to the next update
    // we take those units of 16 ms and update the simulation that many times.
    // a fixed timestep will make collision detection and handling (in the Player class, esp.) a lot simpler
    // A low framerate will not compromise any collision detections, while it'll still run at a consistent speed. 
    
    currentTime = millis();
    long deltaTimeMS = currentTime - previousTime; // how much time has elapsed since the last update
    
    previousTime = currentTime; // reset previousTime
    
    // Find out how many timesteps we can fit inside the elapsed time
    int timeStepAmt = (int)((float)(deltaTimeMS + leftOverDeltaTime) / (float)fixedDeltaTime); 
    
    // Limit the timestep amount to prevent freezing
    timeStepAmt = min(timeStepAmt, 1);
  
    // store left over time for the next frame
    leftOverDeltaTime = (int)deltaTimeMS - (timeStepAmt * fixedDeltaTime);
    
    for (int iteration = 1; iteration <= timeStepAmt; iteration++) {
      for (int i = 0; i < objects.size(); i++) { // loop through every PhysicsObj
      
        PhysicsObj obj = objects.get(i);
        // get their velocity
        float velX = obj.getVX();
        float velY = obj.getVY();
     
        // add gravity
        velY += 980 * fixedDeltaTimeSeconds;
        obj.setVY(velY);
        
        // Always add x velocity
        obj.setX(obj.getX() + velX * fixedDeltaTimeSeconds);
        
        // if it's a player, only add y velocity if he's not on the ground.
        if (obj instanceof Player) {
          if (!(((Player)obj).onGround && velY > 0))
            obj.setY(obj.getY() + velY * fixedDeltaTimeSeconds);  
        } 
        else
          obj.setY(obj.getY() + velY * fixedDeltaTimeSeconds);
        
        // allow the object to do collision detection and other things
        obj.checkConstraints();
      }
    }
  }
}
/* Dynamic Pixel */
// Pixels in motion
class DynamicPixel implements PhysicsObj, RenderObj {
  float x,y; // location
  float lastX, lastY; // last location, used for our "ray casting"
  
  float velX, velY;
  
  float stickiness = 1500; // minimum speed for this pixel to stick
  float bounceFriction = 0.85f; // scalar multiplied to velocity after bouncing
  
  int col; // color of the pixel
 
  int size = 1; // width and height of the pixel
  
  DynamicPixel(int c, float x, float y, float vX, float vY, int size) {
    col = c;
    this.x = x; this.y = y;
    lastX = x; lastY = y;
    velX = vX; velY = vY;
    
    this.size = size;
  }
  
  // Render the pixel (method implemented as a RenderObj)
  public void draw() {
    fill(col);
    noStroke();
    rect(x,y, size, size);
  }
  
  // Methods implemented as a PhysicsObj
  public float getX() { return x; }
  public float getY() { return y; }
  public float getVX() { return velX; }
  public float getVY() { return velY; }
  public void setX(float pX) { x = pX; }
  public void setY(float pY) { y = pY; }
  public void setVX(float vX) { velX = vX; }
  public void setVY(float vY) { velY = vY; }
  
  // CheckConstraints, also implemented as a PhysicsObj
  public void checkConstraints() {
    // Find if there's a collision between the current and last points
    int[] collision = rayCast((int)lastX, (int)lastY, (int)x, (int)y);
    if (collision.length > 0) 
      collide(collision[0], collision[1], collision[2], collision[3]);
      
    // reset last positions
    lastX = x;
    lastY = y;
    
    // Boundary constraints... only remove the pixel if it exits the sides or bottom of the map
    if (x > terrain.width() || x < 0 || y > terrain.height()) {
      renderer.remove(this);
      physics.remove(this);
    }
  }
  
  /* Collide */
  // called whenever checkConstraints() detects a solid pixel in the way
  public void collide(int thisX, int thisY, int thatX, int thatY) {
    // first determine if we should stick or if we should bounce
    if (velX * velX + velY * velY < stickiness * stickiness) { // if the velocity's length is less than our stickiness property, add the pixel
      // draw a rectangle by looping from x to size, and from y to size
      for (int i = 0; i < size; i++) { 
        for (int j = 0; j < size; j++) {
          terrain.addPixel(col, thisX+i, thisY+j);
        }  
      }
      // remove this dynamic pixel
      renderer.remove(this);
      physics.remove(this);
    }
    else { // otherwise, bounce
      // find the normal at the collision point
      
      // to do this, we need to reflect the velocity across the edge normal at the collision point
      // this is done using a 2D vector reflection formula ( http://en.wikipedia.org/wiki/Reflection_(mathematics) )
      
      float pixelNormal[] = terrain.getNormal((int)thatX, (int)thatY);
      
      float d = 2 * (velX * pixelNormal[0] + velY * pixelNormal[1]);
      
      velX -= pixelNormal[0] * d;
      velY -= pixelNormal[1] * d;
      
      // apply bounce friction
      velX *= bounceFriction;
      velY *= bounceFriction;
      
      // reset x and y so that the pixel starts at point of collision
      x = thisX;
      y = thisY;
    }
  }
}
/* Player object */
// Our little box that runs around the map
class Player implements PhysicsObj, RenderObj {
  float x,y;
  float velX, velY;
  
  // variables to track whether or not the user is pressing A/D
  boolean goLeft;
  boolean goRight;
  
  // Are we shooting?
  boolean shooting;
  boolean shootingAlt;
  
  // last time (ms) a bullet was shot, used to limit the firing rate
  long lastShot;
  
  // variables for physics
  boolean onGround; // are we allowed to jump?
  boolean topBlocked;
  
  int playerWidth, playerHeight;
  
  // Constructor
  Player(int x, int y) {
    this.x = x; this.y = y;
    velX = 0; velY = 0; // set the initial velocity to 0
    
    // initialize the player as a 15x15 px box
    playerWidth = 15; 
    playerHeight = 15;
  }
  
  // Shooting toggles
  public void shoot() {
    if (!shootingAlt)
      shooting = true;
  }
  public void stopShooting() {
    shooting = false;  
  }
  public void shootAlt() {
    if (!shooting)
      shootingAlt = true;
  }
  public void stopShootingAlt() {
    shootingAlt = false;  
  }
  
  // jump
  public void jump() {
    if (onGround && !topBlocked && velY > -500)
      velY -= 500;
  }
  
  // moving toggles
  public void moveLeft() {
    goLeft = true;
  }
  public void moveRight() {
    goRight = true;
  }
  public void stopLeft() {
    goLeft = false;
  }
  public void stopRight() {
    goRight = false;
  }
  
  // draw - implemented as a RenderObj
  public void draw() {
    stroke(0);
    fill(255);
    rect(x - playerWidth/2, y - playerHeight/2, playerWidth, playerHeight);  
  }
  
  // checkConstraints - implemented as a PhysicsObj
  public void checkConstraints() {
    // controls
    
    // shooting
    if (shooting || shootingAlt) {
      // Primary fire happens every 200 ms, alternate fire happens every 25 ms.
      if (!(shooting && millis() - lastShot < 150) && !(shootingAlt && millis() - lastShot < 15)) {
        // Create a vector between the player and the mouse, then normalize that vector (to change its length to 1)
        // after multiplying by the desired bullet speed, we get how fast along each axis we want the bullet to be traveling
        float diffX = getMouseX() - x;
        float diffY = getMouseY() - y;
        float len = sqrt(diffX * diffX + diffY * diffY);
        if (shooting) {
          // create the bullet at 2000 px/sec, and add it to our Physics and Rendering lists
          Bullet bullet = new Bullet(x, y, 2000 * diffX / len, 2000 * diffY / len);
          physics.add(bullet);
          renderer.add(bullet);
        }
        else {
          // Change our color from RGB to HSB so we can cycle through hues
          colorMode(HSB,255); 
          for (int i = 0; i < 150; i++) { // create 150 particles
            DynamicPixel pixel = new DynamicPixel(color((int)(((millis()/5000f) * 255f) % 255), 255, 255), // color
              player.x, player.y, // position
              random(-50,50) + random(1500, 2500) * diffX / len, random(-50,50) + random(1500, 2500) * diffY / len, // speed
              terrain.destructionRes); // size
            physics.add(pixel);
            renderer.add(pixel);
          }
          colorMode(RGB,255);
        }
        // reset lastShot
        lastShot = millis();
      }
    }
    
    // movement
    if (goLeft) {
      if (velX > -500)
        velX -= 40;
    }
    else if (velX < 0)
      velX *= 0.8f; // slow down side-ways velocity if we're not moving left
      
    if (goRight) {
      if (velX < 500)
        velX += 40;
    }
    else if (velX > 0)
      velX *= 0.8f;
    
    // Collision detection/handling
    // Loop along each edge of the square until we find a solid pixel
    // if there is one, we find out if there's any adjacent to it (loop perpendicular from that pixel into the box)
    // Once we hit empty space, we move the box to that empty space

    onGround = false;
    for (int bottomX = (int)x - playerWidth/2; bottomX <= (int)x + playerWidth/2; bottomX++) {
      if (terrain.isPixelSolid(bottomX, (int)y + playerHeight/2 + 1) && (velY > 0)) {
        onGround = true;
        for (int yCheck = (int)y + playerHeight/4; yCheck < (int)y + playerHeight/2; yCheck++) {
          if (terrain.isPixelSolid(bottomX, yCheck)) {
            y = yCheck - playerHeight/2;
            break;
          }
        }
        if (velY > 0)
          velY *= -0.25f;
      }
    }
    
    topBlocked = false;
    // start with the top edge
    for (int topX = (int)x - playerWidth/2; topX <= (int)x + playerWidth/2; topX++) {
      if (terrain.isPixelSolid(topX, (int)y - playerHeight/2 - 1)) { // if the pixel is solid
        topBlocked = true;
        if (velY < 0) {
          velY *= -0.5f; 
        }
      } 
    }
    // loop left edge
    if (velX < 0) {
      for (int leftY = (int)y - playerHeight/2; leftY <= (int)y + playerHeight/2; leftY++) {
        if (terrain.isPixelSolid((int)x - playerWidth/2, leftY)) {
          // next move from the edge to the right, inside the box (stop it at 1/4th the player width)
          for (int xCheck = (int)x - playerWidth/4; xCheck < (int)x - playerWidth/2; xCheck--) {
            if (terrain.isPixelSolid(xCheck, leftY)) {
              x = xCheck + playerWidth/2; // push the block over 
              break; 
            }
          }
          if (leftY > y && !topBlocked) {
            y -= 1;
          }
          else {
            velX *= -0.4f;
            x += 2;
          }
        }
      }
    }
    // do the same for the right edge
    if (velX > 0) {
      for (int rightY = (int)y - playerHeight/2; rightY <= (int)y + playerHeight/2; rightY++) {
        if (terrain.isPixelSolid((int)x + playerWidth/2, rightY)) {
          for (int xCheck = (int)x + playerWidth/4; xCheck < (int)x + playerWidth/2 + 1; xCheck++) {
            if (terrain.isPixelSolid(xCheck, rightY)) {
              x = xCheck - playerWidth/2;
              break; 
            }
          }
          if (rightY > y && !topBlocked) {
            y -= 1;
          }
          else {
            velX *= -0.4f;
            x -= 2;
          }
        }
      }
    }
    
    // Boundary Checks
    if (x < 0 && velX < 0) {
      x -= x;
      velX *= -1;
    }
    if (y < 0 && velY < 0) {
      y -= y;
      velY *= -1;
    }
    if (x > terrain.width() && velX > 0) {
      x += terrain.width() - x;
      velX *= -1;
    }
    if (y+playerHeight/2 > terrain.height() && velY > 0) {
      y += terrain.height() - y - playerHeight/2;
      velY = 0;
      onGround = true;
    }
  }
  
  // Methods implemented as a PhysicsObj
  public float getX() { return x; }
  public float getY() { return y; }
  public float getVX() { return velX; }
  public float getVY() { return velY; }
  public void setX(float pX) { x = pX; }
  public void setY(float pY) { y = pY; }
  public void setVX(float vX) { velX = vX; }
  public void setVY(float vY) { velY = vY; }
}
/* RayCast */
// Uses Bresenham's line algorithm to efficiently loop between two points, and find the first solid pixel
// This particular variation always starts from the first point, so collisions don't happen at the wrong end.
// returns an int array
//       ||| x = ([0],[1]) point in empty space before collision point
//       ||| o = ([2],[3]) collision point
//(end)--||ox------- (start)
//       |||
// using http://www.gamedev.net/page/resources/_/reference/programming/sweet-snippets/line-drawing-algorithm-explained-r1275
public int[] rayCast(int startX, int startY, int lastX, int lastY) {
  int deltax = (int) abs(lastX - startX);        // The difference between the x's
  int deltay = (int) abs(lastY - startY);        // The difference between the y's
  int x = (int) startX;                       // Start x off at the first pixel
  int y = (int) startY;                       // Start y off at the first pixel
  int xinc1, xinc2, yinc1, yinc2;
  if (lastX >= startX) {                // The x-values are increasing
    xinc1 = 1;
    xinc2 = 1;
  }
  else {                         // The x-values are decreasing
    xinc1 = -1;
    xinc2 = -1;
  }
  
  if (lastY >= startY) {                // The y-values are increasing
    yinc1 = 1;
    yinc2 = 1;
  }
  else {                         // The y-values are decreasing
    yinc1 = -1;
    yinc2 = -1;
  }
  int den, num, numadd, numpixels;
  if (deltax >= deltay) {        // There is at least one x-value for every y-value
    xinc1 = 0;                  // Don't change the x when numerator >= denominator
    yinc2 = 0;                  // Don't change the y for every iteration
    den = deltax;
    num = deltax / 2;
    numadd = deltay;
    numpixels = deltax;         // There are more x-values than y-values
  }
  else {                         // There is at least one y-value for every x-value
    xinc2 = 0;                  // Don't change the x for every iteration
    yinc1 = 0;                  // Don't change the y when numerator >= denominator
    den = deltay;
    num = deltay / 2;
    numadd = deltax;
    numpixels = deltay;         // There are more y-values than x-values
  }
  int prevX = (int)startX;
  int prevY = (int)startY;
  
  for (int curpixel = 0; curpixel <= numpixels; curpixel++) {
    if (terrain.isPixelSolid(x, y))
        return new int[]{prevX, prevY, x, y};
    prevX = x;
    prevY = y;  
    
    num += numadd;              // Increase the numerator by the top of the fraction
    
    if (num >= den) {             // Check if numerator >= denominator
      num -= den;               // Calculate the new numerator value
      x += xinc1;               // Change the x as appropriate
      y += yinc1;               // Change the y as appropriate
    }
    
    x += xinc2;                 // Change the x as appropriate
    y += yinc2;                 // Change the y as appropriate
  }  
  return new int[]{};
}


// Anything we want drawn should implement this
interface RenderObj {
  public void draw();  
}
/* Renderer */
// Holds a list of all "RenderObj"s, anything with a draw() method.
class Renderer {
  
  ArrayList<RenderObj> objects;
  
  Renderer() {
    objects = new ArrayList<RenderObj>(); 
  }  
  
  public void draw() {
    for (RenderObj obj : objects) 
      obj.draw();
  }
  
  public void add(RenderObj obj) {
    objects.add(obj);
  }
  public void remove(RenderObj obj) {
    objects.remove(obj); 
  }
}

public void showNormals() {
  stroke(0);
  // Scan the terrain in a gridlike pattern, and only draw normals at pixels that have a range of solid pixels surrounding them
  for (int x = 0; x < terrain.width(); x += 10) {
    for (int y = 0; y < terrain.height(); y += 10) {
      int solidCount = 0;
      // scan solid pixels around this pixel
      for (int i = -5; i <= 5; i++) {
        for (int j = -5; j <= 5; j++) {
          if (terrain.isPixelSolid(x+i,y+j)) {
            solidCount++;
          }
        }
      }
      // if there's too many solid pixels, then it's probably underground, and not a surface
      // if there's not enough solid pixels, then it's probably in the air, and not a surface
      if (solidCount < 110 && solidCount > 30) {
        float[] pixelNormal = terrain.getNormal(x,y);
        if (pixelNormal.length > 0 && !Float.isNaN(pixelNormal[0]) && !Float.isNaN(pixelNormal[1]))
          line(x,y, x + 10 * pixelNormal[0], y + 10 * pixelNormal[1]);
      }
    }
  }
}

/* Terrain */
// Provides methods for determining solid/empty pixels, and for removing/adding solid pixels 
class Terrain {
  PImage img; // the terrain image
  int start; // Gibt die Starthöhe der Landschaft vor
	int change; // Entscheidet, ob die Richtung der
						// Landschaftsentwicklung geändert wird
	int faktor = 1; // Entscheidet über Addition oder Substraktion
	int last; // Speichert die letzte Höhe der letzten gezeichneten
						// Linie
	int plus; // Wieviel wird addiert oder subtraiert

	// Variable zur Erzeugung einer Zufallszahl
	Random rnd = new Random();

	// Array zum Speichern der Höhenpunkte
  int destructionRes; // how wide is a static pixel
  
  // Constructor
  Terrain(PImage pic, int destructionRes) {
    this.destructionRes = destructionRes;

    plus = 0;
    
	img = createImage(1000, 450, ARGB);
    img.loadPixels();
    
    int[] map = new int[img.width + 1];
    
	int redvalue = 150;
	int greenvalue = 75;
	int bluevalue = 30;

	// initialize variable factor which decides if + or - value of plus
	// faktor = 1; // Initializing start value of the surface
	start = Math.abs(200 + (rnd.nextInt() % 20));

	// Store start value on the first position in the array
	map[img.width] = start;

	// Loop to initialize all array positions
	for (int i = img.width; i > 0; i--) {
		// get the value before the actual position and store it in last
		last = map[i];

		// Decision if changing direction or not
		change = Math.abs(rnd.nextInt() % 10);
		// changing direction and possibly plus
		if (change > 8) {
			// Andern der Richtung
			faktor = -(faktor);

			// new plus (value 1 or 2)
			plus = Math.abs(rnd.nextInt() % 2);
		}

		/* Make sure that surface values stay in a certain range */
		if (last > 200) {
			faktor = -1;
		} else if (last < 100) {
			// Andern der Richtung
			faktor = 1;
		}

		// Calculate and store surface value on position i
		map[i - 1] = last + (faktor * plus);
		
		redvalue += faktor * Math.abs(rnd.nextInt() % 2);
		greenvalue += faktor * Math.abs(rnd.nextInt() % 2);
		bluevalue += faktor * Math.abs(rnd.nextInt() % 2);
		
		int g = 0;
		
		for (int j = (img.width * img.height - i - map[i - 1] * img.width); j <= (img.width * img.height - i); j += img.width) {
			if (g < 20) {
				img.pixels[j] = color(0, 200, 0);
			} else {
				img.pixels[j] = color(redvalue, greenvalue, bluevalue);
			}
			g++;
		}
	}
    
    img.updatePixels();
  } 
  
  // Render terrain onto the main screen
  public void draw(float x, float y) {
    image(img, x,y);  
  }
  
  // Return the terrain's width and height
  public int width() {
    return img.width;  
  }
  public int height() {
    return img.height; 
  }
  
  // Update - apply pixels[]'s changes onto the image
  public void update() {
    img.updatePixels(); 
  }
  
  // Determine if a pixel is solid based on whether or not it's transparent
  public boolean isPixelSolid(int x, int y) {
    if (x > 0 && x < img.width && y > 0 && y < img.height)
      return img.pixels[x + y * img.width] != color(0,0);
    return false; // border is not solid
  }
  
  // Color in a pixel, making it solid
  public void addPixel(int c, int x, int y) {
    if (x > 0 && x < img.width && y > 0 && y < img.height)
      img.pixels[x + y * img.width] = c;
  }
  // Make a pixel solid
  public void removePixel(int x, int y) {
    if (x > 0 && x < img.width && y > 0 && y < img.height)
      img.pixels[x + y * img.width] = color(0,0);
  }
  // Get a pixel's color
  public int getColor(int x, int y) {
    if (x > 0 && x < img.width && y > 0 && y < img.height)
      return img.pixels[x + y * img.width];
    return 0;
  }
  
  // Find a normal at a position
  public float[] getNormal(int x, int y) {
    // First find all nearby solid pixels, and create a vector to the average solid pixel from (x,y)
    float avgX = 0;
    float avgY = 0;
    for (int w = -3; w <= 3; w++) {
      for (int h = -3; h <= 3; h++) {
        if (isPixelSolid(x + w, y + h)) {
          avgX -= w;
          avgY -= h;
        }
      }
    }
    float len = sqrt(avgX * avgX + avgY * avgY); // get the distance from (x,y)
    return new float[]{avgX/len, avgY/len}; // normalize the vector by dividing by that distance
  }
}
  public int sketchWidth() { return 600; }
  public int sketchHeight() { return 450; }
  public String sketchRenderer() { return JAVA2D; }
  static public void main(String args[]) {
    PApplet.main(new String[] { "game.destructable_terrain" });
  }
}
