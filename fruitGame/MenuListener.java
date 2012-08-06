//Author: Linus Thorelli

package fruitGame;

import java.awt.Point;
import java.util.ArrayList;

import engine.Camera;
import engine.Engine;
import engine.InputHandler;
import engine.Listener;
import engine.Loader;
import engine.Renderer;
import engine.SoundEngine;
import engine.Sprite;


public class MenuListener extends Listener{
	private double mouseX, mouseY;
	private long blinkCounter = 0, transitionCounter = 0, transitionTimer = 0;
	private boolean pressed = false, transitionEffect = false, grow = false;
	private String menuState = "start", levelVariables = "";
	private ArrayList<Integer> button1 = new ArrayList<Integer>();
	private ArrayList<Point> animPoints = new ArrayList<Point>();
	private Sprite optionsButton, levelSelectButton, level_01, level_02, level_03, level_04, level_05, level_06, level_07, level_08, level_09, level_10;
	private Sprite level_11, level_12, level_13, level_14, level_15, backButton, selectedLevel = null;
	private InputHandler inputHandler;
	private Game game;
	private Loader loader;
	private Camera camera;
	private GameListener gameListener;
	
	public MenuListener(Game game, Camera camera, ProgressObject progress){
		this.game = game;
		this.camera = camera;
		camera.setVel(1, 0);
		button1.add(1);
		inputHandler = InputHandler.getInputHandler();
		loader = Loader.getLoader();
	}
	
	public void enterFrame(long deltaTime){
		mouseX = inputHandler.getMousePosition().x;
		mouseY = inputHandler.getMousePosition().y;
		blinkCounter += deltaTime;
		
		if(!inputHandler.checkMouseButtons(button1))
			pressed = false;
		
		if(menuState.equals("start")){
			if(blinkCounter >= 500){
				game.toggleText();
				blinkCounter = 0;
			}
			
			if(inputHandler.checkMouseButtons(button1) && !pressed){
				pressed = true;
				game.startMenu();
			}
		}
		
		if(menuState.equals("menu")){
			
			//LevelSelect
			if(mouseIsOver(levelSelectButton)){
				levelSelectButton.changeAnimation("on");
						
				if(inputHandler.checkMouseButtons(button1) && !pressed){
					pressed = true;
					inputHandler.releaseButton(1);
					System.out.println("LevelSelect");
					game.levelSelect();
					SoundEngine.getSoundEngine().playSound("sounds/blip.wav");
				}
			}
			else{
				levelSelectButton.changeAnimation("off");
			}
			
			//Options
			if(mouseIsOver(optionsButton)){
				optionsButton.changeAnimation("on");
								
				if(inputHandler.checkMouseButtons(button1)  && !pressed){
					pressed = true;
					inputHandler.releaseButton(1);
					System.out.println("Options");
					SoundEngine.getSoundEngine().playSound("sounds/blip.wav");
				}
			}
			else{
				optionsButton.changeAnimation("off");
			}
		}
		
		if(menuState.equals("options")){
			
		}
		
		if(menuState.equals("levelSelect")){
			if(transitionEffect)
				selectAnimation(deltaTime);
								
			if(inputHandler.checkMouseButtons(button1) && !pressed && !transitionEffect){
				pressed = true;
				inputHandler.releaseButton(1);
					
				if(mouseIsOver(backButton)){
					game.startMenu();
					Renderer.getRenderer().removeSprite(backButton);
				}
					
				//Värld 1 --------------------------------------
				else if(mouseIsOver(level_01)){
					transitionEffect = true;
					selectedLevel = level_01;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso.png" + ":" + "images/clouds.png" + ":" + 0.5 + ":" + 0 + ":" + "images/level_01.png" + ":" + 100.0 + ":" + "1";
				}
				else if(mouseIsOver(level_02)){
					transitionEffect = true;
					selectedLevel = level_02;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso.png" + ":" + "images/clouds.png" + ":" + 0.5 + ":" + 0 + ":" + "images/level_02.png" + ":" + 100.0 + ":" + "2";
				}
				else if(mouseIsOver(level_03)){
					transitionEffect = true;
					selectedLevel = level_03;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso.png" + ":" + "images/clouds.png" + ":" + 0.5 + ":" + 0 + ":" + "images/level_03.png" + ":" + 100.0 + ":" + "3";
				}
				else if(mouseIsOver(level_04)){
					transitionEffect = true;
					selectedLevel = level_04;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso.png" + ":" + "images/clouds.png" + ":" + 0.5 + ":" + 0 + ":" + "images/level_04.png" + ":" + 100.0 + ":" + "4";
				}
				else if(mouseIsOver(level_05)){
					transitionEffect = true;
					selectedLevel = level_05;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso.png" + ":" + "images/clouds.png" + ":" + 0.5 + ":" + 0 + ":" + "images/level_05.png" + ":" + 100.0 + ":" + "5";
				}
					
				//Värld 2 --------------------------------------
				else if(mouseIsOver(level_06)){
					transitionEffect = true;
					selectedLevel = level_06;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso_02.png" + ":" + "images/water.jpg" + ":" + 0 + ":" + 0 + ":" + "images/level_01.png" + ":" + 200.0 + ":" + "6";
				}
				else if(mouseIsOver(level_07)){
					transitionEffect = true;
					selectedLevel = level_07;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso_02.png" + ":" + "images/water.jpg" + ":" + 0.4 + ":" + 0 + ":" + "images/level_07.png" + ":" + 200.0 + ":" + "7";
				}
				else if(mouseIsOver(level_08)){
					transitionEffect = true;
					selectedLevel = level_08;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso_02.png" + ":" + "images/water.jpg" + ":" + 0.4 + ":" + 0 + ":" + "images/level_08.png" + ":" + 200.0 + ":" + "8";
				}
				else if(mouseIsOver(level_09)){
					transitionEffect = true;
					selectedLevel = level_09;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso_02.png" + ":" + "images/water.jpg" + ":" + 0.4 + ":" + 0 + ":" + "images/level_09.png" + ":" + 200.0 + ":" + "9";
				}
				else if(mouseIsOver(level_10)){
					transitionEffect = true;
					selectedLevel = level_10;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso_02.png" + ":" + "images/water.jpg" + ":" + 0.4 + ":" + 0 + ":" + "images/level_10.png" + ":" + 200.0 + ":" + "10";
				}
				
				//Värld 3 --------------------------------------
				else if(mouseIsOver(level_11)){
					transitionEffect = true;
					selectedLevel = level_11;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso_03.png" + ":" + "images/lava.jpg" + ":" + 0 + ":" + 0.2 + ":" + "images/level_01.png" + ":" + 50.0 + ":" + "11";
				}
				else if(mouseIsOver(level_12)){
					transitionEffect = true;
					selectedLevel = level_12;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso_03.png" + ":" + "images/lava.jpg" + ":" + 0 + ":" + 0.2 + ":" + "images/level_12.png" + ":" + 50.0 + ":" + "12";
				}
				else if(mouseIsOver(level_13)){
					transitionEffect = true;
					selectedLevel = level_13;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso_03.png" + ":" + "images/lava.jpg" + ":" + 0 + ":" + 0.2 + ":" + "images/level_13.png" + ":" + 50.0 + ":" + "13";
				}
				else if(mouseIsOver(level_14)){
					transitionEffect = true;
					selectedLevel = level_14;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso_03.png" + ":" + "images/lava.jpg" + ":" + 0 + ":" + 0.2 + ":" + "images/level_14.png" + ":" + 50.0 + ":" + "14";
				}
				else if(mouseIsOver(level_15)){
					transitionEffect = true;
					selectedLevel = level_15;
					selectedLevel.setZPos(26);
					levelVariables = "images/background_iso_03.png" + ":" + "images/lava.jpg" + ":" + 0 + ":" + 0.2 + ":" + "images/level_15.png" + ":" + 50.0 + ":" + "15";
				}
			}
			
		}
		
		//Flytta på kameran så att bakgrunden rör sig 
		if(camera.getXPos() > 2048)
			camera.setXPos(0);
		
	}
	
	private void selectAnimation(long deltaTime){
		if(transitionTimer == 0)
			SoundEngine.getSoundEngine().playSound("sounds/start_level.wav");
		transitionCounter += deltaTime;
		transitionTimer += deltaTime;
		double xScale = selectedLevel.getXScale();
		double yScale = selectedLevel.getYScale();
		
		//Gå till rätt bana efter en sekund
		if(transitionTimer >= 1200){
			String variables[] = levelVariables.split(":");
			
			game.startLevel(variables[0], variables[1], Double.parseDouble(variables[2]), Double.parseDouble(variables[3]), variables[4], Double.parseDouble(variables[5]), Integer.parseInt(variables[6]));
			if(Integer.parseInt(variables[6]) > 5 && Integer.parseInt(variables[6]) < 11)
				gameListener.addSnowEmitters();
		}
		
		if(transitionCounter >= 20){
			if(xScale > 1){
				selectedLevel.setXPos(selectedLevel.getXPos() - (-10 + (selectedLevel.getXPos() / 4)));
				selectedLevel.setYPos(selectedLevel.getYPos() - (-6 + (selectedLevel.getYPos() / 4)));
			}
			
			if(grow && xScale < 9){
				selectedLevel.setXScale(xScale * 1.1);
				selectedLevel.setYScale(yScale * 1.1);
			}
			
			else if(xScale > 0.70 && !grow){
				selectedLevel.setXScale(xScale * 0.88);
				selectedLevel.setYScale(yScale * 0.88);
			}
			else if(xScale < 0.85){
				grow = true;
			}
		}
		
	}

	public boolean mouseIsOver(Sprite sprite){
		return mouseX > sprite.getXPos() && mouseX < sprite.getXPos() + sprite.getWidth() && mouseY > sprite.getYPos() && mouseY < sprite.getYPos() + sprite.getHeight();
	}
	
	public void setMenuState(String menuState){ this.menuState = menuState; }
	
	public void setGameListener(GameListener gameListener){
		this.gameListener = gameListener;
	}
	
	public void addMenuButtons(Sprite levelSelectButton, Sprite optionsButton){
		this.optionsButton = optionsButton;
		this.levelSelectButton = levelSelectButton;
		
		animPoints = new ArrayList<Point>();
		animPoints.add(new Point(0,0));
		levelSelectButton.createAnimation(0, animPoints, "off");
		animPoints = new ArrayList<Point>();
		animPoints.add(new Point(0, 43));
		levelSelectButton.createAnimation(0, animPoints, "on");
		
		animPoints = new ArrayList<Point>();
		animPoints.add(new Point(0,0));
		optionsButton.createAnimation(0, animPoints, "off");
		animPoints = new ArrayList<Point>();
		animPoints.add(new Point(0, 43));
		optionsButton.createAnimation(0, animPoints, "on");
		
		loader.addLevelObject(optionsButton);
		loader.addLevelObject(levelSelectButton);
	}
	
	public void addLevelIcons(ArrayList<Sprite> levelIcons){
		Renderer.getRenderer().removeSprite(optionsButton);
		Renderer.getRenderer().removeSprite(levelSelectButton);
		this.level_01 = levelIcons.get(0);
		this.level_02 = levelIcons.get(1);
		this.level_03 = levelIcons.get(2);
		this.level_04 = levelIcons.get(3);
		this.level_05 = levelIcons.get(4);
		this.level_06 = levelIcons.get(5);
		this.level_07 = levelIcons.get(6);
		this.level_08 = levelIcons.get(7);
		this.level_09 = levelIcons.get(8);
		this.level_10 = levelIcons.get(9);
		this.level_11 = levelIcons.get(10);
		this.level_12 = levelIcons.get(11);
		this.level_13 = levelIcons.get(12);
		this.level_14 = levelIcons.get(13);
		this.level_15 = levelIcons.get(14);
		
		loader.addLevelObject(level_01);
		loader.addLevelObject(level_02);
		loader.addLevelObject(level_03);
		loader.addLevelObject(level_04);
		loader.addLevelObject(level_05);
		loader.addLevelObject(level_06);
		loader.addLevelObject(level_07);
		loader.addLevelObject(level_08);
		loader.addLevelObject(level_09);
		loader.addLevelObject(level_10);
		loader.addLevelObject(level_11);
		loader.addLevelObject(level_12);
		loader.addLevelObject(level_13);
		loader.addLevelObject(level_14);
		loader.addLevelObject(level_15);
		
		backButton = new Sprite(30, 730, 64, 50,"images/back_button.png", 0, 0, false, 25, true);
		loader.addLevelObject(backButton);
	}
	
}
