//Author: Linus Thorelli

package engine;


public class SpriteListener extends Listener{
	private Sprite sprite;
	
	public SpriteListener(){
		
	}
	
	public void enterFrame(long deltaTime){
		if(Renderer.getRenderer().getSprites() != null){
			for(int i = 0; i < Renderer.getRenderer().getSprites().size(); i++){
				sprite = Renderer.getRenderer().getSprites().get(i);
				if(sprite.getExplosion() && sprite.getCurrentAnimation().getFrameNumber() == (sprite.getCurrentAnimation().getNumberOfFrames()-1)){
					Renderer.getRenderer().getSprites().remove(sprite);
					Renderer.getRenderer().getRenderQueue().remove(sprite);
				}
			}
		}
	}
}
