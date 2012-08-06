//Author: Linus Thorelli

package fruitGame;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class ProgressObject implements Serializable{
	private ArrayList<ArrayList<String[]>> highScores = new ArrayList<ArrayList<String[]>>();
	private int index = 0;
	private ArrayList<Integer> starsCollected = new ArrayList<Integer>();
	
	public ProgressObject(){

	}
	
	public void initialize(){
		for(int i = 0; i < 15; i++){
			highScores.add(new ArrayList<String[]>());
			starsCollected.add(0);
			addHighScore(100, "Linus", i + 1);
			addHighScore(100, "Gabriel", i + 1);
			addHighScore(100, "Pelle", i + 1);
			addHighScore(100, "Berra", i + 1);
			addHighScore(100, "Clavain", i + 1);
			addHighScore(100, "Sven", i + 1);
			addHighScore(100, "Inga", i + 1);
			addHighScore(100, "Ana", i + 1);
			addHighScore(100, "Ilia", i + 1);
			addHighScore(100, "Bert", i + 1);
		}
	}
	
	
	public int addHighScore(int score, String name, int level){
		String[] list = new String[2];
		list[0] = Integer.toString(score);
		list[1] = name;
		highScores.get(level - 1).add(list);
		sort(level - 1);
		
		if(highScores.get(level - 1).size() > 10){
			highScores.get(level - 1).remove(10);
		}
		
		//Spara när det har skett en ändring
		save();
		
		//Skicka tillbaka vilken position i listan man hamnade på
		return index +1;
	}
	
	public void addStars(int level, int numberOfStars){
		starsCollected.set(level - 1, starsCollected.get(level - 1) + numberOfStars);
		save();
	}
	
	public int getStars(int level){
		return starsCollected.get(level - 1);
	}
	
	public ArrayList<String[]> getHighScore(int level){ return highScores.get(level - 1); }
	
	public void sort(int level){
		int i;
		for(i = highScores.get(level).size()-1; i > 0 && Integer.parseInt(highScores.get(level).get(i)[0]) > Integer.parseInt(highScores.get(level).get(i-1)[0]); i--){
			String[] tmp = highScores.get(level).get(i);
			highScores.get(level).set(i, highScores.get(level).get(i-1));
			highScores.get(level).set(i-1, tmp);
        }
		index = i;
	}
	
	public void save(){
		try{
			FileOutputStream fout = new FileOutputStream("progress.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(this);
			oos.close();
		} catch (Exception e) { System.out.println(e.getMessage()); }
	}
}
