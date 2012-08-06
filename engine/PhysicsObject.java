//Author: Linus Thorelli

package engine;


import java.awt.Rectangle;


public class PhysicsObject extends CoordinateObject{
	protected Rectangle boundingBox;
	
	protected Sprite sprite;
	protected boolean solid, gravity;
	protected double xVel, yVel, previousX, previousY;
	protected int width, height;

	
	
	public PhysicsObject(double xPos, double yPos, double xVel, double yVel, int width, int height, boolean solid, boolean gravity){	
		this.xPos = xPos;
		this.yPos = yPos;
		this.xVel = xVel;
		this.yVel = yVel;
		this.width = width;
		this.height = height;
		this.solid = solid;
		this.gravity = gravity;
		boundingBox = new Rectangle((int)xPos, (int)yPos, width, height);
	}
	
	public PhysicsObject(){
		xPos = 0;
		yPos = 0;
		xVel = 0;
		yVel = 0;
		width = 20;
		height = 20;
		solid = false;
		gravity = false;
		boundingBox = new Rectangle((int)xPos, (int)yPos, width, height);
	}
	
	public PhysicsObject(double xPos, double yPos){
		this.xPos = xPos;
		this.yPos = yPos;
		width = 20;
		height = 20;
		solid = false;
		gravity = false;
		boundingBox = new Rectangle((int)xPos, (int)yPos, width, height);
	}
	
	public PhysicsObject(double xPos, double yPos, int size, boolean solid){
		this.xPos = xPos;
		this.yPos = yPos;
		width = size;
		height = size;
		this.solid = solid;
		gravity = false;
		boundingBox = new Rectangle((int)xPos, (int)yPos, width, height);
	}
	
	public void update(long deltaTime){
		previousX = xPos;
		previousY = yPos;
		
		xPos += (xVel * (deltaTime/1000.0));
		yPos += (yVel * (deltaTime/1000.0));
		boundingBox.setLocation((int)(xPos), (int)(yPos));
		
		if(sprite != null)
			sprite.updatePos(xPos, yPos);
	}
	
	public void attachSprite(Sprite sprite){
		this.sprite = sprite; 
	}
	
	public void addToVelocity(double xVel, double yVel){
		if((this.yVel + yVel) < PhysicsEngine.getPhysicsEngine().getTerminalVelocity()){
			this.xVel += xVel;
			this.yVel += yVel;
		}
	}
	
	
	//Setters
	public void setGravity(boolean gravity){ this.gravity = gravity; }
	public void setSolid(boolean solid){ this.solid = solid; }
	public void setXPos(double xPos){ this.xPos = xPos; }
	public void setYPos(double yPos){ 
		this.yPos = yPos;
		boundingBox.setLocation((int)(xPos), (int)(yPos));
	}
	public void setXVel(double xVel){ this.xVel = xVel; }
	public void setYVel(double yVel){ this.yVel = yVel; }
	
	//Getters
	public Rectangle getBoundingBox(){ return boundingBox; }
	public boolean getGravity(){ return gravity; }
	public boolean getSolid(){ return solid; }
	public double getXVel(){ return xVel; }
	public double getYVel(){ return yVel; }
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	public Sprite getSprite(){ return sprite; }
	public double getPreviousX(){ return previousX; }
	public double getPreviousY(){ return previousY; }
}
