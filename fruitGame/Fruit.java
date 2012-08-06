//Author: Linus Thorelli

package fruitGame;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import engine.PhysicsObject;
import engine.SoundEngine;
import engine.Sprite;


public class Fruit extends PhysicsObject{
	private int fruitType, fruitYOffset, fruitXOffset;
	private double landingY, landingX, friction = 100.0;
	private long squashCounter = 0, squashTimer = 0;
	private boolean selected = false, impact = false, stopped = false, squash = false, grow = false;
	private Point push = new Point(0, 0);
	private Rectangle outerBounds;
	private FruitListener listener;
	private Sprite shadow;
	private GameListener gameListener;
	
	public Fruit(double xPos, double yPos, double xVel, double yVel, boolean solid, int width, int height, int fruitType, GameListener gameListener, int landingY){
		super(xPos, yPos, xVel, yVel, width, height, solid, true);
		this.landingY = landingY;
		landingX = xPos;
		this.fruitType = fruitType;
		this.gameListener = gameListener;
		outerBounds = new Rectangle((int)xPos - 15, (int)yPos - 15, width + 30, height + 30);
		friction = gameListener.getFriction();
		
		if(fruitType == 0){
			fruitYOffset = -3;
			fruitXOffset = 1;
		}
		else if(fruitType == 1){
			fruitYOffset = -49;
			fruitXOffset = 1;
		}
		else if(fruitType == 2){
			fruitYOffset = 0;
			fruitXOffset = - 14;
		}
		else if(fruitType == 3){
			fruitYOffset = -6;
			fruitXOffset = 1;
		}
		else if(fruitType == 4){
			fruitYOffset = -29;
			fruitXOffset = -1;
		}
		else if(fruitType == 5){
			fruitYOffset = -24;
			fruitXOffset = 5;
		}
	}
	
	public void update(long deltaTime){
		previousX = xPos;
		previousY = yPos;
		
		if(squash)
			squashFruit(deltaTime);
		
		
		if(!gravity){
			if(impact){
				impact = false;
				squash = true;
				Random random = new Random();
				int sound = random.nextInt(3);
				if(sound == 0)
					SoundEngine.getSoundEngine().playSound("sounds/impact_03.wav");
				else if(sound == 1)
					SoundEngine.getSoundEngine().playSound("sounds/impact_04.wav");
				else if(sound == 2)
					SoundEngine.getSoundEngine().playSound("sounds/impact_05.wav");

				sprite.setZPos(20);
			}
			
			if(gameListener.getWind() == 0 && !stopped){
				xVel = 0;
				stopped = true;
			}
			
			else if(gameListener.getWind() == 1){
				stopped = false;
				if(xVel <= 30)
					xVel += 10;
			}
			else if(gameListener.getWind() == -1){
				stopped = false;
				if(xVel >= -30){
					xVel -= 10;
					//push.x = -40;
				}
			}
			
			
			//Applicera deceleration
			yVel += (-1 * (push.y / friction));
			xVel += (-1 * (push.x / friction));
				
			//Sätt hastigheten till 0 om den är tillräckligt liten
			if(xVel < 10 && xVel > -10){
				xVel = 0;
				push.x = 0;
			}
			if(yVel < 10 && yVel > -10){
				yVel = 0;
				push.y = 0;
			}
			
			//Studsa mot kanten
			if(xPos <= 0){
				if(gameListener.getWind() == 0)
					SoundEngine.getSoundEngine().playSound("sounds/knock.wav");
				xPos = 8;
				xVel = -xVel;
				push.x = -push.x;
			}
				
			if(xPos + width >= 1200){
				if(gameListener.getWind() == 0)
					SoundEngine.getSoundEngine().playSound("sounds/knock.wav");
				xPos = 1200 - width - 8;
				xVel = -xVel;
				push.x = -push.x;
			}
				
			if(yPos <= 60){
				if(gameListener.getWind() == 0)
					SoundEngine.getSoundEngine().playSound("sounds/knock.wav");
				yPos = 70;
				yVel = -yVel;
				push.y = -push.y;
			}
				
			if(yPos + height >= 800){
				if(gameListener.getWind() == 0)
					SoundEngine.getSoundEngine().playSound("sounds/knock.wav");
				yPos = 800 - height;
				yVel = -yVel;
				push.y = -push.y;
			}
			
			shadow.setXPos(xPos + fruitXOffset);
			shadow.setYPos(yPos + fruitYOffset);
		}
		
		else{
			shadow.setXPos(xPos + fruitXOffset);
			
			if(shadow.getYPos() >= landingY + fruitYOffset)
				shadow.setYPos(landingY + fruitYOffset);	
			else
				shadow.setYPos(shadow.getYPos() + yVel * 3 * (deltaTime / 1000.0));
			
			if(yPos >= landingY){
				yPos = landingY;
				impact = true;
				this.setGravity(false);
				xVel = 0;
				yVel = 0;
			}
		}
		
		xPos += (xVel * (deltaTime/1000.0));
		yPos += (yVel * (deltaTime/1000.0));
		outerBounds.setLocation((int)xPos - 20, (int)yPos - 20);
		boundingBox.setLocation((int)(xPos), (int)(yPos));
		
		if(sprite != null && !squash){
			sprite.updatePos(xPos + fruitXOffset, yPos + fruitYOffset);
		}
	}
	
	private void squashFruit(long deltaTime){
		squashCounter += deltaTime;
		squashTimer += deltaTime;
		
		double yScale = sprite.getYScale();
		double xScale = sprite.getXScale();
		
		if(squashCounter > 10){
			if(yScale < 0.7)
				grow = true;
			if(grow){
				sprite.setYScale(yScale * 1.1);
				sprite.setXScale(xScale * 0.9);
				sprite.setYPos(sprite.getYPos() - sprite.getHeight() / 10);
				sprite.setXPos(sprite.getXPos() + sprite.getWidth() / 14);
			}
			if(yScale > 0.7 && !grow){
				sprite.setYScale(yScale * 0.9);
				sprite.setXScale(xScale * 1.1);
				sprite.setYPos(sprite.getYPos() + sprite.getHeight() / 10);
				sprite.setXPos(sprite.getXPos() - sprite.getWidth() / 14);
			}
			
			
			squashCounter = 0;
		}
		
		if(squashTimer > 150){
			squash = false;
			squashTimer = 0;
			squashCounter = 0;
			sprite.setYScale(1);
			sprite.setXScale(1);
		}
		
		
	}
	
	public void addShadow(Sprite shadow){
		this.shadow = shadow;
	}
	
	public FruitListener getListener(){ return listener; }
	public boolean getSelected(){ return selected; }
	public Point getPush(){ return push; }
	public double getPreviousX(){ return previousX; }
	public double getPreviousY(){ return previousY; }
	public double getLandingY(){ return landingY; }
	public double getLandingX(){ return landingX; }
	public boolean getImpact(){ return impact; }
	public int getFruitType(){ return fruitType; }
	public Sprite getShadow(){ return shadow; }
	public Rectangle getOuterBounds(){ return outerBounds; }
	
	public void setListener(FruitListener listener){ this.listener = listener; }
	public void setSelected(boolean selected){ this.selected = selected; }
	public void setPush(Point push){ this.push = push; }
	public void setXVel(double xVel){ this.xVel = xVel; }
	public void setYVel(double yVel){ this.yVel = yVel; }
}
