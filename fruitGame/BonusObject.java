//Author: Linus Thorelli

package fruitGame;

import java.awt.Point;
import java.util.ArrayList;

import engine.Loader;
import engine.PhysicsEngine;
import engine.PhysicsObject;
import engine.Renderer;
import engine.SoundEngine;
import engine.Sprite;

public class BonusObject extends PhysicsObject{
	private int bonusType, landingY, color;
	private double landingX;
	private long timePassed = 0;
	private GameListener gameListener;
	private Loader loader;
	private Sprite shadow;
	
	
	public BonusObject(double xPos, double yPos, double xVel, double yVel, boolean solid, int width, int height, int bonusType, GameListener gameListener, int landingY, int color){
		super(xPos, yPos, xVel, yVel, width, height, solid, true);
		this.landingY = landingY;
		landingX = xPos;
		this.bonusType = bonusType;
		this.gameListener = gameListener;
		this.color = color;
		loader = Loader.getLoader();
	}
	
	
	public void update(long deltaTime){
		timePassed += deltaTime;
		
		//Objektet lever bara i 5 sekunder
		if(timePassed >= 5000){
			PhysicsEngine.getPhysicsEngine().getCollisionObjects().remove(this);
			PhysicsEngine.getPhysicsEngine().removePhysicsObject(this);
			Renderer.getRenderer().getSprites().remove(sprite);
			Renderer.getRenderer().getSprites().remove(shadow);
			gameListener.setUpdateCollidables(true);
		}
		
		if(!gravity){
			shadow.setXPos(xPos + 5);
			shadow.setYPos(yPos + 5);
		}
		else{
			if(shadow.getYPos() >= landingY + 5)
				shadow.setYPos(landingY + 5);	
			else
				shadow.setYPos(shadow.getYPos() + yVel * 3 * (deltaTime / 1000.0));
			
			xPos += (xVel * (deltaTime/1000.0));
			yPos += (yVel * (deltaTime/1000.0));
			
			xVel += (-1 * (xVel/100));
			
			if(yPos > landingY){
				yPos = landingY;
				this.setGravity(false);
				xVel = 0;
				yVel = 0;
			}
		}
			
		
		boundingBox.setLocation((int)(xPos), (int)(yPos));
			
		if(sprite != null)
			sprite.updatePos(xPos, yPos);
	}
	
	public void resetBonus(){
		if(gameListener.getBonusListener() != null){
			gameListener.getBonusListener().setTimer(0);
			gameListener.getBonusListener().timeOver();
		}
	}
	
// Bonus Effects --------------------------------------------
	
	public void addTime(){
		createEffect("images/bonus_effect.png");
		
		gameListener.setTimePassed(gameListener.getTimeLeft() + 10000);
		Sprite timeBonus = new Sprite(xPos, yPos, 95, 51, "images/10_sec.png", 0, 0, false, 21, true);
		TemporaryObject timeObject = new TemporaryObject(xPos, yPos, 0, -100, 95, 51, 1000, false);
		timeObject.attachSprite(timeBonus);
		
		loader.addLevelObject(timeBonus);
		loader.addLevelObject(timeObject);
	}
	
	public void machineGun(){
		createEffect("images/bonus_effect_red.png");
		
		gameListener.setTriplePoints(false);
		gameListener.setDoublePoints(false);
		gameListener.setSingleFruit(false);
			
		if(!gameListener.getGunMode())
			initializeBonusListener();
		else
			gameListener.getBonusListener().setTimer(10);
	}
	
	public void doublePoints(){
		createEffect("images/bonus_effect_yellow.png");
		
		gameListener.setTriplePoints(false);
		gameListener.setGunMode(false);
		gameListener.setSingleFruit(false);
		
		if(!gameListener.getDoublePoints())
			initializeBonusListener();
		
		else
			gameListener.getBonusListener().setTimer(10);
	}
	
	public void triplePoints(){
		createEffect("images/bonus_effect_yellow.png");
		
		gameListener.setGunMode(false);
		gameListener.setDoublePoints(false);
		gameListener.setSingleFruit(false);
		
		if(!gameListener.getTriplePoints())
			initializeBonusListener();
		else
			gameListener.getBonusListener().setTimer(10);
	}
	
	public void singleFruit(){
		createEffect("images/bonus_effect_red.png");
		
		gameListener.setGunMode(false);
		gameListener.setDoublePoints(false);
		gameListener.setTriplePoints(false);
		
		if(!gameListener.getSingleFruit())
			initializeBonusListener();
		else
			gameListener.getBonusListener().setTimer(10);
	}
	
	public void skull(){
		createEffect("images/bonus_effect_black.png");
		
		//Skaka skärmen
		gameListener.startShaking(8, 20, 5);
		
		PhysicsObject iteratorObject = null;
		
		for(int i = 0; i < gameListener.getCollidableObjectList().size(); i++){
			iteratorObject = gameListener.getCollidableObjectList().get(i);
			
			if(iteratorObject instanceof Fruit){
				((Fruit)iteratorObject).getListener().setDestroyed(true);
				gameListener.setFruitSelected(false);
				((Fruit)iteratorObject).getListener().setSplatImage("images/brown_splat.png");
				gameListener.setFruitWasted(gameListener.getFruitWasted() + 1);
			}
				
		}
	}
	
	public void star(int fruitType){
		if(fruitType == color){
			createEffect("images/bonus_effect_yellow.png");
			gameListener.addStar();
			SoundEngine.getSoundEngine().playSound("sounds/star_get.wav");
		}
		else{
			SoundEngine.getSoundEngine().playSound("sounds/boom.wav");
			//skaka skärmen
			gameListener.startShaking(4, 5, 5);
		}
	}
	
// -------------------------------------------------------------
	
	public void initializeBonusListener(){
		resetBonus();
		BonusListener bonusListener = new BonusListener(gameListener, bonusType);
		gameListener.setBonusListener(bonusListener);
		loader.addLevelObject(bonusListener);
	}
	
	public void createEffect(String fileName){
		Sprite effectSprite = new Sprite(xPos, yPos, 128, 128, fileName, 0, 0, false, 22, true);
		
		ArrayList<Point> animPoints = new ArrayList<Point>();
		int y = 0;
		
		for(int i = 0; i < 6; i++){
			if(i * 128 == 768){
				y += height;
				i = 0;
			}
			animPoints.add(new Point(i * 128, y));
		}
		
		effectSprite.createAnimation(90, animPoints, "effect");
		TemporaryObject effectObject = new TemporaryObject(xPos, yPos, 0, 0, 128, 128, 450, false);
		effectObject.attachSprite(effectSprite);
		
		Loader.getLoader().addLevelObject(effectSprite);
		Loader.getLoader().addLevelObject(effectObject);
	}
	
	public void addShadow(Sprite shadow){ 
		this.shadow = shadow; 
	}
	
	public GameListener getGameListener(){ return gameListener; }
	public double getLandingY(){ return landingY; }
	public double getLandingX(){ return landingX; }
	public int getBonusType(){ return bonusType; }
	public Sprite getShadow(){ return shadow; }
	public int getColor(){ return color; }
	
	public void setListener(GameListener gameListener){ this.gameListener = gameListener; }
	public void setXVel(double xVel){ this.xVel = xVel; }
	public void setYVel(double yVel){ this.yVel = yVel; }
}
