//Author: Linus Thorelli

package engine;

public class Tile extends PhysicsObject{
	private String name;
	
	public Tile(String name, int xPos, int yPos, int size, boolean solid){
		super(xPos, yPos, size, solid);
		this.name = name;
		if(solid == false)
			this.boundingBox = null;
	}
	
	public String getName(){
		return name;
	}
}
