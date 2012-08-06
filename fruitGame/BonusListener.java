//Author: Linus Thorelli

package fruitGame;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import engine.GameLoop;
import engine.InputHandler;
import engine.Listener;
import engine.Loader;
import engine.PhysicsEngine;
import engine.PhysicsObject;
import engine.Renderer;
import engine.SoundEngine;
import engine.Sprite;

public class BonusListener extends Listener{
	private int shotFrequency = 100, shotTimer = 0, bonusTime = 10, mouseX, mouseY, hits = 0, bonusType, updateFrequency;
	private boolean shooting = false;
	private ArrayList<Integer> mouseButton1 = new ArrayList<Integer>();
	private ArrayList<Point> explosionPoints;
	private InputHandler inputHandler;
	private GameListener gameListener;
	private Sprite digit3, digit4, gunSprite, bonusIcon;
	private Timer timer;
	private Loader loader;
	
	public BonusListener(GameListener gameListener, int bonusType){
		this.bonusType = bonusType;
		this.gameListener = gameListener;
		inputHandler = InputHandler.getInputHandler();
		mouseButton1.add(1);
		loader = Loader.getLoader();
		
		//Maskingevär
		if(bonusType == 1){
			gameListener.setGunMode(true);
			updateFrequency = gameListener.getUpdateFrequency();
			gameListener.setUpdateFrequency(300);
			bonusIcon = new Sprite(460, 28, 64, 32,"images/gun_icon.png", 0, 0, false, 25, true);
			digit3 	= new Sprite(655, 16, 48, 48,"images/numbers_red.png", 0, 0, false, 25, true);
			digit4	= new Sprite(695, 16, 48, 48,"images/numbers_red.png", 0, 0, false, 25, true);
			gunSprite = new Sprite(440, 550, 325, 360,"images/machine_gun.png", 0, 0, false, 25, true);
			
			//Maskingevärets frames
			for (int i = 0; i < 9; i++){
				ArrayList<Point> tmp = new ArrayList<Point>();
				ArrayList<Point> tmp2 = new ArrayList<Point>();
				tmp.add(new Point(i * 325, 0));
				tmp2.add(new Point(i * 325, 360));
				tmp2.add(new Point(i * 325, 0));
				gunSprite.createAnimation(0, tmp, "" + i);
				gunSprite.createAnimation(50, tmp2, "" + i + "_fire");
			}
			
			//Siffrornas frames
			for (int i = 0; i < 10; i++){
				ArrayList<Point> tmp = new ArrayList<Point>();
				tmp.add(new Point(i * 48, 0));
				digit3.createAnimation(0, tmp, "" + i);
				digit4.createAnimation(0, tmp, "" + i);
			}
			
			digit3.changeAnimation("0");
			digit4.changeAnimation("0");
			
			gunSprite.changeAnimation("4");
			
			Loader.getLoader().addLevelObject(digit3);
			Loader.getLoader().addLevelObject(digit4);
			Loader.getLoader().addLevelObject(gunSprite);
			Loader.getLoader().addLevelObject(bonusIcon);
			
			explosionPoints = new ArrayList<Point>();
			explosionPoints.add(new Point(0,0));
			explosionPoints.add(new Point(32,0));
			explosionPoints.add(new Point(64,0));
			explosionPoints.add(new Point(96,0));
			explosionPoints.add(new Point(128,0));
		}
		
		if(bonusType == 2){
			bonusIcon = new Sprite(460, 12, 64, 59,"images/x2_icon.png", 0, 0, false, 25, true);
			loader.addLevelObject(bonusIcon);
			gameListener.setDoublePoints(true);
		}
		
		if(bonusType == 3){
			bonusIcon = new Sprite(460, 12, 64, 59,"images/x3_icon.png", 0, 0, false, 25, true);
			loader.addLevelObject(bonusIcon);
			gameListener.setTriplePoints(true);
		}
		if(bonusType == 6){
			bonusIcon = new Sprite(460, 12, 50, 56,"images/medallion_" + gameListener.getSingleFruitType() + "_icon.png", 0, 0, false, 25, true);
			loader.addLevelObject(bonusIcon);
			gameListener.setSingleFruit(true);
		}
		
		//Om det inte finns tillräckligt med tid kvar för hela bonustiden
		if(gameListener.getTimeLeft() < 11000)
			bonusTime = (int)(gameListener.getTimeLeft() / 1000) - 1;
		
		timer = new Timer(540, 8, bonusTime);
		Loader.getLoader().addLevelObject(timer);
		
	}
	
	public void enterFrame(long deltaTime){
		if(gameListener.getTimeLeft() <= 0){
			timeOver();
			return;
		}
		if(bonusType == 1){
			
			mouseX = inputHandler.getMousePosition().x;
			mouseY = inputHandler.getMousePosition().y;
			
			//Träffräknaren
			if(hits < 10){
				digit3.changeAnimation(Integer.toString(0));
				digit4.changeAnimation(Integer.toString(hits));	
			}
			else{
				digit3.changeAnimation(Integer.toString(hits / 10));
				digit4.changeAnimation(Integer.toString(hits % 10));	
			}
			
			
			updateGunAnimation();
			
			
			if(!inputHandler.checkMouseButtons(mouseButton1)){
				shotTimer = 0;
				shooting = false;
			}
			
			//Vad som händer när man skjuter
			if(inputHandler.checkMouseButtons(mouseButton1)){
				shotTimer += deltaTime;
				if(!shooting)
					shot();
				shooting = true;
				if(shotTimer >= shotFrequency){
					shot();
					shotTimer = 0;
				}
			}
		}
		
		//Vad som händer när tiden är slut
		if(timer.getSecondsLeft() <= 0){
			timeOver();
		}
	}
	
	public void shot(){
		SoundEngine.getSoundEngine().playSound("sounds/gun_sound.wav");
		
		//Flamma
		if(gunSprite.getCurrentAnimation().getName().length() == 1){
			//System.out.println(gunSprite.getCurrentAnimation().getName() + "_fire");
			gunSprite.changeAnimation(gunSprite.getCurrentAnimation().getName() + "_fire");
			gunSprite.getCurrentAnimation().setCurrentFrame(0);
		}
		else
			gunSprite.getCurrentAnimation().setCurrentFrame(0);
		
		//Explosion
		Sprite explosion = new Sprite(mouseX - 10, mouseY - 10, 32, 32,"images/small_explosion.png", 0, 0, true, 24, true);
		explosion.createAnimation(50, explosionPoints, "explosion");
		Loader.getLoader().addLevelObject(explosion);
		
		//Kulhål
		Graphics g = Renderer.getRenderer().getImage(0).getGraphics();
		BufferedImage hole = null;
		try {
			hole = ImageIO.read(new File("images/bullet_hole.png"));
		} catch (IOException e) { System.out.println(e.getMessage()); }
		
		g.drawImage(hole, mouseX - 25, mouseY - 95, 30, 32, null);
		
		ArrayList <PhysicsObject> collidableObjectList = new ArrayList<PhysicsObject>();
		collidableObjectList.addAll(PhysicsEngine.getPhysicsEngine().getCollisionObjects());
		gameListener.setBulletHoles(gameListener.getBulletHoles() + 1);
		
		//Kolla kollisioner
		PhysicsObject iteratorObject = null;
		for(int i = 0; i < collidableObjectList.size(); i++){
			
			iteratorObject = collidableObjectList.get(i);
			
			
			if(mouseX >= iteratorObject.getXPos() && mouseX <= iteratorObject.getXPos() + iteratorObject.getWidth() + 8 && mouseY >= iteratorObject.getYPos() &&
			   mouseY <= iteratorObject.getYPos() + iteratorObject.getHeight() + 30){
				
				//Frukt
				if(collidableObjectList.get(i) instanceof Fruit){
					((Fruit)iteratorObject).getListener().setDestroyed(true);
					gameListener.setFruitJuiced(gameListener.getFruitJuiced() + 1);
					hits++;
				}
				
				//Bonusar
				else if(collidableObjectList.get(i) instanceof BonusObject) {
					
					switch(((BonusObject)iteratorObject).getBonusType()){
						case 0:
							SoundEngine.getSoundEngine().playSound("sounds/bonus_sound_02.wav");
							((BonusObject)iteratorObject).addTime();
							break;
						
						case 1:
							SoundEngine.getSoundEngine().playSound("sounds/bonus_sound_03.wav");
							((BonusObject)iteratorObject).machineGun();
							break;
							
						case 2:
							SoundEngine.getSoundEngine().playSound("sounds/bonus_sound_01.wav");
							((BonusObject)iteratorObject).doublePoints();
							break;
							
						case 3:
							SoundEngine.getSoundEngine().playSound("sounds/bonus_sound_01.wav");
							((BonusObject)iteratorObject).triplePoints();
							break;
						case 4:
							SoundEngine.getSoundEngine().playSound("sounds/bad_bonus.wav");
							((BonusObject)iteratorObject).skull();
							break;
						case 5:
							SoundEngine.getSoundEngine().playSound("sounds/fruit_medallion.wav");
							((BonusObject)iteratorObject).singleFruit();
							break;
					}
					
					//Rensa bort iteratorObject
					PhysicsEngine.getPhysicsEngine().getCollisionObjects().remove(iteratorObject);
					PhysicsEngine.getPhysicsEngine().removePhysicsObject(iteratorObject);
					Renderer.getRenderer().getSprites().remove(((BonusObject)iteratorObject).getShadow());
					Renderer.getRenderer().getSprites().remove(iteratorObject.getSprite());
					collidableObjectList.remove(iteratorObject);
					gameListener.setUpdateCollidables(true);
				}
			}
				
		}
	}
	
	public void timeOver(){
		if(bonusType == 1){
			gameListener.addToScore(hits * 100);
			gameListener.setUpdateFrequency(updateFrequency);
			Sprite pointSprite 	= new Sprite(500, 200, 68, 44, "images/100.png", 0, 0, false, 22, true);
			Sprite xSprite 		= new Sprite(570, 200, 48, 48, "images/x.png", 0, 0, false, 22, true);
			
			TemporaryObject points 		= new TemporaryObject(500, 100, 0, -100, 68, 44, 1000, false),
							x			= new TemporaryObject(580, 100, 0, -100, 48, 48, 1000, false),
							multiplier1 = new TemporaryObject(620, 100, 0, -100, 68, 44, 1000, false),
							multiplier2 = new TemporaryObject(650, 100, 0, -100, 68, 44, 1000, false);
			
			x.attachSprite(xSprite);
			multiplier1.attachSprite(digit3);
			multiplier2.attachSprite(digit4);
			points.attachSprite(pointSprite);
			
			Loader.getLoader().addLevelObject(pointSprite);
			Loader.getLoader().addLevelObject(x);
			Loader.getLoader().addLevelObject(xSprite);
			Loader.getLoader().addLevelObject(points);
			Loader.getLoader().addLevelObject(multiplier1);
			Loader.getLoader().addLevelObject(multiplier2);
			
	
			Renderer.getRenderer().removeSprite(gunSprite);
			gunSprite = null;
			gameListener.setGunMode(false);
		}
		
		if(bonusType == 2)
			gameListener.setDoublePoints(false);
		
		if(bonusType == 3)
			gameListener.setTriplePoints(false);
		
		if(bonusType == 6)
			gameListener.setSingleFruit(false);
		
		timer.setTimer(0);
		Renderer.getRenderer().removeSprite(bonusIcon);
		GameLoop.getGameLoop().removeListener(this);
	}
	
	public void updateGunAnimation(){
		if(mouseX < 500)
			gunSprite.changeAnimation(Integer.toString(((mouseX - 100) / 100)));
		if(mouseX > 500 && mouseX < 700)
			gunSprite.changeAnimation("4");
		if(mouseX > 700)
			gunSprite.changeAnimation(Integer.toString(((mouseX - 300) / 100)));
	}
	
	public int getBonusType(){ return bonusType; }
	
	public void setTimer(int seconds){ timer.setTimer(seconds); }
}
