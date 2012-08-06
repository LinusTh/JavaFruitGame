//Author: Linus Thorelli

package engine;


import java.util.ArrayList;

public class SoundEngine {
	private static SoundEngine soundEngineObject;
	
	private ArrayList<Sound> soundList = new ArrayList<Sound>();
	
	private SoundEngine(){}
	
	
	public static SoundEngine getSoundEngine(){
		if(soundEngineObject == null){
			soundEngineObject = new SoundEngine();
		}
		return soundEngineObject;
	}
	
	
	public void addSound(Sound newSound){
		soundList.add(newSound);
		System.out.println("Adding sound");
	}
	
	
	public void removeSound(Sound removeSound){
		soundList.remove(removeSound);
	}
	
	
	public void removeAll(){
		soundList.clear();
	}
	
	
	public void playSound(String name){
		int i = 0;
		while(i < soundList.size()){
			if(soundList.get(i).getName().equals(name)){
				soundList.get(i).play();
				break;
			}
			i++;
		}
	}
	
	
	public void stopSound(String name){
		int i = 0;
		while(i < soundList.size() && !soundList.get(i).getName().equals(name)){
			if(soundList.get(i).getName().equals(name)){
				soundList.get(i).stop();
			}
			i++;
		}
	}	
}
