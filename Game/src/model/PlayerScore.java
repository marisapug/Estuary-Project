package model;

import java.io.Serializable;

/**
 * PlayerScore contains the information for the entries that go into the score board for the maze game.
 * @author Logan
 *
 */
public class PlayerScore implements Serializable{
	private String name;
	private int score;
	
	public PlayerScore(String n, int s){
		name = n;
		score = s;
	}
	
	//GETTERS
	public String getName(){
		return name;
	}
	public int getScore(){
		return score;
	}
	
	//SETTERS
	public void setName(String n){
		name = n;
	}
	public void setScore(int s){
		score = s;
	}
	

	
}
