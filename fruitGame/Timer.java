//Author: Linus Thorelli

package fruitGame;

import java.awt.Point;
import java.util.ArrayList;

import engine.GameLoop;
import engine.Listener;
import engine.Loader;
import engine.Renderer;
import engine.Sprite;

public class Timer extends Listener{
	private Sprite digit1, digit2;
	private int secondsLeft;
	private long timeCounter = 0;
	
	public Timer(int xPos, int yPos, int secondsLeft){
		digit1 = new Sprite(xPos, yPos, 64, 64,"images/numbers_02.png", 0, 0, false, 25, true);
		digit2 = new Sprite(xPos + 48, yPos, 64, 64,"images/numbers_02.png", 0, 0, false, 25, true);
		this.secondsLeft = secondsLeft;
		
		//Siffrornas frames
		for (int i = 0; i < 10; i++){
			ArrayList<Point> tmp = new ArrayList<Point>();
			ArrayList<Point> tmp2 = new ArrayList<Point>();
			tmp.add(new Point(i * 64, 0));
			tmp2.add(new Point(i * 48, 0));
			digit1.createAnimation(0, tmp, "" + i);
			digit2.createAnimation(0, tmp, "" + i);
		}
		
		changeDigits();
		Loader.getLoader().addLevelObject(digit1);
		Loader.getLoader().addLevelObject(digit2);
		
	}
	
	public void enterFrame(long deltaTime){
		timeCounter += deltaTime;
		
		if(timeCounter >= 1000){
			secondsLeft--;
			changeDigits();
			timeCounter = 0;
		}
		
		if(secondsLeft == 0){
			Renderer.getRenderer().removeSprite(digit1);
			Renderer.getRenderer().removeSprite(digit2);
			GameLoop.getGameLoop().removeListener(this);
		}
		
	}
	
	public void changeDigits(){
		if(secondsLeft < 10){
			digit1.changeAnimation(Integer.toString(0));
			digit2.changeAnimation(Integer.toString(secondsLeft));	
		}
		else{
			digit1.changeAnimation(Integer.toString(secondsLeft / 10));
			digit2.changeAnimation(Integer.toString(secondsLeft % 10));	
		}
	}
	
	public void addTimer(int seconds){
		this.secondsLeft += seconds;
		changeDigits();
	}
	public void setTimer(int seconds){
		this.secondsLeft = seconds; 
		changeDigits();
	}
	
	public int getSecondsLeft(){ return secondsLeft; }
}
