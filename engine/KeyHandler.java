//Author: Oscar Falk
//Edited by: Linus Thorelli

package engine;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener{
	
	public KeyHandler(){
		RenderWindow.getRenderWindow().addKeyListener(this);
	}

	public void keyPressed(KeyEvent evt) {
		InputHandler.getInputHandler().pressKey(evt.getKeyCode());
	}

	public void keyReleased(KeyEvent evt) {
		InputHandler.getInputHandler().releaseKey(evt.getKeyCode());
	}

	public void keyTyped(KeyEvent e) {	}

}
