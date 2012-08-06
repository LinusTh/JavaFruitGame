//Author: Linus Thorelli

package engine;

import java.util.ArrayList;


public class PhysicsEngine{
	private static PhysicsEngine physicsEngineObject;
	private double gravity;
	private double terminalVelocity = 1000;
	private ArrayList<PhysicsObject> physicsObjects = new ArrayList<PhysicsObject>();
	private ArrayList<PhysicsObject> collisionObjects = new ArrayList<PhysicsObject>();
	private int[][] collisionArray;
	
	
	private PhysicsEngine(){
		gravity = 1200;
	}
	
	public static PhysicsEngine getPhysicsEngine(){
		if(physicsEngineObject == null){
			physicsEngineObject = new PhysicsEngine();
		}
		return physicsEngineObject;
	}
	
	public boolean collision(PhysicsObject object1, PhysicsObject object2){
		return object1.getBoundingBox().intersects(object2.getBoundingBox());
	}

	
	public void updateObjects(long deltaTime){
		PhysicsObject currentObject;
		
		for(int i = 0; i < physicsObjects.size(); i++){
			currentObject = physicsObjects.get(i);
			if(currentObject.getGravity()){
				if(currentObject.getYVel() <= terminalVelocity){
					currentObject.addToVelocity(0, (gravity*(deltaTime/1000.0)));
				}
			}
			if(currentObject instanceof Tile == false)
				currentObject.update(deltaTime);
		}
	}
	
	
	public void addPhysicsObject(PhysicsObject newObject){
		physicsObjects.add(newObject);
		
		//Om objektet är solid så läggs det till i listan med alla objekt som kan kollidera
		if(newObject.getSolid()){
			collisionObjects.add(newObject);
		}
	}
	
	public void removePhysicsObject(PhysicsObject removeObject){
		physicsObjects.remove(removeObject);
	}
	
	public void removeAll(){
		physicsObjects.clear();
		collisionObjects.clear();
	}
	
	
	public void setGravity(double newGravity){ gravity = newGravity; }
	public void setCollisionArray(int[][] collisionArray){ this.collisionArray = collisionArray; }
	
	public double getTerminalVelocity(){ return terminalVelocity; }
	public ArrayList<PhysicsObject> getPhysicsObjects(){ return physicsObjects; }
	public ArrayList<PhysicsObject> getCollisionObjects(){ return collisionObjects; }
	public double getGravity(){ return gravity; }
	public int[][] getCollisionArray(){ return collisionArray; }
}
