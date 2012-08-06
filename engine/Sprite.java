//Author: Oscar Falk
//Edited by: Linus Thorelli

package engine;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Sprite extends CoordinateObject implements Comparable{
	private ArrayList<Animation> animations;
	private String currentAnimation, filePath;
	private int height, width, imageIndex, zPos;
	private double XScrolling, YScrolling;
	private boolean explosion, uniqueImage;
	private double xScale = 1.0, yScale = 1.0;
	
	public Sprite(double x, double y, int width, int height, String filePath, double XScrolling, double YScrolling, boolean explosion, int zPos, boolean uniqueImage){
		xPos = x;
		yPos = y;
		this.zPos = zPos;
		imageIndex = -1;
		this.height = height;
		this.width = width;
		this.filePath = filePath;
		this.XScrolling = XScrolling;
		this.YScrolling = YScrolling;
		this.explosion = explosion;
		this.uniqueImage = uniqueImage;
		animations = new ArrayList<Animation>();
		ArrayList<Point> initialAnimPoints = new ArrayList<Point>();
		initialAnimPoints.add(new Point(0,0));
		createAnimation(0, initialAnimPoints, "Default");
		currentAnimation = "Default";
	}

	
	public void updatePos(double newX, double newY){
		xPos = newX;
		yPos = newY;
	}
	
	
	public void createAnimation(int speed, ArrayList<Point> sheetPositions, String name){
		Animation newAnimation = new Animation(speed, sheetPositions, name);
		if (animations.size() == 1 && getCurrentAnimation().getName().equals("Default"))
			animations.clear();
		animations.add(newAnimation);
		currentAnimation = newAnimation.getName();
	}
	
	public Animation getCurrentAnimation(){
		for (Animation a: animations){
			if (a.getName().equals(currentAnimation))
				return a;
		}
		return null;
	}
	
	public Image getCurrentAnimationFrame(){
		Point sheetPosition = getCurrentAnimation().getCurrentSheetPosition();
		return getImage().getSubimage((int)sheetPosition.getX(), (int)sheetPosition.getY(), width, height);
	}
	
	public void changeAnimation(String animation){
		for (Animation a: animations){
			if (a.getName().equals(animation))
				currentAnimation=a.getName();
		}
	}
	
	public void tick(long timeDelta){
		for (Animation a: animations){
			if(!this.getCurrentAnimation().getName().equals("Default"))
				a.tick(timeDelta);
		}
	}
	
	public int compareTo(Object otherSprite){
		if(!(otherSprite instanceof Sprite))
			throw new ClassCastException("Not a Sprite");
		return this.zPos - ((Sprite)otherSprite).zPos;
	}
	
	public void setXPos(double parentXPos){ this.xPos = parentXPos; }
	public void setYPos(double parentYPos){ this.yPos = parentYPos; }
	public void setImageIndex(int imageIndex){ this.imageIndex = imageIndex; }
	public void setExplosion(boolean explosion){ this.explosion = explosion; }
	public void setWidth(int width){ this.width = width; }
	public void setHeight(int height){ this.height = height; }
	public void setZPos(int zPos){ this.zPos = zPos; }
	public void setXScale(double xScale){ this.xScale = xScale; }
	public void setYScale(double yScale){ this.yScale = yScale; }
	
	public int getImageIndex(){ return imageIndex; }
	public String getFilePath(){ return filePath; }
	public BufferedImage getImage(){ return Renderer.getRenderer().getImage(imageIndex); }
	public double getXPos(){ return xPos; }
	public double getYPos(){ return yPos; }
	public ArrayList<Animation> getAnimations(){ return animations; }
	public double getXScrolling(){ return XScrolling; }
	public double getYScrolling(){ return YScrolling; }
	public boolean getExplosion(){ return explosion; }
	public int getHeight(){ return height; }
	public int getWidth(){ return width; }
	public boolean getUniqueImage(){ return uniqueImage; }
	public int getZPos(){ return zPos; }
	public double getXScale(){ return xScale; }
	public double getYScale(){ return yScale; }
}
