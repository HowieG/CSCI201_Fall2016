//holds the Question and Answer in two directly related arrays
public class QA
{
	private String question;
	private String answer;
	
	//default constructor;
	 public QA()
	 { 
		 question = "";
		 answer = "";
	 }
	 
	 //constructor
	 public QA(String questionToSet, String answerToSet)
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
