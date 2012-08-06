//Author: Linus Thorelli

package engine;


public class Camera extends CoordinateObject{
	private int slowness;
	private double xVel, yVel;
	private CoordinateObject followObject;
	private RenderWindow renderWindow;
	private PhysicsEngine physicsEngine;
	
	public Camera(int x, int y){
		xPos = x;
		yPos = y;
		xVel = 0;
		yVel = 0;
		slowness = 15;
		renderWindow = RenderWindow.getRenderWindow();
		physicsEngine = PhysicsEngine.getPhysicsEngine();
	}
	
	public void updatePosition(long deltaTime){
		if(physicsEngine.getCollisionArray() != null){
			if(xPos > 0 && xPos < (physicsEngine.getCollisionArray().length * Engine.getEngine().getTileSize()) - Engine.getEngine().getWindowWidth())
				renderWindow.setCameraStuckX(false);
			
			else
				renderWindow.setCameraStuckX(true);
			
			if(yPos > 0 && yPos < (physicsEngine.getCollisionArray()[0].length * Engine.getEngine().getTileSize()) - Engine.getEngine().getWindowHeight())
				renderWindow.setCameraStuckY(false);
			
			else
				renderWindow.setCameraStuckY(true);
		}
		xPos += xVel * (deltaTime/10.0);
		yPos += yVel * (deltaTime/10.0);
		
	}
	
	public void setFollowObject(CoordinateObject object){
		followObject = object;
	}
	
	public void calculateVelocity(){
		//Beräkna hastigheten i förhållande till avståndet till followObject
		//Metoden kan överlagras i sub-klassen om man vill, men det ska finnas en default-funktionalitet
		
		if(followObject != null && followObject.getFollow()){
			xVel = (followObject.getXPos() - (xPos + (Engine.getEngine().getWindowWidth() / 2)) + 64) / slowness;
			yVel = (followObject.getYPos() - (yPos + (Engine.getEngine().getWindowHeight() / 2)) + 64) / slowness;
		}
	}
	
	
	public void setSlowness(int slowness){ this.slowness = slowness; }
	public void setXPos(double xPos){ this.xPos = xPos; }
	public void setYPos(double yPos){ this.yPos = yPos; }
	
	public void setVel(double xVel, double yVel){
		this.xVel = xVel;
		this.yVel = yVel;
	}
	
	public CoordinateObject getFollowObject(){ return followObject; }
	public double getXPos(){ return xPos; }
	public double getYPos(){ return yPos; }
	public double getXVel(){ return xVel; }
	public double getYVel(){ return yVel; }
}
