//Author: Linus Thorelli

package fruitGame;

import engine.PhysicsObject;

public class FireBall extends PhysicsObject{
	
	public FireBall(double xPos, double yPos, double xVel, double yVel, int width, int height){
		super(xPos, yPos, xVel, yVel, width, height, true, false);
	}
	
	public void update(long deltaTime){
		xPos += (xVel * (deltaTime/1000.0));
		yPos += (yVel * (deltaTime/1000.0));
		boundingBox.setLocation((int)(xPos), (int)(yPos));
		
		if(sprite != null)
			sprite.updatePos(xPos - 13, yPos);
	}
}
