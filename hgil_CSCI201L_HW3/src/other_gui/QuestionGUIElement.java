package other_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import frames.MainGUI;
import frames.WinnersGUI;
import game_logic.GameData;
import game_logic.QuestionAnswer;
import game_logic.TeamData;
import listeners.TextFieldFocusListener;

public class QuestionGUIElement extends QuestionAnswer
{

	//the JButton displayed on the game board grid on MainGUI associated with this question
	private JButton gameBoardButton;
	//the panel that will be switched out with the main panel when the gameBoardButton is clicked
	private JPanel questionPanel;
	//the label that would show whether there was a format problem with the submitted answer
	private JLabel announcementsLabel;
	//components for the questionPanel
	private JLabel categoryLabel;
	private JLabel pointLabel;
	private JLabel teamLabel;
	private JTextPane questionLabel;
	private JTextField answerField;
	private JButton submitAnswerButton;
	private Image unchosenQuestionImage;
	private Image chosenQuestionImage;
	private JButton passButton;

	private Boolean hadSecondChance;

	public QuestionGUIElement(String question, String answer, String category, int pointValue, int indexX, int indexY,
			Image unchosenQuestionImage, Image chosenQuestionImage)
	{
		super(question, answer, category, pointValue, indexX, indexY);
		hadSecondChance = false;

		this.unchosenQuestionImage = unchosenQuestionImage;
		this.chosenQuestionImage = chosenQuestionImage;

		initializeComponents();
		createGUI();
		addActionListeners();
		resetQuestion();
	}

	//public methods
	public JButton getGameBoardButton()
	{
		return gameBoardButton;
	}

	//this method is called from the MainGUI; cannot add the action listeners until then
	public void addActionListeners(MainGUI mainGUI, GameData gameData)
	{
		SubmitAnswerActionListener SAL = new SubmitAnswerActionListener(mainGUI, gameData);
		submitAnswerButton.addActionListener(SAL);
		passButton.addActionListener(SAL);
		gameBoardButton.addActionListener(new GameBoardActionListener(mainGUI, gameData));
		answerField.getDocument().addDocumentListener(SAL.new MyDocumentListener());
	}

	//method used to reset the data of this question to it's default
	//called from MainGUI when user chooses 'Restart This Game' option on the menu
	public void resetQuestion()
	{
		submitAnswerButton.setEnabled(true);
		asked = false;
		teamLabel.setText("");
		gameBoardButton.setEnabled(true);
		ImageIcon unchosenQuestionIcon = new ImageIcon(unchosenQuestionImage);
		gameBoardButton.setIcon(unchosenQuestionIcon);
		gameBoardButton.setHorizontalTextPosition(SwingConstants.CENTER);
		answerField.setText("");
	}

	//private methods

	//initialize member variables
	private void initializeComponents()
	{

		questionPanel = new JPanel();
		gameBoardButton = new JButton("$" + pointValue);
		pointLabel = new JLabel("$" + pointValue);
		categoryLabel = new JLabel(category);
		questionLabel = new JTextPane();
		announcementsLabel = new JLabel("");
		answerField = new JTextField("Enter your answer.");
		submitAnswerButton = new JButton("Submit Answer");
		passButton = new JButton("Pass");
		passButton.setVisible(false);
		teamLabel = new JLabel("");
	}

	private void createGUI()
	{

		//local variables
		JPanel infoPanel = new JPanel(new BorderLayout());
		JPanel answerPanel = new JPanel(new BorderLayout());
		JPanel formatErrorPanel = new JPanel();
		JPanel northPanel = new JPanel();
		//appearance settings
		AppearanceSettings.setBackground(Color.darkGray, gameBoardButton, questionPanel, announcementsLabel,
				answerPanel, formatErrorPanel);
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, teamLabel, pointLabel, categoryLabel, infoPanel);
		AppearanceSettings.setForeground(Color.lightGray, gameBoardButton, teamLabel, pointLabel, categoryLabel,
				announcementsLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontLarge, questionLabel, teamLabel, pointLabel, categoryLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, gameBoardButton, announcementsLabel,
				submitAnswerButton, answerField);
		AppearanceSettings.setFont(AppearanceConstants.fontSmallest, passButton);
		AppearanceSettings.setTextAlignment(teamLabel, pointLabel, categoryLabel, announcementsLabel);
		AppearanceSettings.setSize(60, 30, passButton);

		questionLabel.setText(question);
		questionLabel.setEditable(false);
		// sourced from: http://stackoverflow.com/questions/3213045/centering-text-in-a-jtextarea-or-jtextpane-horizontal-text-alignment
		//centers the text in the question pane
		StyledDocument doc = questionLabel.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

		gameBoardButton.setBorder(BorderFactory.createLineBorder(AppearanceConstants.darkBlue));
		gameBoardButton.setOpaque(true);
		answerField.setForeground(Color.gray);
		questionLabel.setBackground(AppearanceConstants.lightBlue);

		//components that need their size set
		questionLabel.setPreferredSize(new Dimension(800, 400));
		answerField.setPreferredSize(new Dimension(600, 100));
		infoPanel.setPreferredSize(new Dimension(900, 80));
		formatErrorPanel.setPreferredSize(new Dimension(800, 100));

		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));
		//add components to the panels
		infoPanel.add(teamLabel, BorderLayout.EAST);
		infoPanel.add(categoryLabel, BorderLayout.CENTER);
		infoPanel.add(pointLabel, BorderLayout.WEST);

		answerPanel.add(answerField, BorderLayout.CENTER);
		answerPanel.add(submitAnswerButton, BorderLayout.EAST);
		answerPanel.add(AppearanceSettings.wrapInJPanelContainer(passButton), BorderLayout.SOUTH);

		formatErrorPanel.add(announcementsLabel);

		northPanel.add(infoPanel);
		northPanel.add(formatErrorPanel);

		questionPanel.add(northPanel, BorderLayout.NORTH);
		questionPanel.add(questionLabel, BorderLayout.CENTER);
		questionPanel.add(answerPanel, BorderLayout.SOUTH);

	}

	//add focus listener to answer text field, and the rest of the action listeners will be added later from a call from MainGUI
	private void addActionListeners()
	{
		answerField.addFocusListener(new TextFieldFocusListener("Enter your answer", answerField));
	}

	//private listener classes
	//action listener for gameBoardButton
	private class GameBoardActionListener implements ActionListener
	{

		private MainGUI mainGUI;
		private GameData gameData;

		public GameBoardActionListener(MainGUI mainGUI, GameData gameData)
		{
			this.mainGUI = mainGUI;
			this.gameData = gameData;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{

			//change panel to the question panel
			mainGUI.changePanel(questionPanel);

			//set the label of which team chose the question
			teamLabel.setText(gameData.getCurrentTeam().getTeamName());
		}

	}

	//action listener for submitAnswerButton
	private class SubmitAnswerActionListener implements ActionListener
	{

		private MainGUI mainGUI;
		private GameData gameData;
		private String update;
		private Boolean answered;
		private boolean noneAnsweredCorrectly = false;

		public SubmitAnswerActionListener(MainGUI mainGUI, GameData gameData)
		{
			this.mainGUI = mainGUI;
			this.gameData = gameData;
			answered = false;
			update = "";
		}

		//checks the answer taken from answerField and determines whether the player gets a second chance to answer
		private void checkAnswer()
		{
			String givenAnswer = answerField.getText();
			TeamData team = gameData.getCurrentTeam();

			//valid format
			if (gameData.validAnswerFormat(givenAnswer))
			{
				//team got the answer right
				if (givenAnswer.trim().toLowerCase().endsWith(answer.toLowerCase()))
				{
					team.addPoints(pointValue);
					update = team.getTeamName() + " got the answer right! $" + pointValue
							+ " will be added to their total. ";
				}
				//team got the answer wrong
				else
				{
					team.deductPoints(pointValue);
					update = team.getTeamName() + " got the answer wrong! $" + pointValue
							+ " will be deducted from their total. ";
				}
				//answer is true, meaning no second chance
				answered = true;
			}
			//valid format
			else
			{
				//if the user already had a second chance, give it to the next player
				if (hadSecondChance)
				{
					announcementsLabel.setText("Your answer is still formatted incorrect. $" + pointValue
							+ " will be deducted from your total.");
					team.deductPoints(pointValue);
					update = team.getTeamName() + "'s answer is still formatted incorrect. $" + pointValue
							+ " will be deducted from their total. ";
					giveQuestionToNextPlayer();
				}
				//if user has not had second chance yet, so answered = false
				else
				{
					announcementsLabel.setText("Invalid format of your answer. Remember to pose it as a question");
					answerField.setText("");
					hadSecondChance = true;
					update = team.getTeamName()
							+ " had a badly formatted answer. They will get a second chance to answer.";
				}
			}
		}

		private boolean isOriginalChooser()
		{
			return (gameData.getWhoseTurn() == gameData.getOriginalChoosingTeam());
		}

		private void giveQuestionToNextPlayer()
		{
			passButton.setVisible(true);
			passButton.setEnabled(true);
			//decrement points label of team that answered wrong
			gameData.getCurrentTeam().updatePointsLabel();

			//update labels for next player
			gameData.nextTurn();

			mainGUI.addUpdate(update);

			//if the new current player is the original chooser, showmainPanel
			if (isOriginalChooser())
			{
				noneAnsweredCorrectly = true;
				update = "\n" + "None of the teams were able to get the question right! The answer was " + answer
						+ ". \n";
				answered = true;
			}
			else
			{
				submitAnswerButton.setEnabled(true);
				teamLabel.setText(gameData.getCurrentTeam().getTeamName());
				answerField.setText("");
				//it's the current player's first time answering the question
				hadSecondChance = false;
				answered = false;
				update = "\n" + "It is now " + gameData.getCurrentTeam().getTeamName()
						+ "'s turn to try to answer the question."; //
			}
		}

		//functionality for when the question has been answered
		private void questionHasBeenAnswered()
		{
			//update team point label on MainGUI
			log("here");
			gameData.getCurrentTeam().updatePointsLabel();
			gameBoardButton.setEnabled(false);
			ImageIcon chosenQuestionIcon = new ImageIcon(chosenQuestionImage);
			gameBoardButton.setIcon(chosenQuestionIcon);
			//increment the number of chosen questions
			gameData.updateNumberOfChosenQuestions();
			//have all the questions been asked? if so, time for final jeopardy
			if (gameData.readyForFinalJeopardy())
			{
				//in case we are playing a Quick Play game, disable remaining buttons
				gameData.disableRemainingButtons();
				playFinalJeopardy();
			}
			//not time for final jeopardy
			else
			{
				if (isOriginalChooser() && noneAnsweredCorrectly)
				{
					//change who's turn it is
					gameData.nextTurn();
				}

				//notify players whose turn it is to choose
				mainGUI.addUpdate("It's " + gameData.getCurrentTeam().getTeamName() + "'s turn to choose!");
				//update original choosing team in GameData
				gameData.updateOriginalChoosingTeam();

				//go back to main screen
				mainGUI.showMainPanel();
			}

		}

		//functionality to determine if the teams are eligible to play final jeopardy
		private void playFinalJeopardy()
		{
			mainGUI.addUpdate("It's time for Final Jeopardy!");
			//figure out the teams that qualified for final jeopardy
			gameData.determineFinalists();
			//if there are no qualifying teams, pop up a WinnersGUI (showing no one won)
			if (gameData.getFinalistsAndEliminatedTeams().getFinalists().size() == 0)
			{
				mainGUI.showMainPanel();
				new WinnersGUI(gameData).setVisible(true);
			}
			//if there are final teams, change the current panel to the final jeopardy view
			else
			{
				gameData.getCurrentTeam().removeActionListeners();
				mainGUI.changePanel(new FinalJeopardyGUI(gameData, mainGUI));
			}

		}

		@Override
		public void actionPerformed(ActionEvent e)
		{

			if (e.getSource() == submitAnswerButton)
			{
				//check whether their answer was correct, and whether we should navigate back to the main screen
				checkAnswer();
				//if it has been answered, check if the game has finished
			}
			else if (e.getSource() == passButton)
			{
				log(Integer.toString(gameData.getOriginalChoosingTeam()));
				update = gameData.getCurrentTeam().getTeamName() + " passed. ";
				giveQuestionToNextPlayer();
			}
			else
			{
				log("This shouldn't happen");
			}

			mainGUI.addUpdate(update);

			update = "";

			if (answered)
			{
				questionHasBeenAnswered();
			}
		}

		//document listener; in each method, simply checking whether the user can start the game
		private class MyDocumentListener implements DocumentListener
		{

			@Override
			public void insertUpdate(DocumentEvent e)
			{
				passButton.setEnabled(false);
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				passButton.setEnabled(false);
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				passButton.setEnabled(false);
			}
		}

	}

	public void log(String message)
	{
		System.out.println(message);
	}
}
