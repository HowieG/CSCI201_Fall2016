import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

//gameplay
public class Jeopardy {
	public int numTeams;
	public Scanner sc;
	public Team[] teams;
	public Team[] teamsInPlayOrder;
	public String selectedCategory;
	public int selectedPointValue;
	public Team currentTeam;
	public int indexOfCategory;
	public int indexOfPoints;
	public Boolean firstTryUsed = false;

	public static void main(String[] args) throws IOException {
		// ReadWithScanner parser = new ReadWithScanner(fileToParse);
		Jeopardy jeopardy = new Jeopardy();

		String file = args[0];

		ReadWithScanner.parse(file);
		jeopardy.chooseTeams();
	}

	public Jeopardy() {
		numTeams = 0;
		sc = new Scanner(System.in);
	}

	public void chooseTeams() {
		log("How many teams are playing?");

		try {
			numTeams = Integer.parseInt(sc.next());
		} catch (NumberFormatException e) {
			log("That's not an integer!");
		}
		while (numTeams < 1 || numTeams > 4) {
			log("Invalid input. Please enter a number 1-4.");
			try {
				numTeams = Integer.parseInt(sc.next());
			} catch (NumberFormatException e) {
				log("That's not an integer!");
			}
		}

		final int NUM_TEAMS = numTeams;
		teams = new Team[NUM_TEAMS];
		teamsInPlayOrder = new Team[NUM_TEAMS];
		nameTeams();
	}

	public void nameTeams() {
		for (int i = 1; i <= numTeams; i++) {
			log("Please enter a name for team " + i + ".");
			String enteredName = sc.next();
			// new instance of Team object
			teams[i - 1] = new Team(enteredName, 0);
		}

		Random rand = new Random();

		int firstTeam = rand.nextInt(numTeams);
		
		log(firstTeam);
		
		for (int i = 0; i < numTeams; i++) {
			if ((firstTeam + i) <= numTeams - 1) {
				teamsInPlayOrder[i] = teams[firstTeam + i];
			} else {
				teamsInPlayOrder[i] = teams[firstTeam + i - numTeams];
			}
		}

		log(teamsInPlayOrder[0].getTeamName() + " is first.");
		currentTeam = teamsInPlayOrder[firstTeam];

		// checks validity of entered team name

		// Boolean isValidName = false;
		//
		// for(int i = 1; i <= numTeams; i++)
		// {
		// log("Please enter a name for team " + i + ".");
		// String enteredName = sc.next();
		//
		// while(!isValidName)
		// {
		// for(int j = i; i > 1; j--)
		// {
		// if(enteredName.toLowerCase().equals(teams[j].getTeamName().toLowerCase()))
		// {
		// log("Name already taken. Please enter another name.");
		// enteredName = sc.next();
		// }
		// }
		// isValidName = true;
		// }
		// }

		selectCategory();
	}
	
//	public Team nextTeam(Team[] teamsInPlayOrder)
//	{
//		//if()
//	}

	public void selectCategory() {
		log("Please select a category.");
		selectedCategory = sc.next();

		indexOfCategory = -1;

		for (int i = 0; i < 5; i++) {
			//// allow user to enter name in lowercase
			if (selectedCategory.toLowerCase().equals(ReadWithScanner.parser.getCategories()[i].toLowerCase())) {
				indexOfCategory = i;
			}
		}

		while (indexOfCategory == -1) {
			log("Invalid category. Please enter a category.");
			selectedCategory = sc.next();

			for (int i = 0; i < 5; i++) {
				//// allow user to enter name in lowercase
				if (selectedCategory.toLowerCase().equals(ReadWithScanner.parser.getCategories()[i].toLowerCase())) {
					indexOfCategory = i;
				}
			}
		}

		log("You selected " + selectedCategory + ".");

		selectPoints();
	}

	public void selectPoints() {
		log("Please select a point value.");

		selectedPointValue = -2;

		try {
			selectedPointValue = Integer.parseInt(sc.next());
		} catch (NumberFormatException e) {
			log("That's not an integer!");
		}

		indexOfPoints = -1;

		for (int i = 0; i < 5; i++) {
			if (selectedPointValue == ReadWithScanner.parser.getPointValues()[i]) {
				indexOfPoints = i;
			}
		}

		while (indexOfPoints == -1) {
			log("Invalid point value. Please enter a point value.");

			try {
				selectedPointValue = Integer.parseInt(sc.next());
			} catch (NumberFormatException e) {
				log("That's not an integer!");
			}

			for (int i = 0; i < 5; i++) {
				if (selectedPointValue == ReadWithScanner.parser.getPointValues()[i]) {
					indexOfPoints = i;
				}
			}
		}
		log("You selected " + selectedPointValue + " points.");

		promptForAnswer();
	}

	public void promptForAnswer() {
		QA currQA = ReadWithScanner.parser.categoryObjects[indexOfCategory].getQA(selectedPointValue);
		log(currQA.getQuestion());

		log("Enter your answer.");
		Scanner lineScan = new Scanner(System.in);
		String enteredAnswer = lineScan.nextLine();

		String[] strArray = enteredAnswer.split("\\s+");

		checkAnswer(strArray, currQA);
	}

	public void checkAnswer(String[] splitAnswer, QA currentQA) {
		Boolean rightSyntax = false;
		Boolean rightAnswer = false;
		final String[] questionIndicators = new String[] { "who", "what", "where", "when" };
		final String[] articles = new String[] { "is", "are" };

		String correctAnswer = currentQA.getAnswer().toLowerCase();

		if (arrayContains(questionIndicators, splitAnswer[0]) && arrayContains(articles, splitAnswer[1])) {
			rightSyntax = true;
		}

		String stringToCheckIfAnswerStartedEarly1 = splitAnswer[0];
		String stringToCheckIfAnswerStartedEarly2 = splitAnswer[1];

		for (int a = 1; a < splitAnswer.length; a++) {
			stringToCheckIfAnswerStartedEarly1 += " ";
			stringToCheckIfAnswerStartedEarly1 += splitAnswer[a];
		}

		for (int b = 2; b < splitAnswer.length; b++) {
			stringToCheckIfAnswerStartedEarly2 += " ";
			stringToCheckIfAnswerStartedEarly2 += splitAnswer[b];
		}

		log(stringToCheckIfAnswerStartedEarly1);
		log(stringToCheckIfAnswerStartedEarly2);

		if (correctAnswer.equals(stringToCheckIfAnswerStartedEarly1)
				|| correctAnswer.equals(stringToCheckIfAnswerStartedEarly2)) {
			rightAnswer = true;
		}

		String trimmedAnswer = splitAnswer[2];

		for (int j = 3; j < splitAnswer.length; j++) {
			trimmedAnswer += " ";
			trimmedAnswer += splitAnswer[j];
		}

		if (trimmedAnswer.toLowerCase().equals(correctAnswer)) {
			rightAnswer = true;
		}

		if (!rightAnswer) {
			log("That is incorrect. The correct answer was " + correctAnswer);
			// subtractPoints
		} else if (rightSyntax && rightAnswer) {
			log("Correct!");
			//addPointsToTeam(currentTeam, selectedPointValue)
		} else {
			if (!firstTryUsed) {
				log("Wrong syntax! Try again.");
				firstTryUsed = true;
				promptForAnswer();
			} else {
				log("Sorry, still incorrect! The correct answer was " + currentQA.getAnswer());
				// subtractPoints
			}
		}
		
		//mark question as used
	}
	
	public void addPointsToTeam(Team teamToEdit, int pointsToAdd)
	{
		teamToEdit.addPoints(pointsToAdd);
	}
	

	public static boolean arrayContains(String[] arr, String targetValue) {
		return Arrays.asList(arr).contains(targetValue);
	}

	private static void log(Object aObject) {
		System.out.println(String.valueOf(aObject));
	}
}
