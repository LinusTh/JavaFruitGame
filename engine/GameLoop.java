//Author Linus Thorelli

package engine;

import java.util.ArrayList;


public class GameLoop {
	private static GameLoop gameLoopObject;
	
	boolean active;
	private int fps, gameSkipStep;
	private long nextGameTic, previousTime, newTime, deltaTime;
	private ArrayList<Listener> listeners = new ArrayList<Listener>();
	
	public GameLoop(){}
	
	public static GameLoop createGameLoop(int fps){
		if (gameLoopObject == null){
			gameLoopObject = new GameLoop();
		}
		gameLoopObject.fps = fps;
		return gameLoopObject;
	}
	
	public static GameLoop getGameLoop(){
		if (gameLoopObject == null){
			gameLoopObject = new GameLoop();
		}
		return gameLoopObject;
	}
	
	public void addListener(Listener newListener){
		listeners.add(newListener);
	}
	
	public void removeListener(Listener removeListener){
		listeners.remove(removeListener);
	}
	
	public void fillList(ArrayList<Listener> newList){
		listeners = newList;
	}
	
	public ArrayList<Listener> getListeners(){
		return listeners;
	}
	
	public void loop(){
		
		gameSkipStep = (1000 / fps);
		
		nextGameTic = System.currentTimeMillis() + gameSkipStep;
		previousTime = System.currentTimeMillis();
		
		while(active){
			
			//Om currentTime har kommit fram till nextGameTic
			if(System.currentTimeMillis() >= nextGameTic){
				newTime = System.currentTimeMillis();
				
				//Kör enterFrame() för varje listener i listan
				for(int i = 0; i < listeners.size(); i++){
					if(!active)
						break;
					deltaTime = newTime - previousTime;
					listeners.get(i).enterFrame(deltaTime);
				}
				
				//Rensa bort alla explosioner som är klara
				for(int i = 0; i < Renderer.getRenderer().getSprites().size(); i++){
					Sprite currentSprite = Renderer.getRenderer().getSprites().get(i);
					
					if(currentSprite.getExplosion() && currentSprite.getCurrentAnimation().getFrameNumber() + 1 == currentSprite.getCurrentAnimation().getNumberOfFrames())
						Renderer.getRenderer().removeSprite(currentSprite);
				}
				
				//Kör fysiken
				newTime = System.currentTimeMillis();
				deltaTime = newTime - previousTime;
				PhysicsEngine.getPhysicsEngine().updateObjects(deltaTime);
				
				//Uppdatera kamerans position
				Renderer.getRenderer().getCamera().calculateVelocity();
				Renderer.getRenderer().getCamera().updatePosition(deltaTime);

				//Sätt ny frame för InputHandler
				InputHandler.getInputHandler().nextFrame();
				nextGameTic += gameSkipStep;
				
				
				//Rita alla objekt på skärmen
				Renderer.getRenderer().drawAll(deltaTime);
			
				previousTime = newTime;
			}
		}
	}
	
	public boolean getActive(){ return active; }
	
	public void setActive(boolean active){ this.active = active; }
}
