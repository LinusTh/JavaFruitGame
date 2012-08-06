//Author: Linus Thorelli

package fruitGame;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import engine.Camera;
import engine.Engine;
import engine.Loader;
import engine.PhysicsObject;
import engine.Renderer;
import engine.Sound;
import engine.Sprite;
import fruitGame.GUI;
import fruitGame.PointCounter;
import fruitGame.GUIListener;


public class Game{
	private int width = 1200, height = 828, fps = 60, tileSize = 64;
	private boolean[][] walls = new boolean[12][8];
	private String wallString, wallString2;
	private ArrayList<Object> levelObjects;
	private ArrayList<Point> positions = new ArrayList<Point>();
	private ArrayList<Sprite> levelIcons = new ArrayList<Sprite>();
	private ArrayList<Sprite> starIcons = new ArrayList<Sprite>();
	private BufferedImage bufferedImage;
	private GameListener gameListener;
	private Sprite background, title, clickToStart, picture;
	private MenuListener menuListener;
	private ProgressObject progress;
	
	public Game(){
		//Ladda in high score och dylikt
		progress = new ProgressObject();
		
		try{
			FileInputStream fin = new FileInputStream("progress.dat");
			ObjectInputStream ois = new ObjectInputStream(fin);
			progress = (ProgressObject)ois.readObject();
			ois.close();
		} catch (Exception e) { progress.initialize(); }
		
		startScreen();
		
		//Skapa och starta Engine
		Engine.createEngine(width, height, fps, levelObjects, null, tileSize, false);
		Engine.getEngine().startEngine();
	}

	public void startScreen(){
		
		levelObjects = new ArrayList<Object>();
		Camera camera = new Camera(0,0);
		levelObjects.add(camera);
		menuListener = new MenuListener(this, camera, progress);
		levelObjects.add(menuListener);
		
		title = new Sprite(250, 400, 700, 151,"images/bit_fruit.png", 0, 0, false, 25, true);
		picture = new Sprite(350, 150, 500, 237, "images/fruit_pile.png", 0, 0, false, 20, true);
		clickToStart = new Sprite(400, 650, 357, 42,"images/click_to_start.png", 0, 0, false, 25, true);
		
		levelObjects.add(title);
		levelObjects.add(picture);
		levelObjects.add(clickToStart);
		
		levelObjects.add(new Sprite(0, 0, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		levelObjects.add(new Sprite(512, 0, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		levelObjects.add(new Sprite(1024, 0, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		levelObjects.add(new Sprite(1536, 0, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		levelObjects.add(new Sprite(2048, 0, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		levelObjects.add(new Sprite(2560, 0, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		levelObjects.add(new Sprite(3072, 0, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		levelObjects.add(new Sprite(0, 512, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		levelObjects.add(new Sprite(512, 512, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		levelObjects.add(new Sprite(1024, 512, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		levelObjects.add(new Sprite(1536, 512, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		levelObjects.add(new Sprite(2048, 512, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		levelObjects.add(new Sprite(2560, 512, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		levelObjects.add(new Sprite(3072, 512, 512, 512, "images/clouds.png", 1, 0, false, 5, true));
		
		levelObjects.add(new Sprite(0, -100, 512, 512, "images/clouds.png", 0.5, 0, false, 3, true));
		levelObjects.add(new Sprite(512, -100, 512, 512, "images/clouds.png", 0.5, 0, false, 3, true));
		levelObjects.add(new Sprite(1024, -100, 512, 512, "images/clouds.png", 0.5, 0, false, 3, true));
		levelObjects.add(new Sprite(1536, -100, 512, 512, "images/clouds.png", 0.5, 0, false, 3, true));
		levelObjects.add(new Sprite(2048, -100, 512, 512, "images/clouds.png", 0.5, 0, false, 3, true));
		levelObjects.add(new Sprite(0, 412, 512, 512, "images/clouds.png", 0.5, 0, false, 3, true));
		levelObjects.add(new Sprite(512, 412, 512, 512, "images/clouds.png", 0.5, 0, false, 3, true));
		levelObjects.add(new Sprite(1024, 412, 512, 512, "images/clouds.png", 0.5, 0, false, 3, true));
		levelObjects.add(new Sprite(1536, 412, 512, 512, "images/clouds.png", 0.5, 0, false, 3, true));
		levelObjects.add(new Sprite(2048, 412, 512, 512, "images/clouds.png", 0.5, 0, false, 3, true));
		
		levelObjects.add(new Sprite(0, 0, 600, 800, "images/sky_02.jpg", 0, 0, false, 1, true));
		levelObjects.add(new Sprite(600, 0, 600, 800, "images/sky_02.jpg", 0, 0, false, 1, true));
	
		addSounds();
	}

	public void startMenu(){
		title.setZPos(1);
		clickToStart.setZPos(1);
		picture.setZPos(1);
		
		for(Sprite s : levelIcons)
			Renderer.getRenderer().removeSprite(s);
		for(Sprite s : starIcons)
			Renderer.getRenderer().removeSprite(s);
		starIcons = new ArrayList<Sprite>();
		
		menuListener.addMenuButtons(new Sprite(500, 400, 196, 43,"images/level_select.png", 0, 0, false, 25, true), new Sprite(500, 500, 196, 43,"images/options.png", 0, 0, false, 25, true));
		menuListener.setMenuState("menu");
	}
	
	public void levelSelect(){
		title.setZPos(1);
		picture.setZPos(1);
		Renderer.getRenderer().removeSprite(clickToStart);
		levelIcons = new ArrayList<Sprite>();
		
		levelIcons.add(new Sprite(200, 150, 110, 76,"images/level_01_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(350, 150, 110, 76,"images/level_02_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(500, 150, 110, 76,"images/level_03_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(650, 150, 110, 76,"images/level_04_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(800, 150, 110, 76,"images/level_05_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(200, 350, 110, 76,"images/level_06_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(350, 350, 110, 76,"images/level_07_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(500, 350, 110, 76,"images/level_08_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(650, 350, 110, 76,"images/level_09_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(800, 350, 110, 76,"images/level_10_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(200, 550, 110, 76,"images/level_11_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(350, 550, 110, 76,"images/level_12_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(500, 550, 110, 76,"images/level_13_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(650, 550, 110, 76,"images/level_14_icon.png", 0, 0, false, 25, true));
		levelIcons.add(new Sprite(800, 550, 110, 76,"images/level_15_icon.png", 0, 0, false, 25, true));
		
		menuListener.addLevelIcons(levelIcons);
		menuListener.setMenuState("levelSelect");
		
		for(int i = 0; i < 15; i++){
			addStarIcons(i + 1);
		}
	}
	
	public void optionScreen(){
		
	}
	
	public void startLevel(String bg, String scenery, double cameraXVel, double cameraYVel, String filepath, double friction, int level){
		
		levelObjects = new ArrayList<Object>();
		
		addSounds();
		
		Camera camera = new Camera(0,0);
		levelObjects.add(camera);
		
		gameListener = new GameListener(camera, this, bg, filepath, friction, scenery, cameraXVel, cameraYVel, positions, progress, level);
		menuListener.setGameListener(gameListener);
		levelObjects.add(gameListener);
		
		background = new Sprite(15, 86, 1161, 714, bg, 0, 0, false, 6, true);
	
		levelObjects.add(background);
		levelObjects.add(new Sprite(435, 0, 314, 80, "images/bonus_box.png", 0, 0, false, 24, true));
		
		Sprite sprite1 = new Sprite(64, 0, 64, 64,"images/numbers_02.png", 0, 0, false, 25, true);
		Sprite sprite2 = new Sprite(112, 0, 64, 64,"images/numbers_02.png", 0, 0, false, 25, true);
		Sprite sprite3 = new Sprite(160, 0, 64, 64,"images/numbers_02.png", 0, 0, false, 25, true);
		Sprite sprite4 = new Sprite(208, 0, 64, 64,"images/numbers_02.png", 0, 0, false, 25, true);
		Sprite sprite5 = new Sprite(256, 0, 64, 64,"images/numbers_02.png", 0, 0, false, 25, true);
		Sprite sprite6 = new Sprite(304, 0, 64, 64,"images/numbers_02.png", 0, 0, false, 25, true);
		
		PointCounter pointCounter = new PointCounter(sprite1, sprite2, sprite3, sprite4, sprite5, sprite6);
		
		Sprite sprite7 = new Sprite(900, 0, 64, 64,"images/numbers_gray.png", 0, 0, false, 25, true);
		Sprite sprite8 = new Sprite(948, 0, 64, 64,"images/numbers_gray.png", 0, 0, false, 25, true);
		Sprite colon = new Sprite(995, 0, 32, 64,"images/colon.png", 0, 0, false, 25, true);
		Sprite sprite9 = new Sprite(1020, 0, 64, 64,"images/numbers_gray.png", 0, 0, false, 25, true);
		Sprite sprite10 = new Sprite(1068, 0, 64, 64,"images/numbers_gray.png", 0, 0, false, 25, true);
		
		Sprite star1 = new Sprite(760, 25, 32, 32,"images/star_icon.png", 0, 0, false, 25, true);
		Sprite star2 = new Sprite(810, 25, 32, 32,"images/star_icon.png", 0, 0, false, 25, true);
		Sprite star3 = new Sprite(860, 25, 32, 32,"images/star_icon.png", 0, 0, false, 25, true);
		ArrayList<Point> animPoints = new ArrayList<Point>();
		animPoints.add(new Point(0, 0));
		animPoints.add(new Point(32, 0));
		star1.createAnimation(0, animPoints, "changing");
		star2.createAnimation(0, animPoints, "changing");
		star3.createAnimation(0, animPoints, "changing");
		gameListener.putStarIcons(star1, star2, star3);
		
		TimeCounter timeCounter = new TimeCounter(sprite7, sprite8, sprite9, sprite10);
		GUI gUI = new GUI(pointCounter, gameListener, timeCounter);
		GUIListener gUIListener = new GUIListener(gUI);
		
		levelObjects.add(sprite1);
		levelObjects.add(sprite2);
		levelObjects.add(sprite3);
		levelObjects.add(sprite4);
		levelObjects.add(sprite5);
		levelObjects.add(sprite6);
		levelObjects.add(sprite7);
		levelObjects.add(sprite8);
		levelObjects.add(sprite9);
		levelObjects.add(sprite10);
		levelObjects.add(star1);
		levelObjects.add(star2);
		levelObjects.add(star3);
		levelObjects.add(colon);
		levelObjects.add(pointCounter);
		levelObjects.add(gUI);
		levelObjects.add(gUIListener);
		
		
		levelObjects.add(new Sprite(0, 0, 600, 800, "images/sky_02.jpg", 0, 0, false, 1, true));
		levelObjects.add(new Sprite(600, 0, 600, 800, "images/sky_02.jpg", 0, 0, false, 1, true));
		
		levelObjects.add(new Sprite(0, 0, 512, 512, scenery, 1, 1, false, 5, true));
		levelObjects.add(new Sprite(512, 0, 512, 512, scenery, 1, 1, false, 5, true));
		levelObjects.add(new Sprite(1024, 0, 512, 512, scenery, 1, 1, false, 5, true));
		
		levelObjects.add(new Sprite(0, 512, 512, 512, scenery, 1, 1, false, 5, true));
		levelObjects.add(new Sprite(512, 512, 512, 512, scenery, 1, 1, false, 5, true));
		levelObjects.add(new Sprite(1024, 512, 512, 512, scenery, 1, 1, false, 5, true));
		
		
		if(cameraXVel > 0){
			levelObjects.add(new Sprite(1536, 0, 512, 512, scenery, 1, 1, false, 5, true));
			levelObjects.add(new Sprite(2048, 0, 512, 512, scenery, 1, 1, false, 5, true));
			levelObjects.add(new Sprite(1536, 512, 512, 512, scenery, 1, 1, false, 5, true));
			levelObjects.add(new Sprite(2048, 512, 512, 512, scenery, 1, 1, false, 5, true));
		}
		
		if(cameraYVel > 0){
			levelObjects.add(new Sprite(0, 1024, 512, 512, scenery, 1, 1, false, 5, true));
			levelObjects.add(new Sprite(512, 1024, 512, 512, scenery, 1, 1, false, 5, true));
			levelObjects.add(new Sprite(1024, 1024, 512, 512, scenery, 1, 1, false, 5, true));
		}
		
		File levelFile = new File(filepath);
		try {
			bufferedImage = ImageIO.read(levelFile);
		} catch (IOException e) { System.out.println(e.getMessage()); }
		
		if(bg.equals("images/background_iso_03.png")){
			wallString = "images/wall_02_lower.png";
			wallString2 = "images/wall_02_upper.png";
		}
		else{
			wallString = "images/wall_lower.png";
			wallString2 = "images/wall_upper.png";
		}
		
		//Loopa igenom alla pixlar i bilden och skapa väggarna
		System.out.println(bufferedImage.getRGB(0, 0));
		for(int y = 0; y < bufferedImage.getHeight(); y++){
			for(int x = 0; x < bufferedImage.getWidth(); x++){
				createWalls(bufferedImage.getRGB(x, y), x, y);
			}
		}
		
		gameListener.setWalls(walls);
		
		Loader.getLoader().loadLevel(levelObjects);
		camera.setVel(cameraXVel, cameraYVel);
	}
	
	public void addSounds(){
		levelObjects.add(new Sound("sounds/pop_02.wav", false));
		levelObjects.add(new Sound("sounds/gun_sound.wav", false));
		levelObjects.add(new Sound("sounds/bonus_sound_01.wav", false));
		levelObjects.add(new Sound("sounds/bonus_sound_02.wav", false));
		levelObjects.add(new Sound("sounds/bonus_sound_03.wav", false));
		levelObjects.add(new Sound("sounds/impact_03.wav", false));
		levelObjects.add(new Sound("sounds/impact_04.wav", false));
		levelObjects.add(new Sound("sounds/impact_05.wav", false));
		levelObjects.add(new Sound("sounds/splat_02.wav", false));
		levelObjects.add(new Sound("sounds/fruit_chain.wav", false));
		levelObjects.add(new Sound("sounds/fruit_crisp.wav", false));
		levelObjects.add(new Sound("sounds/bad_bonus.wav", false));
		levelObjects.add(new Sound("sounds/star_get.wav", false));
		levelObjects.add(new Sound("sounds/boom.wav", false));
		levelObjects.add(new Sound("sounds/fruit_medallion.wav", false));
		levelObjects.add(new Sound("sounds/blip.wav", false));
		levelObjects.add(new Sound("sounds/start_level.wav", false));
		levelObjects.add(new Sound("sounds/thud.wav", false));
		levelObjects.add(new Sound("sounds/knock.wav", false));
	}
	
	public void addStarIcons(int level){
		Sprite stars = null;
		if(level <= 5)
			stars = new Sprite(55 + (150 * level), 235, 96, 32,"images/star_sprite.png", 0, 0, false, 22, true);
		else if(level <= 10)
			stars = new Sprite(55 + (150 * level) - 750, 235 + 200, 96, 32,"images/star_sprite.png", 0, 0, false, 22, true);
		else
			stars = new Sprite(55 + (150 * level) - 1500, 235 + 400, 96, 32,"images/star_sprite.png", 0, 0, false, 22, true);
		
		starIcons.add(stars);
		
		ArrayList<Point> animPoints = new ArrayList<Point>();
		animPoints.add(new Point(0, 0));
		animPoints.add(new Point(0, 32));
		animPoints.add(new Point(0, 64));
		animPoints.add(new Point(0, 96));
		stars.createAnimation(0, animPoints, "status");
		
		if(progress.getStars(level) == 1)
			stars.getCurrentAnimation().setCurrentFrame(1);
		else if(progress.getStars(level) == 2)
			stars.getCurrentAnimation().setCurrentFrame(2);
		else if(progress.getStars(level) == 3)
			stars.getCurrentAnimation().setCurrentFrame(3);
		
		Loader.getLoader().addLevelObject(stars);
		
	}
	
	public void createWalls(int color, int x, int y){
		if(color == -16777216){
			PhysicsObject wall = new PhysicsObject(15 + (x * 97), 88 + (y * 89), 0, 0, 95, 86, true, false);
			Sprite wallSprite = new Sprite(14 + (x * 97), 88 + (y * 89), 97, 86, wallString, 0, 0, false, 20, true);
			Sprite wallSpriteUpper = new Sprite(14 + (x * 97), 50 + (y * 89), 97, 38, wallString2, 0, 0, false, 21, true);
			levelObjects.add(wallSprite);
			levelObjects.add(wallSpriteUpper);
			levelObjects.add(wall);
			walls[x][y] = true;
		}
		else if(color == -1){
			walls[x][y] = false;
			positions.add(new Point(x * 97 + 15, y * 89 + 88));
		}
	}

	public ArrayList<Point> createFrames(int startX, int startY, int numberOfFrames, int width, int height, int imageWidth){
		ArrayList<Point> animPoints = new ArrayList<Point>();
		int y = startY;
		
		for(int i = 0; i < numberOfFrames; i++){
			if(startX + i * width == imageWidth){
				y += height;
				numberOfFrames -= i;
				i = 0;
			}
			animPoints.add(new Point(startX + i * width, y));
		}
		return animPoints;
	}
	
	public void toggleText(){
		if(clickToStart.getZPos() != 1)
			clickToStart.setZPos(1);
		else
			clickToStart.setZPos(25);
	}
	
	public Sprite getBackground(){ return background; }
	public ArrayList<Point> getPositions(){ return positions; }
	public ArrayList<Object> getLevelObjects(){ return levelObjects; }
	
	public static void main(String[] args){
		new Game();
	}
}

