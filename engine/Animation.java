package engine;

import java.awt.Point;
import java.util.ArrayList;


public class Animation{
	private long animationFrameDuration;
	private ArrayList<Point> sheetPositions;
	private int currentFrame;
	private String name;
	private long sinceLastUpdate = 0;
	
	public Animation(int animationFrameDuration, ArrayList<Point> sheetPositions, String name){
		currentFrame = 0;
		this.animationFrameDuration = animationFrameDuration;
		this.sheetPositions = sheetPositions;
		this.name = name;
	}

	
	public Point getCurrentSheetPosition(){
		return sheetPositions.get(currentFrame);
	}
	
	public void tick(long deltaTime){

		sinceLastUpdate += deltaTime;
		if (sinceLastUpdate > animationFrameDuration && animationFrameDuration != 0){
			currentFrame++;
			if (currentFrame == sheetPositions.size())
				currentFrame = 0;
			sinceLastUpdate = 0;
		}
	}
	
	public void setCurrentFrame(int currentFrame){ this.currentFrame = currentFrame; }
	
	public String getName(){ return name; }
	public int getFrameNumber(){ return currentFrame; }
	public int getNumberOfFrames(){ return sheetPositions.size(); }
}
