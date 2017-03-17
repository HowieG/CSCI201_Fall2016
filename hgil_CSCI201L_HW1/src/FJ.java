//same implementation as QA
public class FJ
{
	private String question;
	private String answer;
	
	//default constructor;
	 public FJ()
	 { 
		 question = "";
		 answer = "";
	 }
	 
	 //constructor
	 public FJ(String questionToSet, String answerToSet)
	 {
		 setQuestion(questionToSet);
		 setAnswer(answerToSet);
	 }
	 
	 //set question
	 public void setQuestion(String questionToSet)
	 {
		 question = questionToSet;
	 }
	 
	 //set answer
	 public void setAnswer(String answerToSet)
	 {
		 answer = answerToSet;
	 }
	 
	 //get question
	 public String getQuestion()
	 {
		 return question;
	 }
	 
	 //get answer
	 public String getAnswer()
	 {
		 return answer;
	 }
}
