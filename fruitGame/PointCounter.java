//Author: Linus Thorelli

package fruitGame;

import java.awt.Point;
import java.util.ArrayList;

import engine.PhysicsObject;
import engine.Sprite;


public class PointCounter extends PhysicsObject{
	private Sprite digitOne;
	private Sprite digitTwo;
	private Sprite digitThree;
	private Sprite digitFour;
	private Sprite digitFive;
	private Sprite digitSix;
	
	
	public PointCounter(Sprite digitOne, Sprite digitTwo, Sprite digitThree, Sprite digitFour, Sprite digitFive, Sprite digitSix){
		this.digitOne	= digitOne;
		this.digitTwo	= digitTwo;
		this.digitThree	= digitThree;
		this.digitFour	= digitFour;
		this.digitFive	= digitFive;
		this.digitSix	= digitSix;
		
		for (int i = 0; i < 10; i++){
			ArrayList<Point> tmp = new ArrayList<Point>();
			tmp.add(new Point(i * 64, 0));
			digitOne.createAnimation(0, tmp, "" + i);
			digitTwo.createAnimation(0, tmp, "" + i);
			digitThree.createAnimation(0, tmp, "" + i);
			digitFour.createAnimation(0, tmp, "" + i);
			digitFive.createAnimation(0, tmp, "" + i);
			digitSix.createAnimation(0, tmp, "" + i);
		}
		
	}
	public void setAnimations(String digitOneAnimation, String digitTwoAnimation, String digitThreeAnimation, String digitFourAnimation, String digitFiveAnimation, String digitSixAnimation){
		digitOne.changeAnimation(digitOneAnimation);
		digitTwo.changeAnimation(digitTwoAnimation);
		digitThree.changeAnimation(digitThreeAnimation);
		digitFour.changeAnimation(digitFourAnimation);
		digitFive.changeAnimation(digitFiveAnimation);
		digitSix.changeAnimation(digitSixAnimation);
	}
	
	public void setPosition(int parentXPos, int parentYPos){
		this.xPos = parentXPos;
		this.yPos = parentYPos;
	}
}