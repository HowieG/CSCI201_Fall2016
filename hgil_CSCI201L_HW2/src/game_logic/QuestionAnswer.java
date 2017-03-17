package game_logic;

public class QuestionAnswer {

	private static String question;
	private static String answer;
	private static Boolean asked;

	public QuestionAnswer(String question, String answer) {
		this.question = question;
		this.answer = answer;
		asked = false;
	}

	public static String getQuestion() {
		return question;
	}

	public static Boolean hasBeenAsked() {
		return asked;
	}

	public static void setHasBeenAsked() {
		asked = true;
	}

	public static void resetHasBeenAsked() {
		asked = false;
	}

	public static String getAnswer() {
		return answer;
	}

}
