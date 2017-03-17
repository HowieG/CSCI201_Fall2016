package game_logic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class QuestionAnswer {
	
	protected String question;
	protected String answer;
	protected Boolean asked;
	protected String category;
	protected int pointValue;
	
	//indices of this question
	private int x;
	private int y;
	
	public QuestionAnswer(String question, String answer, String category, int pointValue, int x, int y){
		this.question = question;
		this.answer = answer;
		this.category = category;
		this.pointValue = pointValue;
		this.x = x;
		this.y = y;
		
		asked = false;
	}
	//GETTERS
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}

}
