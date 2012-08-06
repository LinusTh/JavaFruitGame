//Author: Oscar Falk
//Edited by: Linus Thorelli

package engine;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Renderer{
	private SortedList<Sprite> renderQueue;
	private ArrayList<BufferedImage> images;
	private ArrayList<Sprite> sprites;
	private RenderWindow renderWindow;
	private Camera activeCamera;
	private boolean calculateVisible;
	
	private static Renderer rendererObject;
	
	
	private Renderer(boolean calculateVisible){
		this.calculateVisible = calculateVisible;
		renderWindow = RenderWindow.getRenderWindow();
		renderQueue = new SortedList<Sprite>();
		sprites = new ArrayList<Sprite>();
		images = new ArrayList<BufferedImage>();
	}
	
	public static Renderer getRenderer(){	
		return rendererObject; 
	}
	
	public static Renderer createRenderer(boolean calculateVisible){
		if(rendererObject == null){
			rendererObject = new Renderer(calculateVisible);
		}
		return rendererObject;
	}
	
	
	
	public void drawAll(long deltaTime){
		for (Sprite s: sprites){
			s.tick(deltaTime);
		}
		
		calculateVisibleObjects();
		renderWindow.update();
	}
	
	
	public void addSprite(Sprite sprite){
		for(Sprite s : sprites){
			if(sprite.getFilePath().equals(s.getFilePath()) && sprite.getUniqueImage()){
				sprite.setImageIndex(s.getImageIndex());
				sprites.add(sprite);
				return;
			}
		}
		
		sprites.add(sprite);
			
		sprite.setImageIndex(images.size());
		createImage(sprite.getFilePath(), sprite.getImageIndex());
	}
	
	private void createImage(String filePath, int imageIndex){
		BufferedImage loadedImage = null;
		try {
			loadedImage = ImageIO.read(new File(filePath));
		} catch (IOException e) { System.out.println(e.getMessage()); }
		
		images.add(imageIndex, loadedImage);	
	}
	
	public void changeImage(int index, BufferedImage newImage){
		images.set(index, newImage);
	}
	
	
	public void removeSprite(Sprite sprite){
		sprites.remove(sprite);
	}
	
	public void removeAll(){
		sprites.clear();
		images.clear();
		renderQueue.clear();
	}
	
	public void calculateVisibleObjects(){
		for(int i = 0; i< sprites.size(); i++){
			Sprite sprite = sprites.get(i);
			if(calculateVisible){
				if(sprite.getXPos() > renderWindow.getCameraX()-Engine.getEngine().getTileSize() && sprite.getXPos() < renderWindow.getCameraX() + Engine.getEngine().getWindowWidth() &&
					sprite.getYPos() > renderWindow.getCameraY()-Engine.getEngine().getTileSize() && sprite.getYPos() < renderWindow.getCameraY() + Engine.getEngine().getWindowHeight()){
					renderQueue.addSorted(sprite);
				}
				else if(sprite.getXScrolling() < 1 && sprite.getYScrolling() < 1){
					renderQueue.addSorted(sprite);
				}
			}
			else{
				renderQueue.addSorted(sprite);
			}
		}
	}
	
	
	
	public void setCamera(Camera camera){ activeCamera = camera; }
	public void setRenderQueue(){ renderQueue.addAll(sprites); }
	
	public RenderWindow getRenderWindow(){ return renderWindow; }
	public ArrayList<Sprite> getRenderQueue(){ return renderQueue; }
	public Camera getCamera(){ return activeCamera; }
	public BufferedImage getImage(int imageIndex){ return images.get(imageIndex); }
	public ArrayList<Sprite> getSprites(){ return sprites; }
}
