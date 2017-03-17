package game_GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import game_logic.GameData;
import game_logic.GamePlay;
import game_logic.QuestionAnswer;
import game_logic.TeamData;

public class GameBoard extends JFrame {

	public static final long serialVersionUID = 2;
	static int numTeams = 1;
	static int display = 1;
	static int money = 100;
	static String gameFile;

	static JPanel boardArea;
	static JPanel board;
	static JPanel question;
	static JPanel FJ;
	static JButton submitButton;
	static JTextField entryTextField;

	static JLabel[] questionHeader = new JLabel[3];

	static int indexofCurrentCategory;
	static int indexofCurrentValue;

	static JSlider[] betSliders;
	static JLabel[] betLabels;
	static JButton[] setBetButtons;
	static JTextField[] entries;
	static JButton[] submitAnswerButtons;

	static QuestionAnswer currentQuestion;

	static String currentCategory;
	static int currentPoints;

	static JLabel reminder;

	static JTextArea questionTextArea;

	static JButton[][] questionButtons = new QuestionCards[5][5];

	TeamData teamData;

	static String gplText = "Welcome to Jeopardy!";

	int firstTeam;

	static String[] categoriesArray = new String[5];

	// JMenuBar menuBar;
	JMenu exit, restart, chooseNewFile;
	static JTextArea gameProgressInfo;

	public GameBoard() {
		super("Jeopardy!!");

		numTeams = GamePlay.getNumTeams();

		betSliders = new JSlider[numTeams];
		betLabels = new JLabel[numTeams];
		setBetButtons = new JButton[numTeams];
		entries = new JTextField[numTeams];
		submitAnswerButtons = new JButton[numTeams];

		createGUI();
		addEvents();

		createMenu();
	}

	public static void setTheQuestion(String q) {
		questionTextArea.setText(q);
		submitButton.addActionListener(submitListener);
	}

	public static void updateBetLabels(String bet, int row) {
		betLabels[row].setText(bet);
	}

	public static void setDisplay(int i) {
		display = i;
		switch (display) {
		case 1:
			boardArea.setBackground(InitializeComponents.lightGray);
			board.setVisible(true);
			question.setVisible(false);
			FJ.setVisible(false);
			break;
		case 2:
			boardArea.setBackground(InitializeComponents.darkGray);
			board.setVisible(false);
			question.setVisible(true);
			FJ.setVisible(true);
			break;
		case 3:
			boardArea.setBackground(InitializeComponents.darkGray);
			board.setVisible(false);
			question.setVisible(false);
			FJ.setVisible(true);
			break;
		default:
			// throw something
			break;
		}
	}

	private void addEvents() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public static void addComponentsToPane(Container pane) {

		boardArea = new JPanel();
		boardArea.setBackground(InitializeComponents.darkGray);
		boardArea.setPreferredSize(new Dimension(1200, 825));
		pane.add(boardArea, BorderLayout.CENTER);

		board = addGameBoard(boardArea);

		question = addQuestionScreen(boardArea);

		FJ = addFinalJeopardyScreen(boardArea);

		setDisplay(1);

		JPanel sidebar = new JPanel();
		sidebar.setOpaque(true);
		sidebar.setBackground(InitializeComponents.darkGray);
		sidebar.setPreferredSize(new Dimension(300, 825));
		pane.add(sidebar, BorderLayout.EAST);

		fillSideBar(sidebar);
	}

	private static JPanel addFinalJeopardyScreen(Container pane) {
		JPanel FJContainer = new JPanel();
		FJContainer.setOpaque(false);
		FJContainer.setPreferredSize(new Dimension(1000, 850));
		FJContainer.setLayout(new BoxLayout(FJContainer, BoxLayout.Y_AXIS));

		addHeader(FJContainer);

		addBettingPanels(FJContainer, numTeams);

		addAnswerArea(FJContainer);

		pane.add(FJContainer);

		return FJContainer;
	}

	private static void addAnswerArea(Container pane) {

		JPanel FJQuestionContainer = InitializeComponents.addAPanel(pane);
		FJQuestionContainer.setOpaque(true);
		FJQuestionContainer.setBackground(InitializeComponents.lightBlue);
		FJQuestionContainer.setPreferredSize(new Dimension(925, 50));
		FJQuestionContainer.setMaximumSize(new Dimension(925, 50));

		JLabel FJQuestionLabel = InitializeComponents.addALabel("And the question is...", FJQuestionContainer);
		FJQuestionLabel.setFont((new Font("Serif", Font.BOLD, 22)));
		FJQuestionLabel.setForeground(InitializeComponents.darkGray);

		addEntryPanels(pane, numTeams); // not numTeams! How many
										// teams eligible for FJ

	}

	private static void addEntryPanels(Container pane, int numEntries) {
		JPanel entryContainer = new JPanel(new GridBagLayout());
		entryContainer.setOpaque(false);
		entryContainer.setPreferredSize(new Dimension(900, 225));
		entryContainer.setMaximumSize(new Dimension(900, 225));
		GridBagConstraints c = new GridBagConstraints();

		for (int i = 0; i < numEntries; i++) {

			// adds each quarter panel
			// (i + 1) % 2 returns alternating 0101
			// i / 3 returns 0011

			addEntryBox(entryContainer, c, (i + 1) % 2, i / 3, i);
		}

		pane.add(entryContainer);
	}

	public static void addEntryBox(Container pane, GridBagConstraints c, int gridx, int gridy, int teamNumber) {

		JPanel quarterPanel = new JPanel();
		quarterPanel.setOpaque(false);
		quarterPanel.setPreferredSize(new Dimension(450, 100));
		quarterPanel.setLayout(new BoxLayout(quarterPanel, BoxLayout.X_AXIS));

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = gridx;
		c.gridy = gridy;
		pane.add(quarterPanel, c);

		entryTextField = InitializeComponents.addATextField(quarterPanel);
		entryTextField.setText("Team Name " + ", enter your answer.");
		entryTextField.setBorder(new EmptyBorder(10, 5, 10, 5));
		entryTextField.setForeground(InitializeComponents.lightGray); // update
		entryTextField.setPreferredSize(new Dimension(300, 30));
		entryTextField.setMaximumSize(new Dimension(300, 30));

		quarterPanel.add(Box.createRigidArea(new Dimension(10, 0)));

		JButton submitButton = InitializeComponents.addAButton("Submit Answer", quarterPanel);
		submitButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		submitButton.setFont(new Font("Serif", Font.BOLD, 16));
		// submitButton.setBackground(Color.white);
		submitButton.setBackground(Color.lightGray);
		submitButton.setForeground(Color.black);
		submitButton.setOpaque(true);
		submitButton.setBorderPainted(false);
		submitButton.setEnabled(false);
		entryTextField.setPreferredSize(new Dimension(75, 30));

	}

	private static void addBettingPanels(Container pane, int numPanels) {
		JPanel bettingContainer = new JPanel();
		bettingContainer.setOpaque(false);
		bettingContainer.setLayout(new BoxLayout(bettingContainer, BoxLayout.Y_AXIS));
		bettingContainer.setPreferredSize(new Dimension(1000, 400));
		bettingContainer.setMaximumSize(new Dimension(1000, 400));

		for (int i = 0; i < numPanels; i++) {
			addABettingRow(bettingContainer, i);
		}

		pane.add(bettingContainer);
	}

	private static void addABettingRow(Container pane, int rowNum) {
		JPanel bettingPanel = new JPanel();
		bettingPanel.setBorder(new EmptyBorder(10, 20, 10, 10));
		bettingPanel.setLayout(new BoxLayout(bettingPanel, BoxLayout.X_AXIS));
		bettingPanel.setOpaque(false);
		bettingPanel.setPreferredSize(new Dimension(1000, 150));
		bettingPanel.setMaximumSize(new Dimension(1000, 150));
		bettingPanel.add(Box.createRigidArea(new Dimension(0, 5)));

		JPanel teamNamePanel = new JPanel();
		teamNamePanel.setOpaque(false);
		teamNamePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
		teamNamePanel.setPreferredSize(new Dimension(200, 75));
		teamNamePanel.setMaximumSize(new Dimension(200, 75));
		JLabel teamName = new JLabel("Team Name" + "'s bet:");
		teamName.setForeground(Color.lightGray);
		teamName.setFont(new Font("Serif", Font.BOLD, 14));
		teamNamePanel.add(teamName);
		bettingPanel.add(teamNamePanel);

		JPanel sliderPanel = new JPanel();
		sliderPanel.setOpaque(false);
		sliderPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
		sliderPanel.setPreferredSize(new Dimension(600, 75));
		sliderPanel.setMaximumSize(new Dimension(600, 75));
		// betSliders[rowNum] = new BetSlider(0, rowNum,
		// GamePlay.getTeamData(rowNum).getPoints());
		betSliders[rowNum] = new BetSlider(0, rowNum, 500);
		sliderPanel.add(betSliders[rowNum]);
		bettingPanel.add(sliderPanel);

		JPanel amountPanel = new JPanel();
		amountPanel.setOpaque(false);
		amountPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
		amountPanel.setPreferredSize(new Dimension(50, 75));
		amountPanel.setMaximumSize(new Dimension(50, 75));
		// betLabels[rowNum] = new JLabel("$" +
		// GamePlay.getTeamData(rowNum).getPoints());
		betLabels[rowNum] = new JLabel("$" + 500);
		betLabels[rowNum].setForeground(Color.lightGray);
		amountPanel.add(betLabels[rowNum]);
		bettingPanel.add(betLabels[rowNum]);

		JPanel setBetPanel = new JPanel();
		setBetPanel.setOpaque(false);
		setBetPanel.setPreferredSize(new Dimension(100, 75));
		setBetPanel.setMaximumSize(new Dimension(100, 75));
		setBetButtons[rowNum] = new JButton("Set Bet");
		setBetButtons[rowNum].setBorder(new EmptyBorder(15, 15, 15, 15));
		setBetButtons[rowNum].setFont(new Font("Serif", Font.BOLD, 16));
		setBetButtons[rowNum].setBackground(Color.white);
		setBetButtons[rowNum].setOpaque(true);
		setBetButtons[rowNum].setBorderPainted(false);
		setBetPanel.add(setBetButtons[rowNum]);
		bettingPanel.add(setBetPanel);

		pane.add(bettingPanel);
	}

	private static void addHeader(Container pane) {
		JPanel titleContainer = new JPanel();
		titleContainer.setOpaque(true);
		titleContainer.setMaximumSize(new Dimension(1000, 50));
		titleContainer.setBackground(InitializeComponents.darkBlue);

		JLabel title = InitializeComponents.addALabel("Final Jeopardy Round", titleContainer);
		title.setFont((new Font("Serif", Font.BOLD, 32)));
		title.setForeground(Color.lightGray);
		title.setBorder(new EmptyBorder(10, 0, 10, 0));

		pane.add(titleContainer);
	}

	// borderlayout with boxlayouts inside
	private static void fillSideBar(Container pane) {
		JPanel teamInfo = InitializeComponents.addAPanel(pane, InitializeComponents.darkGray);
		teamInfo.setBorder(new EmptyBorder(10, 0, 0, 0));
		teamInfo.setPreferredSize(new Dimension(300, 300));

		JPanel teamNames = new JPanel();
		teamNames.setOpaque(false);
		JPanel teamScores = new JPanel();
		teamScores.setOpaque(false);
		teamNames.setLayout(new BoxLayout(teamNames, BoxLayout.Y_AXIS));
		teamScores.setLayout(new BoxLayout(teamScores, BoxLayout.Y_AXIS));

		for (int i = 0; i < numTeams; i++) {

			JPanel teamNamePanel = InitializeComponents.addAPanel(teamNames);
			teamNamePanel.setPreferredSize(new Dimension(190, 75));

			JPanel teamScorePanel = InitializeComponents.addAPanel(teamScores);
			teamScorePanel.setPreferredSize(new Dimension(100, 75));

			JLabel teamName = InitializeComponents.addALabel(GamePlay.getTeamData(i).getTeamName(), teamNamePanel);
			teamName.setHorizontalAlignment(SwingConstants.CENTER);
			teamName.setFont(new Font("Serif", Font.BOLD, 16));
			teamName.setForeground(Color.white);

			JLabel moneyLabel = InitializeComponents.addALabel("$" + GamePlay.getTeamData(i).getPoints(),
					teamScorePanel);
			moneyLabel.setHorizontalAlignment(SwingConstants.LEFT);
			moneyLabel.setFont(new Font("Serif", Font.BOLD, 16));
			moneyLabel.setForeground(Color.white);
		}

		teamInfo.add(teamNames, BorderLayout.WEST);
		teamInfo.add(teamScores, BorderLayout.EAST);

		// pane.add(Box.createRigidArea(new Dimension(0, 2)));

		addGameProgress(pane);
	}

	public static void updateGameProgressInfo(String textToAdd) {
		gplText = textToAdd + "\n" + gplText;
		gameProgressInfo.setText(gplText);
	}

	// boxlayout
	private static void addGameProgress(Container pane) {
		JPanel gameProgressContainer = new JPanel();
		gameProgressContainer.setBackground(InitializeComponents.lightBlue);
		gameProgressContainer.setPreferredSize(new Dimension(300, 425));
		gameProgressContainer.setLayout(new BoxLayout(gameProgressContainer, BoxLayout.Y_AXIS));

		JPanel gameProgressHeader = new JPanel();
		gameProgressHeader.setOpaque(false);
		JLabel gameProgressLabel = new JLabel("Game Progress");
		gameProgressLabel.setFont(new Font("Serif", Font.BOLD, 28));
		gameProgressLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
		gameProgressHeader.add(gameProgressLabel);
		gameProgressContainer.add(gameProgressHeader);

		JPanel line = new JPanel();
		line.setOpaque(true);
		line.setBackground(InitializeComponents.lightGray);
		line.setPreferredSize(new Dimension(300, 3));
		gameProgressContainer.add(line);

		gameProgressInfo = new JTextArea(
				"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum");
		gameProgressInfo.setOpaque(false);
		gameProgressInfo.setFont(new Font("Serif", Font.BOLD, 14));
		gameProgressInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
		gameProgressInfo.setEditable(false);
		gameProgressInfo.setLineWrap(true);
		gameProgressInfo.setWrapStyleWord(true);
		gameProgressContainer.add(gameProgressInfo);

		pane.add(gameProgressContainer);

	}

	private static JPanel addGameBoard(Container pane) {
		JPanel boardHolder = InitializeComponents.addAPanel(pane);
		boardHolder.setBackground(InitializeComponents.lightGray);

		JPanel gameboard = new JPanel();
		gameboard.setLayout(new BoxLayout(gameboard, BoxLayout.Y_AXIS));
		gameboard.setOpaque(false);

		JPanel titlePanel = InitializeComponents.addAPanel(gameboard, InitializeComponents.lightBlue);
		JLabel label = InitializeComponents.addALabel("Jeopardy", titlePanel, InitializeComponents.lightBlue);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Serif", Font.BOLD, 24));
		label.setForeground(InitializeComponents.darkGray);
		label.setBorder(new EmptyBorder(20, 50, 20, 50));
		label.setPreferredSize(new Dimension(800, 30)); /////

		gameboard.add(Box.createRigidArea(new Dimension(0, 3)));

		addCategoryBoxes(gameboard);

		gameboard.add(Box.createRigidArea(new Dimension(0, 3)));

		addQuestionBoxes(gameboard);

		boardHolder.add(gameboard);

		pane.add(boardHolder);

		return boardHolder;
	}

	private static void addCategoryBoxes(Container pane) {
		// JPanel holder = new JPanel();

		JPanel categoryPanel = new JPanel();
		categoryPanel.setBackground(InitializeComponents.darkBlue);
		categoryPanel.setPreferredSize(new Dimension(1000, 70));
		categoryPanel.setMinimumSize(new Dimension(1000, 70));

		categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.X_AXIS));

		for (int i = 0; i < 5; i++) {
			JLabel label = InitializeComponents.addALabel(categoriesArray[i], categoryPanel,
					InitializeComponents.darkGray);
			label.setPreferredSize(new Dimension(180, 60));
			label.setMinimumSize(new Dimension(175, 60));
			label.setMaximumSize(new Dimension(190, 60));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setFont(new Font("Serif", Font.BOLD, 20));
			label.setForeground(Color.white);
			label.setBorder(new EmptyBorder(20, 10, 20, 10));
			categoryPanel.add(Box.createRigidArea(new Dimension(4, 0)));
		}
		pane.add(categoryPanel);
	}

	private static void addQuestionBoxes(Container pane) {
		JPanel questionPanel = new JPanel();
		questionPanel.setBackground(InitializeComponents.darkBlue);
		questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

		for (int i = 0; i < 5; i++) {

			JPanel row = new JPanel();
			row.setOpaque(false);
			row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

			for (int j = 0; j < 5; j++) {

				QuestionCards thisQuestion = new QuestionCards(row, "$" + (i + 1) * 100, j, i);
				questionButtons[j][i] = thisQuestion;
				row.add(Box.createRigidArea(new Dimension(4, 0)));
			}
			questionPanel.add(Box.createRigidArea(new Dimension(0, 4)));
			questionPanel.add(row);
		}

		pane.add(questionPanel);
	}

	private static JPanel addQuestionScreen(Container pane) {
		JPanel questionScreenContainer = new JPanel();
		questionScreenContainer.setOpaque(false);
		questionScreenContainer.setBackground(Color.white);
		questionScreenContainer.setPreferredSize(new Dimension(1000, 850));
		questionScreenContainer.setLayout(new BoxLayout(questionScreenContainer, BoxLayout.Y_AXIS));

		JPanel questionHeader = new JPanel();
		questionHeader.setOpaque(true);
		questionHeader.setMaximumSize(new Dimension(1000, 75));
		questionHeader.setBackground(InitializeComponents.darkBlue);

		addPanelToQuestionHeader(GamePlay.getTeamData(GamePlay.getWhoseTurn()).getTeamName(), questionHeader, 0);
		addPanelToQuestionHeader(currentCategory, questionHeader, 1);
		addPanelToQuestionHeader("$" + currentPoints, questionHeader, 2);

		questionScreenContainer.add(questionHeader);

		addReminder(questionScreenContainer);

		addQuestionTextArea(questionScreenContainer);

		addEntryField(questionScreenContainer);

		pane.add(questionScreenContainer);

		return questionScreenContainer;

	}

	public static void setCurrentQuestion(int catIndex, int valIndex) {
		currentCategory = categoriesArray[catIndex];
		System.out.print(currentCategory);
		currentPoints = GameData.getPointValue(valIndex);
		questionHeader[0].setText(GamePlay.getTeamData(GamePlay.getWhoseTurn()).getTeamName());
		questionHeader[1].setText(currentCategory);
		questionHeader[2].setText("$" + currentPoints);
	}

	private static void addEntryField(Container pane) {
		JPanel entryContainer = new JPanel();
		entryContainer.setOpaque(false);
		entryContainer.setPreferredSize(new Dimension(1000, 100));
		entryContainer.setMaximumSize(new Dimension(1000, 100));

		JTextField entry = new JTextField();
		entry.setPreferredSize(new Dimension(653, 75));
		entry.setMinimumSize(new Dimension(653, 75));
		entryContainer.add(entry);

		submitButton = new JButton("Submit Answer");
		submitButton.setBorder(new EmptyBorder(25, 20, 25, 20));
		submitButton.setFont(new Font("Serif", Font.BOLD, 16));
		submitButton.setBackground(Color.white);
		submitButton.setOpaque(true);
		submitButton.setBorderPainted(false);
		entryContainer.add(submitButton);

		pane.add(entryContainer);
	}

	private static void addQuestionTextArea(Container pane) {
		JPanel textAreaContainer = new JPanel();
		textAreaContainer.setOpaque(false);
		textAreaContainer.setPreferredSize(new Dimension(1000, 400));
		textAreaContainer.setMaximumSize(new Dimension(1000, 400));

		questionTextArea = new JTextArea();
		questionTextArea.setPreferredSize(new Dimension(800, 400));
		questionTextArea.setMinimumSize(new Dimension(800, 350));
		questionTextArea.setBackground(InitializeComponents.lightBlue);
		questionTextArea.setOpaque(true);
		questionTextArea.setFont(new Font("Serif", Font.BOLD, 28));
		questionTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));
		questionTextArea.setEditable(false);
		questionTextArea.setLineWrap(true);
		questionTextArea.setWrapStyleWord(true);
		textAreaContainer.add(questionTextArea);

		pane.add(textAreaContainer);
	}

	private static void addReminder(Container pane) {
		JPanel reminderPanel = new JPanel();
		reminderPanel.setOpaque(false);
		reminderPanel.setPreferredSize(new Dimension(1000, 50));
		reminderPanel.setMaximumSize(new Dimension(1000, 100));

		reminder = InitializeComponents.addALabel("Remember to pose your answer as a question", reminderPanel);
		reminder.setForeground(Color.lightGray);
		reminder.setFont(new Font("Serif", Font.BOLD, 18));
		reminder.setBorder(new EmptyBorder(40, 0, 0, 40));
		reminder.setVisible(false);

		pane.add(reminderPanel);
	}

	public static void showReminder(boolean show) {
		reminder.setVisible(true);
	}

	private static void addPanelToQuestionHeader(String text, Container pane, int i) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(320, 75));
		questionHeader[i] = InitializeComponents.addALabel(text, pane);
		questionHeader[i].setForeground(Color.lightGray);
		questionHeader[i].setFont(new Font("Serif", Font.BOLD, 28));
		questionHeader[i].setBorder(new EmptyBorder(25, 25, 25, 25));
		panel.add(questionHeader[i]);
		pane.add(panel);
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		menuBar.add(menu);

		JMenuItem exit = new JMenuItem("Exit Game");
		JMenuItem restart = new JMenuItem("Restart This Game");
		JMenuItem chooseNewFile = new JMenuItem("Choose New Game File");

		exit.addActionListener(menuListener);
		restart.addActionListener(menuListener);
		chooseNewFile.addActionListener(menuListener);

		menu.add(restart);
		menu.add(chooseNewFile);
		menu.add(exit);
		setJMenuBar(menuBar);
	}

	private void createGUI() {
		setSize(1500, 825);
		addComponentsToPane(getContentPane());
	}

	public static void showDialogBox(String displayText) {
		JFrame dialogFrame = new JFrame("Alert");
		JOptionPane.showMessageDialog(dialogFrame, displayText);
	}

	public static void main(String[] args) {
		GameBoard cgui = new GameBoard();
		cgui.setVisible(true);
	}

	// dumb code

	public static void fillCategoryArray(String cat, int index) {
		categoriesArray[index] = cat;
	}

	public static String getCategory(int index) {
		return categoriesArray[index];
	}

	public void newGame() {
		WelcomeScreen.setVis(true);
		setVisible(false);
		GamePlay.resetData();
	}

	private ActionListener menuListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();

			if (command.equals("Exit Game")) {
				System.exit(0);
			} else if (command.equals("Restart This Game")) {
				GamePlay.resetData();
				setDisplay(1);
				// resetAllButtons
			} else if (command.equals("Choose New Game File")) {
				newGame();
			}

		}

	};

	private static ActionListener submitListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			setDisplay(3);

			// GamePlay.answerQuestion(indexofCurrentCategory,
			// indexofCurrentValue,
			// GameData.getPointValue(indexofCurrentValue),
			// entryTextField.getText());
			// if second try, go back to game board
		}
	};

}
