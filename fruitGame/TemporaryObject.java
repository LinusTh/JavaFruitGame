//Author: Linus Thorelli

package fruitGame;

import engine.PhysicsObject;

public class TemporaryObject extends PhysicsObject{
	private int lifeTime = 0, timeLimit, groundLevel;
	
	public TemporaryObject(double xPos, double yPos, double xVel, double yVel, int width, int height, int timeLimit, boolean gravity){
		super(xPos, yPos, xVel, yVel, width, height, false, gravity);
		this.timeLimit = timeLimit;
		groundLevel = (int)yPos + 50;
	}
	
	public void update(long deltaTime){
		lifeTime += deltaTime;
		
		xPos += (xVel * (deltaTime/1000.0));
		yPos += (yVel * (deltaTime/1000.0));
		
		if(yPos > groundLevel){
			yPos = groundLevel - 2;
			yVel = -yVel * 0.7;
			xVel = xVel * 0.8;
		}
			
		boundingBox.setLocation((int)(xPos), (int)(yPos));
		
		if(sprite != null)
			sprite.updatePos(xPos, yPos);
	}
	
	public int getLifeTime(){ return lifeTime; }
	public int getTimeLimit(){ return timeLimit; }
}
