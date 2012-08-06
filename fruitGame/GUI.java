//Author: Linus Thorelli

package fruitGame;

import engine.CoordinateObject;

public class GUI extends CoordinateObject{
	private int minutes, seconds, timeInt;
	private long timeTemp = 0;
	private String score = "", time = "";
	private PointCounter pointCounter;
	private TimeCounter timeCounter;
	private GameListener gameListener;
	
	public GUI(PointCounter pointCounter, GameListener gameListener, TimeCounter timeCounter){
		this.pointCounter = pointCounter;
		this.gameListener = gameListener;
		this.timeCounter = timeCounter;
		time = Integer.toString(gameListener.getTimeLimit());
		timeInt = Integer.parseInt(time);
		minutes = timeInt / 60;
		seconds = timeInt % 60;
		
		String digitOne = "0";
		String digitTwo = Integer.toString(minutes);
		String digitThree = "";
		String digitFour = "";
		if(seconds >= 10){
			digitThree = Character.toString(Integer.toString(seconds).charAt(0));
			digitFour = Character.toString(Integer.toString(seconds).charAt(1));
		}
		else{
			digitThree = "0";
			digitFour = Character.toString(Integer.toString(seconds).charAt(0));
		}
		
		timeCounter.setAnimations(digitOne, digitTwo, digitThree, digitFour);
	}
	
	public void update(long deltaTime){
		timeTemp += deltaTime;
		
		if(timeTemp >= 1000 || Long.parseLong(time) != gameListener.getTimeLeft() / 1000){
			time = Long.toString(gameListener.getTimeLeft() / 1000);
			
			timeInt = Integer.parseInt(time);
			minutes = timeInt / 60;
			seconds = timeInt % 60;
			
			String digitOne = "0";
			String digitTwo = Integer.toString(minutes);
			String digitThree = "";
			String digitFour = "";
			if(seconds >= 10){
				digitThree = Character.toString(Integer.toString(seconds).charAt(0));
				digitFour = Character.toString(Integer.toString(seconds).charAt(1));
			}
			else{
				digitThree = "0";
				digitFour = Character.toString(Integer.toString(seconds).charAt(0));
			}
			
			timeCounter.setAnimations(digitOne, digitTwo, digitThree, digitFour);
			
			gameListener.setUpdateFrequency(gameListener.getUpdateFrequency() - 4);
			timeTemp = 0;
		}
		
		if(!score.equals(Long.toString(gameListener.getScore()))){
			if(gameListener.getScore() < 10)
				score = "00000" + Long.toString(gameListener.getScore());
			else if(gameListener.getScore() < 100)
				score = "0000" + Long.toString(gameListener.getScore());
			else if(gameListener.getScore() < 1000)
				score = "000" + Long.toString(gameListener.getScore());
			else if(gameListener.getScore() < 10000)
				score = "00" + Long.toString(gameListener.getScore());
			else if(gameListener.getScore() < 100000)
				score = "0" + Long.toString(gameListener.getScore());
			else{
				score = Long.toString(gameListener.getScore());
			}
			
			String digitOne = Character.toString(score.charAt(0));
			String digitTwo = Character.toString(score.charAt(1));
			String digitThree = Character.toString(score.charAt(2));
			String digitFour = Character.toString(score.charAt(3));
			String digitFive = Character.toString(score.charAt(4));
			String digitSix = Character.toString(score.charAt(5));
			
			pointCounter.setAnimations(digitOne, digitTwo, digitThree, digitFour, digitFive, digitSix);
		}
	}
	
	public String getAnimationName(int number, int digitPosition){
		int temp = number%(int)Math.pow(10, digitPosition);
		temp /= (int)Math.pow(10, digitPosition-1);
		return "" + temp;
	}
}
