package frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import game_logic.ServerGameData;
import game_logic.User;
import listeners.NetworkedWindowListener;
import main.HW5Constants;
import messages.PlayerLeftMessage;
import messages.RestartGameMessage;
import other_gui.FinalJeopardyGUINetworked;
import other_gui.QuestionGUIElement;
import other_gui.QuestionGUIElementNetworked;
import other_gui.TeamGUIComponents;
import server.Client;

//used only for a networked game and inherits from MainGUI
public class MainGUINetworked extends MainGUI
{
	private Client client;
	//has a networked game data
	private ServerGameData serverGameData;
	//had a networked FJ panel that I need as a member variable
	private FinalJeopardyGUINetworked fjGUI;
	public static Timer mainGUITimer;
	private static int timeToCountdown = 15;
	private static String timerString;
	//	private static Timer swingTimer;

	public MainGUINetworked(ServerGameData gameData, Client client, User loggedInUser)
	{
		super(loggedInUser);
		this.serverGameData = gameData;
		this.client = client;
		//calls a method in MainGUI that basically acts as a constructor
		//since you can only call the super class's constructor as the first line of the child constructor,
		//but I need to have serverGameData initialized before I can cosntruct my GUI, this is the solution
		make(gameData);
		updatesLabel.setText("Game Progress    " + gameData.getTeamName(client.getTeamIndex()));
		mainGUITimer = new Timer();
		startMainGUITimer();
		//		swingTimer = new Timer(1000, this);
		//		swingTimer.start();
	}
	////////////////

	//	public void actionPerformed(ActionEvent e)
	//	{
	//		timeToCountdown--;
	//		String formattedCountdown = String.format("%02d", timeToCountdown);
	//		timerString = ":" + formattedCountdown;
	//		System.out.println("MainGUI Timer: " + timerString);
	//		timerLabel.setText("Jeopardy  " + timerString);
	//		if (timeToCountdown == 0)
	//		{
	//			swingTimer.stop();
	//			nextPlayerPicksQuestion();
	//		}
	//	}
	//
	//	public static void start()
	//	{
	//		timeToCountdown = HW5Constants.PickQuestionTime;
	//		swingTimer.restart();
	//
	//	}
	//
	//	public static void stop()
	//	{
	//		swingTimer.stop();
	//	}

	/////////////////
	public void startMainGUITimer()
	{
		mainGUITimer.cancel();
		mainGUITimer = new Timer();
		timeToCountdown = HW5Constants.PickQuestionTime;
		mainGUITimer.scheduleAtFixedRate(new TimerTask() {

			public void run()
			{
				timeToCountdown--;
				String formattedCountdown = String.format("%02d", timeToCountdown);
				timerString = ":" + formattedCountdown;
				System.out.println("MainGUI Timer: " + timerString);
				timerLabel.setText("Jeopardy  " + timerString);
				if (timeToCountdown == 0)
				{
					//					mainGUITimer.cancel();
					nextPlayerPicksQuestion();
				}
			}
		}, 0, 1000);

	}

	public void nextPlayerPicksQuestion()
	{
		addUpdate(gameData.getCurrentTeam().getTeamName() + " failed to choose a question in time.");
		gameData.nextTurn();
		addUpdate("It is now " + gameData.getCurrentTeam().getTeamName() + "'s turn to choose a question");
		startMainGUITimer();
		updateClockPanels(gameData.getCurrentTeam().getTeamIndex());
		if (gameData.getCurrentTeam().getTeamIndex() == client.getTeamIndex())
		{
			enableAllButtons();
		}
		else
		{
			disableAllButtons();
		}
	}

	public FinalJeopardyGUINetworked getFJGUI()
	{
		return fjGUI;
	}

	//disables all enabled buttons to have enabled icon
	public void disableAllButtons()
	{
		for (QuestionGUIElement question : gameData.getQuestions())
		{
			if (!question.isAsked())
			{
				question.getGameBoardButton().setDisabledIcon(QuestionGUIElement.getEnabledIcon());
				question.getGameBoardButton().setEnabled(false);
			}
		}
	}

	//enables all questions that have not been chosen
	public void enableAllButtons()
	{
		for (QuestionGUIElement question : gameData.getQuestions())
		{
			if (!question.isAsked())
			{
				question.getGameBoardButton().setIcon(QuestionGUIElement.getEnabledIcon());
				question.getGameBoardButton().setEnabled(true);
			}
		}
	}

	//depending on whether the current team is same at the client's team index, we enable or disable all buttons
	//override showMainPanel from super class in order to always check if we should enable.disbale buttons
	@Override
	public void showMainPanel()
	{
		//		start();
		startMainGUITimer();
		if (gameData.getCurrentTeam().getTeamIndex() != client.getTeamIndex())
			disableAllButtons();

		else
			enableAllButtons();

		super.showMainPanel();
	}

	//override from super class; only add the restart option if the client is the host
	@Override
	protected void createMenu()
	{

		if (client.isHost())
		{
			menu.add(restartThisGameButton);
		}

		menu.add(chooseNewGameFileButton);
		menu.add(logoutButton);
		menu.add(exitButton);
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	//in the non networked game, this logic happens in the AnsweringLogic class in the QuestionGUIElement
	//but we need to be able to call this from QuestionAnsweredAction class
	public void startFinalJeopardy()
	{
		gameData.disableRemainingButtons();
		addUpdate("It's time for Final Jeopardy!");
		gameData.determineFinalists();
		//if no one made it show the main panel and show the rating window
		if (gameData.getFinalistsAndEliminatedTeams().getFinalists().size() == 0)
		{
			showMainPanel();
			new WinnersAndRatingGUINetworked(serverGameData, this, client, true).setVisible(true);
		}
		else
		{
			//if this client did not make it to FJ, show the rating window
			if (gameData.getFinalistsAndEliminatedTeams().getElimindatedTeamsIndices().contains(client.getTeamIndex()))
			{
				showMainPanel();
				client.setElimindated(true);
				new WinnersAndRatingGUINetworked(serverGameData, this, client, false).setVisible(true);
			}
			// create and store a networked fjpanel and switch to it
			else
			{
				fjGUI = new FinalJeopardyGUINetworked(serverGameData, this, client);
				changePanel(fjGUI);
			}
		}

	}

	//sets the bet for the provided team with the provided bet amount, called from SetBetAction class
	public void setBet(int team, int bet)
	{
		TeamGUIComponents teamData = serverGameData.getTeam(team);
		teamData.setBet(bet);
		fjGUI.updateTeamBet(teamData);
	}

	//since we serialize over the gameData with all GUI objects transient, we need to repopulate them on the client side
	//we override this from the super class in order to add different action listeners to the question object
	//and so we can iterate over the networked questions instead
	@Override
	protected void populateQuestionButtons()
	{
		for (int x = 0; x < QUESTIONS_LENGTH_AND_WIDTH; x++)
		{
			for (int y = 0; y < QUESTIONS_LENGTH_AND_WIDTH; y++)
			{
				QuestionGUIElementNetworked question = serverGameData.getNetworkedQuestions()[x][y];
				question.setClient(client, gameData.getNumberOfTeams());
				question.addActionListeners(this, serverGameData);
				questionButtons[question.getX()][question.getY()] = question.getGameBoardButton();
			}
		}

	}

	// adding event listeners, override from MainGUI
	@Override
	protected void addListeners()
	{

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//add window listener
		addWindowListener(new NetworkedWindowListener(client, MainGUINetworked.this));
		//add action listeners
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				sendPlayerLeftMessage();
				System.exit(0);
			}
		});

		restartThisGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				//choose a different team to start the game
				gameData.chooseFirstTeam();
				client.sendMessage(new RestartGameMessage(gameData.getCurrentTeam().getTeamIndex()));
			}
		});

		chooseNewGameFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				sendPlayerLeftMessage();
				new StartWindowGUI(loggedInUser).setVisible(true);
			}
		});

		logoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				sendPlayerLeftMessage();
				new LoginGUI();
			}
		});
	}

	private void sendPlayerLeftMessage()
	{
		client.sendMessage(new PlayerLeftMessage(client.getTeamIndex()));
		client.close();
		dispose();
	}

}
