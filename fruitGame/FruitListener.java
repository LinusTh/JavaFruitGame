//Author: Linus Thorelli

package fruitGame;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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

public class FruitListener extends Listener{
	private int left, right, top, bottom, oldLeft, oldRight, oldTop, oldBottom;
	private int width, height, direction = -1, pointMultiplier = 1;
	private double xOffset, yOffset, mouseX, mouseY, xChange = 1.03, yChange = 0.97;
	private long wobbleCounter = 0;
	private boolean destroyed = false, collision = false;
	private String splatImage;
	private ArrayList<Fruit> collidedFruits = new ArrayList<Fruit>(), fruitChain = new ArrayList<Fruit>();
	private ArrayList <PhysicsObject> collidableObjectList = new ArrayList<PhysicsObject>();
	private Fruit fruit = null;
	private ArrayList<Integer> leftButton = new ArrayList<Integer>();
	private InputHandler inputHandler;
	private GameListener gameListener;
	
	public FruitListener(Fruit fruit, GameListener gameListener, int index){
		this.fruit = fruit;
		this.gameListener = gameListener;
		this.width = fruit.getWidth();
		this.height = fruit.getHeight();
		leftButton.add(1);
		inputHandler = InputHandler.getInputHandler();
		
		Random random = new Random();
		boolean swap = random.nextBoolean();
		if(swap)
			direction = direction * -1;
		
		swap = random.nextBoolean();
		if(swap)
			direction = direction * -1;

		
		switch(fruit.getFruitType()){
			case 0:
				splatImage = "images/beige_splat.png";
				break;
			case 1:
				splatImage = "images/yellow_splat.png";
				break;
			case 2:
				splatImage = "images/pink_splat.png";
				break;
			case 3:
				splatImage = "images/orange_splat.png";
				break;
			case 4:
				splatImage = "images/light_yellow_splat.png";
				break;
			case 5:
				splatImage = "images/green_splat.png";
				break;
		}
	}
	
	public void enterFrame(long deltaTime){
		if(gameListener.getGameOver())
			destroyed = true;
		
		if(destroyed){
			if(splatImage != null)
				drawOnGround(splatImage, fruit.getXPos() - 64 -15, fruit.getYPos() - 64 - 86);
			createPieces();
			destroyFruit();
			return;
		}
		
		mouseX = inputHandler.getMousePosition().x;
		mouseY = inputHandler.getMousePosition().y;
		
		if(gameListener.getDoublePoints())
			pointMultiplier = 2;
		else if(gameListener.getTriplePoints())
			pointMultiplier = 3;
		else if(!gameListener.getDoublePoints() && !gameListener.getTriplePoints())
			pointMultiplier = 1;
			
		
		//Om man släpper musknappen så får frukten en knuff som baseras på hur snabbt man rörde musen
		if(!inputHandler.checkMouseButtons(leftButton) && fruit.getSelected()){
				
			fruit.setSelected(false);
			fruit.setXVel((mouseX - inputHandler.getPreviousMousePosition().x) * 35);
			fruit.setYVel((inputHandler.getMousePosition().y - inputHandler.getPreviousMousePosition().y) * 35);
			fruit.setPush(new Point((int)fruit.getXVel(), (int)fruit.getYVel()));
			gameListener.setFruitSelected(false);
			
			//Nollställ skalan på frukten
			fruit.getSprite().setXScale(1);
			fruit.getSprite().setYScale(1);
		}
			
		//Om man håller in musknappen och frukten är under muspekaren så blir frukten selectad
		if(mouseX >= fruit.getXPos() && mouseX <= fruit.getXPos() + width && mouseY >= fruit.getYPos() && mouseY <= fruit.getYPos() + height &&
		   inputHandler.checkMouseButtons(leftButton) && !gameListener.getFruitSelected() && !fruit.getGravity() && !gameListener.getGunMode()){
				
			xOffset = (mouseX - fruit.getXPos());
			yOffset = (mouseY - fruit.getYPos());
			fruit.setSelected(true);
			gameListener.setFruitSelected(true);
		}
			
		if(fruit.getSelected()){
			fruit.setXPos(mouseX - xOffset);
			fruit.setYPos(mouseY - yOffset);
			wobbleFruit(deltaTime);
		}
			
			
		collidableObjectList.clear();
		collidableObjectList.addAll(gameListener.getCollidableObjectList());
		collidableObjectList.remove(fruit);

			
		fruit.getSprite().getCurrentAnimation().setCurrentFrame(0);
			
		//Kolla kollisioner
		PhysicsObject iteratorObject = null;
		for(int i = 0; i < collidableObjectList.size(); i++){
			if(collidableObjectList.get(i) instanceof Fruit){
				iteratorObject = (Fruit)collidableObjectList.get(i);
					
				//Om frukten är tillräckligt nära en annan frukt av samma sort så lyses den upp
				if(fruit.getOuterBounds().intersects(((Fruit)iteratorObject).getOuterBounds()) && fruit.getFruitType() == ((Fruit)iteratorObject).getFruitType() &&
				  !fruit.getGravity() && !iteratorObject.getGravity()){
					iteratorObject.getSprite().getCurrentAnimation().setCurrentFrame(1);
					fruit.getSprite().getCurrentAnimation().setCurrentFrame(1);
				}
			}
				
			if(collidableObjectList.get(i) instanceof BonusObject)
				iteratorObject = (BonusObject)collidableObjectList.get(i);
			else
				iteratorObject = collidableObjectList.get(i);
				
				
			//Om en frukt krockar med något
			if(PhysicsEngine.getPhysicsEngine().collision(fruit, iteratorObject) && !fruit.getGravity() && !iteratorObject.getGravity()){
					
				collision = true;
				if(iteratorObject instanceof Fruit || iteratorObject instanceof BonusObject || iteratorObject instanceof FireBall){
					
					fruit.setSelected(false);
					gameListener.setFruitSelected(false);
					destroyed = true;
						
					//Om frukten spluttas av en inkastad frukt
					if(iteratorObject instanceof Fruit){
						if(fruit.getImpact() || ((Fruit)iteratorObject).getImpact()){
							SoundEngine.getSoundEngine().playSound("sounds/splat_02.wav");
							((Fruit)iteratorObject).getListener().setDestroyed(true);
							gameListener.setFruitSelected(false);
							splatImage = "images/brown_splat.png";
							((Fruit)iteratorObject).getListener().setSplatImage(null);
							gameListener.setFruitWasted(gameListener.getFruitWasted() + 1);
						}
					}
					if(iteratorObject instanceof FireBall){
						SoundEngine.getSoundEngine().playSound("sounds/fruit_crisp.wav");
						gameListener.setFruitSelected(false);
						splatImage = "images/black_splat.png";
						gameListener.setFruitWasted(gameListener.getFruitWasted() + 1);
					}
					
					//Skapa en fruktkedja av frukterna av samma sort
					if(iteratorObject instanceof Fruit && ((Fruit) iteratorObject).getFruitType() == fruit.getFruitType()){
						fruitChain.add((Fruit)iteratorObject);
						searchFruits((Fruit)iteratorObject);
					}
						
					if(!splatImage.equals("images/brown_splat.png")){
								
						//Loopa igenom fruktkedjan, förstör frukterna och skapa rätt poäng-objekt
						int x = 1;
						for(Fruit f : fruitChain){
							Sprite pointSprite = null, pointSprite2 = null, pointSprite3 = null;
							TemporaryObject points = null, points2 = null, points3 = null;
									
							f.getListener().setDestroyed(true);
									
							int bonusPoints = (50 * (int)(Math.pow(2, x)));
							int width = 0, height = 0, direction = 0;
							String pointString;
									
							if(bonusPoints <= 1600){
								pointString = "images/" + Integer.toString(bonusPoints) + ".png";
								if(bonusPoints == 100){
									width = 68;
									height = 44;
									direction = 100;
								}
								else if(bonusPoints == 200){
									width = 71;
									height = 41;
									direction = -100;
								}
								else if(bonusPoints == 400){
									width = 73;
									height = 41;
									direction = 100;
								}
								else if(bonusPoints == 800){
									width = 72;
									height = 41;
									direction = -100;
								}
								else if(bonusPoints == 1600){
									width = 87;
									height = 41;
									direction = 150;
								}
							}
							else{
								pointString = "images/3200.png";
								width = 97;
								height = 41;
								direction = -150;
							}
									
							pointSprite = new Sprite(f.getXPos(), f.getYPos(), width, height, pointString, 0, 0, false, 23, true);
							points = new TemporaryObject(f.getXPos(), f.getYPos(), direction, -400, width, height, 2000, true);
							points.attachSprite(pointSprite);
									
							Loader.getLoader().addLevelObject(pointSprite);
							Loader.getLoader().addLevelObject(points);
									
							if(pointMultiplier == 2 || pointMultiplier == 3){
								pointSprite2 = new Sprite(f.getXPos(), f.getYPos(), width, height, pointString, 0, 0, false, 23, true);
								points2 = new TemporaryObject(f.getXPos(), f.getYPos(), -direction, -350, width, height, 2000, true);
								points2.attachSprite(pointSprite2);
								Loader.getLoader().addLevelObject(pointSprite2);
								Loader.getLoader().addLevelObject(points2);
							}
							if(pointMultiplier == 3){
								pointSprite3 = new Sprite(f.getXPos(), f.getYPos(), width, height, pointString, 0, 0, false, 23, true);
								points3 = new TemporaryObject(f.getXPos(), f.getYPos(), -direction, -450, width, height, 2000, true);
								points3.attachSprite(pointSprite3);
								Loader.getLoader().addLevelObject(pointSprite3);
								Loader.getLoader().addLevelObject(points3);
							}
									
							gameListener.addToScore(bonusPoints * pointMultiplier);
							x++;
							SoundEngine.getSoundEngine().playSound("sounds/fruit_chain.wav");
						}
						
						gameListener.setFruitJuiced(gameListener.getFruitJuiced() + fruitChain.size());
							
						//Om man det var två olika frukter som krockade
						if(fruitChain.size() == 0 && !(iteratorObject instanceof FireBall) && !(iteratorObject instanceof BonusObject)){
							SoundEngine.getSoundEngine().playSound("sounds/pop_02.wav");
							Sprite pointSprite = new Sprite(fruit.getXPos(), fruit.getYPos(), 68, 44, "images/100.png", 0, 0, false, 23, true);
							TemporaryObject points = new TemporaryObject(fruit.getXPos(), fruit.getYPos(), -50, -500, 68, 44, 2000, true);
							points.attachSprite(pointSprite);
									
							Loader.getLoader().addLevelObject(pointSprite);
							Loader.getLoader().addLevelObject(points);
								
							if(pointMultiplier == 2 || pointMultiplier == 3){
								pointSprite = new Sprite(fruit.getXPos(), fruit.getYPos(), 68, 44, "images/100.png", 0, 0, false, 23, true);
								points = new TemporaryObject(fruit.getXPos(), fruit.getYPos(), -direction, -350, 68, 44, 2000, true);
								points.attachSprite(pointSprite);
								Loader.getLoader().addLevelObject(pointSprite);
								Loader.getLoader().addLevelObject(points);
							}
							if(pointMultiplier == 3){
								pointSprite = new Sprite(fruit.getXPos(), fruit.getYPos(), 68, 44, "images/100.png", 0, 0, false, 23, true);
								points = new TemporaryObject(fruit.getXPos(), fruit.getYPos(), -direction, -450, 68, 44, 2000, true);
								points.attachSprite(pointSprite);
								Loader.getLoader().addLevelObject(pointSprite);
								Loader.getLoader().addLevelObject(points);
							}
								
							gameListener.addToScore(100 * pointMultiplier);
							gameListener.setFruitJuiced(gameListener.getFruitJuiced() + 2);
						}
					}
							
							
					if(iteratorObject instanceof Fruit){
						((Fruit) iteratorObject).getListener().setDestroyed(true);
						collidedFruits.add((Fruit)iteratorObject);
					}
					else if(iteratorObject instanceof BonusObject){
						switch(((BonusObject)iteratorObject).getBonusType()){
							case 0:
								((BonusObject)iteratorObject).addTime();
								SoundEngine.getSoundEngine().playSound("sounds/bonus_sound_02.wav");
								break;
									
							case 1:
								((BonusObject)iteratorObject).machineGun();
								SoundEngine.getSoundEngine().playSound("sounds/bonus_sound_03.wav");
								break;
										
							case 2:
								((BonusObject)iteratorObject).doublePoints();
								SoundEngine.getSoundEngine().playSound("sounds/bonus_sound_01.wav");
								break;
										
							case 3:
								((BonusObject)iteratorObject).triplePoints();
								SoundEngine.getSoundEngine().playSound("sounds/bonus_sound_01.wav");
								break;
							case 4:
								((BonusObject)iteratorObject).skull();
								SoundEngine.getSoundEngine().playSound("sounds/bad_bonus.wav");
								break;
							case 5:
								((BonusObject)iteratorObject).singleFruit();
								SoundEngine.getSoundEngine().playSound("sounds/fruit_medallion.wav");
								break;
							case 6:
								((BonusObject)iteratorObject).star(fruit.getFruitType());
								break;
						}
								
						PhysicsEngine.getPhysicsEngine().getCollisionObjects().remove(iteratorObject);
						PhysicsEngine.getPhysicsEngine().removePhysicsObject(iteratorObject);
						Renderer.getRenderer().getSprites().remove(iteratorObject.getSprite());
						Renderer.getRenderer().getSprites().remove(((BonusObject)iteratorObject).getShadow());
						iteratorObject = null;
						break;
					}
				}
				
				//Annars krockade frukten med en vägg
				else{
					if(!fruit.getImpact()){
						if(gameListener.getWind() == 0)
							SoundEngine.getSoundEngine().playSound("sounds/thud.wav");
						fruit.setSelected(false);
						gameListener.setFruitSelected(false);
							
						left = (int)fruit.getXPos();
						right = (int)fruit.getXPos() + fruit.getWidth();
						top = (int)fruit.getYPos();
						bottom = (int)fruit.getYPos() + fruit.getHeight();
							
						oldLeft = (int)fruit.getPreviousX();
						oldRight = (int)fruit.getPreviousX() + fruit.getWidth();
						oldTop = (int)fruit.getPreviousY();
						oldBottom = (int)fruit.getPreviousY() + fruit.getHeight();
							
						fruit.setXPos(oldLeft);
						fruit.setYPos(oldTop);
							
						if(collidedFromTop(iteratorObject) || collidedFromBottom(iteratorObject)){
							fruit.setYVel(-fruit.getYVel() / 2);
							fruit.setPush(new Point(fruit.getPush().x, -fruit.getPush().y / 2));
						}
						else if(collidedFromLeft(iteratorObject) || collidedFromRight(iteratorObject)){
							fruit.setXVel(-fruit.getXVel() / 2);
							fruit.setPush(new Point(-fruit.getPush().x / 2, fruit.getPush().y));
						}
					}
				}
			}
				
			if(!collision)
				collidableObjectList.remove(fruit);
		}
	}
	
	private void wobbleFruit(long deltaTime){
		wobbleCounter += deltaTime;
		
		if(wobbleCounter >= 20){
			double xScale = fruit.getSprite().getXScale();
			double yScale = fruit.getSprite().getYScale();
			
			fruit.getSprite().setXScale(xScale * xChange);
			fruit.getSprite().setYScale(yScale * yChange);
			fruit.getShadow().setXScale(xScale * xChange);
			fruit.getShadow().setYScale(yScale * yChange);
			
			if(xScale > 1.1){
				xChange = 0.97;
				yChange = 1.03;
			}
			else if(xScale < 0.9){
				xChange = 1.03;
				yChange = 0.97;
			}
			
			wobbleCounter = 0;
		}
	}

	public boolean collidedFromLeft(PhysicsObject object2){
		return oldRight < object2.getXPos() && right >= object2.getXPos();
	}
	
	public boolean collidedFromRight(PhysicsObject object2){
	    return oldLeft >= object2.getXPos() + object2.getWidth() && left < object2.getXPos() + object2.getWidth();
	}

	public boolean collidedFromTop(PhysicsObject object2){
	    return oldBottom < object2.getYPos() && bottom >= object2.getYPos();
	}

	public boolean collidedFromBottom(PhysicsObject object2){
	    return oldTop >= object2.getYPos() + object2.getHeight() && top < object2.getYPos() + object2.getHeight();
	}
	
	public void searchFruits(Fruit currentFruit){
		ArrayList <PhysicsObject> collidableObjectList = new ArrayList<PhysicsObject>();
		collidableObjectList.addAll(gameListener.getCollidableObjectList());
		
		Fruit iteratorObject = null;
		for(int i = 0; i < collidableObjectList.size(); i++){
			if(collidableObjectList.get(i) instanceof Fruit){
				iteratorObject = (Fruit)collidableObjectList.get(i);
				
				if(currentFruit.getOuterBounds().intersects(iteratorObject.getOuterBounds()) && !fruitChain.contains(iteratorObject) && iteratorObject.getFruitType() == fruitChain.get(0).getFruitType()){
					fruitChain.add(iteratorObject);
					searchFruits(iteratorObject);
				}
			}
		}
	}
	
	public void createPieces(){
		Random random = new Random();
		String image = "";
		if(fruit.getFruitType() == 0)
			image = "images/fruit_piece_red_02.png";
		else if(fruit.getFruitType() == 1 || fruit.getFruitType() == 4)
			image = "images/fruit_piece_02.png";
		else if(fruit.getFruitType() == 3)
			image = "images/fruit_piece_orange_02.png";
		else if(fruit.getFruitType() == 2)
			image = "images/fruit_piece_pink_02.png";
		else if(fruit.getFruitType() == 5)
			image = "images/fruit_piece_green_02.png";
		
		for(int i = 0; i < 80; i++){
			double x = random.nextInt(1000) - 500;
			double y = random.nextInt(1000) - 500;
			int life = random.nextInt(1500);
			
			Sprite sprite = new Sprite(fruit.getXPos() + (fruit.getWidth()/2), fruit.getYPos() +(fruit.getHeight()/2), 16, 16, image, 0, 0, false, 22, true);
			TemporaryObject piece = new TemporaryObject(fruit.getXPos(), fruit.getYPos(), x, y, 16, 16, life, true);
			piece.attachSprite(sprite);
			
			Loader.getLoader().addLevelObject(sprite);
			Loader.getLoader().addLevelObject(piece);
		}
		
	}
	
	public void drawOnGround(String filePath, double xPos, double yPos){
		Graphics g = Renderer.getRenderer().getImage(0).getGraphics();
		BufferedImage splat = null;
		try {
			splat = ImageIO.read(new File(filePath));
		} catch (IOException e) { System.out.println(e.getMessage()); }
		
		g.drawImage(splat, (int)xPos, (int)yPos, 256, 256, null);
	}
	
	public void destroyFruit(){
		Renderer.getRenderer().removeSprite(fruit.getShadow());
		PhysicsEngine.getPhysicsEngine().getCollisionObjects().remove(fruit);
		PhysicsEngine.getPhysicsEngine().removePhysicsObject(fruit);
		Renderer.getRenderer().getSprites().remove(fruit.getSprite());
		fruit = null;
		GameLoop.getGameLoop().removeListener(this);
		
		//Uppdatera kollisionslistan
		gameListener.setUpdateCollidables(true);
	}
	
	public ArrayList<Fruit> getFruitChain(){ return fruitChain; }
	
	public void setDestroyed(boolean destroyed){ this.destroyed = destroyed; }
	public void setSplatImage(String splatImage){ this.splatImage = splatImage; }
	

}
