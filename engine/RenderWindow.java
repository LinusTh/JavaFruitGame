//Author: Oscar Falk
//Edited by: Linus Thorelli

package engine;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.ArrayList;

import javax.swing.*;




public class RenderWindow extends JComponent {
	private ArrayList<Sprite> rq;
	private static RenderWindow renderWindow;
	private BufferedImage offScreen, tempImage;
	private Graphics bufferG, temp;
	private AffineTransform screenTransform, spriteTransform;
	private double xScale = 1, yScale = 1;
	private int cameraX, cameraY;
	private boolean cameraStuckX = false, cameraStuckY = false;
	
	private RenderWindow(){
		this.setFocusable(true);
		requestFocusInWindow();
		screenTransform = new AffineTransform();
		spriteTransform = new AffineTransform();
	}

	public void paint(Graphics g){
		
		rq = new ArrayList<Sprite>();
		rq.addAll(Renderer.getRenderer().getRenderQueue());
		
		if(!cameraStuckX)
			cameraX = (int)Renderer.getRenderer().getCamera().getXPos();
			
		if(!cameraStuckY)
			cameraY = (int)Renderer.getRenderer().getCamera().getYPos();
		
		
		if(!rq.isEmpty())
			bufferG.fillRect(0, 0, Engine.getEngine().getWindowWidth(), Engine.getEngine().getWindowHeight());
		
		for (Sprite s: rq){
			int sx = (int)s.getXPos();
			int sy = (int)s.getYPos();
			
			Image cf = s.getCurrentAnimationFrame();
			
			spriteTransform.setToTranslation(sx - (int)(cameraX * s.getXScrolling()), sy - (int)(cameraY * s.getYScrolling()));
			spriteTransform.scale(s.getXScale(), s.getYScale());

			Graphics2D buffer2D = (Graphics2D)bufferG;
			
			buffer2D.drawImage(cf, spriteTransform, this);
		}
		
		Renderer.getRenderer().getRenderQueue().clear();
		
		screenTransform.setToScale(xScale, yScale);
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(offScreen, screenTransform, this);
	}
	
	public void update(){
		paint(this.getGraphics());
	}
	
	public static RenderWindow getRenderWindow(){
		if (renderWindow == null)
			renderWindow = new RenderWindow();
		return renderWindow;
	}
	
	public void setScale(double xScale, double yScale){
		this.xScale = xScale;
		this.yScale = yScale;
	}
	
	public void setBufferSize(int width, int height){
		offScreen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bufferG = offScreen.getGraphics();
	}
	
	public void setCameraStuckX(boolean cameraStuckX){ this.cameraStuckX = cameraStuckX; }
	public void setCameraStuckY(boolean cameraStuckY){ this.cameraStuckY = cameraStuckY; }
	public boolean getCameraStuckX(){ return cameraStuckX;}
	public boolean getCameraStuckY(){ return cameraStuckY;}
	
	public int getCameraX(){ return cameraX; }
	public int getCameraY(){ return cameraY; }
}
