//Author: Linus Thorelli

package engine;

import java.util.ArrayList;
import java.util.Iterator;


public class Loader {
	private static Loader loaderObject;
	
	private ArrayList<Object> levelObjects = new ArrayList<Object>();
	
	
	private Loader(){}
	
	public static Loader getLoader(){
		if(loaderObject == null){
			loaderObject = new Loader();
		}
		return loaderObject;
	}
	
	public void loadLevel(ArrayList<Object> levelObjects){
		
		this.levelObjects.clear();
		GameLoop.getGameLoop().setActive(false);
		Renderer.getRenderer().removeAll();
		PhysicsEngine.getPhysicsEngine().removeAll();
		GameLoop.getGameLoop().getListeners().clear();
		SoundEngine.getSoundEngine().removeAll();
		
		this.levelObjects = levelObjects;
		
		if(this.levelObjects.isEmpty())
			System.out.println("levelObjects är tom!");
		
		Iterator<Object> iterator = levelObjects.listIterator(0);
		while(iterator.hasNext()){
			Object temp = iterator.next();
			if(temp instanceof PhysicsObject )
				PhysicsEngine.getPhysicsEngine().addPhysicsObject((PhysicsObject)temp);
			else if(temp instanceof Sprite){
				Renderer.getRenderer().addSprite((Sprite)temp);
			}
			else if(temp instanceof Listener)
				GameLoop.getGameLoop().addListener((Listener)temp);
			else if(temp instanceof Sound)
				SoundEngine.getSoundEngine().addSound((Sound)temp);
			else if(temp instanceof Camera)
				Renderer.getRenderer().setCamera((Camera)temp);
		}
		GameLoop.getGameLoop().setActive(true);
	}
	
	
	public void addLevelObject(Object newObject){
		levelObjects.add(newObject);
		
		//Skicka objektet till rätt sub-system
		if(newObject instanceof PhysicsObject )
			PhysicsEngine.getPhysicsEngine().addPhysicsObject((PhysicsObject)newObject);
		else if(newObject instanceof Sprite)
			Renderer.getRenderer().addSprite((Sprite)newObject);
		else if(newObject instanceof Listener)
			GameLoop.getGameLoop().addListener((Listener)newObject);
		else if(newObject instanceof Sound)
			SoundEngine.getSoundEngine().addSound((Sound)newObject);
	}
	
	public void removeObject(Object removeObject){
		levelObjects.remove(removeObject);
	}
	
	public ArrayList<Object> getLevelObjects(){
		return levelObjects;
	}
}
