package other_gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import frames.MainGUI;
import game_logic.GameData;
import main.HW5Constants;
import messages.BuzzInMessage;
import messages.QuestionAnsweredMessage;
import messages.QuestionClickedMessage;
import server.Client;

//inherits from QuestionGUIElement
public class QuestionGUIElementNetworked extends QuestionGUIElement
{

	private Client client;
	//very similar variables as in the AnsweringLogic class
	public Boolean hadSecondChance;
	private TeamGUIComponents currentTeam;
	private TeamGUIComponents originalTeam;
	int teamIndex;
	int numTeams;
	//stores team index as the key to a Boolean indicating whether they have gotten a chance to answer the question
	private HashMap<Integer, Boolean> teamHasAnswered;
	transient public static ImageAnimator clockAnimation;

	public QuestionGUIElementNetworked(String question, String answer, String category, int pointValue, int indexX,
			int indexY)
	{
		super(question, answer, category, pointValue, indexX, indexY);
	}

	//set the client and also set the map with the booleans to all have false
	public void setClient(Client client, int numTeams)
	{
		this.client = client;
		this.numTeams = numTeams;
		teamIndex = client.getTeamIndex();
		setTeamHasAnswered(new HashMap<>());
		for (int i = 0; i < numTeams; i++)
			getTeamHasAnswered().put(i, false);
	}

	//returns whether every team has had a chance at answering this question
	public Boolean questionDone()
	{
		Boolean questionDone = true;
		for (Boolean currentTeam : getTeamHasAnswered().values())
			questionDone = questionDone && currentTeam;
		return questionDone;
	}

	//overrides the addActionListeners method in super class
	@Override
	public void addActionListeners(MainGUI mainGUI, GameData gameData)
	{
		passButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				//send a buzz in message
				client.sendMessage(new BuzzInMessage(teamIndex));
			}

		});

		gameBoardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				//send a question clicked message
				client.sendMessage(new QuestionClickedMessage(getX(), getY()));
			}
		});

		submitAnswerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//send question answered message
				client.sendMessage(new QuestionAnsweredMessage(answerField.getText()));
			}

		});
	}

	public void timerFinished()
	{
		client.sendMessage(new QuestionAnsweredMessage(answerField.getText()));
		System.out.println("timeFinished called");
	}

	//override the resetQuestion method
	@Override
	public void resetQuestion()
	{
		super.resetQuestion();
		hadSecondChance = false;
		currentTeam = null;
		originalTeam = null;
		getTeamHasAnswered().clear();
		//reset teamHasAnswered map so all teams get a chance to anaswer again
		for (int i = 0; i < numTeams; i++)
			getTeamHasAnswered().put(i, false);
	}

	@Override
	public void populate()
	{
		super.populate();
		passButton.setText("Buzz In!");
		clockAnimation = new ImageAnimator(143, HW5Constants.AnswerTime, "Clock");
	}

	public int getOriginalTeam()
	{
		return originalTeam.getTeamIndex();
	}

	public void updateAnnouncements(String announcement)
	{
		announcementsLabel.setText(announcement);
	}

	public void setOriginalTeam(int team, GameData gameData)
	{
		originalTeam = gameData.getTeamDataList().get(team);
		updateTeam(team, gameData);
	}

	//update the current team of this question
	public void updateTeam(int team, GameData gameData)
	{
		currentTeam = gameData.getTeamDataList().get(team);
		passButton.setVisible(false);
		answerField.setText("");
		//if the current team is this client
		if (team == teamIndex)
		{
			AppearanceSettings.setEnabled(true, submitAnswerButton, answerField);
			announcementsLabel.setText("It's your turn to try to answer the question!");
			//no fish
			fishPanel.setVisible(false);
		}
		//if the current team is an opponent
		else
		{
			AppearanceSettings.setEnabled(false, submitAnswerButton, answerField);
			announcementsLabel.setText("It's " + currentTeam.getTeamName() + "'s turn to try to answer the question.");
			//yes fish
			fishPanel.setVisible(true);
		}
		//mark down that this team has had a chance to answer
		getTeamHasAnswered().replace(team, true);
		hadSecondChance = false;
		teamLabel.setText(currentTeam.getTeamName());
	}

	//called from QuestionAnswerAction when there is an illformated answer
	public void illFormattedAnswer()
	{

		if (currentTeam.getTeamIndex() == teamIndex)
		{
			announcementsLabel.setText("You had an illformatted answer. Please try again");
		}
		else
		{
			announcementsLabel
					.setText(currentTeam.getTeamName() + " had an illformatted answer. They get to answer again.");
		}
	}

	//set the gui to be a buzz in period, also called from QuestionAnswerAction
	public void setBuzzInPeriod()
	{
		for (int i = 0; i < MainGUI.clockPanels.size(); i++)
		{
			MainGUI.clockPanels.get(i).setVisible(false);
		}

		fishPanel.setVisible(false);
		showClockinInfoPanel();

		passButton.setVisible(true);
		teamLabel.setText("");

		if (getTeamHasAnswered().get(teamIndex))
		{
			AppearanceSettings.setEnabled(false, submitAnswerButton, answerField, passButton);
			announcementsLabel.setText("It's time to buzz in! But you've already had your chance..");
		}
		else
		{
			announcementsLabel.setText("Buzz in to answer the question!");
			passButton.setEnabled(true);
			AppearanceSettings.setEnabled(false, submitAnswerButton, answerField);
		}
	}

	public void showClockinInfoPanel()
	{
		//		clockAnimation = new ImageAnimator(143, HW5Constants.AnswerTime, "Clock");
		//		setTeamPanel("Clock");
		//		clockAnimation = new ImageAnimator(143, 15, "Clock");
		infoPanel.remove(teamLabel);
		infoPanel.add(clockAnimation, 1);
		//		infoPanel.revalidate();
		//		infoPanel.repaint();
		//		clockHolder = clockAnimation;
		//		clockAnimation.restartAnimation();
	}

	public void showTeaminInfoPanel()
	{
		//		setTeamPanel("Team");
		infoPanel.remove(clockAnimation);
		infoPanel.add(teamLabel, 1);
		//		infoPanel.revalidate();
		//		infoPanel.repaint();
	}

	public TeamGUIComponents getCurrentTeam()
	{
		return currentTeam;
	}

	public HashMap<Integer, Boolean> getTeamHasAnswered()
	{
		return teamHasAnswered;
	}

	public void setTeamHasAnswered(HashMap<Integer, Boolean> teamHasAnswered)
	{
		this.teamHasAnswered = teamHasAnswered;
	}
}
