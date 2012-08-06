//Author Oscar Falk
//Edited by: Linus Thorelli

package engine;

import java.awt.Point;
import java.util.ArrayList;


public class InputHandler {
	private ArrayList<Integer> currentKeys = new ArrayList<Integer>();;
	private ArrayList<Integer> previousKeys = new ArrayList<Integer>();
	private ArrayList<Integer> releasedKeys = new ArrayList<Integer>();
	private Point currentMousePosition;
	private Point previousMousePosition;
	private ArrayList<Integer> currentMouseButtons = new ArrayList<Integer>();
	private ArrayList<Integer> previousMouseButtons = new ArrayList<Integer>();
	private ArrayList<Integer> releasedMouseButtons = new ArrayList<Integer>();
	private static InputHandler inputHandler;
	private KeyHandler keyHandler;
	private MouseHandler mouseHandler;
	
	private InputHandler(){
		keyHandler = new KeyHandler();
		mouseHandler = new MouseHandler();
		previousMousePosition = new Point();
		currentMousePosition = new Point();
		previousMouseButtons = new ArrayList<Integer>();
		currentMouseButtons = new ArrayList<Integer>();
		currentKeys = new ArrayList<Integer>();
		previousKeys = new ArrayList<Integer>();
		
	}
	
	public void pressButton(int buttonCode){
		boolean allreadyIn = false;
		for(int currentButton: currentMouseButtons){
			if (currentButton == buttonCode)
				allreadyIn = true;
		}
		if(!allreadyIn)
			currentMouseButtons.add(buttonCode);
	}
	
	public void releaseButton(int buttonCode){
		boolean allreadyIn = false;
		for(int releasedButton: releasedMouseButtons){
			if (releasedButton == buttonCode)
				allreadyIn = true;
		}
		if(!allreadyIn)
			releasedMouseButtons.add(buttonCode);
	}
	
	public void pressKey(int keyCode){
		boolean allreadyIn = false;
		for(int currentKey: currentKeys){
			if (currentKey == keyCode)
				allreadyIn = true;
		}
		if(!allreadyIn)
			currentKeys.add(keyCode);
	}
	
	public void releaseKey(int keyCode){
		boolean allreadyIn = false;
		for(int releasedKey: releasedKeys){
			if (releasedKey == keyCode)
				allreadyIn = true;
		}
		if(!allreadyIn)
			releasedKeys.add(keyCode);
	}
	
	public void nextFrame(){		
		currentKeys.removeAll(releasedKeys);
		releasedKeys.clear();
		previousKeys.clear();
		previousKeys.addAll(currentKeys);
		
		currentMouseButtons.removeAll(releasedMouseButtons);
		releasedMouseButtons.clear();
		previousMouseButtons.clear();
		previousMouseButtons.addAll(currentMouseButtons);
		previousMousePosition = currentMousePosition;
	}
	
	public static InputHandler getInputHandler(){
		if(inputHandler == null){
			inputHandler = new InputHandler();
		}
		return inputHandler;
	}
	
	public void setMousePosition(Point coordinates){ //inte riktigt framebaserad
		currentMousePosition = coordinates;
	}
	
	public boolean checkKeys(ArrayList<Integer> keysToCheck){
		boolean allPressed = true;
		
		for(int checkedKey: keysToCheck){
			boolean inCurrentKeys = false;
			
			for(int pressedKey: currentKeys){
				if(checkedKey == pressedKey)
					inCurrentKeys = true; 
			}
			
			if(!inCurrentKeys)
				allPressed = false;
		}
		return allPressed;
	}
	
	public boolean checkPreviousKeys(ArrayList<Integer> keysToCheck){
		boolean allPressed = true;
		
		for(int checkedKey: keysToCheck){
			
			boolean inCurrentKeys = false;
			for(int pressedKey: previousKeys){
				
				if(checkedKey == pressedKey)
					inCurrentKeys = true; 
			}
			if(!inCurrentKeys)
				allPressed = false;
		}
		return allPressed;
	}
	
	public boolean checkMouseButtons(ArrayList<Integer> buttonsChecked){
		boolean allPressed = true;
		for(int checkedButton: buttonsChecked){
			boolean inCurrentMouseButtons = false;
			for (int pressedButton: currentMouseButtons){
				if (checkedButton == pressedButton)
					inCurrentMouseButtons = true;
			}
			if (!inCurrentMouseButtons)
				allPressed = false;
		}
		return allPressed;
	}
	
	public boolean checkButtonsAndKeys(ArrayList<Integer> buttonsChecked, ArrayList<Integer> keysChecked){
		boolean inCurrentMouseButtons = checkMouseButtons(buttonsChecked);
		boolean inCurrentButtons = checkKeys(keysChecked);
		boolean allPressed = false;
		if(inCurrentMouseButtons && inCurrentButtons)
			allPressed = true;
		return allPressed;
	}
	
	public KeyHandler getKeyHandler(){ return keyHandler; }
	public MouseHandler getMouseHandler(){ return mouseHandler; }
	public Point getMousePosition(){ return currentMousePosition; }
	public Point getPreviousMousePosition(){ return previousMousePosition; }
	
}
