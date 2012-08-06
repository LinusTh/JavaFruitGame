//Author: Linus Thorelli

package fruitGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import engine.Camera;
import engine.GameLoop;
import engine.InputHandler;
import engine.Listener;
import engine.Loader;
import engine.ParticleSystem;
import engine.PhysicsEngine;
import engine.PhysicsObject;
import engine.Renderer;
import engine.SoundEngine;
import engine.Sprite;

public class GameListener extends Listener{
	private int score = 0, stars = 0, singleFruitType, xShake = 5, yShake = 5;
	private int timeLimit = 91000, bonusFrequency = 8000, fruitFrequency = 1500, fruitJuiced = 0, fruitWasted = 0, bulletHoles = 0, washY = 3, wind = 0, level, shakeDuration, shakeTimer = 0;
	private double mouseX, mouseY, friction, cameraXVel, cameraYVel;
	private long fruitTime = 0, timeLeft, bonusTime = 0, washCounter = 0, starCounter = 0, shakeCounter;
	private boolean fruitSelected = false, sameFruits = false, gunMode = false, gameOver = false, doublePoints = false, triplePoints = false, singleFruit = false, updateCollidables = true, shaking = false;
	private boolean[][] walls;
	private String bg, filepath, scenery;
	private ArrayList<Integer> mouseButton1 = new ArrayList<Integer>();
	private ArrayList<Point> landingPoints = new ArrayList<Point>(), positions = new ArrayList<Point>();
	private ArrayList <PhysicsObject> collidableObjectList = new ArrayList<PhysicsObject>();
	private ArrayList<Point> fruitAnimPoints = new ArrayList<Point>();
	private ArrayList<String[]> highScore = new ArrayList<String[]>();
	private BufferedImage ground = null;
	
	private Sprite sprite, background, shadow, bonusShadow;
	private Sprite highScoreButton, menuButton, playButton, scraper, star1, star2, star3;
	private Fruit fruit;
	private BonusObject bonusObject;
	private Listener fruitListener;
	private BonusListener bonusListener;
	private Random random = new Random();
	private ParticleSystem left1, left2, right1, right2;
	
	private Camera camera;
	private InputHandler inputHandler;
	private Game game;
	private Loader loader;
	private ProgressObject progress;
	private Graphics panelG;
	
	public GameListener(Camera camera, Game game, String bg, String filepath, double friction, String scenery, double cameraXVel, double cameraYVel,
		   ArrayList<Point> landingPoints, ProgressObject progress, int level){
		this.camera = camera;
		this.game = game;
		this.bg = bg;
		this.filepath = filepath;
		this.friction = friction;
		this.scenery = scenery;
		this.cameraXVel = cameraXVel;
		this.cameraYVel = cameraYVel;
		this.landingPoints = landingPoints;
		this.positions = game.getPositions();
		this.progress = progress;
		this.level = level;
		
		highScore = progress.getHighScore(level);
		
		loader = Loader.getLoader();
		timeLeft = timeLimit;
		inputHandler = InputHandler.getInputHandler();
		mouseButton1.add(1);
		getNewCollidables();
	}
	
	public void enterFrame(long deltaTime){
		if(!gameOver){
			fruitTime += deltaTime;
			timeLeft -= deltaTime;
			bonusTime += deltaTime;
			
			if(shaking)
				shake(deltaTime);
			
			if(updateCollidables)
				getNewCollidables();
			
			//Kasta in en frukt när det är dags
			if(fruitTime >= fruitFrequency){
				int fruitType = 0;
				if(!singleFruit)
					fruitType = random.nextInt(6);
				else
					fruitType = singleFruitType;
				int index = random.nextInt(landingPoints.size());
				int landingY = landingPoints.get(index).y;
				int xPos = landingPoints.get(index).x;

				fruitAnimPoints = new ArrayList<Point>();
				fruitAnimPoints.add(new Point(0,0));
				
				switch(fruitType){
				
					case 0:
						sprite = new Sprite(xPos, -80, 77, 83, "images/apple_no_shadow.png", 0, 0, false, 22, true);
						fruit = new Fruit(xPos, -80, 0, 0, true, 80, 80, fruitType, this, landingY);
						shadow = new Sprite(xPos + 5, -80, 86, 92, "images/apple_shadow.png", 0, 0, false, 19, true);
						fruitAnimPoints.add(new Point(77,0));
						sprite.createAnimation(0, fruitAnimPoints, "normal");
						break;
					case 1:
						sprite = new Sprite(xPos, -80, 78, 129, "images/pineapple_no_shadow.png", 0, 0, false, 22, true);
						fruit = new Fruit(xPos, -80, 0, 0,  true, 80, 80, fruitType, this, landingY);
						shadow = new Sprite(xPos + 5, -80, 87, 139, "images/pineapple_shadow.png", 0, 0, false, 19, true);
						fruitAnimPoints.add(new Point(78,0));
						sprite.createAnimation(0, fruitAnimPoints, "normal");
						break;
					case 2:
						sprite = new Sprite(xPos, -80, 107, 80, "images/melon_slice_no_shadow.png", 0, 0, false, 22, true);
						fruit = new Fruit(xPos, -80, 0, 0,  true, 80, 80, fruitType, this, landingY);
						shadow = new Sprite(xPos + 5, -80, 116, 88, "images/melon_slice_shadow.png", 0, 0, false, 19, true);
						fruitAnimPoints.add(new Point(107,0));
						sprite.createAnimation(0, fruitAnimPoints, "normal");
						break;
					case 3:
						sprite = new Sprite(xPos, -80, 77, 74, "images/orange_no_shadow.png", 0, 0, false, 22, true);
						fruit = new Fruit(xPos, -80, 0, 0,  true, 80, 80, fruitType, this, landingY);
						shadow = new Sprite(xPos + 5, -80, 86, 82, "images/orange_shadow.png", 0, 0, false, 19, true);
						fruitAnimPoints.add(new Point(77,0));
						sprite.createAnimation(0, fruitAnimPoints, "normal");
						break;
					case 4:
						sprite = new Sprite(xPos, -80, 83, 109, "images/banana_no_shadow.png", 0, 0, false, 22, true);
						fruit = new Fruit(xPos, -80, 0, 0,  true, 80, 80, fruitType, this, landingY);
						shadow = new Sprite(xPos + 5, -80, 93, 117, "images/banana_shadow.png", 0, 0, false, 19, true);
						fruitAnimPoints.add(new Point(83,0));
						sprite.createAnimation(0, fruitAnimPoints, "normal");
						break;
					case 5:
						sprite = new Sprite(xPos, -80, 70, 104, "images/pear_no_shadow.png", 0, 0, false, 22, true);
						fruit = new Fruit(xPos, -80, 0, 0,  true, 80, 80, fruitType, this, landingY);
						shadow = new Sprite(xPos + 5, -80, 78, 112, "images/pear_shadow.png", 0, 0, false, 19, true);
						fruitAnimPoints.add(new Point(70,0));
						sprite.createAnimation(0, fruitAnimPoints, "normal");
						break;
				}
				
				fruit.attachSprite(sprite);
				fruitListener = new FruitListener(fruit, this, index);
				fruit.setListener((FruitListener)fruitListener);
				fruit.addShadow(shadow);
				
				loader.addLevelObject(sprite);
				loader.addLevelObject(fruit);
				loader.addLevelObject(shadow);
				loader.addLevelObject(fruitListener);
				
				fruitTime = 0;
				updateCollidables = true;
			}
			
			//Kasta in en random bonus när det är dags
			if(bonusTime >= bonusFrequency){
				starCounter++;
				//Om det är en isbana så blåser det
				if(bg.equals("images/background_iso_02.png"))
					changeWind();
				if(bg.equals("images/background_iso_03.png"))
					createFireball();
				int bonusType = 0;
				
				//Om det är dags att slänga in en stjärna och man har färre än tre stjärnor
				if(starCounter >= 2 && progress.getStars(level) < 3){
					bonusType = 6; 
					starCounter = 0;
				}
				//Annars slumpas bonustypen fram
				else
					bonusType = random.nextInt(5);
				
				int index = random.nextInt(landingPoints.size());
				int landingY = landingPoints.get(index).y;
				int xPos = landingPoints.get(index).x;
				int width = 0, height = 0, color = 0;
				
				switch(bonusType){
					case 0:
						width = 94;
						height= 113;
						sprite = new Sprite(xPos, -1200, 94, 113, "images/clock_no_shadow.png", 0, 0, false, 22, true);
						bonusShadow = new Sprite(xPos + 5, -80, 103, 122, "images/clock_shadow.png", 0, 0, false, 19, true);
						break;
					case 1:
						width = 119;
						height= 59;
						sprite = new Sprite(xPos, -1200, 119, 59, "images/gun_no_shadow.png", 0, 0, false, 22, true);
						bonusShadow = new Sprite(xPos + 5, -80, 125, 63, "images/gun_shadow.png", 0, 0, false, 19, true);
						break;
					case 2:
						width = 95;
						height= 88;
						sprite = new Sprite(xPos, -1200, 95, 88, "images/x2_no_shadow.png", 0, 0, false, 22, true);
						bonusShadow = new Sprite(xPos + 5, -80, 105, 97, "images/x2_shadow.png", 0, 0, false, 19, true);
						break;
					case 3:
						width = 95;
						height= 88;
						sprite = new Sprite(xPos, -1200, 95, 88, "images/x3_no_shadow.png", 0, 0, false, 22, true);
						bonusShadow = new Sprite(xPos + 5, -80, 100, 91, "images/x3_shadow.png", 0, 0, false, 19, true);
						break;
					case 4:
						width = 77;
						height= 100;
						sprite = new Sprite(xPos, -1200, 77, 100, "images/skull_no_shadow.png", 0, 0, false, 22, true);
						bonusShadow = new Sprite(xPos + 5, -80, 85, 108, "images/skull_shadow.png", 0, 0, false, 19, true);
						break;
					case 5:
						width = 80;
						height = 79;
						singleFruitType = random.nextInt(6);
						sprite = new Sprite(xPos, -1200, 80, 90, "images/medallion_" + singleFruitType + ".png", 0, 0, false, 22, true);
						bonusShadow = new Sprite(xPos + 5, -80, 82, 92, "images/medallion_shadow.png", 0, 0, false, 19, true);
						break;
					case 6:
						width = 80;
						height= 79;
						color = random.nextInt(6);
						sprite = new Sprite(xPos, -1200, 80, 84, "images/star_" + color + ".png", 0, 0, false, 22, true);
						bonusShadow = new Sprite(xPos + 5, -80, 84, 88, "images/star_shadow.png", 0, 0, false, 19, true);
						break;
				}
				
				loader.addLevelObject(sprite);
				
				bonusObject = new BonusObject(xPos, -80, 0, 0, true, width, height, bonusType, this, landingY, color);
				bonusObject.attachSprite(sprite);
				bonusObject.addShadow(bonusShadow);
				
				loader.addLevelObject(bonusObject);
				loader.addLevelObject(bonusShadow);
				
				bonusTime = 0;
				updateCollidables = true;
			}
			
			//När tiden tar slut
			if(timeLeft <= 0){
				
				if(stars > progress.getStars(level))
					progress.addStars(level, stars);
				
				Loader.getLoader().addLevelObject(new Sprite(15, 86, 1161, 714, bg, 0, 0, false, 5, false));
				
				scraper = new Sprite(0, -29, 1200, 29, "images/scraper_02.png", 0, 0, false, 23, true);
				loader.addLevelObject(scraper);
				
				gameOver = true;
				Sprite scorePanel = new Sprite(350, 80, 500, 500, "images/score_panel.png", 0, 0, false, 24, true);
				loader.addLevelObject(scorePanel);
				
				panelG = scorePanel.getImage().getGraphics();
				
				writeStatistics(panelG);
				
				//Skapa knapparna
				highScoreButton = createButton(350, 600, 200, 43, "images/view_high_score.png");
				menuButton = createButton(570, 600, 109, 43, "images/to_menu.png");
				playButton = createButton(706, 600, 144, 43, "images/play_again.png");
				
				loader.addLevelObject(highScoreButton);
				loader.addLevelObject(menuButton);
				loader.addLevelObject(playButton);
			}
		}
		
		else{
			washCounter += deltaTime;
			mouseX = inputHandler.getMousePosition().x;
			mouseY = inputHandler.getMousePosition().y;
			
			//Gucket skrapas av
			if(scraper.getYPos() <= 800){
				ground = Renderer.getRenderer().getImage(0);
				background = game.getBackground();
			
			
			
			
				if(washCounter >= 30){
					
					if(background.getHeight() > 5 && scraper.getYPos() >= 76){
						Renderer.getRenderer().changeImage(0, ground.getSubimage(0, washY, 1161, background.getHeight() - washY));
						background.setHeight(background.getHeight() - washY);
						background.setYPos(background.getYPos() + washY);
					}
					
					if(scraper.getYPos() <= 800)
						scraper.setYPos(scraper.getYPos() + washY);
					
					washCounter = 0;
				}
			}
			
			
			//Visa high score
			if(mouseX > highScoreButton.getXPos() && mouseX < highScoreButton.getXPos() + highScoreButton.getWidth() &&
			   mouseY > highScoreButton.getYPos() && mouseY < highScoreButton.getYPos() + highScoreButton.getHeight()){
				highScoreButton.changeAnimation("on");
				
				if(inputHandler.checkMouseButtons(mouseButton1)){
					panelG.setColor(Color.BLACK);
					panelG.fillRect(15, 15, 470, 470);
					panelG.setColor(Color.YELLOW);
					
					panelG.setFont(new Font("Arial", 0, 40));
					panelG.drawString("HIGH SCORES", 100, 50);
					panelG.setFont(new Font("Arial", 0, 20));
					panelG.setColor(Color.WHITE);
					
					for(int i = 0; i < highScore.size(); i++){
						panelG.drawString((i + 1) + ". " + highScore.get(i)[1], 110, i * 30 + 100);
						panelG.drawString(highScore.get(i)[0], 300, i * 30 + 100);
					}
				}
			}
			else{
				highScoreButton.changeAnimation("off");
			}
			
			//Tillbaka till menyn
			if(mouseX > menuButton.getXPos() && mouseX < menuButton.getXPos() + menuButton.getWidth() &&
			   mouseY > menuButton.getYPos() && mouseY < menuButton.getYPos() + menuButton.getHeight()){
				menuButton.changeAnimation("on");
				
				if(inputHandler.checkMouseButtons(mouseButton1)){
					inputHandler.releaseButton(1);
					game.startScreen();
					Loader.getLoader().loadLevel(game.getLevelObjects());
					game.levelSelect();
				}
			}
			else{
				menuButton.changeAnimation("off");
			}
			
			//Spela igen
			if(mouseX > playButton.getXPos() && mouseX < playButton.getXPos() + playButton.getWidth() &&
			   mouseY > playButton.getYPos() && mouseY < playButton.getYPos() + playButton.getHeight()){
				playButton.changeAnimation("on");
				
				if(inputHandler.checkMouseButtons(mouseButton1)){
					game.startLevel(bg, scenery, cameraXVel, cameraYVel, filepath, friction, level);
				}
			}
			else{
				playButton.changeAnimation("off");
			}
		}
		
		//Flytta på kameran så att bakgrunden rör sig 
		if(camera.getXPos() > 512 || camera.getXPos() < -512)
			camera.setXPos(0);
		
		if(camera.getYPos() > 512 || camera.getYPos() < -512)
			camera.setYPos(0);
		
		//Kolla om det är dags att ta bort någon temporaryObject
		for(int i = 0; i < PhysicsEngine.getPhysicsEngine().getPhysicsObjects().size(); i++){
			PhysicsObject object = PhysicsEngine.getPhysicsEngine().getPhysicsObjects().get(i);
			
			if(object instanceof TemporaryObject){
				if(((TemporaryObject)object).getLifeTime() > ((TemporaryObject)object).getTimeLimit()){
					PhysicsEngine.getPhysicsEngine().removePhysicsObject(object);
					Renderer.getRenderer().removeSprite(object.getSprite());
				}
			}
		}
		
	}
	
	public void startShaking(int shakeDuration, int xShake, int yShake){
		shaking = true;
		this.xShake = xShake;
		this.yShake = yShake;
		this.shakeDuration = shakeDuration;
	}
	
	private void shake(long deltaTime){
		shakeCounter += deltaTime;
		//shakeTimer += deltaTime;
		
		//Här måste positionen på spritesen återställas om de råkar hamna fel
		if(shakeTimer >= shakeDuration){
			shakeTimer = 0;
			shakeCounter = 0;
			shaking = false;
		}
		
		if(shakeCounter >= 20){
			
			int xOffset = 0, yOffset = 0;
			for(Sprite s : Renderer.getRenderer().getSprites()){
				s.setXPos(s.getXPos() + xShake);
				s.setYPos(s.getYPos() + yShake);
				xOffset += xShake;
				yOffset += yShake;
			}
			
			if(xOffset >= xShake * 4){
				xShake = -xShake;
				yShake = -yShake;
				shakeTimer += 1;
			}
			
			else if(xOffset <= -xShake * 4){
				xShake = -xShake;
				yShake = -yShake;
				shakeTimer += 1;
			}
			
			shakeCounter = 0;
		}
	}

	public Sprite createButton(int x, int y, int width, int height, String filepath){
		Sprite button = new Sprite(x, y, width, height, filepath, 0, 0, false, 25, true);
		ArrayList<Point> animPoints = new ArrayList<Point>();
		animPoints.add(new Point(0,0));
		button.createAnimation(0, animPoints, "off");
		animPoints = new ArrayList<Point>();
		animPoints.add(new Point(0, height));
		button.createAnimation(0, animPoints, "on");
		return button;
	}
	
	public void writeStatistics(Graphics g){
		g.setFont(new Font("Arial", 0, 30));
		g.drawString("Time's up!", 200, 40);
		
		g.setFont(new Font("Arial", 0, 20));
		g.drawString("Juiced fruit:", 50, 100);
		g.drawString("Wasted fruit:", 50, 150);
		g.drawString("High Score:", 50, 200);
		g.drawString("Total:", 50, 300);
		
		if(fruitWasted == 0){
			g.setColor(Color.GREEN);
			g.drawString("+1000", 300, 150);
			score += 1000;
			g.setColor(Color.WHITE);
		}
		
		if(fruitWasted >= 1){
			g.setColor(Color.RED);
			g.drawString("-" + (fruitWasted * 100), 300, 150);
			score -= (fruitWasted * 100);
			g.setColor(Color.WHITE);
		}
		
		if(score > Integer.parseInt(highScore.get(9)[0])){
			int position = progress.addHighScore(score, "Player", level);
			
			g.setColor(Color.YELLOW);
			g.drawString("Ranking: " + position , 320, 200);
			g.setColor(Color.WHITE);
		}
		else
			g.drawString("No High Score" , 280, 200);
		
		g.drawString(Integer.toString(fruitJuiced), 400, 100);
		g.drawString(Integer.toString(fruitWasted), 400, 150);
		g.drawString(Long.toString(score), 400, 300);
		
		
	}
	
	public void createFireball(){
		Random random = new Random();
		Sprite fireballSprite = new Sprite(100, 100, 108, 82, "images/fireball.png", 0, 0, false, 22, true);
		ArrayList<Point> animPoints = new ArrayList<Point>();
		animPoints.add(new Point(0,0));
		animPoints.add(new Point(108,0));
		animPoints.add(new Point(216,0));
		animPoints.add(new Point(324,0));
		fireballSprite.createAnimation(100, animPoints, "tumbling");
		
		FireBall fireball = new FireBall(random.nextInt(1000) + 100.0, 0, random.nextInt(100) - 50.0, 150, 82, 82);
		fireball.attachSprite(fireballSprite);
		loader.addLevelObject(fireball);
		loader.addLevelObject(fireballSprite);
	}
	
	public void addSnowEmitters(){
		if(bg.equals("images/background_iso_02.png")){
			System.out.println("isbana");
			left1 = new ParticleSystem(-512, 100, -1, 13, 10, 70, 85, 60, 16, 19, false, false, "images/snowflake_02.png", 50, false);
			left2 = new ParticleSystem(-512, 450, -1, 13, 10, 70, 85, 50, 16, 19, false, false, "images/snowflake_02.png", 50, false);
			right1 = new ParticleSystem(1536, 100, -1, 13, 10, -85, -70, 60, 16, 19, false, false, "images/snowflake_02.png", 50, false);
			right2 = new ParticleSystem(1712, 450, -1, 13, 10, -85, -70, 50, 16, 19, false, false, "images/snowflake_02.png", 50, false);
			left1.setScrolling(0, 1);
			right1.setScrolling(0, 1);
		}
	}
	
	public void changeWind(){
		if(wind == 0){
			Random random = new Random();
			if(random.nextBoolean()){
				wind = -1;
				loader.addLevelObject(right1);
				loader.addLevelObject(right2);
			}
			else{
				wind = 1;
				loader.addLevelObject(left1);
				loader.addLevelObject(left2);
			}
		}
		else{
			wind = 0;
			GameLoop.getGameLoop().removeListener(left1);
			GameLoop.getGameLoop().removeListener(left2);
			GameLoop.getGameLoop().removeListener(right1);
			GameLoop.getGameLoop().removeListener(right2);
		}
		
		System.out.println("wind: " + wind);
	}
	
	public void addStar(){
		stars++;
		if(stars == 1){
			star1.getCurrentAnimation().setCurrentFrame(1);
		}
		else if(stars == 2){
			star2.getCurrentAnimation().setCurrentFrame(1);
		}
		else if(stars == 3){
			star3.getCurrentAnimation().setCurrentFrame(1);
		}
	}
	
	public void putStarIcons(Sprite star1, Sprite star2, Sprite star3){
		this.star1 = star1;
		this.star2 = star2;
		this.star3 = star3;
	}
	
	public void removeFromCollidableObjects(PhysicsObject removeObject){
		collidableObjectList.remove(removeObject);
	}
	
	public void getNewCollidables(){
		
		collidableObjectList.clear();
		collidableObjectList.addAll(PhysicsEngine.getPhysicsEngine().getCollisionObjects());
		updateCollidables = false;
	}
	
	
	public boolean getFruitSelected(){ return fruitSelected; }
	public long getScore(){ return score; }
	public boolean getsameFruits(){ return sameFruits; }
	public int getTimeLimit(){ return timeLimit; }
	public long getTimeLeft(){ return timeLeft; }
	public int getUpdateFrequency(){ return fruitFrequency; }
	public boolean getGunMode(){ return gunMode; }
	public BonusListener getBonusListener(){ return bonusListener; }
	public boolean getGameOver(){ return gameOver; }
	public int getBulletHoles(){ return bulletHoles; }
	public int getFruitWasted(){ return fruitWasted; }
	public int getFruitJuiced(){ return fruitJuiced; }
	public boolean getDoublePoints(){ return doublePoints; }
	public boolean getTriplePoints(){ return triplePoints; }
	public boolean getSingleFruit(){ return singleFruit; }
	public ArrayList<PhysicsObject> getCollidableObjectList(){ return collidableObjectList; }
	public boolean[][] getWalls(){ return walls; }
	public double getFriction(){ return friction; }
	public int getWind(){ return wind; }
	public ArrayList<Point> getPositions(){ return positions; }
	public int getSingleFruitType(){ return singleFruitType; }
	
	public void addToScore(int score){ this.score += score; }
	public void setFruitSelected(boolean fruitSelected){ this.fruitSelected = fruitSelected; }
	public void setSameFruits(boolean sameFruits){ this.sameFruits = sameFruits; }
	public void setTimeLimit(int timeLimit){ this.timeLimit = timeLimit; }
	public void setTimePassed(long timePassed){ this.timeLeft = timePassed; }
	public void setUpdateFrequency(int updateFrequency){ this.fruitFrequency = updateFrequency; }
	public void setGunMode(boolean gunMode){ this.gunMode = gunMode; }
	public void setFruitJuiced(int fruitJuiced){ this.fruitJuiced = fruitJuiced; }
	public void setFruitWasted(int fruitWasted){ this.fruitWasted = fruitWasted; }
	public void setBulletHoles(int bulletHoles){ this.bulletHoles = bulletHoles; }
	public void setDoublePoints(boolean doublePoints){ this.doublePoints = doublePoints; }
	public void setTriplePoints(boolean triplePoints){ this.triplePoints = triplePoints; }
	public void setSingleFruit(boolean singleFruit){ this.singleFruit = singleFruit; }
	public void setBonusListener(BonusListener bonusListener){ this.bonusListener = bonusListener; }
	public void setWalls(boolean[][] walls){ this.walls = walls; }
	public void setUpdateCollidables(boolean updateCollidables){ this.updateCollidables = updateCollidables; }
}
