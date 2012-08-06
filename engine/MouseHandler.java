//Author: Oscar Falk
//Edited by: Linus Thorelli

package engine;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class MouseHandler implements MouseListener, MouseMotionListener{
	
	public MouseHandler(){
		RenderWindow.getRenderWindow().addMouseListener(this);
		RenderWindow.getRenderWindow().addMouseMotionListener(this);
	}

	public void mousePressed(MouseEvent evt) {
		InputHandler.getInputHandler().pressButton(evt.getButton());
	}

	public void mouseReleased(MouseEvent evt) {
		InputHandler.getInputHandler().releaseButton(evt.getButton());
	}

	public void mouseMoved(MouseEvent evt) {
		InputHandler.getInputHandler().setMousePosition(evt.getPoint());
	}
	
	
	public void mouseDragged(MouseEvent evt) { 
		InputHandler.getInputHandler().setMousePosition(evt.getPoint()); 
	}
	
	public void mouseClicked(MouseEvent evt) { }
	public void mouseEntered(MouseEvent evt) { }
	public void mouseExited(MouseEvent evt) { }
}
