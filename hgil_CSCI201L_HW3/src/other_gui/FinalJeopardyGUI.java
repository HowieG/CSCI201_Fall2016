package other_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import frames.MainGUI;
import frames.WinnersGUI;
import game_logic.GameData;

//JPanel that contains the final jeopardy gui elements and functionality
public class FinalJeopardyGUI extends JPanel
{

	private JLabel jeopardyQuestion;
	private GameData gameData;
	private int numTeamsBet;
	private int numTeamsAnswered;
	private MainGUI mainGUI;

	public FinalJeopardyGUI(GameData gameData, MainGUI mainGUI)
	{

		this.gameData = gameData;
		this.mainGUI = mainGUI;
		numTeamsBet = 0;
		numTeamsAnswered = 0;

		initialize();
		createGUI();
	}

	//public methods
	//called every time a 'Set Bet' button is pressed
	public void increaseNumberOfBets(String update)
	{
		//increase the number of teams that have made their bet
		numTeamsBet++;
		mainGUI.addUpdate(update);

		if (allTeamsBet())
		{
			mainGUI.addUpdate("All teams have bet! The Final Jeopardy question is: " + "\n"
					+ gameData.getFinalJeopardyQuestion());
			//display jeopardy question
			jeopardyQuestion.setText(gameData.getFinalJeopardyQuestion());
			//enabling all of the 'Submit Answer' buttons
			for (TeamGUIComponents team : gameData.getTeamDataList())
			{
				team.getFJAnswerButton().setEnabled(true);
			}
		}
	}

	public void increaseNumberOfAnswers()
	{
		numTeamsAnswered++;

		if (allTeamsAnswered())
		{
			mainGUI.addUpdate(
					"All teams have answered. The Final Jeopardy answer is: " + gameData.getFinalJeopardyAnswer());
			gameData.addOrDeductTeamBets(mainGUI);
			new WinnersGUI(gameData).setVisible(true);
		}
	}

	//returns a boolean indicating whether all teams have made their bet
	public Boolean allTeamsBet()
	{
		return numTeamsBet == gameData.getFinalistsAndEliminatedTeams().getFinalists().size();
	}

	//returns a boolean indication whether all teams have answered the final jeopardy question
	public Boolean allTeamsAnswered()
	{
		return numTeamsAnswered == gameData.getFinalistsAndEliminatedTeams().getFinalists().size();
	}

	//private methods
	//all other GUI components we will reference from the TeamGUIComponents objects
	private void initialize()
	{
		jeopardyQuestion = new JLabel("Wait for it...");
	}

	private void createGUI()
	{
		//local variables
		JPanel betPanel = new JPanel();
		JPanel answerPanel = new JPanel();
		JPanel questionPanel = new JPanel();
		JPanel titlePanel = new JPanel();
		JPanel combinedPanel = new JPanel(new BorderLayout());

		JLabel titleLabel = new JLabel("Final Jeopardy Round");
		//setting appearance of components
		AppearanceSettings.setBackground(Color.darkGray, betPanel, answerPanel, this);
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, questionPanel, jeopardyQuestion);
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, titlePanel, titleLabel);
		AppearanceSettings.setTextAlignment(jeopardyQuestion, titleLabel);

		titleLabel.setForeground(Color.lightGray);
		titleLabel.setOpaque(true);
		titleLabel.setFont(AppearanceConstants.fontLarge);

		jeopardyQuestion.setForeground(Color.darkGray);
		jeopardyQuestion.setFont(AppearanceConstants.fontMedium);
		jeopardyQuestion.setPreferredSize(new Dimension(1000, 100));

		betPanel.setLayout(new GridLayout(4, 1));
		answerPanel.setLayout(new GridLayout(2, 2));

		//iterate over the final teams and add their gui components to the panels
		for (int i = 0; i < gameData.getFinalistsAndEliminatedTeams().getFinalists().size(); i++)
		{

			JPanel teamBetPanel = new JPanel(new BorderLayout());
			JPanel teamAnswerPanel = new JPanel(new BorderLayout());
			JPanel eastBetPanel = new JPanel();

			TeamGUIComponents team = gameData.getFinalistsAndEliminatedTeams().getFinalists().get(i);
			//initialize the team's slider based on their total, and add action listeners to its buttons
			team.prepareForFinalJeopardy(this, gameData);

			teamBetPanel.setPreferredSize(new Dimension(800, 100));
			teamAnswerPanel.setPreferredSize(new Dimension(500, 60));
			AppearanceSettings.setBackground(Color.darkGray, teamBetPanel, teamAnswerPanel, eastBetPanel);
			//create/add this team's bet panel
			teamBetPanel.setLayout(new BoxLayout(teamBetPanel, BoxLayout.LINE_AXIS));
			teamBetPanel.add(team.getFJTeamNameLabel());
			teamBetPanel.add(Box.createHorizontalGlue());
			teamBetPanel.add(team.getBetSlider());
			teamBetPanel.add(Box.createHorizontalGlue());
			teamBetPanel.add(team.getBetLabel());
			teamBetPanel.add(Box.createHorizontalGlue());
			teamBetPanel.add(team.getBetButton());
			betPanel.add(teamBetPanel);
			//create/add this team's answer panel
			teamAnswerPanel.add(team.getFJAnswerTextField(), BorderLayout.CENTER);
			teamAnswerPanel.add(team.getFJAnswerButton(), BorderLayout.EAST);

			answerPanel.add(teamAnswerPanel);
		}

		questionPanel.add(jeopardyQuestion);
		titlePanel.add(titleLabel);

		combinedPanel.add(betPanel, BorderLayout.NORTH);
		combinedPanel.add(questionPanel, BorderLayout.SOUTH);
		titlePanel.setPreferredSize(new Dimension(1000, 70));

		add(titlePanel, BorderLayout.NORTH);
		add(combinedPanel, BorderLayout.CENTER);
		add(answerPanel, BorderLayout.SOUTH);
	}
}
