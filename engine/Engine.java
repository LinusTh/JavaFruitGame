//Author: Oscar Falk
//Edited by: Linus Thorelli

package engine;

import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JFrame;


public class Engine {
	private Loader loader;
	private Renderer renderer;
	private PhysicsEngine physicsEngine;
	private GameLoop gameLoop;
	private JFrame gameWindow;
	private static Engine gameEngine;
	private int width, height, tileSize;
	
	private Engine(int width, int height, int fps, ArrayList<Object> levelObjects, int[][] collisionArray, int tileSize, boolean calculateVisible){
		this.width = width;
		this.height = height;
		this.tileSize = tileSize;
		gameWindow = new JFrame();
		gameWindow.setSize(width, height);
		gameWindow.setResizable(false);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameWindow.setVisible(true);
		
		gameLoop = GameLoop.createGameLoop(fps);
		physicsEngine = PhysicsEngine.getPhysicsEngine();
		physicsEngine.setCollisionArray(collisionArray);
		renderer = Renderer.createRenderer(calculateVisible);
		loader = Loader.getLoader();
		gameWindow.add(RenderWindow.getRenderWindow());
		
		if(levelObjects != null){
			loader.loadLevel(levelObjects);
		}
		
		gameWindow.addComponentListener(new java.awt.event.ComponentAdapter(){
			public void componentResized(ComponentEvent e)
			{
				double xScale = gameWindow.getWidth() / (double)Engine.getEngine().getWindowWidth();
				double yScale = gameWindow.getHeight() / (double)Engine.getEngine().getWindowHeight();
				renderer.getRenderWindow().setScale(xScale, yScale);
			}
		});
	}
	
	public static Engine createEngine(int width, int height, int fps, ArrayList<Object> levelObjects, int[][] collisionArray, int tileSize, boolean calculateVisible){
		if(gameEngine == null){
			gameEngine = new Engine(width, height, fps, levelObjects, collisionArray, tileSize, calculateVisible);
		}
		return gameEngine;
	}
	
	public static Engine getEngine(){
		return gameEngine;
	}
	
	public void startEngine(){
		System.out.println("Nu startas engine");
		RenderWindow.getRenderWindow().setBufferSize(width, height);
		gameWindow.setVisible(true);
		gameLoop.setActive(true);
		gameLoop.loop();
	}
	
	
	public void setWindowSize(int width, int height){ gameWindow.setSize(width, height); }
	
	public JFrame getGameWindow(){ return gameWindow; }
	public Point getResolution(){ return new Point(width, height); }
	public int getWindowWidth(){ return width; }
	public int getWindowHeight(){ return height; }
	public int getTileSize(){ return tileSize; }
}
