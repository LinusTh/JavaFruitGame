//Author: Linus Thorelli

package engine;

public class Particle extends PhysicsObject{
	private int lifeTime, age = 0;
	private boolean alive = true;
	
	public Particle(double xPos, double yPos, double xVel, double yVel, int width, int height, boolean solid, boolean gravity, String filePath, int lifeTime, boolean singleAnim){
		super(xPos, yPos, xVel, yVel, width, height, solid, gravity);
		Sprite sprite = new Sprite(xPos, yPos, width, height, filePath, 1, 1, singleAnim, 25, true);
		this.attachSprite(sprite);
		PhysicsEngine.getPhysicsEngine().addPhysicsObject(this);
		Renderer.getRenderer().addSprite(sprite);
		this.lifeTime = lifeTime;
	}
	
	public void tick(){
		if(age >= lifeTime){
			alive = false;
			PhysicsEngine.getPhysicsEngine().removePhysicsObject(this);
			Renderer.getRenderer().removeSprite(this.sprite);
		}
		age++;
	}
	
	public boolean getAlive(){ return alive; }
}
