//Author: Linus Thorelli

package fruitGame;

import engine.Listener;
import fruitGame.GUI;


public class GUIListener extends Listener{
	GUI object;
	
	public GUIListener(GUI object){
		this.object = object;
	}
	
	public void enterFrame(long deltaTime){
		object.update(deltaTime);
	}
}
