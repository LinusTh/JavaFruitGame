//Author: Linus Thorelli

package engine;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Sound{
	private boolean looping;
	private String name;
	private Clip clip1, clip2, clip3;
	private Clip currentClip;
	private AudioInputStream ais1, ais2, ais3;
	private ArrayList<Clip> channels = new ArrayList<Clip>();
	private int index = 0;
	
	public Sound(String name, boolean looping){
		this.name = name;
		this.looping = looping;
		try {
			clip1 = AudioSystem.getClip();
			clip2 = AudioSystem.getClip();
			clip3 = AudioSystem.getClip();
			ais1 = AudioSystem.getAudioInputStream(new File(name));
			ais2 = AudioSystem.getAudioInputStream(new File(name));
			ais3 = AudioSystem.getAudioInputStream(new File(name));
			clip1.open(ais1);
			clip2.open(ais2);
			clip3.open(ais3);
		} 
		catch (LineUnavailableException e) {System.out.println(e.getMessage());}
		catch (IOException e) {System.out.println(e.getMessage());} 
		catch (UnsupportedAudioFileException e) {System.out.println(e.getMessage());}
		
		channels.add(clip1);
		channels.add(clip2);
		channels.add(clip3);
		
	}
	
	public void play(){
		currentClip = channels.get(index);
		
		if(looping == true)
			currentClip.loop(-1);
			
		else{
			currentClip.setFramePosition(0);
			currentClip.start();
		}
		index++;
		if(index == 3)
			index = 0;
	}
	
	public void stop(){
		for(int i = 0; i < channels.size(); i++){
			if(channels.get(i).isRunning()){
				channels.get(i).stop();
				channels.get(i).close();
			}
		}
	}
	
	public String getName(){
		return name;
	}
	
	public boolean getIsRunning(){
		for(int i = 0; i < channels.size(); i++){
			if(channels.get(i).isRunning())
				return true;
		}
		return false;
	}
}
