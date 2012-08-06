//Author: Linus Thorelli

package engine;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;


public class ParticleSystem extends Listener{
	LinkedList<Particle> particles = new LinkedList<Particle>();
	private int particlesToEmit, particlesEmitted = 0, maxSpeed, minSpeed, maxAngle, minAngle, lifeTime, width, height, particleWait, animationSpeed, xScrolling = 0, yScrolling = 0, particleCounter = 0;
	private double xPos, yPos, startX, startY;

	private boolean gravity, solid, singleAnim;
	private String filePath, animationName = null;
	private ArrayList<Point> animPoints = new ArrayList<Point>();
	
	public ParticleSystem(double xPos, double yPos, int particlesToEmit, int maxSpeed, int minSpeed, int maxAngle, int minAngle, int lifeTime,
						  int width, int height, boolean solid, boolean gravity, String filePath, int particleFlow, boolean singleAnim){
		this.particlesToEmit = particlesToEmit;
		this.maxSpeed = maxSpeed;
		this.minSpeed = minSpeed;
		this.maxAngle = maxAngle;
		this.minAngle = minAngle;
		this.singleAnim = singleAnim;
		
		this.filePath = filePath;
		this.lifeTime = lifeTime;
		this.width = width;
		this.height = height;
		this.xPos = xPos;
		this.yPos = yPos;
		startX = xPos;
		startY = yPos;
		
		this.solid = solid;
		this.gravity = gravity;
		
		particleWait = (1000 / particleFlow);
	}
	
	public void setAnimation(int animationSpeed, ArrayList<Point> animPoints, String animationName){
		this.animationSpeed = animationSpeed;
		this.animPoints.addAll(animPoints);
		this.animationName = animationName;
	}
	
	public void setScrolling(int xScrolling, int yScrolling){
		this.xScrolling = xScrolling;
		this.yScrolling = yScrolling;
		System.out.println("yScrolling: " + yScrolling);
	}
	
	
	public void enterFrame(long deltaTime){
		particleCounter += deltaTime;
		
		if(particleCounter >= particleWait){
			double speed = (minSpeed + (Math.random() * ((maxSpeed - minSpeed) + 1)))*100;
			double angle = minAngle + (Math.random() * ((maxAngle - minAngle) + 1));
			
			double xVel = Math.sin(Math.toRadians(angle)) * speed;
			double yVel = Math.cos(Math.toRadians(angle)) * speed;
			
			
			if(particlesEmitted < particlesToEmit || particlesToEmit == -1){
				Particle newParticle = new Particle(xPos, yPos, xVel, yVel, width, height, solid, gravity, filePath, lifeTime, singleAnim);
				
				if(animationName != null)
					newParticle.getSprite().createAnimation(animationSpeed, animPoints, animationName);
				
				particles.add(newParticle);
				
				if(particlesToEmit != -1)
					particlesEmitted++;
			}
			
			
			Particle particle = null;
			for(int i = 0; i < particles.size(); i++){
				particle = particles.get(i);
				particle.tick();
				
				if(!particle.getAlive()){
					Renderer.getRenderer().removeSprite(particle.getSprite());
					PhysicsEngine.getPhysicsEngine().removePhysicsObject(particle);
					particles.remove(i);
				}
			}
			
			if(particlesEmitted == particlesToEmit && particles.size() == 0){
				GameLoop.getGameLoop().removeListener(this);
			}
			
			particleCounter = 0;
		}
	}
}
