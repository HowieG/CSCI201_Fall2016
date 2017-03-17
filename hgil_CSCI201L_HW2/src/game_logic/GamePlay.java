package game_logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import game_GUI.GameBoard;
import game_GUI.WelcomeScreen;

public class GamePlay extends GameData {
	private static int numberOfTeams = 1;
	private static int whoseTurn;

	// how many questions have been chosen
	private static int numberOfChosenQuestions;
	// total points for each team, each TeamPoints object holds team index, team
	// points, and team name
	static List<TeamData> teamData;

	private static Set<String> unmodifiableSetAnswerVerbs;
	private static Set<String> unmodifiableSetAnswerNouns;
	private static final String EXIT = "exit";
	private static final String RESTART = "restart";
	String[] teamNames;

	// DOES NOT IMPLEMENT EXIT AND REPLAY LOGIC
	public GamePlay(String fileName, String[] teamNames, int numTeams) {
		super(fileName);

		this.teamNames = teamNames;
		setNumTeams(numTeams);

		br = new BufferedReader(new InputStreamReader(System.in));
		// initialize private variables
		numberOfChosenQuestions = 0;
		teamData = new ArrayList<>();

		initializeAnswerFormatSet();
		addTeamData(teamNames);
		String[] dummy = new String[1];
		GameBoard.main(dummy);
		playGame();
	}

	private void playGame() {
		startGame();
		// askQuestions();
		// playFinalJeopardy();
	}

	public void initializeAnswerFormatSet() {
		Set<String> nounsModifiableSet = new HashSet<>();
		Set<String> verbsModifiableSet = new HashSet<>();
		nounsModifiableSet.add("who");
		nounsModifiableSet.add("where");
		nounsModifiableSet.add("when");
		nounsModifiableSet.add("what");
		verbsModifiableSet.add("is");
		verbsModifiableSet.add("are");

		unmodifiableSetAnswerNouns = Collections.unmodifiableSet(nounsModifiableSet);
		unmodifiableSetAnswerVerbs = Collections.unmodifiableSet(verbsModifiableSet);
	}

	// increment whose turn it is
	private static int nextTurn(int currentTurn) {

		return (currentTurn + 1) == numberOfTeams ? 0 : currentTurn + 1;
	}

	// check whether the answer is in the format of a question
	private static boolean validAnswerFormat(String answer) {

		if (answer.length() < 1)
			return false;

		String[] splitAnswer = answer.trim().split("\\s+");

		if (splitAnswer.length < 2)
			return false;

		return unmodifiableSetAnswerVerbs.contains(splitAnswer[1].toLowerCase())
				&& unmodifiableSetAnswerNouns.contains(splitAnswer[0].toLowerCase());
	}

	public static void addTeamData(String[] teamNames) {

		teamData = new ArrayList<>(numberOfTeams);

		for (int i = 0; i < numberOfTeams; i++) {
			teamData.add(new TeamData(i, 0L, teamNames[i]));
		}
	}

	public static boolean findDuplicateTeamNames(String[] teamNames, int numTeams) {

		// determines whether the user gave a valid input
		boolean successful = true;

		setNumTeams(numTeams);

		// teamData = new ArrayList<>(numberOfTeams);
		Set<String> duplicateTeamNamesCheck = new HashSet<>();

		// choose team names
		for (int i = 0; i < numTeams; i++) {

			try {
				String input = teamNames[i];

				if (duplicateTeamNamesCheck.contains(input.toLowerCase())) {
					WelcomeScreen.showDialogBox("Duplicate names. Please resolve.", "Error");
					successful = false;
				} else if (input.trim().equals("")) {
					WelcomeScreen.showDialogBox("Your team name cannot be white space.", "Error");
					successful = false;
				} else {
					duplicateTeamNamesCheck.add(input.toLowerCase());
				}

			} finally {

			}
		}

		return successful;
	}

	public static void startGame() {

		Random rand = new Random();
		int firstTeam = rand.nextInt(numberOfTeams);
		teamData.get(firstTeam);
		GameBoard.updateGameProgressInfo("The team to go first will be " + teamData.get(firstTeam).getTeamName());

		whoseTurn = firstTeam;
	}

	private void checkForRestartOrExit(String line) {

		line = line.trim().toLowerCase();

		if (line.equals(EXIT)) {
			System.exit(0);
		}

		else if (line.equals(RESTART)) {
			resetData();
			playGame();
		}
	}

	public static void resetData() {
		whoseTurn = -1;
		numberOfChosenQuestions = 0;
		// total points for each team, each TeamPoints object holds team index,
		// team points, and team name
		for (TeamData team : teamData) {
			team.setPoints(0);
		}

		for (int i = 0; i < 5; i++) {

			for (int j = 0; j < 5; j++) {
				questions[i][j].resetHasBeenAsked();
			}
		}

	}

	public static void answerQuestion(int xIndex, int yIndex, int pointValue, String givenAnswer) {

		questions[xIndex][yIndex].setHasBeenAsked();

		String expectedAnswer = questions[xIndex][yIndex].getAnswer();
		boolean skipAnswerCheck = false;

		// if the answer is not in a question format, give them another
		// chance
		if (!validAnswerFormat(givenAnswer)) {
			GameBoard.showReminder(true);

			// if still not valid format, mark question as incorrect
			if (!validAnswerFormat(givenAnswer.trim())) {

				GameBoard.updateGameProgressInfo("Invalid question format. Answer marked as incorrect. " + pointValue
						+ " deducted from " + teamData.get(whoseTurn).getTeamName() + "'s total.");
				teamData.get(whoseTurn).deductPoints(pointValue);
				skipAnswerCheck = true;
			}
		}

		// if we have not already deducted points because of format, actually
		// check validity of their answer
		if (!skipAnswerCheck) {

			if (!givenAnswer.toLowerCase().endsWith(expectedAnswer.toLowerCase())) {
				GameBoard.updateGameProgressInfo(teamData.get(whoseTurn).getTeamName() + " got the answer wrong! "
						+ pointValue + " was deducted from their total.");
				GameBoard.updateGameProgressInfo("The expected answer was: " + expectedAnswer);
				teamData.get(whoseTurn).deductPoints(pointValue);
			}

			else {

				GameBoard.updateGameProgressInfo(teamData.get(whoseTurn).getTeamName() + " got the answer right! "
						+ pointValue + " was added to their total. ");
				teamData.get(whoseTurn).addPoints(pointValue);
			}
		}

		Output.printScores(numberOfTeams, teamData);

		if (numberOfChosenQuestions == 25) {
			System.out.println("All the questions have been chosen. Time for final jeopardy!");
		}

		else {
			whoseTurn = nextTurn(whoseTurn);
		}

		numberOfChosenQuestions++;
	}

	private void makeBets(List<TeamData> finalists) {
		boolean successfulBet = false;

		// let each team make a bet for final jeopardy
		for (int i = 0; i < finalists.size(); i++) {
			TeamData currentTeam = finalists.get(i);

			while (!successfulBet) {
				System.out.println("Team " + currentTeam.getTeamName()
						+ ", please give a dollar amount from your total that you would like to bet");

				try {
					String input = br.readLine();
					checkForRestartOrExit(input);
					long tempBet = Integer.parseInt(input);
					successfulBet = (tempBet > 0) && (tempBet < (currentTeam.getPoints() + 1)) ? true : false;
					currentTeam.setBet(tempBet);
				} catch (NumberFormatException e) {
				} catch (IOException e) {
				} finally {

					if (!successfulBet) {
						System.out.println("Invalid bet; Please try again.");
					}
				}
			}

			successfulBet = false;
		}

	}

	private List<TeamData> getFinalists() {
		List<TeamData> finalTeams = new ArrayList<>();

		for (int i = 0; i < teamData.size(); i++) {

			TeamData team = teamData.get(i);

			if (team.getPoints() > 0) {
				finalTeams.add(team);
			}

			else {
				System.out.println("Sorry, " + team.getTeamName() + ", you have been eliminated from the game!");
			}
		}

		return finalTeams;
	}

	private void playFinalJeopardy() {

		System.out.println("Welcome to Final Jeopardy!");
		// get the finalists for the round
		List<TeamData> finalTeams = getFinalists();

		if (finalTeams.isEmpty()) {
			exit("None of the teams made it to the Final Jeopardy round! Nobody wins, GAME OVER!");
		}

		// have all the teams make a bet
		makeBets(finalTeams);

		System.out.println("The question is: ");
		System.out.println(finalJeopardyQuestion);

		// have each question provide an answer and calculate their new score
		for (int i = 0; i < finalTeams.size(); i++) {
			TeamData currentTeam = finalTeams.get(i);
			System.out.println("Team " + currentTeam.getTeamName() + ", please enter your answer.");
			String answer = "";

			try {
				answer = br.readLine();
				checkForRestartOrExit(answer);

				if (!validAnswerFormat(answer.trim())) {
					System.out.println("Invalid question format. Your answer will be marked as incorrect");
					currentTeam.deductPoints(currentTeam.getBet());
				}

				else {

					if (answer.toLowerCase().endsWith(finalJeopardyAnswer.toLowerCase())) {
						currentTeam.addPoints(currentTeam.getBet());
					}

					else {
						currentTeam.deductPoints(currentTeam.getBet());
					}
				}

			} catch (IOException e) {
				System.out.println("Something went wrong!");
			}

			Output.printScores(finalTeams.size(), finalTeams);
		}

		ArrayList<Integer> winners = getWinners(finalTeams);

		if (winners.size() == 0) {
			System.out.println("There were no winners!");
		}

		else {
			String toPrint = winners.size() > 1 ? "And the winners are " : "And the winner is ";
			System.out.print(toPrint + finalTeams.get(winners.get(0)).getTeamName());

			if (winners.size() > 1) {

				for (int i = 1; i < winners.size(); i++) {
					System.out.print(", " + finalTeams.get(winners.get(i)).getTeamName());
				}
			}
		}
	}

	private ArrayList<Integer> getWinners(List<TeamData> finalTeams) {

		// sorts the finalists in order of their total score
		Collections.sort(finalTeams, TeamData.getComparator());
		ArrayList<Integer> winners = new ArrayList<>();

		// the team at the end of the list must have the highest score and is
		// definitely a winner
		TeamData definiteWinnerObject = finalTeams.get(finalTeams.size() - 1);
		int definiteWinner = definiteWinnerObject.getTeam();
		long max = definiteWinnerObject.getPoints();
		// if the max score is 0, we know that no one won
		if (max == 0)
			return winners;

		winners.add(definiteWinner);

		// check to see if there are other winners
		if (finalTeams.size() > 1) {

			for (int i = finalTeams.size() - 2; i > -1; i--) {

				if (finalTeams.get(i).getPoints() == max) {
					winners.add(finalTeams.get(i).getTeam());
				}
			}
		}

		return winners;
	}

	public static TeamData getTeamData(int index) {
		return teamData.get(index);
	}

	public static int getNumTeams() {
		return numberOfTeams;
	}

	public static void setNumTeams(int numTeams) {
		numberOfTeams = numTeams;
	}

	public static int getWhoseTurn() {
		return whoseTurn;
	}

}
