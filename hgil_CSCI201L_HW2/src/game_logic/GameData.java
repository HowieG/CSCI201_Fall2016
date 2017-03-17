package game_logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import game_GUI.GameBoard;
import game_GUI.WelcomeScreen;

public class GameData extends JFrame {

	protected BufferedReader br;
	private FileReader fr;
	// private static JFrame theView;
	private static WelcomeScreen welcomeScreen;
	private GameBoard gameBoard;
	// contains questions with their answer and boolean flag as to whether they
	// have been asked
	protected static QuestionAnswer[][] questions;

	// maps from the point value/category to their index in the appropriate
	// array
	protected static Map<Integer, Integer> pointValuesMapToIndex;
	static int[] pointsArray = new int[5];
	protected Map<String, Category> categoriesMap;

	protected String finalJeopardyQuestion;
	protected String finalJeopardyAnswer;

	public GameData(String fileName) {

		pointValuesMapToIndex = new HashMap<>();
		categoriesMap = new HashMap<>();
		questions = new QuestionAnswer[5][5];

		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			parseCategoriesAndPoints();
			parseQuestions();
		} catch (FileNotFoundException e) {
			WelcomeScreen.showFileErrorMessage("File not found! Please select another file.");
		} catch (IOException e) {
			WelcomeScreen.showFileErrorMessage("Invalid file! Please select another file");
		} finally {

			try {
				close();
			} catch (IOException ioe) {
				WelcomeScreen.showFileErrorMessage("Something went wrong!");
			}
		}

	}

	public void close() throws IOException {

		if (fr != null)
			fr.close();
		if (br != null)
			br.close();
	}

	public static void exit(String errorMessage) {

		// welcomeScreen.displayErrorMessage(errorMessage);
		// System.out.println(errorMessage);
		System.exit(0);
	}

	void displayErrorMessage(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage);
	}

	public void parseCategoriesAndPoints() throws IOException {

		String categories = br.readLine();
		String[] parsedCategories = categories.split("::");

		if (parsedCategories.length != 5) {
			WelcomeScreen.showFileErrorMessage("Too many or too few categories provided.");
		}

		for (String str : parsedCategories) {

			if (str.trim().equals("")) {
				WelcomeScreen.showFileErrorMessage("One of the categories is whitespace.");
			}
		}

		String pointValues = br.readLine();
		String[] parsedPointValues = pointValues.split("::");

		if (parsedPointValues.length != 5) {
			WelcomeScreen.showFileErrorMessage("Too many or too few dollar values provided.");
		}

		for (int i = 0; i < 5; i++) {
			gameBoard.fillCategoryArray(parsedCategories[i], i);
			categoriesMap.put(parsedCategories[i].toLowerCase().trim(), new Category(parsedCategories[i].trim(), i));

			try {
				pointValuesMapToIndex.put(Integer.parseInt(parsedPointValues[i].trim()), i);
				pointsArray[i] = Integer.parseInt(parsedPointValues[i].trim());
				// System.out.print(pointValuesMapToIndex.get(200));
			} catch (NumberFormatException nfe) {
				WelcomeScreen.showFileErrorMessage("One of the point values is a string.");
			}
		}
	}

	public void parseQuestions() throws IOException {

		String templine = "";
		String fullData = "";
		int questionCount = 0;
		boolean haveFinalJeopardy = false;

		while (questionCount != 26) {

			templine = br.readLine();
			if (templine == null) {
				WelcomeScreen.showFileErrorMessage("Not enough questions in the file");
			}

			if (!templine.startsWith("::")) {
				fullData += templine;
			} else {

				// parsePrevious question
				if (questionCount != 0) {
					haveFinalJeopardy = parseQuestions(fullData, haveFinalJeopardy);
				}

				fullData = templine.substring(2);
				questionCount++;
			}

		}

		haveFinalJeopardy = parseQuestions(fullData, haveFinalJeopardy);

		if (br.readLine() != null) {
			WelcomeScreen.showFileErrorMessage("Two many questions provided.");
		}

		if (!haveFinalJeopardy) {
			WelcomeScreen.showFileErrorMessage("This game file does not have a final jeopardy question.");
		}
	}

	private Boolean parseQuestions(String line, Boolean haveFinalJeopardy) {

		Boolean finalJeopardy = haveFinalJeopardy;

		if (line.toLowerCase().startsWith("fj")) {

			if (haveFinalJeopardy) {
				WelcomeScreen.showFileErrorMessage("Cannot have more than one final jeopardy question.");
			} else {

				parseFinalJeopardy(line);
				finalJeopardy = true;
			}

		} else {
			parseQuestionString(line);
		}
		return finalJeopardy;
	}

	private void parseFinalJeopardy(String finalJeopardyString) {

		String[] questionData = finalJeopardyString.split("::");

		if (questionData.length != 3)
			WelcomeScreen.showFileErrorMessage("Too much or not enough data provided for the final jeopardy question.");

		if (questionData[1].trim().equals(""))
			WelcomeScreen.showFileErrorMessage("The Final Jeopardy question cannot be whitespace");

		if (questionData[2].trim().equals(""))
			WelcomeScreen.showFileErrorMessage("The Final Jeopardy answer cannot be whitespace");

		finalJeopardyQuestion = questionData[1].trim();
		finalJeopardyAnswer = questionData[2].trim();

	}

	// does not check whether there is a duplicate category/point value question
	private void parseQuestionString(String question) {

		String[] questionData = question.split("::");

		if (questionData.length != 4) {
			WelcomeScreen.showFileErrorMessage("Too much or not enough data provided for this question");
		} else {

			String category = questionData[0].trim();

			if (!categoriesMap.containsKey(category.toLowerCase()))
				WelcomeScreen.showFileErrorMessage("This category does not exist: " + category);

			Integer pointValue = -1;

			try {
				pointValue = Integer.parseInt(questionData[1].trim());
			} catch (NumberFormatException nfe) {
				WelcomeScreen.showFileErrorMessage("The point value cannot be a String.");
			}

			if (!pointValuesMapToIndex.containsKey(pointValue))
				WelcomeScreen.showFileErrorMessage("This point value does not exist: " + pointValue);

			int indexX = categoriesMap.get(category.toLowerCase().trim()).getIndex();
			int indexY = pointValuesMapToIndex.get(pointValue);

			if (questionData[2].trim().equals(""))
				WelcomeScreen.showFileErrorMessage("The question cannot be whitespace.");

			if (questionData[3].trim().equals(""))
				WelcomeScreen.showFileErrorMessage("The answer cannot be whitespace.");

			questions[indexX][indexY] = new QuestionAnswer(questionData[2].trim(), questionData[3].trim());
		}
	}

	public static QuestionAnswer getQA(int x, int y) {
		return questions[x][y];
	}

	public static int getPointValue(int index) {
		return pointsArray[index];
	}

	public static class Category {

		private static String category;
		private static int index;

		public Category(String category, int index) {
			this.category = category;
			this.index = index;
		}

		public static String getCategory() {
			return category;
		}

		public static int getIndex() {
			return index;
		}
	}
}
