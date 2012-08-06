//Author Linus Thorelli

package engine;


public class CoordinateObject {
	protected double xPos, yPos;
	private boolean follow = true;
	
	public void setFollow(boolean follow){ this.follow = follow; }
	
	public double getXPos(){ return xPos; }
	public double getYPos(){ return yPos; }
	public boolean getFollow(){ return follow; }
}
